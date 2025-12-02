package routes
import hcloud.models.PlacementGroup
import hcloud.api.PlacementGroups
import hcloud.models.CreatePlacementgroupRequest
import hcloud.models.CreatePlacementgroupResponse
import hcloud.models.GetPlacementgroupResponse
import hcloud.models.ListPlacementGroupsResponse
import hcloud.models.ReplacePlacementgroupRequest
import hcloud.models.ReplacePlacementgroupResponse
import hcloud.JsonSupport.{*, given}
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend
import cats.syntax.flatMap.toFlatMapOps

abstract class PlacementGroupRoutes[F[*]: Concurrent](backend: Backend[F]) extends Http4sDsl[F] {}
