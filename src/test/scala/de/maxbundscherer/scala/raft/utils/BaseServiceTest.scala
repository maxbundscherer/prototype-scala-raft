package de.maxbundscherer.scala.raft.utils

import akka.actor.ActorSystem
import akka.util.Timeout
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._

class BaseServiceTest() extends FlatSpec with Matchers {

  import de.maxbundscherer.scala.raft.services._

  object BaseService {

    lazy implicit val actorSystem: ActorSystem = ActorSystem("system")

    private lazy implicit val timeout: Timeout = 10.seconds

    lazy val pingPongService = new PingPongService()

  }

  implicit val executionContext: ExecutionContextExecutor = BaseService.actorSystem.dispatcher

  lazy val pingPongService: PingPongService = BaseService.pingPongService

}