package config

import java.util.UUID

import org.joda.time.DateTime

trait SystemUtilities {
  def dateTime(): DateTime = DateTime.now()

  def randomUuid(): UUID = UUID.randomUUID()
}

object SystemUtilities extends SystemUtilities
