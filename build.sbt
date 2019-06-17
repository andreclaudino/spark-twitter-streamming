name := "SparkTwitterStream"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.3",
  "org.apache.spark" %% "spark-sql" % "2.4.3",
  "org.apache.spark" %% "spark-streaming" % "2.4.3",

  "org.apache.bahir" %% "spark-streaming-twitter" % "2.3.3",
  "org.clapper" %% "argot" % "1.0.3"
)