
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
docker exec -it 89d4f551353f /opt/kafka/bin/kafka-console-producer.sh --broker-list kafka:9092 --topic my-topic
```

#Consume topic
```shell
docker exec -it 89d4f551353f /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server kafka:9092 --topic my-topic --from-beginning
```

```shell
mongo --host localhost --port 27017 -u root -p *** --authenticationDatabase admin
show dbs
show collections
db.successMessage.find()
```

prometheus: http://localhost:9090/targets
Grafana: http://localhost:3000/

slack: https://app.slack.com/client/T07D61D72RY/C07D5TQCS1H
