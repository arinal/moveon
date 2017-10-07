package com.veon.moveon.app.rest.injection

import com.softwaremill.macwire.wire
import com.typesafe.config.Config
import com.veon.common.repo.slick.SlickProfile
import com.veon.moveon.infra.repo.inmemory._
import com.veon.moveon.infra.repo.slick.{MovieSlickRepo, SessionSlickRepo}
import com.veon.moveon.core.movie.Movie

class MySqlRepoModule {

  trait Slick extends SlickProfile {
    override lazy val profile = slick.jdbc.MySQLProfile
    import slick.jdbc.MySQLProfile.api._
    override lazy val db = Database.forConfig(Config.dbSection)
  }

  lazy val sessionRepo = new SessionSlickRepo with Slick
  lazy val movieRepo   = new MovieSlickRepo with Slick
}

class H2RepoModule {

  trait Slick extends SlickProfile {
    override lazy val profile = slick.jdbc.H2Profile
    import slick.jdbc.H2Profile.api._
    override lazy val db = Database.forConfig("db-moveon-h2")
  }

  lazy val sessionRepo = new SessionSlickRepo with Slick
  lazy val movieRepo   = new MovieSlickRepo with Slick

  sessionRepo.mkTable()
  movieRepo.mkTable()

  Seq(Movie("tt0113497", "Jumanji", "Joe Johnston"),
      Movie("tt0059742", "The Sound of Music", "Robert Wise"),
      Movie("tt0110200", "Fist of Legend", "Gordon Chan"))
    .foreach(movieRepo.store)
}

class InMemoryRepoModule {
  lazy val sessionRepo = wire[SessionInMemoryRepo]
  lazy val movieRepo   = wire[MovieInMemoryRepo]

  Seq(Movie("tt0113497", "Jumanji", "Joe Johnston"),
      Movie("tt0059742", "The Sound of Music", "Robert Wise"),
      Movie("tt0110200", "Fist of Legend", "Gordon Chan"))
    .foreach(movieRepo.store)
}
