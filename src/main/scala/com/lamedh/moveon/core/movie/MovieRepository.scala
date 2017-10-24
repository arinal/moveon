package com.lamedh.moveon.core.movie

import com.lamedh.common.core.{ ErrorToken, InputError, Repository }

trait MovieRepository extends Repository[Movie, String] {
  def alreadyExistsError(movie: Movie) =
    ErrorToken(s"Movie with code ${movie.imdbId} already exists", InputError)
}
