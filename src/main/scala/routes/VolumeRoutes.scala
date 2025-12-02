package routes
import hcloud.models.Volume
import hcloud.api.Volumes
import hcloud.models.AttachVolumeToServerRequest
import hcloud.models.AttachVolumeToServerResponse
import hcloud.models.ChangeVolumeProtectionRequest
import hcloud.models.ChangeVolumeProtectionResponse
import hcloud.models.CreateVolumeRequest
import hcloud.models.CreateVolumeResponse
import hcloud.models.DetachVolumeResponse
import hcloud.models.GetActionResponse
import hcloud.models.GetVolumeResponse
import hcloud.models.ListActionsResponse
import hcloud.models.ListVolumesResponse
import hcloud.models.ReplaceVolumeRequest
import hcloud.models.ReplaceVolumeResponse
import hcloud.models.ResizeVolumeRequest
import hcloud.models.ResizeVolumeResponse
import hcloud.JsonSupport.{*, given}
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend
import cats.syntax.flatMap.toFlatMapOps

abstract class VolumeRoutes[F[*]: Concurrent](backend: Backend[F]) extends Http4sDsl[F] {}
