import Dependencies._

lazy val root =
  (project in file("."))
    .enablePlugins(PlayScala, BuildInfoPlugin)
    .settings(
      name := "video-broadcast-api",
      organization := "com.ruchij",
      version := "0.0.1",
      maintainer := "ruchira088@gmail.com",
      scalaVersion := SCALA_VERSION,
      buildInfoKeys := BuildInfoKey.ofN(name, organization, version, scalaVersion, sbtVersion),
      buildInfoPackage := "com.eed3si9n.ruchij",
      testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-results/unit-tests"),
      javaOptions in Test += "-Dconfig.file=conf/application.h2.conf",
      libraryDependencies ++= rootDependencies ++ rootTestDependencies.map(_ % Test)
    )

lazy val rootDependencies = Seq(guice, jodaTime, scalazCore, playSlick, playSlickEvolutions, postgresql, sqlite)

lazy val rootTestDependencies = Seq(h2, faker, scalaTestPlusPlay, pegdown)

addCommandAlias("runWithPostgreSQL", "run -Dconfig.file=conf/application.postgresql.conf")
addCommandAlias("runWithH2", "run -Dconfig.file=conf/application.h2.conf")
addCommandAlias("testWithCoverage", "; clean; coverage; test; coverageReport")
