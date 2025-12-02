package routes
import hcloud.models.ServerType
import hcloud.api.ServerTypes
import hcloud.models.GetServerTypeResponse
import hcloud.models.ListServerTypesResponse
import hcloud.JsonSupport.{*, given}
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend
import cats.syntax.flatMap.toFlatMapOps

abstract class ServerTypeRoutes[F[*]: Concurrent](backend: Backend[F]) extends Http4sDsl[F] {}
