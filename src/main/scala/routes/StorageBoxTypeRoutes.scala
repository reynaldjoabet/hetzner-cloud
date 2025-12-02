package routes
import hcloud.models.StorageBoxType
import hcloud.api.StorageBoxTypes
import hcloud.models.GetStorageBoxTypeResponse
import hcloud.models.ListStorageBoxTypesResponse
import hcloud.JsonSupport.{*, given}
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend
import cats.syntax.flatMap.toFlatMapOps

abstract class StorageBoxTypeRoutes[F[*]: Concurrent](backend: Backend[F]) extends Http4sDsl[F] {}
