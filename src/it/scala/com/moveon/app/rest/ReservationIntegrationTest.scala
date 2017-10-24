package com.lamedh.moveon.app.rest

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.lamedh.common.akkahttp.AkkaHttpClient
import com.lamedh.moveon.app.rest.Models.{MovieSession, RegisterMovie, ReserveSeat}
import org.scalatest.{AsyncFlatSpec, Matchers}

class ReservationIntegrationTest extends AsyncFlatSpec
    with Matchers
    with AkkaHttpClient {

  import io.circe.syntax._
  import io.circe.generic.auto._
  import akka.http.scaladsl.model.StatusCodes._

  implicit val _sys = ActorSystem("integration-test")
  implicit val _ec = _sys.dispatcher
  implicit val _mat: Materializer = ActorMaterializer()

  val baseUrl = "http://localhost:8080/movies"

  "Doing reservation in happy path manner" should
  "behave accordingly" in {

    val register = RegisterMovie("tt0113497", 2, "SCN1")
    val reserve = ReserveSeat(register.imdbId, register.screenId)
    val (regJson, rsvpJson) = (register.asJson.noSpaces, reserve.asJson.noSpaces)

    val result = for {
      _     <- httpPost(s"$baseUrl/register", regJson)
      sess1 <- httpCallAndDecode[MovieSession](s"$baseUrl?screenId=SCN1&imdbId=tt0113497")

      _     <- httpPost(s"$baseUrl/reserve", rsvpJson)
      sess2 <- httpCallAndDecode[MovieSession](s"$baseUrl?screenId=SCN1&imdbId=tt0113497")

      _     <- httpPost(s"$baseUrl/reserve", rsvpJson)
      sess3 <- httpCallAndDecode[MovieSession](s"$baseUrl?screenId=SCN1&imdbId=tt0113497")

      resp  <- httpPost(s"$baseUrl/reserve", rsvpJson)
    } yield (sess1, sess2, sess3, resp)

    result.map { case (sess1, sess2, sess3, resp) =>
      sess1.movieTitle     shouldEqual "Jumanji"

      sess1.availableSeats shouldEqual 2
      sess2.availableSeats shouldEqual 1
      sess3.availableSeats shouldEqual 0

      resp.status shouldEqual BadRequest
    }
  }
}
