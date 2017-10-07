package com.veon.moveon.app.rest.injection

import com.typesafe.config.ConfigFactory


object Config {
  implicit val config = ConfigFactory.load()

  val dbSection = config.getString("db.section")

  val allocationHost = config.getString("allocation.host")
  val allocationPort = config.getInt("allocation.port")
}
