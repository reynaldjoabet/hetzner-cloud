package support

import hcloud.models.ActionEnums
import org.http4s.Method
import org.http4s.Uri
import sttp.model.StatusCode

/** `-language:strictEquality` requires a `CanEqual` witness for any type compared with `==`/`!=`, including
  * singleton-pattern matches like `case GET -> Root / "x" =>` or `case ActionEnums.Status.success =>`. These are
  * third-party (http4s, sttp) or codegen-generated types that don't declare one themselves, but all have plain
  * case-object/case-class equality, so it's safe to opt them in wholesale rather than per call site.
  */
object CanEqualInstances {
  given CanEqual[Method, Method] = CanEqual.canEqualAny
  given CanEqual[Uri.Path, Uri.Path] = CanEqual.canEqualAny
  given CanEqual[StatusCode, StatusCode] = CanEqual.canEqualAny
  given CanEqual[ActionEnums.Status, ActionEnums.Status] = CanEqual.canEqualAny
}
