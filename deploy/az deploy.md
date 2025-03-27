<details><summary> Azure Container Registry</summary>
#Create a resource group [sku # Basic,Standard,Premium] 

````shell
az acr create --resource-group learn-all --name adittest1111 --sku Basic --admin-enabled true --location southindia
````


#Build and Push Docker Image to ACR
# Create a docker image
```shell
docker build -t app:1.4 .
docker tag app:1.4 adittest1111.azurecr.io/app:1.4
#docker tag app:1.4 <myacrregistry>.azurecr.io/app:1.4
````

#Login to ACR
```shell
az acr login --name adittest1111
Login Succeeded
```

    # Push the image to ACR
```shell
docker push adittest1111.azurecr.io/app:1.4       
The push refers to repository [adittest1111.azurecr.io/app]
204c0e88dc99: Pushed 
```


# Show the crentials for ACR
```shell
az acr credential show --name adittest1111
````

# Create a Kubernetes secret for ACR, else image pull fails
```shell

kubectl create secret docker-registry acr-secret \
--docker-server=adittest1111.azurecr.io \
--docker-username=adittest1111 \
--docker-password=******** \
--namespace=default
```
</details>
