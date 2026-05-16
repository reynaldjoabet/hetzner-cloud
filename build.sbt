import Dependencies.*

ThisBuild / scalaVersion := "3.3.7"
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalacOptions ++= Seq(
  // "-no-indent",
  "-deprecation", // Warns about deprecated APIs
  "-feature", // Warns about advanced language features
  "-unchecked",
  // "-Wunused:imports",
  //   "-Wunused:privates",
  //   "-Wunused:locals",
  //   "-Wunused:explicits",
  //   "-Wunused:implicits",
  //   "-Wunused:params",
  //   "-Wvalue-discard",
  // "-language:strictEquality",
  "-Xmax-inlines:100000"
)

lazy val root = (project in file("."))
  .settings(
    name := "hetzner-cloud",
    libraryDependencies ++= Seq(
      sttpCore,
      sttpJsoniter,
      http4sBackend,
      http4sDsl,
      emberServer,
      fs2,
      chimney,
      emberClient,
      catsEffect,
      pureconfig,
      slf4j,
      logback,
      scribe,
      scribeSlf4j,
      scribeCats,
      jsoniter,
      jsoniterMacros,
      jsoniterCirce,
      munit
    )
  )
  .dependsOn(`hcloud-codegen` % "compile->compile")
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](
      name,
      version,
      scalaVersion,
      sbtVersion
    ),
    buildInfoPackage := "hcloud.generated",
    buildInfoObject := "HCloudBuildInfo",
    scalacOptions ++= Seq("-no-indent")
  )
  .aggregate(`hcloud-codegen`)

lazy val `hcloud-codegen` = (project in file("modules/hcloud-codegen"))
  .enablePlugins(OpenApiGeneratorPlugin)
  .settings(
    name := "hcloud-codegen",
    // openApiInputSpec := "src/main/resources/swagger.json",
    // openApiGeneratorName := "sclala-sttp-client4",
    openApiModelNamePrefix := "",
    openApiModelNameSuffix := "",
    // openApiRemoveOperationIdPrefix := Some(true),
    openApiGenerateMetadata := SettingDisabled,
    // Use the same JSON so CLI and SBT stay in sync
    openApiConfigFile := ((Compile / baseDirectory).value / "config.json").getPath,
    openApiIgnoreFileOverride := s"${baseDirectory.value.getPath}/openapi-ignore-file",

    // Put generated sources where SBT expects managed sources
    openApiOutputDir := ((Compile / baseDirectory).value / "src/main/scala").getAbsolutePath,
    openApiGenerateModelTests := SettingDisabled,
    openApiGenerateApiTests := SettingDisabled,
    openApiValidateSpec := SettingDisabled,
    // Fail fast on bad specs (optional but recommended)
    openApiValidateSpec := Some(true),
    // Compile / sourceGenerators += openApiGenerate.taskValue,
    (Compile / compile) := ((Compile / compile) dependsOn generate).value,
    // (Compile/compile) := ((compile in Compile) dependsOn openApiGenerate).value

    // Define the simple generate command to generate full client codes
    generate := {
      val _ = openApiGenerate.value

      // Delete the generated build.sbt file so that it is not used for our sbt config
      val buildSbtFile = file(openApiOutputDir.value) / "build.sbt"
      if (buildSbtFile.exists()) {
        buildSbtFile.delete()
      }
    },
    libraryDependencies ++= Seq(
      sttpJsoniter,
      jsoniter,
      jsoniterMacros,
      jsoniterCirce
    )
  )

lazy val populateTestDB =
  taskKey[Unit]("Run PopulateTestDatabase main class from the test folder")

populateTestDB := {
  val log = streams.value.log
  (Test / runMain)
    .toTask(s"utils.PopulateTestDatabase")
    .value
}

val commonSettings = Seq(
  scalaVersion := "3.3.7",
  openApiInputSpec := (baseDirectory.value / (name.value + ".json")).getPath,
  openApiModelNamePrefix := "",
  openApiModelNameSuffix := "",
  openApiApiPackage := s"kubescala.${name.value.replace("-", ".")}" + ".api",
  openApiModelPackage := s"kubescala.${name.value.replace("-", ".")}" + ".models",
  openApiInvokerPackage := s"kubescala.${name.value.replace("-", ".")}",
  // openApiRemoveOperationIdPrefix := Some(true),
  openApiGenerateMetadata := Some(false),
  openApiGenerateMetadata := SettingDisabled,
  // Use the module-local config.json
  openApiConfigFile := (baseDirectory.value / "config.json").getPath,

  // Put generated sources where SBT expects managed sources
  openApiOutputDir := ((Compile / baseDirectory).value / "src/main/scala").getAbsolutePath,
  openApiGenerateModelTests := SettingDisabled,
  openApiGenerateApiTests := SettingDisabled,
  openApiValidateSpec := SettingDisabled,
  // Fail fast on bad specs (optional but recommended)
  // openApiValidateSpec := Some(true),
  // Compile / sourceGenerators += openApiGenerate.taskValue,
  (Compile / compile) := ((Compile / compile) dependsOn generate).value,
  // (Compile/compile) := ((compile in Compile) dependsOn openApiGenerate).value

  // Define the simple generate command to generate full client codes
  generate := {
    val _ = openApiGenerate.value

    // Delete the generated build.sbt file so that it is not used for our sbt config
    val buildSbtFile = file(openApiOutputDir.value) / "build.sbt"
    if (buildSbtFile.exists()) {
      buildSbtFile.delete()
    }
  },
  libraryDependencies ++= Seq(
    sttpJsoniter,
    jsoniter,
    jsoniterMacros,
    jsoniterCirce
  )
)

