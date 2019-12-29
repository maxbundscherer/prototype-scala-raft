package de.maxbundscherer.scala.raft

import de.maxbundscherer.scala.raft.utils.Configuration
import akka.actor.ActorSystem
import akka.util.Timeout
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._

object Main extends App with Configuration {

  import de.maxbundscherer.scala.raft.services._

  private implicit val actorSystem: ActorSystem = ActorSystem("raftSystem")
  private implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher
  private implicit val timeout: Timeout = 15.seconds

  private val log = actorSystem.log

  private val raftService = new RaftService(numberNodes = Config.nodes)

}