package services
import hcloud.models.ListPricesResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.Pricing

abstract class PricingService {

  /** Returns prices for all resources available on the platform. VAT and
    * currency of the Project owner are used for calculations. Both net and
    * gross prices are included in the response.
    *
    * Expected answers: code 200 : ListPricesResponse (The `pricing` key in the
    * reply contains an pricing object with this structure.)
    *
    * Available security schemes: APIToken (http)
    */
  def listPrices(token: String): sttp.client4.Request[
    Either[ResponseException[String], ListPricesResponse]
  ] =
    Pricing().withBearerTokenAuth(token).listPrices
}
