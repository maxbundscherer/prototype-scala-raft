package de.maxbundscherer.scala.raft.utils

import de.maxbundscherer.scala.raft.services.PingPongService

import akka.actor.ActorSystem
import akka.util.Timeout
import scala.concurrent.duration._
import org.scalatest.{AsyncFlatSpec, Matchers}

object BaseServiceTest {

  private lazy implicit val actorSystem: ActorSystem = ActorSystem("testSystem")

  private lazy implicit val timeout: Timeout = 15.seconds

  private lazy val pingPongService = new PingPongService()

}

trait BaseServiceTest extends AsyncFlatSpec with Matchers {

  val pingPongService: PingPongService = BaseServiceTest.pingPongService

}