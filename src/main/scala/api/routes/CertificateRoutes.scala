package routes
import hcloud.models.Certificate
import hcloud.api.Certificates
import hcloud.models.CreateCertificateRequest
import hcloud.models.CreateCertificateResponse
import hcloud.models.CertificateResponse
import hcloud.models.UpdateCertificateRequest
import hcloud.JsonSupport.{*, given}
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend
import cats.syntax.flatMap.toFlatMapOps

abstract class CertificateRoutes[F[*]: Concurrent](backend: Backend[F]) extends Http4sDsl[F] {}
