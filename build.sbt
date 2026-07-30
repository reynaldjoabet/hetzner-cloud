import Dependencies.*

ThisBuild / scalaVersion := "3.3.8"
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalacOptions := Seq(
  "-encoding",
  "UTF-8",
  "-no-indent",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-source:3.3",
  "-java-output-version:17",
  "-Werror",
  "-Wvalue-discard",
  "-Wnonunit-statement",
  "-Xlint:all",
  "-Xcheck-macros",
  "-Xmax-inlines:64"
)

Global / onChangedBuildSource := ReloadOnSourceChanges

val generatedScalacOptions = Seq(
  "-encoding",
  "UTF-8",
  "-java-output-version:17",
  "-Xmax-inlines:64"
)

val commonSettings = Seq(
  // Generated code is not held to our lint flags: it uses indentation syntax
  // (trips -no-indent) and is full of unused/discarded values. Resetting to
  // empty rather than subtracting individual flags means this can't drift out
  // of sync with the ThisBuild list above.
  scalacOptions := generatedScalacOptions,
  openApiModelNamePrefix := "",
  openApiModelNameSuffix := "",
  openApiGenerateMetadata := SettingDisabled,
  // Module-local config.json, shared with the openapi-generator CLI so the two
  // stay in sync. It owns the spec path, package layout, and -- importantly --
  // globalProperties.supportingFiles: see the sourceGenerators note below.
  openApiConfigFile := (baseDirectory.value / "config.json").getPath,
  // Single shared ignore file at modules/, one level above each module dir
  openApiIgnoreFileOverride := (baseDirectory.value.getParentFile / ".openapi-generator-ignore").getPath,
  openApiOutputDir := (baseDirectory.value / "src/main/scala").getAbsolutePath,
  openApiGenerateModelTests := SettingDisabled,
  openApiGenerateApiTests := SettingDisabled,
  // Fail fast on bad specs -- the hcloud spec passes the validator
  openApiValidateSpec := Some(true),
  generate := Def.uncached {
    openApiGenerate.value
  },

  Compile / sourceGenerators += generate.taskValue,
  Compile / unmanagedSourceDirectories := Seq.empty,
  libraryDependencies ++= Seq(
    sttpJsoniter,
    jsoniter,
    jsoniterMacros,
    jsoniterCirce
  )
)

/** Defines a codegen module `<id>-codegen` generating into `modules/<id>-codegen`. The spec path and package layout
  * come from that module's config.json.
  */
def codegenModule(id: String): Project =
  Project(s"$id-codegen", file(s"modules/$id-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(name := s"$id-codegen")

lazy val hcloud = codegenModule("hcloud")

lazy val modules: Seq[Project] = Seq(hcloud)

lazy val root = (project in file("."))
  .settings(
    semanticdbEnabled := true,
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
      pureconfigGeneric,
      slf4j,
      logback,
      scribe,
      scribeSlf4j,
      scribeCats,
      jsoniter,
      jsoniterMacros,
      jsoniterCirce,
      munit,
      munitCatsEffect
    ),
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "hcloud.generated",
    buildInfoObject := "HCloudBuildInfo"
  )
  .enablePlugins(BuildInfoPlugin)
  .dependsOn(modules.map(_ % "compile->compile") *)
  .aggregate(modules.map(m => LocalProject(m.id)) *)

lazy val populateTestDB =
  taskKey[Unit]("Run PopulateTestDatabase main class from the test folder")

populateTestDB := Def.uncached {
  val log = streams.value.log
  (Test / runMain).toTask(s"utils.PopulateTestDatabase").value
}
