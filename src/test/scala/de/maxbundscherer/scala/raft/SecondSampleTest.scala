package de.maxbundscherer.scala.raft

import de.maxbundscherer.scala.raft.utils.BaseServiceTest

class SecondSampleTest extends BaseServiceTest {

  import de.maxbundscherer.scala.raft.aggregates.PingPongAggregate._

  "PingPongServiceSecond" should {

    "pongSecond" in {

      pingPongService.ping("msgSecond")
        .map(
          res => res shouldBe Pong("msgSecond-pong")
        )

    }

  }

}