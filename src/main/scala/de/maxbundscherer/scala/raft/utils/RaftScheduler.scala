package de.maxbundscherer.scala.raft.utils

import akka.actor.{Actor, ActorLogging}

/**
  * RaftScheduler
  */
trait RaftScheduler extends Actor with ActorLogging {

  import de.maxbundscherer.scala.raft.actors.NodeActor.NodeState
  import de.maxbundscherer.scala.raft.aggregates.RaftAggregate.SchedulerTrigger
  import scala.concurrent.ExecutionContext
  import scala.concurrent.duration._

  /*
   * Set up by actor
   */
  val state: NodeState
  implicit val executionContext: ExecutionContext

  //Set electionTimeout to randomInt in [1, maxSecondsElectionTimeout]
  //TODO: Set maxSecondsElectionTimeout by config
  private val electionTimeout: Int = 1 + scala.util.Random.nextInt(5)

  log.debug(s"Set electionTimeout to $electionTimeout seconds")

  /**
    * Stop ElectionTimeoutTimer
    */
  def stopElectionTimeoutTimer(): Unit = {

    if (state.electionTimeoutTimer.isDefined) {

      log.debug("Stop ElectionTimeoutTimer")

      state.electionTimeoutTimer.get.cancel()
      state.electionTimeoutTimer = None
    }

  }

  /**
    * Start ElectionTimeoutTimer (if already running = stop)
    */
  def restartElectionTimeoutTimer(): Unit = {

    stopElectionTimeoutTimer()

    log.debug("Start ElectionTimeoutTimer")

    state.electionTimeoutTimer = Some(
      context.system.scheduler.scheduleWithFixedDelay(
        initialDelay = electionTimeout.seconds,
        delay = electionTimeout.seconds,
        receiver = self,
        message = SchedulerTrigger.ElectionTimeout
      ))

  }

}
