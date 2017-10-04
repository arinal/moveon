package com.veon.moveon.app.rest

import scala.io.StdIn
import com.veon.moveon.core.movie.Movie

object Boot extends App
  with Injection {

  val jumanji      = Movie("tt0113497", "Jumanji", "Joe Johnston")
  val soundOfMusic = Movie("tt0059742", "The Sound of Music", "Robert Wise")
  val fistOfLegend = Movie("tt0110200", "Fist of Legend", "Gordon Chan")
  val allMovies = Seq(jumanji, soundOfMusic, fistOfLegend)

  allMovies.foreach(movieRepo.store)

  val binding = restServer.start()
  println("Press enter to stop..")
  StdIn.readLine()
  restServer.stop(binding)
}
