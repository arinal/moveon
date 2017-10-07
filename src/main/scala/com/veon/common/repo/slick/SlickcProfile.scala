package com.veon.common.repo.slick

import slick.jdbc.JdbcProfile

trait SlickProfile {
  val profile: JdbcProfile
  import profile.api._
  val db: Database
  protected implicit val _ec = db.executor.executionContext
}
