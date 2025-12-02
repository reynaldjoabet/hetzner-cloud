package routes
import hcloud.models.LoadBalancerType
import hcloud.api.LoadBalancerTypes
import hcloud.models.GetLoadBalancerTypeResponse
import hcloud.models.ListLoadBalancerTypesResponse
import hcloud.JsonSupport.{*, given}
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend
import cats.syntax.flatMap.toFlatMapOps

abstract class LoadBalancerTypeRoutes[F[*]: Concurrent](backend: Backend[F]) extends Http4sDsl[F] {}
