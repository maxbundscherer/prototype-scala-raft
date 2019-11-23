package de.maxbundscherer.scala.raft.actors

import akka.actor.{Actor, ActorLogging, Props}

object PingPongActor {

  val prefix: String = "pingPongActor"
  def props: Props   = Props(new PingPongActor())

}

class PingPongActor extends Actor with ActorLogging {

  import de.maxbundscherer.scala.raft.aggregates.PingPongAggregate._

  log.debug("Actor online")

  /**
   * Default behavior
   * @return Receive
   */
  override def receive: Receive = {

    case req: Request => processRequest(req)

    case any: Any => log.warning(s"Got unhandled message '$any'")

  }

  /**
   * Process request
   * @param req Request
   */
  private def processRequest(req: Request): Unit = req match {

    case Ping(msg) =>

      tellSender( Pong(s"$msg-pong") )

    case any: Any => log.error(s"Got unhandled request '$any'")

  }

  /**
   * Tell sender (fire and forget)
   * @param res Response
   */
  private def tellSender(res: Response): Unit = { sender ! res }

}