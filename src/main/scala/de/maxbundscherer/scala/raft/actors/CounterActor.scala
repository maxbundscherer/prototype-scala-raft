package de.maxbundscherer.scala.raft.actors

import akka.actor.{Actor, ActorLogging, Props}

object CounterActor {

  val prefix: String = "counterActor"
  def props: Props   = Props(new CounterActor())

  case class CounterState(balance: Int = 0) {

    def reset()               : CounterState = copy(balance = 0)
    def increment(value: Int) : CounterState = copy(balance = balance + value)
    def decrement(value: Int) : CounterState = copy(balance = balance - value)

  }

}

class CounterActor extends Actor with ActorLogging {

  import CounterActor._
  import de.maxbundscherer.scala.raft.aggregates.CounterAggregate._

  /**
   * Mutable actor state
   */
  private var state = CounterState()

  log.debug("Actor online")

  /**
   * Default behavior
   * @return Receive
   */
  override def receive: Receive = {

    case req: Request => processRequest(req)

    case any: Any => log.warning(s"Got unhandled message '$any'")

  }

  /**
   * Process request
   * @param req Request
   */
  private def processRequest(req: Request): Unit = req match {

    case Reset() =>

      this.state = this.state.reset()
      tellSender( NewValue(this.state.balance) )

    case Increment(value) =>

      this.state = this.state increment value
      tellSender( NewValue(this.state.balance) )

    case Decrement(value) =>

      this.state = this.state decrement value
      tellSender( NewValue(this.state.balance) )

    case any: Any => log.error(s"Got unhandled request '$any'")

  }

  /**
   * Tell sender (fire and forget)
   * @param res Response
   */
  private def tellSender(res: Response): Unit = { sender ! res }

}