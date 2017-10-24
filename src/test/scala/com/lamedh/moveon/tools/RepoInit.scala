package com.lamedh.moveon.tools

import com.lamedh.moveon.core.movie.{ Movie, MovieRepository }
import com.lamedh.moveon.core.session.SessionRepository
import com.lamedh.moveon.infra.repo.inmemory.{ MovieInMemoryRepo, SessionInMemoryRepo }
import scala.concurrent.{ ExecutionContext, Future }

trait RepoInit {

  lazy val sessionRepo: SessionRepository = new SessionInMemoryRepo
  lazy val movieRepo: MovieRepository     = new MovieInMemoryRepo

  val jumanji      = Movie("tt0113497", "Jumanji", "Joe Johnston")
  val soundOfMusic = Movie("tt0059742", "The Sound of Music", "Robert Wise")
  val fistOfLegend = Movie("tt0110200", "Fist of Legend", "Gordon Chan")

  val allMovies = Seq(jumanji, soundOfMusic, fistOfLegend)

  def initMovies()(implicit ec: ExecutionContext) =
    Future.traverse(allMovies)(movieRepo.insert)
}
