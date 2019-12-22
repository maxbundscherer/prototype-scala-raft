package de.maxbundscherer.scala.raft.actors

import akka.actor.Actor

/**
 * ------------------
 * --- Raft Node ----
 * ------------------
 *
 * # 3 Behaviors (Finite-state machine)
 *
 * - FOLLOWER (Default)
 * - LEADER
 * - CANDIDATE
 */
class NodeActor extends Actor {

  /**
   * Set default behavior to FOLLOWER
   */
  override def receive: Receive = followerBehavior

  /**
   * Raft FOLLOWER
   */
  def followerBehavior: Receive = ???

  /**
   * Raft CANDIDATE
   */
  def candidateBehavior: Receive = ???

  /**
   * Raft LEADER
   */
  def leaderBehavior: Receive = ???

}
