package com.veon.moveon.core.movie

import com.veon.common.core.Entity

case class Movie(imdbId: String, title: String, director: String) extends Entity[String] {
  val id: String = imdbId
}
