package de.maxbundscherer.scala.raft.aggregates

object RaftAggregate {

  import akka.actor.ActorRef

  trait Request
  trait Response

  case class InitActor(neighbours: Vector[ActorRef]) extends Request

  object RequestVote  extends Request
  object GrantVote    extends Response
  object Heartbeat    extends Request

  object BehaviorEnum extends Enumeration {
    type BehaviorEnum = Value
    val UNINITIALIZED, FOLLOWER, CANDIDATE, LEADER = Value
  }

  object SchedulerTrigger {
    object ElectionTimeout
    object Heartbeat
  }

}
