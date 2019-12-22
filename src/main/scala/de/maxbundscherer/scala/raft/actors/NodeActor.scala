package de.maxbundscherer.scala.raft.actors

import de.maxbundscherer.scala.raft.utils.RaftScheduler
import akka.actor.{Actor, ActorLogging, ActorRef}

object NodeActor {

  import akka.actor.{Cancellable, Props}

  val prefix  : String  = "nodeActor"
  def props   : Props   = Props(new NodeActor())

  case class NodeState(
      var neighbours            : Vector[ActorRef]    = Vector.empty,
      var electionTimeoutTimer  : Option[Cancellable] = None
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
class NodeActor extends Actor with ActorLogging with RaftScheduler {

  import NodeActor._
  import de.maxbundscherer.scala.raft.aggregates.RaftAggregate._
  import de.maxbundscherer.scala.raft.aggregates.RaftAggregate.BehaviorEnum.BehaviorEnum
  import scala.concurrent.ExecutionContext

  /**
    * Mutable actor state
    */
  override val state = NodeState()
  override implicit val executionContext: ExecutionContext = context.system.dispatcher

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

      case BehaviorEnum.FOLLOWER =>

        restartElectionTimeoutTimer()
        followerBehavior

      case BehaviorEnum.CANDIDATE =>

        stopElectionTimeoutTimer()
        candidateBehavior

      case BehaviorEnum.LEADER =>

        stopElectionTimeoutTimer()
        followerBehavior

      case _ =>

        stopElectionTimeoutTimer()
        receive
    }

    context.become(newBehavior)

  }

  /**
    * Uninitialized behavior
    */
  override def receive: Receive = {

    case InitActor(neighbours) =>

      state.neighbours = neighbours

      changeBehavior(fromBehavior = BehaviorEnum.UNINITIALIZED,
                     toBehavior = BehaviorEnum.FOLLOWER,
                     loggerMessage = s"Got ${state.neighbours.size} neighbours")

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
