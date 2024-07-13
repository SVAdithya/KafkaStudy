#creates all zookeeper, kafka and application docker
```shell
docker-compose up -d
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
docker exec -it 89d4f551353f /opt/kafka/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic my-topic
```

#Consume topic
```shell
docker exec -it 89d4f551353f /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic my-topic --from-beginning
```



