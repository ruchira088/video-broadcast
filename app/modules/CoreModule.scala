package modules

import com.google.inject.{AbstractModule, Provides}
import config.SystemUtilities
import dao.broadcaster.{BroadcasterDao, SlickBroadcasterDao}
import javax.inject.Singleton
import services.broadcast.{BroadcasterService, BroadcasterServiceImpl}

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._
import scala.language.postfixOps

class CoreModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[SystemUtilities]).toInstance(SystemUtilities)
    bind(classOf[BroadcasterService]).to(classOf[BroadcasterServiceImpl])
  }

  @Singleton
  @Provides
  def broadcasterDao(
    slickBroadcasterDao: SlickBroadcasterDao
  )(implicit executionContext: ExecutionContext): BroadcasterDao =
    Await.result(slickBroadcasterDao.initialize().map(_ => slickBroadcasterDao), 30 seconds)
}
