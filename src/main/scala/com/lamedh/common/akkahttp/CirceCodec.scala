package com.lamedh.common.akkahttp

import akka.http.scaladsl.marshalling.Marshaller
import akka.http.scaladsl.model.{MediaTypes, RequestEntity}
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, PredefinedFromEntityUnmarshallers}
import akka.http.scaladsl.util.FastFuture
import cats.Show
import io.circe.{Decoder, Encoder, Json}

import scala.concurrent.Future
import scala.util.control.NoStackTrace

object CirceCodec {

  implicit val jsonMarshaller: Marshaller[Json, RequestEntity] =
    Marshaller.StringMarshaller.wrap(MediaTypes.`application/json`)(_.noSpaces)

  implicit def jsonTMarshaller[T](implicit enc: Encoder[T]): Marshaller[T, RequestEntity] =
    jsonMarshaller.compose[T](enc.apply)

  import io.circe.parser._

  implicit val entityUnmarshaller: FromEntityUnmarshaller[Json] =
    PredefinedFromEntityUnmarshallers
      .stringUnmarshaller
      .flatMap(_ => _ => str => xorToFuture(parse(str)))

  implicit def entityTUnmarshaller[T](implicit dec: Decoder[T]): FromEntityUnmarshaller[T] =
    PredefinedFromEntityUnmarshallers
      .stringUnmarshaller
      .flatMap(_ => _ => str => xorToFuture(decode[T](str)))

  private def xorToFuture[A, B](value: Either[A, B])(implicit SA: Show[A]): Future[B] =
    value.fold(
      error => FastFuture.failed(new Throwable(SA.show(error)) with NoStackTrace),
      FastFuture.successful
    )
}
