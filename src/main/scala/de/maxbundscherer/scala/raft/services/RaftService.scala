package de.maxbundscherer.scala.raft.services

import akka.pattern.ask
import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import scala.concurrent.{ExecutionContext, Future}

class RaftService(numberNodes: Int)(implicit actorSystem: ActorSystem,
                                    timeout: Timeout,
                                    executionContext: ExecutionContext) {

  import de.maxbundscherer.scala.raft.actors.RaftNodeActor
  import de.maxbundscherer.scala.raft.aggregates.RaftAggregate._

  /**
    * Declare and start up nodes
    */
  final val nodes: Map[Int, ActorRef] =
    (0 until numberNodes)
      .map(i => {
        i -> actorSystem.actorOf(props = RaftNodeActor.props,
                                 name = s"${RaftNodeActor.prefix}-$i")
      })
      .toMap

  /**
    * Init nodes (each node with neighbors)
    */
  nodes.foreach(node =>
    node._2 ! InitActor(nodes.filter(_._1 != node._1).values.toVector))

  /**
   * Terminates actor system
   */
  def terminate(): Future[Boolean] = actorSystem.terminate().map(_ => true)

}
