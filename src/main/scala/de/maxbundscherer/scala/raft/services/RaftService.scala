package de.maxbundscherer.scala.raft.services

import akka.pattern.ask
import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import scala.concurrent.ExecutionContext

class RaftService(numberNodes: Int)(implicit actorSystem: ActorSystem,
                                    timeout: Timeout,
                                    executionContext: ExecutionContext) {

  import de.maxbundscherer.scala.raft.actors.NodeActor
  import de.maxbundscherer.scala.raft.aggregates.RaftAggregate._

  /**
    * Declare and start up nodes
    */
  final val nodes: Map[Int, ActorRef] =
    (0 until numberNodes)
      .map(i => {
        i -> actorSystem.actorOf(props = NodeActor.props,
                                 name = s"${NodeActor.prefix}-$i")
      })
      .toMap

  /**
    * Init nodes (each node with neighbors)
    */
  nodes.foreach(node =>
    node._2 ! InitActor(nodes.filter(_._1 != node._1).values.toVector))

}
