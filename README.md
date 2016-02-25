# SpringBootDataRestExample
Example of Spring Boot Data Rest app showing JSON usage from JUG meetup on 22nd Apr 2015

Instructions to follow during the DEMO:

Singlenode:
1. Start xd-singlenode and xd-shell
2. Show xd-shell ‘help' and ‘module list’ and ’stream list’
3. stream create --name timeToLog --definition "time | log"
4. stream list
5. stream deploy --name timeToLog
6. Show logs of container and XD UI
7. stream undeploy --name timeToLog
8. Show logs of container and XD UI
9. stream create --name httpToFile --definition "http | file —dir=/data/xd" —deploy
http post --target http://localhost:9000 --data "Hello JUG”
http post --target http://localhost:9000 --data "Hello from XD stream"
10. Show files created 

Distributed:
1. Start Zookeper (./zkServer.sh start)
2. Start ./hsqldb-server
3. Start ./xd-admin
4. Start ./xd-container
5. Start ./xd-container
6. Show XD UI with all streams came from Zookeeper
7. Show Zookeeper data:
./zkCli.sh
ls /xd
ls /xd/modules/source
ls /xd/streams
ls /xd/deployments/streams
8. Deploy timeToLog stream and show Redis exceptions
9. Start Redis distributed with Spring XD (./redis-server)
stream deploy timeToLog
10. Show modules deployed to diff containers
11. Stop one module and show recovery

Taps:
1. Show counter usage as tap:
stream create --name timeToLogTap --definition "tap:stream:timeToLog > counter --name=simpleCounter" —deploy
stream deploy timeToLog
counter list
counter display simpleCounter
2. Show XD UI and zookeeper (ls /xd/taps)

Hadoop integration:
1. Start HDFS (sbin/start-dfs.sh)
2. Show Hadoop UI (http://localhost:50070/dfshealth.html)
3. Show HDFS content:
./hdfs dfs -ls /xd
4. Show HDFS console from XD shell
hadoop config fs --namenode hdfs://localhost:8020
hadoop fs ls /xd
5. Deploy twitterSearchToHDFS stream:
create stream --name twitterToHdfs —definition “twittersearch --query='java' | hdfs --partitionPath=payload.lang --rollover=1M --idleTimeout=10000”
stream deploy twitterToHdfs
hadoop fs ls /xd
stream undeploy twitterToHdfs
hadoop fs rm -r /xd/twitterToHdfs

Final example (works only on xd-singlenode because of some BUG with headers population in distributed mode, however anyway required header-enricher custom module to be installed):
1. module delete processor:tweet-transformer
2. module upload --file /Users/oleg/dev/Projects/JavaDayKiev2015/SpringXDCustomTweetsTransformer/target/tweets-transformer-1.0-SNAPSHOT.jar --name tweet-transformer --type processor
3. stream create --name tweetsIngestion --definition "twittersearch --query=java --outputType=application/json | filter --expression=#jsonPath(payload,'$.lang').equals('en') | header-enricher --headers='{\"Content-Type\":\"''application/json;charset=UTF-8''\"}' | http-client --url='''http://localhost:8080/tweets''' --httpMethod=POST | null"
4. stream create --name debugForTweetsIngestion --definition "tap:stream:tweetsIngestion > tweet-transformer --extractField=id | log" --deploy
5. stream create --name tweetsIngestionCount --definition “tap:stream:tweetsIngestion > field-value-counter --fieldName=lang --name=countByLanguage” --deploy
6. stream create --name tweetsToHdfs --definition “tap:stream:tweetsIngestion > hdfs --partitionPath=payload.lang --rollover=1M --idleTimeout=10000” --deploy
7. stream deploy tweetsIngestion
