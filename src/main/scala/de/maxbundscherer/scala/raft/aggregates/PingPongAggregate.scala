package de.maxbundscherer.scala.raft.aggregates

object PingPongAggregate {

  trait Request
  trait Response

  case class Ping(msg: String) extends Request
  case class Pong(msg: String) extends Response

}
