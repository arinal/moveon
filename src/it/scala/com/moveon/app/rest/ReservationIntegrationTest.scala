package com.veon.moveon.app.rest

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.veon.common.akkahttp.AkkaHttpClient
import com.veon.moveon.core.movie.Movie
import org.scalatest.{AsyncFlatSpec, Matchers}

class ReservationIntegrationTest extends AsyncFlatSpec
    with Matchers
    with AkkaHttpClient {

  implicit val _sys = ActorSystem("integration-test")
  implicit val _ec = _sys.dispatcher
  implicit val _mat: Materializer = ActorMaterializer()

  val baseUrl = "http://localhost:8080/movies"

  "Doing reservation in happy path manner" should
  "behave accordingly" in {

    val jumanji = Movie("tt0113497", "Jumanji", "Joe Johnston")
    val screenId = "SCN1"
    val initialSeats = 10
    val registerJson = s"""{
        |"imdbId": "${jumanji.imdbId}",
        |"screenId": "$screenId",
        |"availableSeats": $initialSeats
      |}""".stripMargin

    println(registerJson)

    val f = for {
      _   <- httpPostCall[String](s"$baseUrl/register", registerJson)
      res <- httpCall[String](s"$baseUrl?screenId=SCN1&imdbId=tt0113497")
    } yield res

    f.map(_ shouldEqual 5)
  }
}
