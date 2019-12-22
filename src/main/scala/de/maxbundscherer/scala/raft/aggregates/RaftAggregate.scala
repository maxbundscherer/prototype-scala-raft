package de.maxbundscherer.scala.raft.aggregates

object RaftAggregate {

  import akka.actor.ActorRef

  trait Request
  trait Response

  case class InitActor(neighbours: Vector[ActorRef]) extends Request

}
