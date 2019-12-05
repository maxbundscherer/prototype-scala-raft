package de.maxbundscherer.scala.raft

import de.maxbundscherer.scala.raft.utils.BaseServiceTest

class CounterServiceTest extends BaseServiceTest {

  import de.maxbundscherer.scala.raft.aggregates.CounterAggregate._

  "CounterService" should {

    "reset" in {
      counterService.reset()
        .map(
          response => response shouldBe NewValue(0)
        )
    }

    "increment" in {
      counterService.increment(5)
        .map(
          response => response shouldBe NewValue(5)
        )
    }

    "decrement" in {
      counterService.decrement(4)
        .map(
          response => response shouldBe NewValue(1)
        )
    }

  }

}