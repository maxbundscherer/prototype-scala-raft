package de.maxbundscherer.scala.raft

import akka.actor.ActorSystem
import de.maxbundscherer.scala.raft.services.PingPongService

object Main extends App {

  private implicit val actorSystem: ActorSystem = ActorSystem("system")

  private val pingPongService = new PingPongService()

  pingPongService.ping("test")

}
