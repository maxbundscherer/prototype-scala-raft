package de.maxbundscherer.scala.raft.actors

import akka.actor.Actor

/**
 * ------------------
 * --- Raft Node ----
 * ------------------
 *
 * # 3 Behaviors
 * - FOLLOWER (Default)
 * - LEADER
 * - CANDIDATE
 */
class RaftNode extends Actor {

  /**
   * Set default behavior to FOLLOWER
   */
  override def receive: Receive = behaviorFollower

  /**
   * Raft FOLLOWER
   */
  def behaviorFollower: Receive = ???

  /**
   * Raft CANDIDATE
   */
  def behaviorCandidate: Receive = ???

  /**
   * Raft LEADER
   */
  def behaviorLeader: Receive = ???

}
