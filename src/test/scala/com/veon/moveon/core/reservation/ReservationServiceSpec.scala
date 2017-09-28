package com.veon.moveon.core.reservation

import com.veon.common.core.{Error, ForceAwait, SyncExecutionContext}
import com.veon.moveon.core.movie.Movie
import com.veon.moveon.infra.repo.inmemory.{MovieInMemoryRepo, SessionInMemoryRepo}
import org.scalatest.{AsyncFlatSpec, FlatSpec, Matchers}

import scala.concurrent.Future
import scala.language.postfixOps

class ReservationServiceSpec extends AsyncFlatSpec
  with Matchers
  with InitializedRepoAndFinder {

  val service = new ReservationService(SessionInMemoryRepo, MovieInMemoryRepo, screenFinder)

  "find session in empty repository" should
    "return nothing" in {
      SessionInMemoryRepo.find("SCN1").map(_ shouldEqual None)
    }

  "starting session" should
    "initialize session and store the sessions in repo" in {
      val multiSession = for {
        jumSess <- service.startSession("SCN1", jumanji.imdbId)
        somSess <- service.startSession("SCN2", soundOfMusic.imdbId)
        foLSess <- service.startSession("SCN3", fistOfLegend.imdbId)
      } yield (jumSess, somSess, foLSess)

      multiSession.map { case (jumanjiSess, soMSess, foLSess) =>
        jumanjiSess.movie shouldEqual jumanji
        jumanjiSess.initialSeats shouldEqual 100

        soMSess.movie shouldEqual soundOfMusic
        soMSess.initialSeats shouldEqual 50

        foLSess.movie shouldEqual fistOfLegend
        foLSess.initialSeats shouldEqual 50
      }
    }

  "find Jumanji session in non-empty repository" should
    "return jumanji session" in {
      val jumanjiSess = SessionInMemoryRepo.find("SCN1")
      jumanjiSess.map(_.get.movie shouldEqual jumanji)
    }

  "reserving 'Fist of Legend'" should
    "update seats status" in {
      service.reserveSession("SCN3", fistOfLegend.imdbId).map { sess =>
        sess.movie shouldEqual fistOfLegend
        sess.reservedSeats shouldEqual 1
        sess.availableSeats shouldEqual (sess.initialSeats - 1)
      }
    }

  "finding 'Sound of Music' and 'Fist of Legend'" should
  "return the sessions consistenly with updated states" in {
    val sessions = for {
      somSess <- service.find("SCN2", soundOfMusic.imdbId)
      folSess <- service.find("SCN3", fistOfLegend.imdbId)
    } yield (somSess, folSess)

    sessions.map { case (somSess, folSess) =>
      somSess.movie shouldEqual soundOfMusic
      somSess.reservedSeats shouldEqual 0

      folSess.movie shouldEqual fistOfLegend
      folSess.reservedSeats shouldEqual 1
    }
  }
}

trait InitializedRepoAndFinder {

  val screenFinder = new AllocationFinder {
    override def find(id: String): Future[(String, Int)] =
      if (id == "SCN1") Future.successful("SCN1" -> 100)
      else if (id == "SCN2") Future.successful("SCN2" -> 50)
      else if (id == "SCN3") Future.successful("SCN3" -> 50)
      else Error.future("screen id not found")
  }

  val jumanji = Movie("tt0113497", "Jumanji", "Joe Johnston")
  val soundOfMusic = Movie("tt0059742", "The Sound of Music", "Robert Wise")
  val fistOfLegend = Movie("tt0110200", "Fist of Legend", "Gordon Chan")

  Seq(jumanji, soundOfMusic, fistOfLegend)
    .foreach(MovieInMemoryRepo.store)
}

