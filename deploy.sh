#!/bin/bash
minikube start --driver=docker
echo "-------minikube status"
minikube status
echo "-------/"
# Define paths to YAML files
PVC_FILES=(
  "k8s/data/mongo-data-persistentvolumeclaim.yaml"
  "k8s/data/postgres-data-persistentvolumeclaim.yaml"
  "k8s/data/sonarqube-data-persistentvolumeclaim.yaml"
)

DEPLOYMENTS_SERVICES_FILES=(
  "k8s/deploy/kafka-deployment.yaml"
  "k8s/deploy/mongodb-deployment.yaml"
  "k8s/deploy/postgres-deployment.yaml"
  "k8s/deploy/sonarqube-deployment.yaml"
  "k8s/deploy/zookeeper-deployment.yaml"
  "k8s/service/kafka-service.yaml"
  "k8s/service/mongodb-service.yaml"
  "k8s/service/postgres-service.yaml"
  "k8s/service/sonarqube-service.yaml"
  "k8s/service/zookeeper-service.yaml"
)

# Apply Persistent Volume Claims
echo "-------Applying Persistent Volume Claims..."
for pvc in "${PVC_FILES[@]}"; do
  kubectl apply -f "$pvc"
done

# Apply Deployments and Services
echo "-------Applying Deployments and Services..."
for file in "${DEPLOYMENTS_SERVICES_FILES[@]}"; do
  kubectl apply -f "$file"
done

# Wait for Pods to be ready
echo "-------Waiting for Pods to be ready..."
kubectl wait --for=condition=ready pod --all --timeout=5m

# Check Pod status
echo "-------Checking Pod status..."
kubectl get pods

# Display Service URLs
echo "-------Retrieving service details..."
kubectl get services

echo "-------Deployment completed. Access your services as needed."
