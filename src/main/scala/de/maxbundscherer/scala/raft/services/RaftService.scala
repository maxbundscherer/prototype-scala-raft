package de.maxbundscherer.scala.raft.services

import akka.pattern.ask
import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import scala.concurrent.Future

class RaftService()(implicit actorSystem: ActorSystem, timeout: Timeout) {

  import de.maxbundscherer.scala.raft.actors.NodeActor
  import de.maxbundscherer.scala.raft.aggregates.RaftAggregate._

  /**
    * Private actor ref (use proxy in cluster)
    */
  private val oneNodeActorRef: ActorRef =
    actorSystem.actorOf(NodeActor.props, NodeActor.prefix + "0")

}
