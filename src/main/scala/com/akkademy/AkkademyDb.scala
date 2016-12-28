package com.akkademy

import akka.actor.{Actor, ActorSystem, Props, Status}
import akka.event.Logging
import com.akkademy.messages.{GetRequest, KeyNotFoundException, SetRequest, UnknownRequestException}

import scala.collection.mutable

class AkkademyDb extends Actor {
  val map = new mutable.HashMap[String, Object]
  val log = Logging(context.system, this)

  override def receive = {
    case SetRequest(key, value) => log.info("received SetRequest - key: {} value: {}", key, value)
      map.put(key, value)
      sender() ! Status.Success

    case GetRequest(key) => log.info("received GetRequest - key: {}", key)
      val response = map.get(key)

      response match {
        case Some(x) => sender() ! x
        case None => sender() ! Status.Failure(new KeyNotFoundException(key))
      }

    case o => sender() ! Status.Failure(new UnknownRequestException(o.toString))
  }
}

object AkkademyDbActor {
  def props(): Props = { Props(classOf[AkkademyDb]) }
}

object Main extends App {
  val system = ActorSystem("akkademy")
  val helloActor = system.actorOf(AkkademyDbActor.props(), name = "akkademy-db")
}
