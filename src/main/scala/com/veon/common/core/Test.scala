package com.veon.common.core

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.language.postfixOps
import scala.concurrent.duration._

trait ForceAwait {
  implicit class AwaitSyntax[T](future: Future[T]) {
    def await(): T = Await.result(future, 5 seconds)
  }
}

trait SyncExecutionContext {
  implicit val syncExecutionCtx: ExecutionContext =
    new ExecutionContext {
      override def execute(runnable: Runnable): Unit = runnable.run()
      override def reportFailure(cause: Throwable): Unit = {}
    }
}
