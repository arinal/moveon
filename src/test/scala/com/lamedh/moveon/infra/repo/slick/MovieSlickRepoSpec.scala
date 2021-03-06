package com.lamedh.moveon.core.reservation

import com.lamedh.common.core.ErrorToken
import com.lamedh.moveon.infra.repo.slick.MovieSlickRepo
import com.lamedh.moveon.tools.RepoInit
import org.scalatest.{AsyncFlatSpec, Matchers}

class MovieSlickRepoSpec extends AsyncFlatSpec
    with Matchers
    with RepoInit {

  override lazy val movieRepo = new MovieSlickRepo {
    override lazy val profile = slick.jdbc.H2Profile
    import slick.jdbc.H2Profile.api._
    override lazy val db = Database.forConfig("db-unittest-moveon")

    mkTable()
  }

  "find session in empty repository" should
  "return nothing" in {
    movieRepo.count.map(_ shouldEqual 0)
    movieRepo.find(jumanji.imdbId).map(_ shouldEqual None)
  }

  "inserting movies, retrieving count and get all movies" should
  "return correct count and movies" in {
    initMovies()
    movieRepo.count.map(_ shouldEqual 3)
    movieRepo.all.map(_ shouldEqual allMovies)
    movieRepo.find(fistOfLegend.imdbId)
      .map(_ shouldEqual Some(fistOfLegend))
  }

  "updating Sound of Music to Sound of Satan" should
  "changed the title" in {
    val changed = soundOfMusic.copy(title = "Sound of Satan")
    movieRepo.update(changed)
    movieRepo.find(soundOfMusic.imdbId)
      .map(_ shouldEqual Some(changed))
  }

  "inserting Sound of Music again" should
  "return already exists error" in {
    recoverToSucceededIf[ErrorToken] {
      movieRepo.insert(soundOfMusic)
    }
  }
}
