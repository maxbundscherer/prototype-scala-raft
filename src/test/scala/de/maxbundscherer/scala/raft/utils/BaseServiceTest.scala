package de.maxbundscherer.scala.raft.utils

import de.maxbundscherer.scala.raft.services.CounterService

import akka.actor.ActorSystem
import akka.util.Timeout
import scala.concurrent.duration._
import org.scalatest.{AsyncWordSpec, Matchers}

object BaseServiceTest {

  private lazy implicit val actorSystem: ActorSystem = ActorSystem("testSystem")

  private lazy implicit val timeout: Timeout = 15.seconds

  private lazy val counterService = new CounterService()

}

trait BaseServiceTest extends AsyncWordSpec with Matchers {

  val counterService: CounterService = BaseServiceTest.counterService

}