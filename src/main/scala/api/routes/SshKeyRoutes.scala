package routes
import hcloud.models.SshKey
import hcloud.api.SshKeys
import hcloud.models.CreateSshKeyRequest
import hcloud.models.CreateSshKeyResponse
import hcloud.models.GetSshKeyResponse
import hcloud.models.ListSshKeysResponse
import hcloud.models.ReplaceSshKeyRequest
import hcloud.models.ReplaceSshKeyResponse
import hcloud.JsonSupport.{*, given}
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend
import cats.syntax.flatMap.toFlatMapOps

abstract class SshKeyRoutes[F[*]: Concurrent](backend: Backend[F])
    extends Http4sDsl[F] {}
