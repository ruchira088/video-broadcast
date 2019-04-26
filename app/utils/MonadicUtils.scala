package utils

import scalaz.{MonadError, OptionT}

import scala.language.higherKinds

object MonadicUtils {
  implicit class OptionTWrapper[F[_], A](optionT: OptionT[F, A]) {
    def ifNotEmpty(throwable: => Throwable)(implicit monadError: MonadError[F, Throwable]): F[Unit] =
      monadError.bind(optionT.run) {
        _.fold[F[Unit]](monadError.raiseError(throwable))(_ => monadError.pure((): Unit))
      }

    def ifEmpty(throwable: => Throwable)(implicit monadError: MonadError[F, Throwable]): F[A] =
      monadError.bind(optionT.run) {
        _.fold[F[A]](monadError.raiseError(throwable))(value => monadError.pure(value))
      }
  }
}
