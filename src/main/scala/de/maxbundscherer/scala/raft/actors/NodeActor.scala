package de.maxbundscherer.scala.raft.actors

import akka.actor.{Actor, ActorLogging, Props, ActorRef}

object NodeActor {

  val prefix: String  = "nodeActor"
  def props: Props    = Props(new NodeActor())

  private case class NodeState(
      var neighbours: Vector[ActorRef] = Vector.empty
  )

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
  import de.maxbundscherer.scala.raft.aggregates.RaftAggregate.BehaviorEnum.BehaviorEnum

  /**
    * Mutable actor state
    */
  private val state = NodeState()

  log.debug("Actor online (uninitialized)")

  /**
   * Change actor behavior
   * @param fromBehavior Behavior
   * @param toBehavior Behavior
   * @param loggerMessage String (logging)
   */
  private def changeBehavior(fromBehavior: BehaviorEnum,
                             toBehavior: BehaviorEnum,
                             loggerMessage: String): Unit = {

    log.debug(s"Change behavior from '$fromBehavior' to '$toBehavior' ($loggerMessage)")

    val newBehavior: Receive = toBehavior match {
      case BehaviorEnum.FOLLOWER      => this.followerBehavior
      case BehaviorEnum.CANDIDATE     => this.candidateBehavior
      case BehaviorEnum.LEADER        => this.followerBehavior
      case _                          => this.receive
    }

    this.context.become(newBehavior)

  }

  /**
    * Uninitialized behavior
    */
  override def receive: Receive = {

    case InitActor(neighbours) =>

      this.state.neighbours = neighbours

      this.changeBehavior(fromBehavior = BehaviorEnum.UNINITIALIZED,
                          toBehavior = BehaviorEnum.FOLLOWER,
                          loggerMessage = s"Got ${this.state.neighbours.size} neighbours")

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
