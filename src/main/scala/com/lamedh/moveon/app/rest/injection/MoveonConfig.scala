package com.lamedh.moveon.app.rest.injection

import com.typesafe.config.ConfigFactory

case class MoveonConfig(
  dbSestion: String,
  allocationHost: String,
  allocationPort: Int
)

object MoveonConfig {

  def fromFile = {
    val config = ConfigFactory.load()
    MoveonConfig(
      config.getString("db.section"),
      config.getString("allocation.host"),
      config.getInt("allocation.port")
    )
  }
}
