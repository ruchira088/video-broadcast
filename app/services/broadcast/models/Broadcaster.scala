package services.broadcast.models

import org.joda.time.DateTime

case class Broadcaster(
  username: String,
  description: Option[String],
  signedInAt: DateTime,
  signedOutAt: Option[DateTime]
)
