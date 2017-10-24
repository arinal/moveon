package com.lamedh.moveon.core.movie

import com.lamedh.common.core.Entity

case class Movie(imdbId: String, title: String, director: String) extends Entity[String] {
  val id: String = imdbId
}
