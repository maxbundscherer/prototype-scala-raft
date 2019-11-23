package de.maxbundscherer.scala.raft.utils

import akka.actor.ActorSystem
import akka.util.Timeout
import scala.concurrent.duration._
import org.scalatest.{AsyncFlatSpec, Matchers}

class BaseServiceTest() extends AsyncFlatSpec with Matchers {

  import de.maxbundscherer.scala.raft.services._

  private object BaseService {

    private lazy implicit val actorSystem: ActorSystem = ActorSystem("testSystem")

    private lazy implicit val timeout: Timeout = 15.seconds

    lazy val pingPongService = new PingPongService()

  }

  lazy val pingPongService: PingPongService = BaseService.pingPongService

}