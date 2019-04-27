package web.requests.models

import play.api.libs.json.{Json, OFormat}

case class BroadcasterSignInRequest(username: String, description: Option[String])

object BroadcasterSignInRequest {
  implicit val broadcasterSignInRequestFormat: OFormat[BroadcasterSignInRequest] =
    Json.format[BroadcasterSignInRequest]
}
