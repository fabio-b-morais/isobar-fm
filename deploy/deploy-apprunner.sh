#!/usr/bin/env bash
#
# Deploy isobar-fm to AWS App Runner (container image from ECR).
# Idempotent: safe to re-run to ship a new image version.
#
# Prereqs: awscli v2 configured (`aws configure`), docker running.
# Usage:   bash deploy/deploy-apprunner.sh
#
set -euo pipefail

REGION="${AWS_REGION:-us-east-1}"
REPO="isobar-fm"
SERVICE="isobar-fm"
TAG="latest"
ROLE_NAME="AppRunnerECRAccessRole"

ACCOUNT_ID="$(aws sts get-caller-identity --query Account --output text)"
REGISTRY="${ACCOUNT_ID}.dkr.ecr.${REGION}.amazonaws.com"
IMAGE_URI="${REGISTRY}/${REPO}:${TAG}"

echo ">> Account: ${ACCOUNT_ID}  Region: ${REGION}"

# 1) ECR repository
aws ecr describe-repositories --repository-names "${REPO}" --region "${REGION}" >/dev/null 2>&1 \
  || aws ecr create-repository --repository-name "${REPO}" --region "${REGION}" >/dev/null
echo ">> ECR repo ready: ${REPO}"

# 2) Build & push image
aws ecr get-login-password --region "${REGION}" \
  | docker login --username AWS --password-stdin "${REGISTRY}"
# --provenance=false -> single-platform manifest; App Runner cannot pull an OCI image index
docker build --provenance=false -t "${REPO}:${TAG}" .
docker tag "${REPO}:${TAG}" "${IMAGE_URI}"
docker push "${IMAGE_URI}"
echo ">> Image pushed: ${IMAGE_URI}"

# 3) IAM role that lets App Runner pull from ECR
if ! aws iam get-role --role-name "${ROLE_NAME}" >/dev/null 2>&1; then
  aws iam create-role --role-name "${ROLE_NAME}" \
    --assume-role-policy-document '{
      "Version":"2012-10-17",
      "Statement":[{"Effect":"Allow","Principal":{"Service":"build.apprunner.amazonaws.com"},"Action":"sts:AssumeRole"}]
    }' >/dev/null
  aws iam attach-role-policy --role-name "${ROLE_NAME}" \
    --policy-arn arn:aws:iam::aws:policy/service-role/AWSAppRunnerServicePolicyForECRAccess >/dev/null
  echo ">> Created IAM role ${ROLE_NAME} (waiting for propagation)"
  sleep 10
fi
ROLE_ARN="$(aws iam get-role --role-name "${ROLE_NAME}" --query Role.Arn --output text)"

# 4) Create or update the App Runner service
SRC_CONFIG=$(cat <<JSON
{
  "AuthenticationConfiguration": {"AccessRoleArn": "${ROLE_ARN}"},
  "AutoDeploymentsEnabled": false,
  "ImageRepository": {
    "ImageIdentifier": "${IMAGE_URI}",
    "ImageRepositoryType": "ECR",
    "ImageConfiguration": {"Port": "8080"}
  }
}
JSON
)
HEALTH_CONFIG='{"Protocol":"HTTP","Path":"/actuator/health","Interval":10,"Timeout":5,"HealthyThreshold":1,"UnhealthyThreshold":5}'
INSTANCE_CONFIG='{"Cpu":"1024","Memory":"2048"}'

SERVICE_ARN="$(aws apprunner list-services --region "${REGION}" \
  --query "ServiceSummaryList[?ServiceName=='${SERVICE}'].ServiceArn | [0]" --output text)"

if [ "${SERVICE_ARN}" = "None" ] || [ -z "${SERVICE_ARN}" ]; then
  aws apprunner create-service --region "${REGION}" \
    --service-name "${SERVICE}" \
    --source-configuration "${SRC_CONFIG}" \
    --health-check-configuration "${HEALTH_CONFIG}" \
    --instance-configuration "${INSTANCE_CONFIG}" >/dev/null
  echo ">> Creating App Runner service ${SERVICE}..."
else
  aws apprunner start-deployment --region "${REGION}" --service-arn "${SERVICE_ARN}" >/dev/null
  echo ">> Redeploying existing service ${SERVICE}..."
fi

SERVICE_ARN="$(aws apprunner list-services --region "${REGION}" \
  --query "ServiceSummaryList[?ServiceName=='${SERVICE}'].ServiceArn | [0]" --output text)"
echo ">> Waiting for service to become RUNNING (this takes a few minutes)..."
while true; do
  STATUS="$(aws apprunner describe-service --region "${REGION}" --service-arn "${SERVICE_ARN}" --query Service.Status --output text)"
  echo "   status: ${STATUS}"
  [ "${STATUS}" = "RUNNING" ] && break
  [ "${STATUS}" = "CREATE_FAILED" ] || [ "${STATUS}" = "OPERATION_IN_PROGRESS" ] || true
  if [ "${STATUS}" = "CREATE_FAILED" ]; then echo "Deployment failed"; exit 1; fi
  sleep 20
done

URL="$(aws apprunner describe-service --region "${REGION}" --service-arn "${SERVICE_ARN}" --query Service.ServiceUrl --output text)"
echo ""
echo ">> DEPLOYED: https://${URL}"
echo ">> Try:     curl https://${URL}/api/v1/bands"
