package routes
import hcloud.models.FloatingIp
import hcloud.api.FloatingIps
import hcloud.models.AssignFloatingIpToServerRequest
import hcloud.models.AssignFloatingIpToServerResponse
import hcloud.models.ChangeFloatingIpProtectionResponse
import hcloud.models.ChangeReverseDnsRecordsForFloatingIpResponse
import hcloud.models.CreateFloatingIpRequest
import hcloud.models.CreateFloatingIpResponse
import hcloud.models.DnsPtr
import hcloud.models.GetActionResponse
import hcloud.models.GetFloatingIpResponse
import hcloud.models.ListActionsResponse
import hcloud.models.ListFloatingIpsResponse
import hcloud.models.Protection
import hcloud.models.ReplaceFloatingIpRequest
import hcloud.models.ReplaceFloatingIpResponse
import hcloud.models.UnassignFloatingIpResponse
import hcloud.JsonSupport.{*, given}
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend
import cats.syntax.flatMap.toFlatMapOps

abstract class FloatingIpRoutes[F[*]: Concurrent](backend: Backend[F]) extends Http4sDsl[F] {}
