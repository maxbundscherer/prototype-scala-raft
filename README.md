# (Prototype) Raft Consensus Algorithm in Scala

**Protoype Raft Consensus Algorithm in Scala**

Tested on ``macOs 10.15.2`` with ``openjdk64-11.0.2`` and ``sbt 1.3.3``

[![shields.io](http://img.shields.io/badge/license-Apache2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)
![](https://github.com/maxbundscherer/prototype-scala-raft/workflows/CI%20Test/badge.svg)

Author: [Maximilian Bundscherer](https://bundscherer-online.de)

## Let's get started

- You need [sbt](https://www.scala-sbt.org/) to build and run project
- You need [openjdk64-11.0.2](https://jdk.java.net/archive/)

- Run with: ``sbt run``
- Test with: ``sbt test`` (or see ci-tests in GitHub-Actions-CI-Pipeline)

### What is implemented?

- RaftNode as Finite-state machine (**FSM**) with **key-value storage**
    - ``(Uninitialized)``: Not initialized
    - ``Follower`` (Default behavior): Waiting for heartbeats from leader-node with hashCode from data. If local stored data's hashCode is not equal to leader-node data's hashCode the node synchronizes with leader-node. If there is no heartbeat from leader-node in configured randomized interval received, the node is changing to candidate-behavior. 
    - ``Candidate``: The candidate requests votes from all followers and votes for himself. If he gets the majority in configured interval, he become the leader. If not he become follower again.
    - ``Leader``: The leader is sending continuous heartbeats to all followers with hashCode from his stored data. The leader is the only node that is allowed to write data.
    - ``(Sleep)``: Is used for simulating leader-crashes (triggered by ***crashIntervalHeartbeats*** in normal run or by ***SimulateLeaderCrash*** in test run). In this behavior the node does not respond to non-debug-messages. After configured downtime the node is changing to follower-behavior.
    
## Talk about ...

- ... enums in scala
- ... typed akka actors (and service layer alternative)
- ... fsm in akka actor
- ... difference between ``scheduleWithFixedDelay`` and ``scheduleAtFixedRate`` in akka