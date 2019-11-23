package de.maxbundscherer.scala.raft.services

import akka.pattern.ask
import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import scala.concurrent.Future

class PingPongService()(implicit actorSystem: ActorSystem, timeout: Timeout) {

  import de.maxbundscherer.scala.raft.actors.PingPongActor
  import de.maxbundscherer.scala.raft.aggregates.PingPongAggregate._

  private val actor: ActorRef = actorSystem.actorOf( PingPongActor.props, PingPongActor.prefix + "0" )

  /**
   * Ask internal actor
   * @param req Request
   * @tparam RequestType Class of Request
   * @tparam ResponseType Class of Response
   * @return Future with Response
   */
  private def askActor[RequestType, ResponseType](req: RequestType): Future[ResponseType] =
    (actor ? req).asInstanceOf[Future[ResponseType]]

  def ping(msg: String): Future[Pong] = askActor( Ping(msg) )

}