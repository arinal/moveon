package com.veon.moveon.ui.rest

import com.veon.moveon.core.movie.Movie

import scala.io.StdIn

object Boot extends App
  with Injection {

  Seq(Movie("tt0113497", "Jumanji", "Joe Johnston"),
    Movie("tt0059742", "The Sound of Music", "Robert Wise"),
    Movie("tt0110200", "Fist of Legend", "Gordon Chan")
  ).foreach(movieRepo.store)

  val binding = restServer.start()
  println("Press enter to stop..")
  StdIn.readLine()
  restServer.stop(binding)
}
