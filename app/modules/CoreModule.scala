package modules

import com.google.inject.AbstractModule
import config.SystemUtilities
import dao.broadcaster.{BroadcasterDao, SlickBroadcasterDao}

class CoreModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[SystemUtilities]).toInstance(SystemUtilities)
    bind(classOf[BroadcasterDao]).to(classOf[SlickBroadcasterDao])
  }
}
