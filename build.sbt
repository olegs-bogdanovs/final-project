lazy val root = (project in file(".")).
  settings(
    name := "SparkCleaner",
    version := "0.1",
    scalaVersion := "2.10.5",
    mainClass in Compile := Some("App")
  )

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.0" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "1.6.0" % "provided"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "com.databricks" %% "spark-csv" % "1.5.0"

libraryDependencies +="org.scala-lang" % "scala-library" % "2.10.5"