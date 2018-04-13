
SETUP
-----
1. Download Kafka 1.1 and unzip into train_and_predict_flow folder
https://www.apache.org/dyn/closer.cgi?path=/kafka/1.1.0/kafka_2.11-1.1.0.tgz
- Unzip: tar -xzf kafka_2.11-1.1.0.tgz; cd kafka_2.11-1.1.0
- Start zookeeper: bin/zookeeper-server-start.sh config/zookeeper.properties 
- Start kafak: bin/kafka-server-start.sh config/server.properties
- Create two topics: bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic recommender_input; bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic recommender_output

2. Download NiFi and unzip into train_and_predict_flow folder
https://www.apache.org/dyn/closer.lua?path=/nifi/1.6.0/nifi-1.6.0-bin.tar.gz
- Unzip: tar -zxvf nifi-1.6.0-bin.tar.gz; cd nifi-1.6.0
- Start nifi: bin/nifi.sh start
- Open browser and go to: http://localhost:8080/nifi/
- Load the template file, recommender_demo.xml, into NiFi: https://nifi.apache.org/docs/nifi-docs/html/user-guide.html#Import_Template
- Right-click on each processor and make sure path is correct. Or you should be able to change path directly on recommender_demo.xml

3. Check and make sure env folder had the following sub-folders:
- data_predicted: NiFi flow outputs predictions to
- data_train: NiFi flow checks and gets train data files from
- models: pkl model file
- scripts: python files
- test_files: sample train and test files

RUN
---
1. NiFi
- Hit the play button to start flow

2. Train model
- Open a terminal and cd into env folder. Then run: cp test_files/train_feature_label.small.txt data_train

3. Start prediction
- Open two terminals (one for Kafka producer and the other for Kafka consumer), cd into kafka_2.11-1.1.0 folder, then run the following commands:

Terminal 1: while read line; do echo $line; sleep 1 ; done < ../env/test_files/test_feature.small.txt |  bin/kafka-console-producer.sh --broker-list localhost:9092 --topic recommender_input

Terminal 2: bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic recommender_output

You should see features and predictions displaying on the 2nd terminal.
