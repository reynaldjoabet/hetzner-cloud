package routes
import hcloud.models.PlacementGroup
import hcloud.api.PlacementGroups
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend
import cats.syntax.flatMap.toFlatMapOps

abstract class PlacementGroupRoutes[F[*]: Concurrent](backend: Backend[F])
    extends Http4sDsl[F] {}
