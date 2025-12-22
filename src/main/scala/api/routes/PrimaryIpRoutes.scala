package routes
import hcloud.models.PrimaryIP
import hcloud.api.PrimaryIps
import hcloud.models.AssignPrimaryIpToResourceRequest
import hcloud.models.AssignPrimaryIpToResourceResponse
import hcloud.models.ChangePrimaryIpProtectionResponse
import hcloud.models.ChangeReverseDnsRecordsForPrimaryIpResponse
import hcloud.models.CreatePrimaryIpRequest
import hcloud.models.CreatePrimaryIpResponse
import hcloud.models.DnsPtr
import hcloud.models.GetActionResponse
import hcloud.models.GetPrimaryIpResponse
import hcloud.models.ListActionsResponse
import hcloud.models.ListPrimaryIpsResponse
import hcloud.models.Protection
import hcloud.models.ReplacePrimaryIpRequest
import hcloud.models.ReplacePrimaryIpResponse
import hcloud.models.UnassignPrimaryIpFromResourceResponse
import hcloud.JsonSupport.{*, given}
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend
import cats.syntax.flatMap.toFlatMapOps

abstract class PrimaryIpRoutes[F[*]: Concurrent](backend: Backend[F])
    extends Http4sDsl[F] {}
