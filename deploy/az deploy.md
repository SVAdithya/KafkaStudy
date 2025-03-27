<details><summary> Azure Container Registry</summary>

# Create an Azure Container Registry (ACR)
````shell
# Create a resource group [sku # Basic,Standard,Premium] 
az acr create --resource-group learn-all --name adittest1111 --sku Basic --admin-enabled true --location southindia

#Login to ACR
az acr login --name adittest1111
Login Succeeded
````

# Build and Push Docker Image to ACR
#Create a docker image
```shell
docker build -t app:1.4 .
docker tag app:1.4 adittest1111.azurecr.io/app:1.4
#docker tag app:1.4 <myacrregistry>.azurecr.io/app:1.4

docker push adittest1111.azurecr.io/app:1.4       
The push refers to repository [adittest1111.azurecr.io/app]
204c0e88dc99: Pushed 
````
# Verify Image and create secret to pull image from ACR
```shell
acr repository list --name adittest1111 --output table # list all repos
az acr repository show-tags --name adittest1111 --repository app --output table # list all tags

# Show the crentials for ACR
az acr credential show --name adittest1111


#Create a Kubernetes secret for ACR, else image pull fails for private repos
kubectl create secret docker-registry acr-secret \
--docker-server=adittest1111.azurecr.io \
--docker-username=adittest1111 \
--docker-password=******** \
--namespace=default
```
# Deletetion of ACR
```shell
#Delete the ACR image
az acr repository delete --name adittest1111 --repository app --yes
#Delete the ACR
az acr delete --name adittest1111 --yes 
#Verify the ACR deletion
az acr list --output table
```
</details>
