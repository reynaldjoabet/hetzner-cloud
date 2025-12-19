package routes
import hcloud.models.Firewall
import hcloud.api.Firewalls
import hcloud.models.ApplyToResourcesRequest
import hcloud.models.ApplyToResourcesResponse
import hcloud.models.CreateFirewallRequest
import hcloud.models.CreateFirewallResponse
import hcloud.models.GetActionResponse
import hcloud.models.GetFirewallResponse
import hcloud.models.ListActionsResponse
import hcloud.models.ListFirewallsResponse
import hcloud.models.RemoveFromResourcesRequest
import hcloud.models.RemoveFromResourcesResponse
import hcloud.models.ReplaceFirewallRequest
import hcloud.models.ReplaceFirewallResponse
import hcloud.models.SetRulesRequest
import hcloud.models.SetRulesResponse
import hcloud.JsonSupport.{*, given}
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend
import cats.syntax.flatMap.toFlatMapOps

abstract class FirewallRoutes[F[*]: Concurrent](backend: Backend[F])
    extends Http4sDsl[F] {}
