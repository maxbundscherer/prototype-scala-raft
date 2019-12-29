package de.maxbundscherer.scala.raft.utils

trait Configuration {

  object Config {

    import com.typesafe.config.ConfigFactory

    private val raftPrototypeConfig = ConfigFactory.load("raftPrototype")

    //Election Timer Min (Seconds)
    val electionTimerIntervalMin: Int = raftPrototypeConfig.getInt("electionTimerIntervalMin")

    //Election Timer Max (Seconds)
    val electionTimerIntervalMax: Int = raftPrototypeConfig.getInt("electionTimerIntervalMax")

    //Heartbeat Timer Interval (Seconds)
    val heartbeatTimerInterval: Int = raftPrototypeConfig.getInt("heartbeatTimerInterval")

    //Raft Nodes (Amount)
    val nodes: Int = raftPrototypeConfig.getInt("nodes")

    //Crash Interval (auto simulate crash after some heartbeats in LEADER behavior)
    val crashIntervalHeartbeats: Int = raftPrototypeConfig.getInt("crashIntervalHeartbeats")

  }

}