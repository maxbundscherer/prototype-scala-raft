package de.maxbundscherer.scala.raft

import de.maxbundscherer.scala.raft.utils.BaseServiceTest

class RaftServiceTest extends BaseServiceTest {

  import de.maxbundscherer.scala.raft.aggregates.RaftAggregate._

  "RaftService" should {

    "terminate" in {
      raftService.terminate().map(response => response shouldBe true)
    }

  }

}