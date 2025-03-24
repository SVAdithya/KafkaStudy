APACHE KAFKA - Kraft
<details><summary>🚀 Podman Script</summary>

#creates all kraft kafka and application podman
```shell
podman-compose up -d --build # to get new changes loaded
```
#get list of all containers and ID for producer and consumer
```shell
podman ps 
```
```shell
podman-compose down
podman-compose build
podman-compose up
```
#produce to topic
```shell
podman exec -it 87932ae7f6a5 /opt/kafka/bin/kafka-console-producer.sh --bootstrap-server kafka:9092 --topic my-topic
```

#Consume topic
```shell
podman exec -it 87932ae7f6a5 /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server kafka:9092 --topic my-topic --from-beginning
```
</details>
<details> <summary>🐳 Docker Script</summary>

#creates all kraft kafka and application podman
```shell
docker-compose up -d --build # to get new changes loaded
```
#get list of all containers and ID for producer and consumer
```shell
docker ps 
```
```shell
docker-compose down
docker-compose build
docker-compose up
```
#produce to topic
```shell
docker exec -it 89d4f551353f /opt/kafka/bin/kafka-console-producer.sh --bootstrap-server kafka:9092 --topic my-topic
```

#Consume topic
```shell
docker exec -it 89d4f551353f /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server kafka:9092 --topic my-topic --from-beginning
```
</details>
<details><summary>Kubernetes Script</summary>

```shell

kubectl get pods -l io.kompose.service=kafka
#Publish msg to topic
kubectl exec -it kafka-deployment-65c956686d-jrmnr -- /opt/kafka/bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic my-topic
#Consume msg from topic
kubectl exec -it kafka-deployment-65c956686d-jrmnr -- /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic my-topic --from-beginning

#App
docker build -t app:latest ./../.. #dockerFile location
minikube image load app:1.1 # load image to minikube
minikube image ls --format table # list all images - build/load/ls/pull/push/rm/save/tag
kubectl get all # get all resources

kubectl apply -f app-deployment.yaml # apply deployment
kubectl exec -it my-app-85bbdcc57b-lc44w -- /bin/sh # load shell script in pod

nslookup kafka-service # get ip address of any-service

kubectl rollout restart deployment my-app # restart deployment(all pods), to apply new changes

```

</details>
<details><summary>Mongo connection Script</summary>

```shell
mongo --host localhost --port 27017 -u root -p *** --authenticationDatabase admin
show dbs
show collections
db.successMessage.find()
```
</details>
prometheus: http://localhost:9090/targets <br/>
Grafana: http://localhost:3000/ <br/>
slack: https://app.slack.com/client/T07D61D72RY/C07D5TQCS1H <br/>
