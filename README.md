# (Prototype) Raft Consensus Algorithm in Scala

**Protoype [Raft Consensus](https://raft.github.io/raft.pdf) Algorithm in Scala**

![](./docImg/logos.png)

Tested on ``macOs 10.15.2`` with ``openjdk64-11.0.2`` and ``sbt 1.3.3``

[![shields.io](http://img.shields.io/badge/license-Apache2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)
![](https://github.com/maxbundscherer/prototype-scala-raft/workflows/CI%20Test/badge.svg)

Test line-coverage: 88,11% ([12-30-2019](./docImg/test-report-12-30-2019.zip))

Author: [Maximilian Bundscherer](https://bundscherer-online.de)

## Let's get started

- [sbt](https://www.scala-sbt.org/) and [openjdk64-11.0.2](https://jdk.java.net/archive/) are required to build and run project

- Run with: ``sbt run`` (see ***What happens in normal run?*** below)
- Test with: ``sbt test`` (or see ci-tests in GitHub-Actions-CI-Pipeline) (see ***What happens in test run?*** below)
- Generate test-coverage-html-report with: ``sbt jacoco``

### Used dependencies

- [akka actors](https://doc.akka.io/docs/akka/current/actors.html): Actor model implementation (scala/java).
- [scalactic](http://www.scalactic.org/): Test kit for scala.
- [sbt-jacoco](https://github.com/sbt/sbt-jacoco): SBT plugin for generating coverage-reports.

### What is implemented?

- RaftNode as Finite-state machine (**FSM**) with **key-value storage**

    - ``(Uninitialized)``: Not initialized
    - ``Follower`` (Default behavior): Waiting for heartbeats from leader-node with hashCode from data. If local stored data's hashCode is not equal to leader-node data's hashCode the node synchronizes with leader-node. If there is no heartbeat from leader-node in configured randomized interval received, the node is changing to candidate-behavior. 
    - ``Candidate``: The candidate requests votes from all followers and votes for himself. If he gets the majority in configured interval, he becomes the leader. If not he becomes follower again.
    - ``Leader``: The leader is sending continuous heartbeats to all followers with hashCode from his stored data. The leader is the only node that is allowed to write data.
    - ``(Sleep)``: Is used for simulating leader-crashes (triggered by crashIntervalHeartbeats in normal run or by SimulateLeaderCrash in test run). In this behavior the node does not respond to non-debug-messages. After configured downtime the node is changing to follower-behavior.
    
![](./docImg/raftFsm.png)

#### Configuration

There are two configurations:

- ``./src/main/resources/application.conf`` used for normal run
- ``./src/test/resources/application.conf`` used for test run
    
```
akka {

    # Log Level (DEBUG, INFO, WARNING, ERROR)
    loglevel = "DEBUG"

}

raftPrototype {

    # Election Timer Min (Seconds)
    electionTimerIntervalMin = 2

    # Election Timer Max (Seconds)
    electionTimerIntervalMax = 3

    # Heartbeat Timer Interval (Seconds)
    heartbeatTimerInterval = 1

    # Raft Nodes (Amount)
    nodes = 5

    # Crash Interval (auto simulate crash after some heartbeats in LEADER behavior)
    crashIntervalHeartbeats = 10

    # Sleep downtime (Seconds) (after simulated crash in SLEEP behavior)
    sleepDowntime = 8

}
```

### What happens in normal run?

All nodes starts in follower behavior (some of them will change their behavior to candidate) and will elect the first leader. After some (configured) heartbeats from leader, the leader is simulating crash and is "sleeping" for configured downtime. The next leader will be elected.

This happens again and again and again... till you stop the program or the earth is going to overheat ;)

Data exchange (write data trough leader to followers) will be tested in test run (see below).

### What happens in test run?

1. Leader election (after init nodes)
2. Write data trough leader to followers (first write data to leader and replicate data to followers)
3. Get back data from all nodes (all nodes should have same data)
4. Simulate leader crash (triggered in test)
5. New leader election (old leader is gone)
6. Write data trough leader to followers (first write data to leader and replicate data to followers)
7. Get back data from all nodes (all nodes should have same data)


The ***integration-test*** is well documented - it's self explaining:

- ``./src/test/scala/de/maxbundscherer/scala/raft/RaftServiceTest.scala``

## Exciting (scala) stuff

Concurrent programming in scala is usually done with akka actors. Akka actors is an actor model implementation for scala and java. Akka is developed/maintained by [Lightbend](https://www.lightbend.com/) (earlier called Typesafe).

The program and business logic is divided into separated actors. Each of these actors has its own state (own protected memory) and can only communicate with other actors by immutable messages.

![](./docImg/ActorModel.png)
(Image source: https://blog.scottlogic.com/2014/08/15/using-akka-and-scala-to-render-a-mandelbrot-set.html)

### Akka Actors Example

```scala
package de.maxbundscherer.scala.raft.examples

import akka.actor.{Actor, ActorLogging}

class SimpleActor extends Actor with ActorLogging {
  
  override def receive: Receive = {

    case data: String =>
      
      sender ! data + "-pong"

    case any: Any =>
      
      log.error(s"Got unhandled message '$any'")
      
  }
  
}
```

In this example you see a very simple akka actor: The actor is waiting for string-messages and replies with a new string (``!`` is used for [fire-and-forget-pattern](https://doc.akka.io/docs/akka/current/typed/interaction-patterns.html#fire-and-forget) / use ``?`` to use [ask-pattern](https://doc.akka.io/docs/akka/current/typed/interaction-patterns.html#request-response-with-ask-from-outside-an-actor) instead).

Non-string-messages are displayed by an error-logger.

### Raft nodes as akka actors

In this project raft nodes are implemented as an akka actor (``RaftNodeActor``) with finite-state machine (FSM) behavior (see description and image above).

#### Finite-state machine (FSM) in akka

You can define multiple behaviors in an akka actor - see example:

```scala
package de.maxbundscherer.scala.raft.examples

import akka.actor.{Actor, ActorLogging}

object SimpleFSMActor {
  
  //Initialize message/command
  case class Initialize(state: Int)
  
}

class SimpleFSMActor extends Actor with ActorLogging {

  import SimpleFSMActor._
  
  //Actor mutable state
  private var state = -1

  //Initialized behavior 
  def initialized: Receive = {

    case any: Any => log.info(s"Got message '$any'")
    
  }

  //Default behavior
  override def receive: Receive = {

    case Initialize(newState) =>

      state = newState
      context.become(initialized)

    case any: Any => log.error(s"Not initialized '$any'")

  }

}
```

#### Service-Layer

Classic akka actors are not type safety. To "simulate" type safety the service-layer (``RaftService``) was implemented. The service-layer is also used to spawn & initialize actors and to supervise the actor system - see examples:

- Spawn akka actor:
```scala
actorSystem.actorOf(props = RaftNodeActor.props, name = "myRaftNode")
```

- Ask (type safety non-blocking request):
```scala
def ping(): Future[Pong] = {
  ( actorRef ? Ping() ).asInstanceOf[Future[Pong]]
}
```

### Aggregates

tbd.

(TODO: enums in scala)

#### ``Configuration`` trait

tbd.

#### ``RaftScheduler`` trait

tbd.

(TODO: difference between ``scheduleWithFixedDelay`` and ``scheduleAtFixedRate`` in akka)

#### Service Configurator Pattern

tbd.

(TODO: [Based on](https://www.usenix.org/legacy/publications/library/proceedings/coots97/full_papers/jain/jain.pdf))

## Prospects

tbd.

(TODO: Akka cluster / Disable auto test in normal run / java serializer in production)