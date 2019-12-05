package de.maxbundscherer.scala.raft

import akka.actor.ActorSystem
import akka.util.Timeout
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._

object Main extends App {

  import de.maxbundscherer.scala.raft.services._

  private implicit val actorSystem: ActorSystem = ActorSystem("system")
  private implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher
  private implicit val timeout: Timeout = 15.seconds

  private val log = actorSystem.log

  private val counterService = new CounterService()

  counterService.ping("test")
    .map(
      p => log.info(s"Got response '$p'")
    )

}