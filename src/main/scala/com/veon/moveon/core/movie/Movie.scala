package com.veon.moveon.core.movie

import com.veon.common.core.Entity

case class Movie(imdbId: String, title: String, author: String) extends Entity[String] {
  val id = imdbId
}
