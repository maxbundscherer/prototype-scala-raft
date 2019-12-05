package de.maxbundscherer.scala.raft.aggregates

object CounterAggregate {

  trait Request
  trait Response

  case class Reset()                extends Request
  case class Increment(value: Int)  extends Request
  case class Decrement(value: Int)  extends Request

  case class NewValue(value: Int)   extends Response

}