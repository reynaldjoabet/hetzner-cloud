package routes
import hcloud.models.Image
import hcloud.api.Images
import hcloud.models.ChangeImageProtectionRequest
import hcloud.models.ChangeImageProtectionResponse
import hcloud.models.GetActionResponse
import hcloud.models.GetImageResponse
import hcloud.models.ListActionsResponse
import hcloud.models.ListImagesResponse
import hcloud.models.ReplaceImageRequest
import hcloud.models.ReplaceImageResponse
import hcloud.JsonSupport.{*, given}
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend
import cats.syntax.flatMap.toFlatMapOps

abstract class ImageRoutes[F[*]: Concurrent](backend: Backend[F])
    extends Http4sDsl[F] {}
