package com.veon.moveon.infra.allocationfinder

import com.veon.moveon.core.reservation.AllocationFinder
import com.veon.common.core.{ErrorToken, NotFoundError}

import scala.concurrent.Future

class DummyAllocationFinder extends AllocationFinder {

  override def find(id: String): Future[(String, Int)] =
    if (id == "SCN1") Future.successful("SCN1" -> 100)
    else if (id == "SCN2") Future.successful("SCN2" -> 50)
    else if (id == "SCN3") Future.successful("SCN3" -> 50)
    else ErrorToken.future("screen id not found", NotFoundError)
}

