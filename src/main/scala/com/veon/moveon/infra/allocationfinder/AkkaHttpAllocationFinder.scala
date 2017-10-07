package com.veon.moveon.infra.allocationfinder

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.stream.Materializer
import com.veon.common.akkahttp.AkkaHttpClient
import com.veon.common.akkahttp.AkkaHttpClient.ResponseError
import com.veon.moveon.core.reservation.AllocationFinder
import com.veon.common.core.{ErrorToken, NotFoundError}
import io.circe.Decoder
import scala.concurrent.{ ExecutionContext, Future }

case class Allocation(screenId: String, capacity: Int)

class AkkaHttpAllocationFinder(host: String, port: Int)
                              (implicit _as: ActorSystem,
                               _mat: Materializer,
                               _ec: ExecutionContext) extends AllocationFinder
    with AkkaHttpClient {

  implicit val decodeUser: Decoder[Allocation] =
    Decoder.forProduct2("screen_id", "capacity")(Allocation.apply)

  override def find(id: String): Future[(String, Int)] =
    httpCallAndDecode[Allocation](s"http://$host:$port/api/screen/$id")
      .map(alloc => alloc.screenId -> alloc.capacity)
      .recoverWith {
        case ResponseError(resp) if resp.status == StatusCodes.NotFound =>
          ErrorToken.future("screen id not found", NotFoundError)
      }
}
