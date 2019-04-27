package dao.broadcaster

import scalaz.OptionT
import services.broadcast.models.Broadcaster

import scala.concurrent.{ExecutionContext, Future}

trait BroadcasterDao {
  def insert(broadcaster: Broadcaster)(implicit executionContext: ExecutionContext): Future[Broadcaster]

  def getByUsername(username: String)(implicit executionContext: ExecutionContext): OptionT[Future, Broadcaster]

  def getAll(page: Int)(implicit executionContext: ExecutionContext): Future[List[Broadcaster]]

  def deleteByUsername(username: String)(implicit executionContext: ExecutionContext): OptionT[Future, Broadcaster]

  def getAllIncludingDeleted(page: Int)(implicit executionContext: ExecutionContext): Future[List[Broadcaster]]
}
