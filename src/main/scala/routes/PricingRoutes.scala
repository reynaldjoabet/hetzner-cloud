package routes
import hcloud.models.Price
import hcloud.api.Pricing
import hcloud.models.ListPricesResponse
import hcloud.JsonSupport.{*, given}
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend
import cats.syntax.flatMap.toFlatMapOps

abstract class PricingRoutes[F[*]: Concurrent](backend: Backend[F]) extends Http4sDsl[F] {}
