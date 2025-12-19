package configs

final case class HetznerConfig(
    token: String,
    baseUrl: String = "https://api.hetzner.cloud/v1",
    requestTimeout: Int = 30000
)
object HetznerConfig {}
