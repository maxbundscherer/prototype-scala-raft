package de.maxbundscherer.scala.raft

import de.maxbundscherer.scala.raft.utils.BaseServiceTest

class SecondSampleTest extends BaseServiceTest {

  import de.maxbundscherer.scala.raft.aggregates.CounterAggregate._

  "CounterServiceSecond" should {

    "reset" in {
      counterService.reset()
        .map(
          response => response shouldBe NewValue(0)
        )
    }

    "increment" in {
      counterService.increment(4)
        .map(
          response => response shouldBe NewValue(4)
        )
    }

    "decrement" in {
      counterService.decrement(2)
        .map(
          response => response shouldBe NewValue(2)
        )
    }

  }

}