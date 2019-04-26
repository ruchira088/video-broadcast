package web.controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class BroadcastController @Inject()(controllerComponents: ControllerComponents)
    extends AbstractController(controllerComponents) {
  def signIn() =
    Action.async(parse.json) {
      request => ???
    }
}
