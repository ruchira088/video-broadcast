package modules

import com.google.inject.AbstractModule
import config.SystemUtilities
import dao.broadcaster.{BroadcasterDao, SlickBroadcasterDao}
import services.broadcast.{BroadcasterService, BroadcasterServiceImpl}

class CoreModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[SystemUtilities]).toInstance(SystemUtilities)
    bind(classOf[BroadcasterDao]).to(classOf[SlickBroadcasterDao])
    bind(classOf[BroadcasterService]).to(classOf[BroadcasterServiceImpl])
  }
}
