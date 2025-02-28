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
podman exec -it 87932ae7f6a5 /opt/kafka/bin/kafka-console-producer.sh --broker-list kafka:9092 --topic my-topic
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
docker exec -it 89d4f551353f /opt/kafka/bin/kafka-console-producer.sh --broker-list kafka:9092 --topic my-topic
```

#Consume topic
```shell
docker exec -it 89d4f551353f /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server kafka:9092 --topic my-topic --from-beginning
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
