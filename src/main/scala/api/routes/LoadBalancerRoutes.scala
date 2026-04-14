package routes
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend

abstract class LoadBalancerRoutes[F[*]: Concurrent](backend: Backend[F])
    extends Http4sDsl[F] {}
