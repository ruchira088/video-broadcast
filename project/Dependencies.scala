import sbt._

object Dependencies {
  val SCALA_VERSION = "2.12.8"

  lazy val jodaTime = "joda-time" % "joda-time" % "2.10.1"
  
  lazy val scalazCore = "org.scalaz" %% "scalaz-core" % "7.2.27"
  
  lazy val playSlick = "com.typesafe.play" %% "play-slick" % "4.0.1"
  
  lazy val playSlickEvolutions = "com.typesafe.play" %% "play-slick-evolutions" % "4.0.1"
  
  lazy val postgresql = "org.postgresql" % "postgresql" % "42.2.5"
  
  lazy val sqlite = "org.xerial" % "sqlite-jdbc" % "3.27.2.1"

  lazy val h2 = "com.h2database" % "h2" % "1.4.199"
  
  lazy val scalaTestPlusPlay = "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.2"

  lazy val pegdown = "org.pegdown" % "pegdown" % "1.6.0"
}
