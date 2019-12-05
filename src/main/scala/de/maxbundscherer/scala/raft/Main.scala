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

  counterService.reset().map(response =>
    log.info(s"Got response from reset '$response'")
  )

  counterService.increment(5).map(response =>
    log.info(s"Got response from increment '$response'")
  )

  counterService.decrement(4).map(response => {
    log.info(s"Got response from decrement '$response'")
    actorSystem.terminate()
  })

}