package com.lamedh.moveon.core.reservation

import scala.language.postfixOps

import com.lamedh.moveon.infra.allocationfinder.DummyAllocationFinder
import com.lamedh.moveon.tools.RepoInit
import org.scalatest.{AsyncFlatSpec, Matchers}

class ReservationServiceSpec extends AsyncFlatSpec
  with Matchers
  with RepoInit {

  initMovies()

  "find session in empty repository" should
  "return nothing" in {
    sessionRepo.find("SCN1").map(_ shouldEqual None)
  }

  val allocFinder = new DummyAllocationFinder
  val service = new ReservationService(sessionRepo, movieRepo, allocFinder)

  "starting session" should
  "initialize session and store the sessions in repo" in {
    val multiSession = for {
      jumSess <- service.startSession("SCN1", jumanji.imdbId)
      somSess <- service.startSession("SCN2", soundOfMusic.imdbId)
      foLSess <- service.startSession("SCN3", fistOfLegend.imdbId)
    } yield (jumSess, somSess, foLSess)

    multiSession.map { case (jumSess, soMSess, foLSess) =>
      jumSess.imdbId       shouldEqual jumanji.imdbId
      jumSess.initialSeats shouldEqual 100

      soMSess.imdbId       shouldEqual soundOfMusic.imdbId
      soMSess.initialSeats shouldEqual 50

      foLSess.imdbId       shouldEqual fistOfLegend.imdbId
      foLSess.initialSeats shouldEqual 50
    }
  }

  "find Jumanji session in non-empty repository" should
  "return jumanji session" in {
    val jumSess = sessionRepo.find("SCN1")
    jumSess.map(_.get.imdbId shouldEqual jumanji.imdbId)
  }

  "reserving 'Fist of Legend'" should
  "update seats status" in {
    service.reserveSession("SCN3", fistOfLegend.imdbId).map { sess =>
      sess.imdbId         shouldEqual fistOfLegend.imdbId
      sess.reservedSeats  shouldEqual 1
      sess.availableSeats shouldEqual (sess.initialSeats - 1)
    }
  }

  "finding 'Sound of Music' and 'Fist of Legend'" should
  "return the sessions consistenly with updated states" in {
    val sessions = for {
      (somSess, _) <- service.find("SCN2", soundOfMusic.imdbId)
      (folSess, _) <- service.find("SCN3", fistOfLegend.imdbId)
    } yield (somSess, folSess)

    sessions.map { case (somSess, folSess) =>
      somSess.imdbId        shouldEqual soundOfMusic.imdbId
      somSess.reservedSeats shouldEqual 0

      folSess.imdbId        shouldEqual fistOfLegend.imdbId
      folSess.reservedSeats shouldEqual 1
    }
  }
}