lazy val `digitalocean-codegen` =
  (project in file("modules/digitalocean-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "digitalocean-codegen",
      openApiInputSpec := (baseDirectory.value / "spec.yaml").getPath
    )

lazy val `scaleway-autoscaling-codegen` =
  (project in file("modules/scaleway-autoscaling-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "scaleway-autoscaling-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.autoscaling.v1alpha1.Api.yml").getPath,
      openApiApiPackage := "scaleway.autoscaling.api",
      openApiModelPackage := "scaleway.autoscaling.models",
      openApiInvokerPackage := "scaleway.autoscaling"
    )

lazy val `scaleway-containers-codegen` =
  (project in file("modules/scaleway-containers-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "scaleway-containers-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.containers.v1beta1.Api.yml").getPath,
      openApiApiPackage := "scaleway.containers.api",
      openApiModelPackage := "scaleway.containers.models",
      openApiInvokerPackage := "scaleway.containers"
    )

lazy val `scaleway-iam-codegen` =
  (project in file("modules/scaleway-iam-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "scaleway-iam-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.iam.v1alpha1.Api.yml").getPath,
      openApiApiPackage := "scaleway.iam.api",
      openApiModelPackage := "scaleway.iam.models",
      openApiInvokerPackage := "scaleway.iam"
    )

lazy val `scaleway-ipam-codegen` =
  (project in file("modules/scaleway-ipam-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "scaleway-ipam-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.ipam.v1.Api.yml").getPath,
      openApiApiPackage := "scaleway.ipam.api",
      openApiModelPackage := "scaleway.ipam.models",
      openApiInvokerPackage := "scaleway.ipam"
    )

lazy val `scaleway-k8s-codegen` =
  (project in file("modules/scaleway-k8s-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "scaleway-k8s-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.k8s.v1.Api.yml").getPath,
      openApiApiPackage := "scaleway.k8s.api",
      openApiModelPackage := "scaleway.k8s.models",
      openApiInvokerPackage := "scaleway.k8s"
    )

lazy val `scaleway-key-manager-codegen` =
  (project in file("modules/scaleway-key-manager-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "scaleway-key-manager-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.key_manager.v1alpha1.Api.yml").getPath,
      openApiApiPackage := "scaleway.keymanager.api",
      openApiModelPackage := "scaleway.keymanager.models",
      openApiInvokerPackage := "scaleway.keymanager"
    )

lazy val `scaleway-lb-codegen` =
  (project in file("modules/scaleway-lb-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "scaleway-lb-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.lb.v1.ZonedApi.yml").getPath,
      openApiApiPackage := "scaleway.lb.api",
      openApiModelPackage := "scaleway.lb.models",
      openApiInvokerPackage := "scaleway.lb"
    )

lazy val `scaleway-mongodb-codegen` =
  (project in file("modules/scaleway-mongodb-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "scaleway-mongodb-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.mongodb.v1.Api.yml").getPath,
      openApiApiPackage := "scaleway.mongodb.api",
      openApiModelPackage := "scaleway.mongodb.models",
      openApiInvokerPackage := "scaleway.mongodb"
    )

lazy val `scaleway-secret-manager-codegen` =
  (project in file("modules/scaleway-secret-manager-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "scaleway-secret-manager-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.secret_manager.v1beta1.Api.yml").getPath,
      openApiApiPackage := "scaleway.secretmanager.api",
      openApiModelPackage := "scaleway.secretmanager.models",
      openApiInvokerPackage := "scaleway.secretmanager"
    )

lazy val `scaleway-vpc-codegen` =
  (project in file("modules/scaleway-vpc-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "scaleway-vpc-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.vpc.v2.Api.yml").getPath,
      openApiApiPackage := "scaleway.vpc.api",
      openApiModelPackage := "scaleway.vpc.models",
      openApiInvokerPackage := "scaleway.vpc"
    )

lazy val `scaleway-vpc-gw-codegen` =
  (project in file("modules/scaleway-vpc-gw-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "scaleway-vpc-gw-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.vpc_gw.v2.Api.yml").getPath,
      openApiApiPackage := "scaleway.vpcgw.api",
      openApiModelPackage := "scaleway.vpcgw.models",
      openApiInvokerPackage := "scaleway.vpcgw"
    )
