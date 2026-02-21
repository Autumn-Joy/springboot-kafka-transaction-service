# Midas
Project repo for the JPMC Advanced Software Engineering Forage program

---

# Project notes
**Autumn Hicks | SWE, Java track @ WGU | Class of 2026**

*~ 02/20/2026 ~*

## Project Context



## Task 1: Clone Repo and Add Project Dependencies

- test 1 was failing because of the mis-configured application.yml originally.
- step 1: read the test and figure out what it's trying to test.

learning some tips:
- use documentation to verify project dependencies
  - Spring Initializr
  - Maven
- run one test at a time instead of all of them at once
  - at the start of a project when the tests are already written, we're working on getting one working at a time.
  - test-driven development: red, green, refactor.
- SDKMan with ZSH seems to work more smoothly with Java than Homebrew did with Fish (on Mac)

## Task 2:

This task is all about Kafka, so I'm starting by reading some of the documentation to get familiar with the context of the task.

### ( ** detour to go learn about Kafka so i know a little bit more about what i'm even trying to do for this task ** ...)

https://kafka.apache.org/42/getting-started/introduction/

key terms: 
- Kafka
- event streaming
  - publish/subscribe (pub/sub) design pattern
  - store streams of events
  - process streams of events

reading or writing data to kafka is called an event
- key, value, timestamp, and optional other information (metadata headers)
- (sounds like JSON over HTTP to me)

- producers write events to kafka
- consumers read and process the events

A key aspect of the pub/sub design is that the
producers and consumers are not tied to each other (decoupled).
they can function without depending on each other.

Events are organized into topics.
example: topics are like the folder and the files in the folder are the events.

for storage (?), topics are partitioned. for fault tolerance and high availibility, topics can be replicated (configuration options decide how replicated).

Kafka APIs:
- admin
- producer
- consumer
- Kafka Streams
- Kafka Connect

https://kafka.apache.org/quickstart/
https://www.youtube.com/watch?v=vHbvbwSEYGo&t=1s
(this video is priceless intro to Kafka wow.)

programs store information in databases.
databases encourage us to think in terms of things and state.
but instead of thinking of things first, what if we think of events first?
events have things included in them.

instead of storing events in databases,
we write them into logs.

Kafka manages those logs (topics).
topics are order collections of events.
each event represents a thing happening in the business.

when databases ruled the world,
large monoliths were the norm.

but thinking of events first and things second
leads to lots of smaller programs that can work independently
but that can also communicate through something like Kafka.

on top of those, 
we can build new services that provide real time analysis of that data
(just consuming messages from the topics in real time)
as opposed to running batch processes (over night etc.)

getting Kafka to talk with the databases can be done with Kafka Connect.


read (input)
process
write (output)

just a few general categories of what you like to do with those events: grouping, aggregating, enriching (joins).

Kafka Streams handles that kind of processing of the events.

### ( ** detour done, back to the task instructions** )

"your task is to integrate Kafka into Midas Core (this application)"

the application needs a way to receive all incoming transactions.
this will be the listener (consumer) for the Kafka topic.

the name of the topic has already been configured in the application.yml file.



configuring a Kafka listener
- deserialize the incoming message into the Transaction class (what does deserialize mean)
  - one type of deserialization: JSON to Java object (referencing the JSON Serializer and Deserializer)
  - https://medium.com/simform-engineering/kafka-integration-made-easy-with-spring-boot-b7aaf44d8889
- this task focuses *only* on integrating Kafka into Midas Core
- the test basically evaluates whether we're successfully receiving transactions (messages from Kafka)

### (** detour to go learn about Spring for Apache Kafka and Kafka Streams ** ...)


https://spring.io/projects/spring-kafka

https://docs.spring.io/spring-kafka/reference/quick-tour.html

we'll be using the KafkaListener annotation to configure the listener.
````
    @KafkaListener(id = "myId", topics = "topic1")
    public void listen(String in) {
        System.out.println(in);
    }
````

https://docs.spring.io/spring-kafka/reference/kafka/receiving-messages.html




https://docs.spring.io/spring-kafka/docs/3.1.4/api/

https://www.confluent.io/learn/spring-boot-kafka

"Spring Boot streamlines Kafka integration 
by leveraging the Spring Kafka library, 
which offers annotations and configurations for 
Kafka producers, consumers, and listeners.
It automates much of the boilerplate setup, 
such as connector management and serialization, 
allowing developers to focus on business logic."


* check the pom.xml for the correct dependencies *

- it looks like this task is not asking me to create a producer at all.
- the test actually spins up a producer and gives something to "listen to" (other parts developed later)

**another detour: what architecture is this project using? Not MVC**

![img_1.png](img_1.png)
https://medium.com/@cedric-lekene/breaking-the-mvc-mold-clean-hexagonal-architecture-ea7b5a30e32a

https://kitemetric.com/blogs/hexagonal-vs-mvc-java-spring-boot-guide
```
hexagonal-architecture-example/
├── src/
│   ├── main/
│   │   ├── java/com/exemplo/
│   │   │   ├── domain/           // Core business logic
│   │   │   ├── application/      // Use cases
│   │   │   ├── infrastructure/   // Infrastructure components
│   │   │   │   ├── repository/  // Data persistence
│   │   │   │   ├── controller/  // REST API
│   │   │   ├── HexagonalApplication.java
│   ├── test/
```

it doesn't quite follow this one either though.

### A core question: where to integrate Kafka?

putting the Kafka listener directly into `Transaction` would violate the design principle 
that each class should have a single responsibility.

the Kafka listener doesn't live in the Transaction object.
It **produces** a Transaction object ...
through deserialization!!

whatever the producer is sending, we're taking that form and turning it into a Transaction object.

since this is a service-type component, 
i can put the Kafka listener in the component package.



okay. now we have a basic Kafka listener implemented in the  `component` package.

next we need to learn what this even means: "deserialize kafka message to java object"

- JSON deserialization with Jackson
- https://www.baeldung.com/jackson-json-deserialization
- https://www.baeldung.com/jackson-json-serialization


https://docs.spring.io/spring-kafka/reference/kafka/serdes.html

```aiignore
JacksonJsonDeserializer<Thing> thingDeserializer = new JacksonJsonDeserializer<>(Thing.class);
```

but are we actually deserializing from a string format?  

the tests for task2 ask for:

```
String[] transactionLines = fileLoader.loadStrings("/test_data/poiuytrewq.uiop");
```




https://www.youtube.com/watch?v=JGEo6mHu-2c
it looks like it *was* supposed to be the application.yml that should have been modified next
to handle the deserialization



https://www.confluent.io/learn/spring-boot-kafka/
this is the example application.yml file from the confluent tutorial:
```aiignore
spring:
  kafka:
    bootstrap-servers: < BOOTSTRAP SERVERS >
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
    consumer:
      group-id: my-group
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
```

question:
- does this section of the application.yml file replace a full configuration file (for simple versions)?

we probably don't need the producer section for this part, since it's only focused on 
the consumer aspect?

(note: a listener is a high-level type of consumer.)

questions I'm asking:
- according to the test, what bootstrap server should be configured? (error i'm getting as i run the tests)

- origiinally, the application.yml file only had StringSerializer and StringDeserializer.
- but the test is actually requiring JSON serialization and deserialization in order to deserialize to `Transaction` objects.

updated version of the `application.yml` file:
```
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
    consumer:
      group-id: my-group
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.JsonDeserializer
```

