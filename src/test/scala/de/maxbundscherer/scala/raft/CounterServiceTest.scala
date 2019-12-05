package de.maxbundscherer.scala.raft

import de.maxbundscherer.scala.raft.utils.BaseServiceTest

class CounterServiceTest extends BaseServiceTest {

  import de.maxbundscherer.scala.raft.aggregates.CounterAggregate._

  "CounterService" should {

    "pong" in {

      counterService.ping("msg")
        .map(
          res => res shouldBe Pong("msg-pong")
        )

    }

  }

}