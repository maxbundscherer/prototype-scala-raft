package de.maxbundscherer.scala.raft.services

import akka.actor.{ActorRef, ActorSystem}

class PingPongService()(implicit actorSystem: ActorSystem) {

  import de.maxbundscherer.scala.raft.actors.PingPongActor
  import de.maxbundscherer.scala.raft.aggregates.PingPongAggregate._

  private val actor: ActorRef = actorSystem.actorOf( PingPongActor.props, PingPongActor.prefix + "0" )

  def ping(msg: String): Unit = actor ! Ping(msg)

}