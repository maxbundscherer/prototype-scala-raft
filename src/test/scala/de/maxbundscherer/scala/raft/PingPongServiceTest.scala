package de.maxbundscherer.scala.raft

import de.maxbundscherer.scala.raft.utils.BaseServiceTest

class PingPongServiceTest extends BaseServiceTest {

  import de.maxbundscherer.scala.raft.aggregates.PingPongAggregate._

  "PingPongService" should "pong" in {

    pingPongService.ping("msg").map(res =>
      res shouldBe( Pong("msg-pong") )
    )

  }

}