package com.veon.moveon.core.movie

import com.veon.common.core.{ ErrorToken, InputError, Repository }

trait MovieRepository extends Repository[Movie, String] {
  def alreadyExistsError(movie: Movie) =
    ErrorToken(s"Movie with code ${movie.imdbId} already exists", InputError)
}
