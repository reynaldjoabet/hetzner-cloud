package routes
import hcloud.models.Network
import hcloud.api.Networks
import hcloud.models.AddRouteToNetworkResponse
import hcloud.models.AddSubnetToNetworkResponse
import hcloud.models.ChangeIpRangeOfNetworkRequest
import hcloud.models.ChangeIpRangeOfNetworkResponse
import hcloud.models.ChangeNetworkProtectionRequest
import hcloud.models.ChangeNetworkProtectionResponse
import hcloud.models.CreateNetworkRequest
import hcloud.models.CreateNetworkResponse
import hcloud.models.DeleteRouteFromNetworkResponse
import hcloud.models.DeleteSubnetFromNetworkRequest
import hcloud.models.DeleteSubnetFromNetworkResponse
import hcloud.models.GetActionResponse
import hcloud.models.GetNetworkResponse
import hcloud.models.ListActionsResponse
import hcloud.models.ListNetworksResponse
import hcloud.models.ReplaceNetworkRequest
import hcloud.models.ReplaceNetworkResponse
import hcloud.models.Route
import hcloud.models.Subnet
import hcloud.JsonSupport.{*, given}
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend
import cats.syntax.flatMap.toFlatMapOps

abstract class NetworkRoutes[F[*]: Concurrent](backend: Backend[F]) extends Http4sDsl[F] {}
