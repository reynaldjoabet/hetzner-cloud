package routes
import hcloud.models.Action
import hcloud.api.Actions
import hcloud.models.GetActionResponse
import hcloud.models.GetMultipleActionsResponse
import hcloud.JsonSupport.{*, given}
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend
import cats.syntax.flatMap.toFlatMapOps

abstract class ActionRoutes[F[*]: Concurrent](backend: Backend[F]) extends Http4sDsl[F] {

def routes[U]: AuthedRoutes[U, F] = AuthedRoutes.of[U, F] {

    // --- top-level actions (literal) ----------------
    case GET -> Root / "actions" as user =>
        Actions().withBearerTokenAuth("token").getMultipleActions(Seq()).send(backend).flatMap {
         resp=> resp.code match {
            case sttp.model.StatusCode.Ok => ??? // process resp.body
            case _ => ??? // handle error
          }
        }
    case GET -> Root / "actions" / actionId as user =>
        Actions().withBearerTokenAuth("token").getAction(actionId.toLong).send(backend).flatMap {
            resp=> resp.code match {
                case sttp.model.StatusCode.Ok => Ok() // process resp.body
                case _ => ??? // handle error
            }
        }

  }
}
