package services.broadcast

import bindings.GuiceUtils.application
import config.SystemUtilities
import dao.broadcaster.BroadcasterDao
import org.joda.time.DateTime
import org.scalatest.{AsyncFlatSpec, MustMatchers}
import utils.Random
import web.requests.models.BroadcasterSignInRequest

class BroadcastServiceImplSpec extends AsyncFlatSpec with MustMatchers {

  "Signing in a user" should "create an entry in the database" in {
    val currentDateTime = SystemUtilities.dateTime()

    val systemUtilities = new SystemUtilities {
      override def dateTime(): DateTime = currentDateTime
    }

    val app = application(classOf[SystemUtilities] -> systemUtilities)

    val broadcasterService = app.injector.instanceOf[BroadcasterServiceImpl]
    val broadcasterDao = app.injector.instanceOf[BroadcasterDao]

    val broadcasterSignInRequest = Random[BroadcasterSignInRequest]

    for {
      broadcaster <- broadcasterService.signIn(broadcasterSignInRequest)

      _ = {
        broadcaster.username mustBe broadcasterSignInRequest.username
        broadcaster.description mustBe broadcasterSignInRequest.description
        broadcaster.signedInAt mustBe currentDateTime
        broadcaster.signedOutAt mustBe None
      }

      signedInBroadcasters <- broadcasterService.signedInBroadcasters(0)

      _ = signedInBroadcasters must contain(broadcaster)

      signedOutBroadcaster <- broadcasterService.signOut(broadcasterSignInRequest.username)

      _ = {
        signedOutBroadcaster.signedOutAt mustBe Some(currentDateTime)
        signedOutBroadcaster.copy(signedOutAt = None) mustBe broadcaster
      }

      broadcastersAfterSignOut <- broadcasterService.signedInBroadcasters(0)

      _ = broadcastersAfterSignOut.map(_.username) must not contain broadcaster.username

      allBroadcasters <- broadcasterDao.getAllIncludingDeleted(0)

      _ <- app.stop()
    }
    yield allBroadcasters must contain(signedOutBroadcaster)
  }
}
