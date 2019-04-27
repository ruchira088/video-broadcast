package services.broadcast

import config.SystemUtilities
import dao.broadcaster.BroadcasterDao
import exceptions.{ExistingUsernameException, NonExistingUsernameException}
import javax.inject.{Inject, Singleton}
import scalaz.std.scalaFuture.futureInstance
import services.broadcast.models.Broadcaster
import utils.MonadicUtils.OptionTWrapper
import web.requests.models.BroadcasterSignInRequest

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BroadcasterServiceImpl @Inject()(broadcasterDao: BroadcasterDao)(implicit systemUtilities: SystemUtilities) extends BroadcasterService {
  override def signIn(broadcasterSignInRequest: BroadcasterSignInRequest)(implicit executionContext: ExecutionContext): Future[Broadcaster] =
    for {
      _ <- broadcasterDao.getByUsername(broadcasterSignInRequest.username) ifNotEmpty ExistingUsernameException(broadcasterSignInRequest.username)
      broadcaster <- broadcasterDao.insert(Broadcaster(broadcasterSignInRequest.username, broadcasterSignInRequest.description, systemUtilities.currentTime(), None))
    }
    yield broadcaster

  override def signedInBroadcasters(page: Int)(implicit executionContext: ExecutionContext): Future[List[Broadcaster]] =
    broadcasterDao.getAll(page)

  override def signOut(username: String)(implicit executionContext: ExecutionContext): Future[Broadcaster] =
    broadcasterDao.deleteByUsername(username) ifEmpty NonExistingUsernameException(username)
}
