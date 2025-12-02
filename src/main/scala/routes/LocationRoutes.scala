package routes
import hcloud.models.Location
import hcloud.api.Locations
import hcloud.models.GetLocationResponse
import hcloud.models.ListLocationsResponse
import hcloud.JsonSupport.{*, given}
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend
import cats.syntax.flatMap.toFlatMapOps

abstract class LocationRoutes[F[*]: Concurrent](backend: Backend[F]) extends Http4sDsl[F] {}
