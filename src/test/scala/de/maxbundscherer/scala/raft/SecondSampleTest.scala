package de.maxbundscherer.scala.raft

import de.maxbundscherer.scala.raft.utils.BaseServiceTest

class SecondSampleTest extends BaseServiceTest {

  import de.maxbundscherer.scala.raft.aggregates.CounterAggregate._

  "CounterServiceSecond" should {

    "pongSecond" in {

      counterService.ping("msgSecond")
        .map(
          res => res shouldBe Pong("msgSecond-pong")
        )

    }

  }

}