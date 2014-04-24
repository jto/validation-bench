import sbt._
import Keys._
import sbtassembly.Plugin._

object Resolvers {
  val typesafeReleases = "Typesafe Releases Repository" at "http://repo.typesafe.com/typesafe/releases/"

  val all = Seq(
  	Resolver.sonatypeRepo("snapshots"),
    Resolver.sonatypeRepo("releases"),
    typesafeReleases)
}

object BuildSettings {
	import Resolvers._

	val org = "jto.github.io"
	val buildScalaVersion = "2.10.3"
	val buildVersion = "1.0-SNAPSHOT"

	val commonSettings = Seq(
    organization := org,
    version := buildVersion,
    scalaVersion := buildScalaVersion,
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",       // yes, this is 2 args
      "-feature",
      "-unchecked",
      "-Xfatal-warnings",
      "-Xlint",
      "-optimise",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",        // N.B. doesn't work well with the ??? hole
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard"),
    resolvers ++= all,
    fork in Test := true,
    libraryDependencies ++= Seq(
      "org.hibernate" % "hibernate-validator" % "5.1.0.Final",
      "javax.el" % "javax.el-api" % "2.2.4",
      "org.glassfish.web" % "javax.el" % "2.2.4",
      "com.fasterxml.jackson.datatype" % "jackson-datatype-joda" % "2.3.3",
      "com.github.axel22" %% "scalameter" % "0.5-SNAPSHOT" % "test",
      "org.specs2" %% "specs2" % "2.1.1"),
    testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework"),
    logBuffered := false,
    parallelExecution in Test := false,
    logLevel in Test := Level.Warn)

}

object ValidationBuild extends Build {

	import BuildSettings._

	lazy val core = Project("validation-bench", file("."))
		.settings(commonSettings: _*)
    .settings(assemblySettings: _*)
		.dependsOn(ProjectRef(file("/Users/jto/Documents/validation"), "validation-json"))
}