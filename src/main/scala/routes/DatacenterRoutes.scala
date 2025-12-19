package routes
import hcloud.models.DataCenter
import hcloud.api.Datacenters
import hcloud.models.GetDataCenterResponse
import hcloud.models.ListDataCentersResponse
import hcloud.JsonSupport.{*, given}
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend
import cats.syntax.flatMap.toFlatMapOps

abstract class DatacenterRoutes[F[*]: Concurrent](backend: Backend[F])
    extends Http4sDsl[F] {}
