package de.maxbundscherer.scala.raft

import de.maxbundscherer.scala.raft.utils.{BaseServiceTest, Configuration}

class RaftServiceTest extends BaseServiceTest with Configuration {

  import de.maxbundscherer.scala.raft.aggregates.RaftAggregate._

  /**
   * Freeze test (actorSystem is still working)
   * @param seconds Int
   * @param loggerMessage String
   */
  private def freezeTest(seconds: Int, loggerMessage: String): Unit = {

    log.warning(s"Test is in sleepMode for $seconds seconds ($loggerMessage)")
    Thread.sleep(seconds * 1000)
    log.warning(s"Test continues")

  }

  "RaftService" should {

    var temporaryFirstLeaderName: String = ""

    "elect only one leader" in {

      freezeTest(seconds = 20, loggerMessage = "Waiting for first leader election")

      val data: Vector[Either[IamTheLeader, IamNotTheLeader]] = raftService.evaluateActualLeaders

      val localLeaderName = data.filter(_.isLeft).head match {
        case Left(left) => left.actorName
        case _ => ""
      }

      temporaryFirstLeaderName = localLeaderName

      data.count(_.isLeft)  shouldBe 1 //Only on leader
      data.count(_.isRight) shouldBe ( Config.nodes - 1 ) //Other nodes shouldBe follower
    }

    "simulate leader crash" in {

      val data: Vector[Either[LeaderIsSimulatingCrash, IamNotTheLeader]] = raftService.simulateLeaderCrash()

      val localLeaderName = data.filter(_.isLeft).head match {
        case Left(left) => left.actorName
        case _ => ""
      }

      localLeaderName       shouldBe temporaryFirstLeaderName
      data.count(_.isLeft)  shouldBe 1 //Only on leader
      data.count(_.isRight) shouldBe ( Config.nodes - 1 ) //Other nodes shouldBe follower
    }


    "elect new leader after leader crash" in {

      freezeTest(seconds = 20, loggerMessage = "Waiting for second leader election")

      val data: Vector[Either[IamTheLeader, IamNotTheLeader]] = raftService.evaluateActualLeaders

      val localLeaderName = data.filter(_.isLeft).head match {
        case Left(left) => left.actorName
        case _ => ""
      }

      localLeaderName       should not be temporaryFirstLeaderName
      data.count(_.isLeft)  shouldBe 1 //Only on leader
      data.count(_.isRight) shouldBe ( Config.nodes - 1 ) //Other nodes shouldBe follower
    }

    "terminate actor system" in {

      raftService.terminate().map(response => response shouldBe true)
    }

  }

}