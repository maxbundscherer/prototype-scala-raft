package de.maxbundscherer.scala.raft.services

import akka.pattern.ask
import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import scala.concurrent.Future

class CounterService()(implicit actorSystem: ActorSystem, timeout: Timeout) {

  import de.maxbundscherer.scala.raft.actors.CounterActor
  import de.maxbundscherer.scala.raft.aggregates.CounterAggregate._

  /**
   * Private actor ref (use proxy in cluster)
   */
  private val actor: ActorRef = actorSystem.actorOf( CounterActor.props, CounterActor.prefix + "0" )

  /**
   * Ask internal actor
   * @param req Request
   * @tparam RequestType Class of Request
   * @tparam ResponseType Class of Response
   * @return Future with Response
   */
  private def askActor[RequestType, ResponseType](req: RequestType): Future[ResponseType] =
    (actor ? req).asInstanceOf[Future[ResponseType]]

  def reset()               : Future[NewValue] = askActor[Reset, NewValue]    ( Reset() )
  def increment(value: Int) : Future[NewValue] = askActor[Increment, NewValue]( Increment(value) )
  def decrement(value: Int) : Future[NewValue] = askActor[Decrement, NewValue]( Decrement(value) )

}