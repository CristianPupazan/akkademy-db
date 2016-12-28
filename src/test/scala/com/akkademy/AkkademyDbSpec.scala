package com.akkademy

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.TestActorRef
import akka.util.Timeout
import com.akkademy.messages.{GetRequest, KeyNotFoundException, SetRequest, UnknownRequestException}
import com.typesafe.config.ConfigFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, FunSpecLike, Matchers}

import scala.concurrent.duration._

class AkkademyDbSpec extends FunSpecLike with Matchers with BeforeAndAfterAll with ScalaFutures {
  implicit val system = ActorSystem("test", ConfigFactory.empty) //ignore config for remoting
  implicit val timeout = Timeout(5 seconds)

  override def afterAll() = system.shutdown()

  private val actorRef = TestActorRef(new AkkademyDb)
  private val akkademyDb = actorRef.underlyingActor

  describe("akkademyDb") {
    describe("given SetRequest"){
      it("should place key/value into map"){
        actorRef ! SetRequest("key", "value")

        akkademyDb.map.get("key") should equal(Some("value"))
      }
    }

    describe("given GetRequest") {
      it("should get value for existing key") {
        akkademyDb.map.put("foo", "bar")

        val futureResult = actorRef ? GetRequest("foo")

        whenReady(futureResult.mapTo[String]) {
          _ should equal("bar")
        }
      }

      it("should KeyNotFound for non-existing key") {
        val futureResult = actorRef ? GetRequest("unknown")

        whenReady(futureResult.failed) {
          _ shouldBe a[KeyNotFoundException]
        }
      }
    }

    describe("given unknown request") {
      it("should return failure") {
        val actorRef = TestActorRef(new AkkademyDb)

        val futureResult = actorRef ? "unknown"

        whenReady(futureResult.failed) {
          _ shouldBe a[UnknownRequestException]
        }
      }
    }
  }
}