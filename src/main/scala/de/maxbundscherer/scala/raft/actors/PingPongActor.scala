package de.maxbundscherer.scala.raft.actors

import akka.actor.{Actor, ActorLogging, Props}

object PingPongActor {

  val prefix: String = "pingPongActor"
  def props: Props   = Props(new PingPongActor())

}

class PingPongActor extends Actor with ActorLogging {

  import de.maxbundscherer.scala.raft.aggregates.PingPongAggregate._

  log.debug("Actor online!")

  override def receive: Receive = {

    case req: Request =>

      log.debug(s"Got request '$req'")

      req match {
        case Ping(msg) => tellSender( Pong(msg) )
      }

    case any: Any =>

      log.warning(s"Got unhandled request '$any'")

  }

  private def tellSender(res: Response): Unit = { sender() ! res }

}