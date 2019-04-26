package dao.broadcaster

import config.SystemUtilities
import scalaz.OptionT
import services.broadcast.models.Broadcaster

import scala.concurrent.{ExecutionContext, Future}

trait BroadcasterDao {
  def insert(broadcaster: Broadcaster)(implicit executionContext: ExecutionContext): Future[Broadcaster]

  def getByUsername(username: String)(implicit executionContext: ExecutionContext): OptionT[Future, Broadcaster]

  def deleteByUsername(username: String)(implicit systemUtilities: SystemUtilities, executionContext: ExecutionContext): OptionT[Future, Broadcaster]
}
