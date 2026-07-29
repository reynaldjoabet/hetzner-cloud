package configs

import cats.effect.IO
import pureconfig.ConfigReader
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderFailures

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.*

final case class RetryConfig(
    maxAttempts: Int = 5,
    initialBackoff: FiniteDuration = 500.millis,
    maxBackoff: FiniteDuration = 30.seconds
) derives ConfigReader

final case class ActionPollConfig(
    interval: FiniteDuration = 2.seconds,
    maxInterval: FiniteDuration = 10.seconds,
    timeout: FiniteDuration = 5.minutes
) derives ConfigReader

final case class HetznerConfig(
    // No default value on purpose: a missing token should fail config
    // loading, not silently fall back to some placeholder credential.
    token: String,
    baseUrl: String = "https://api.hetzner.cloud/v1",
    requestTimeout: FiniteDuration = 30.seconds,
    retry: RetryConfig = RetryConfig(),
    actionPoll: ActionPollConfig = ActionPollConfig()
) derives ConfigReader

object HetznerConfig {

  final case class ConfigLoadError(failures: ConfigReaderFailures) extends RuntimeException(failures.prettyPrint())

  /** Loads config from `application.conf`'s `hetzner` block, with `token` and `base-url` overridable via the
    * `HCLOUD_TOKEN` / `HCLOUD_BASE_URL` environment variables (see src/main/resources/application.conf).
    */
  def load: IO[HetznerConfig] =
    IO.fromEither(
      ConfigSource.default
        .at("hetzner")
        .load[HetznerConfig]
        .left
        .map(ConfigLoadError.apply)
    )

}
