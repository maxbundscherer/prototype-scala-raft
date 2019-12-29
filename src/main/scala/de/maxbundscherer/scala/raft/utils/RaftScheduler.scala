package de.maxbundscherer.scala.raft.utils

import akka.actor.{Actor, ActorLogging}

/**
  * RaftScheduler
  */
trait RaftScheduler extends Actor with ActorLogging with Configuration {

  import de.maxbundscherer.scala.raft.actors.NodeActor.NodeState
  import de.maxbundscherer.scala.raft.aggregates.RaftAggregate.SchedulerTrigger
  import scala.concurrent.ExecutionContext
  import scala.concurrent.duration._

  /*
   * Set up by actor
   */
  val state: NodeState
  implicit val executionContext: ExecutionContext

  //Set electionTimeout randomized in [electionTimerIntervalMin, electionTimerIntervalMax]
  private val electionTimeout : Int = Config.electionTimerIntervalMin + scala.util.Random.nextInt(Config.electionTimerIntervalMax)

  //Set heartbeat to fixed interval
  private val heartbeatTimeout: Int = Config.heartbeatTimerInterval

  log.debug(s"Set electionTimeout to $electionTimeout seconds")
  log.debug(s"Set heartbeatTimeout to $heartbeatTimeout seconds")

  /**
    * Stop electionTimer
    */
  def stopElectionTimer(): Unit = {

    if (state.electionTimer.isDefined) {
      log.debug("Stop electionTimer")
      state.electionTimer.get.cancel()
      state.electionTimer = None
    }

  }

  /**
    * Start electionTimer (if already running = stop timer)
    */
  def restartElectionTimer(): Unit = {

    stopElectionTimer()

    log.debug("Start electionTimer")

    state.electionTimer = Some(
      context.system.scheduler.scheduleWithFixedDelay(
        initialDelay = electionTimeout.seconds,
        delay = electionTimeout.seconds,
        receiver = self,
        message = SchedulerTrigger.ElectionTimeout
      ))

  }

  /**
    * Stop heartbeatTimer
    */
  def stopHeartbeatTimer(): Unit = {

    if (state.heartbeatTimer.isDefined) {
      log.debug("Stop heartbeatTimer")
      state.heartbeatTimer.get.cancel()
      state.heartbeatTimer = None
    }

  }

  /**
    * Start heartbeatTimer (if already running = stop timer)
    */
  def restartHeartbeatTimer(): Unit = {

    stopHeartbeatTimer()

    log.debug("Start heartbeatTimer")

    state.heartbeatTimer = Some(
      context.system.scheduler.scheduleWithFixedDelay(
        initialDelay = heartbeatTimeout.seconds,
        delay = heartbeatTimeout.seconds,
        receiver = self,
        message = SchedulerTrigger.Heartbeat
      ))

  }

}
