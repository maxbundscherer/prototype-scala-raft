package de.maxbundscherer.scala.raft.actors

import akka.actor.{Actor, ActorLogging, Props}

object NodeActor {

  val prefix: String  = "nodeActor"
  def props: Props    = Props(new NodeActor())

}

/**
  * ------------------
  * --- Raft Node ----
  * ------------------
  *
  * # 3 Behaviors (Finite-state machine)
  *
  * - FOLLOWER (Default)
  * - LEADER
  * - CANDIDATE
  */
class NodeActor extends Actor with ActorLogging {

  import NodeActor._
  import de.maxbundscherer.scala.raft.aggregates.RaftAggregate._

  log.debug("Actor online")

  /**
    * Set default behavior to FOLLOWER
    */
  override def receive: Receive = followerBehavior

  /**
    * Raft FOLLOWER
    */
  def followerBehavior: Receive = {

    case req: Request =>
      log.warning(s"Got unhandled request in followerBehavior '$req'")

    case any: Any =>
      log.error(s"Got unhandled message in followerBehavior '$any'")

  }

  /**
    * Raft CANDIDATE
    */
  def candidateBehavior: Receive = {

    case req: Request =>
      log.warning(s"Got unhandled request in candidateBehavior '$req'")

    case any: Any =>
      log.error(s"Got unhandled message in candidateBehavior '$any'")

  }

  /**
    * Raft LEADER
    */
  def leaderBehavior: Receive = {

    case req: Request =>
      log.warning(s"Got unhandled request in candidateBehavior '$req'")

    case any: Any =>
      log.error(s"Got unhandled message in leaderBehavior '$any'")

  }

  /**
    * Tell sender (fire and forget)
    * @param res Response
    */
  private def tellSender(res: Response): Unit = { sender ! res }

}
