name := "YuezhengV2"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  filters,
  "com.typesafe.play" %% "play-mailer" % "2.4.1"
)  

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.27"

play.Project.playJavaSettings
