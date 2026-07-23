package routes
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.kernel.Concurrent
import sttp.client4.Backend

abstract class ZoneRoutes[F[*]: Concurrent](backend: Backend[F]) extends Http4sDsl[F] {
  def routes[U]: AuthedRoutes[U, F] = AuthedRoutes.of[U, F] {

    // --- top-level actions (literal) ----------------
    case GET -> Root / "zones" / "actions" as user =>
      ???

    case GET -> Root / "zones" / "actions" / actionId as user =>
      ???

    case GET -> Root / "zones" as user =>
      ???

    // --- per-zone: actions (put literals before generic idOrName) ---
    case POST -> Root / "zones" / idOrName / "actions" / "import_zonefile" as user =>
      ???

    case GET -> Root / "zones" / idOrName / "actions" / actionId as user =>
      ???

    case GET -> Root / "zones" / idOrName / "actions" as user =>
      ???

    case POST -> Root / "zones" / idOrName / "actions" / "change_primary_nameservers" as user =>
      ???

    case POST -> Root / "zones" / idOrName / "actions" / "change_protection" as user =>
      ???

    // --- rrsets - specific routes (more specific than generic /:id) ---
    case POST -> Root / "zones" / idOrName / "rrsets" / rrName / rrType / "actions" / "add_records" as user =>
      ???

    case POST -> Root / "zones" / idOrName / "rrsets" / rrName / rrType / "actions" / "remove_records" as user =>
      ???

    case POST -> Root / "zones" / idOrName / "rrsets" / rrName / rrType / "actions" / "set_records" as user =>
      ???

    case POST -> Root / "zones" / idOrName / "rrsets" / rrName / rrType / "actions" / "update_records" as user =>
      ???

    case PUT -> Root / "zones" / idOrName / "rrsets" / rrName / rrType as user =>
      ???

    case GET -> Root / "zones" / idOrName / "rrsets" / rrName / rrType as user =>
      ???

    case GET -> Root / "zones" / idOrName / "rrsets" as user =>
      ???

    // --- zonefile / rrset deletion / zone CRUD (generic) ---
    case GET -> Root / "zones" / idOrName / "zonefile" as user =>
      ???

    case DELETE -> Root / "zones" / idOrName / "rrsets" / rrName / rrType as user =>
      ???

    case DELETE -> Root / "zones" / idOrName as user =>
      ???

    case PUT -> Root / "zones" / idOrName as user =>
      ???

    // fallback - avoid placing this above literals
    case GET -> Root / "zones" / idOrName as user =>
      ???
  }
}
