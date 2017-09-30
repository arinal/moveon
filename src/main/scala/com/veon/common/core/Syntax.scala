package com.veon.common.core

import scala.concurrent.Future
import scala.util.{Either, Failure, Success, Try}

object Syntax {

  implicit class FutureSyntax[M[_], A](m: M[A]) {

    def toFuture(ifFail: ErrorToken = ErrorToken.empty): Future[A] = m match {
      case o: Option[A] => option2Future(o, ifFail)
      case t: Try[A] => try2Future(t, ifFail)
    }

    def option2Future(option: Option[A], ifFail: ErrorToken) = option match {
      case Some(s) => Future.successful(s)
      case None => Future.failed(ifFail)
    }

    def try2Future(tryMonad: Try[A], ifFail: ErrorToken) = tryMonad match {
      case Success(s) => Future.successful(s)
      case Failure(err) => Future.failed(ifFail)
    }
  }

  implicit class FutureSyntax2[M[_, _], A](m: M[ErrorToken, A]) {

    def toFuture: Future[A] = m match {
      // pretty safe to ignore this warning, m is always has type params of [Error, A]
      case e: Either[ErrorToken, A] => either2Future(e)
    }

    private def either2Future(either: Either[ErrorToken, A]) = either match {
      case Right(s) => Future.successful(s)
      case Left(err) => Future.failed(err)
    }
  }
}
