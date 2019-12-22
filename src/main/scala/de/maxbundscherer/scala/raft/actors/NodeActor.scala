package de.maxbundscherer.scala.raft.actors

import akka.actor.{Actor, ActorLogging, Props, ActorRef}

object NodeActor {

  val prefix: String  = "nodeActor"
  def props: Props    = Props(new NodeActor())

  private case class NodeState(neighbours: Vector[ActorRef] = Vector.empty) {

    def initNeighbours(neighbours: Vector[ActorRef]): NodeState =
      copy(neighbours = neighbours)

  }

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

  /**
    * Mutable actor state
    */
  private var state = NodeState()

  log.debug("Actor online (uninitialized)")

  /**
    * Change actor behavior
    * @param fromBehaviorLabel String (logging)
    * @param toBehaviorLabel String (logging)
    * @param toBehavior Behavior
    * @param loggerMessage String (logging)
    */
  private def changeBehavior(fromBehaviorLabel: String,
                             toBehaviorLabel: String,
                             toBehavior: Receive,
                             loggerMessage: String): Unit = {

    log.debug(s"Change behavior from '$fromBehaviorLabel' to '$toBehaviorLabel' ($loggerMessage)")
    this.context.become(toBehavior)

  }

  /**
    * Uninitialized behavior
    */
  override def receive: Receive = {

    case InitActor(neighbours) =>

      this.state = this.state initNeighbours neighbours

      this.changeBehavior(fromBehaviorLabel = "uninitialized",
                          toBehaviorLabel = "follower",
                          toBehavior = followerBehavior,
                          loggerMessage = s"Got ${neighbours.size} neighbours")

    case _: Any => log.error("Node is not initialized")

  }

  /**
    * Raft FOLLOWER
    */
  def followerBehavior: Receive = {

    case any: Any =>
      log.error(s"Got unhandled message in followerBehavior '$any'")

  }

  /**
    * Raft CANDIDATE
    */
  def candidateBehavior: Receive = {

    case any: Any =>
      log.error(s"Got unhandled message in candidateBehavior '$any'")

  }

  /**
    * Raft LEADER
    */
  def leaderBehavior: Receive = {

    case any: Any =>
      log.error(s"Got unhandled message in leaderBehavior '$any'")

  }

  /**
    * Tell sender (fire and forget)
    * @param res Response
    */
  private def tellSender(res: Response): Unit = { sender ! res }

}
