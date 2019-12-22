package de.maxbundscherer.scala.raft.services

import akka.pattern.ask
import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import scala.concurrent.Future

class RaftService(numberNodes: Int)(implicit actorSystem: ActorSystem, timeout: Timeout) {

  import de.maxbundscherer.scala.raft.actors.NodeActor
  import de.maxbundscherer.scala.raft.aggregates.RaftAggregate._

  /**
   * Declare nodes
   */
  final val nodes: Array[ActorRef] = new Array[ActorRef](numberNodes)

  /**
   * Start nodes
   */
  for (i <- 0 until numberNodes)
    nodes(i) = actorSystem.actorOf(props = NodeActor.props,
                                   name = s"${NodeActor.prefix}-$i")

}
