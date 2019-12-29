package de.maxbundscherer.scala.raft.utils

import de.maxbundscherer.scala.raft.services.RaftService
import org.scalatest.{AsyncWordSpec, Matchers}

object BaseServiceTest extends Configuration {

  import akka.actor.ActorSystem
  import scala.concurrent.ExecutionContextExecutor
  import scala.concurrent.duration._
  import akka.util.Timeout

  private implicit val actorSystem: ActorSystem = ActorSystem("testSystem")
  private implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher
  private implicit val timeout: Timeout = 15.seconds

  private lazy val raftService = new RaftService(numberNodes = Config.nodes)

}

trait BaseServiceTest extends AsyncWordSpec with Matchers {

  val raftService: RaftService = BaseServiceTest.raftService

}