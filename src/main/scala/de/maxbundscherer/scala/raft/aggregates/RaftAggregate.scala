package de.maxbundscherer.scala.raft.aggregates

object RaftAggregate {

  import akka.actor.ActorRef

  trait Request
  trait Response

  case class InitActor(neighbours: Vector[ActorRef]) extends Request

  object RequestVote  extends Request
  object GrantVote    extends Response

  case class  Heartbeat(lastHashCode: Int)            extends Request
  object      IamNotConsistent                        extends Response
  case class  OverrideData(data: Map[String, String]) extends Request

  case class  GetActualData(data: Map[String, String]) extends Request
  case class  ActualData(data: Map[String, String])    extends Response

  object      WhoIsLeader                         extends Request
  case class  IamTheLeader(actorName: String)     extends Response
  case class  IamNotTheLeader(actorName: String)  extends Response

  object      SimulateLeaderCrash                         extends Request
  case class  LeaderIsSimulatingCrash(actorName: String)  extends Response

  case class  AppendData(key: String, value: String)  extends Request
  case class  WriteSuccess(actorName: String)         extends Response

  //FSM States (RaftNodeActor)
  object BehaviorEnum extends Enumeration {
    type BehaviorEnum = Value
    val UNINITIALIZED, FOLLOWER, CANDIDATE, LEADER, SLEEP = Value
  }

  //Used by RaftScheduler
  object SchedulerTrigger {
    object ElectionTimeout
    object Heartbeat
    object Awake
  }

}