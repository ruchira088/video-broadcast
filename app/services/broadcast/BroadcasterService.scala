package services.broadcast

import services.broadcast.models.Broadcaster
import web.requests.models.BroadcasterSignInRequest

import scala.concurrent.{ExecutionContext, Future}

trait BroadcasterService {
  def signIn(broadcasterSignInRequest: BroadcasterSignInRequest)(implicit executionContext: ExecutionContext): Future[Broadcaster]

  def signedInBroadcasters(page: Int)(implicit executionContext: ExecutionContext): Future[List[Broadcaster]]

  def signOut(username: String)(implicit executionContext: ExecutionContext): Future[Broadcaster]
}
