package de.maxbundscherer.scala.raft

import de.maxbundscherer.scala.raft.utils.BaseServiceTest

class SecondSampleTest extends BaseServiceTest {

  import de.maxbundscherer.scala.raft.aggregates.PingPongAggregate._

  "PingPongServiceSecond" should "pongSecond" in {

    pingPongService.ping("msg2").map(res =>
      res shouldBe( Pong("msg2-pong") )
    )

  }

}