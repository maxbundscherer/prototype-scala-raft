package de.maxbundscherer.scala.raft.aggregates

object CounterAggregate {

  trait Request
  trait Response

  case class Ping(msg: String) extends Request
  case class Pong(msg: String) extends Response

}
