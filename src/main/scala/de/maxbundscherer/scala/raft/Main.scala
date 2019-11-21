package de.maxbundscherer.scala.raft

import akka.actor.ActorSystem

object Main extends App {

  import de.maxbundscherer.scala.raft.services._

  private implicit val actorSystem: ActorSystem = ActorSystem("system")

  private val pingPongService = new PingPongService()

  pingPongService.ping("test")

}
