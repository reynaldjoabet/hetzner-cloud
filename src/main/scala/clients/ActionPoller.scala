package clients

import cats.effect.IO
import configs.ActionPollConfig
import hcloud.models.Action
import hcloud.models.ActionEnums

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.MILLISECONDS

/** Hetzner's mutating endpoints return immediately with an Action in `running` state; the underlying change isn't
  * applied until that Action reaches `success` (or `error`). This polls a single Action to completion, so callers never
  * have to hand-roll that loop -- see [[HetznerClient.awaitAction]].
  */
object ActionPoller {

  /** @param getAction
    *   fetches the current state of one Action by ID. Kept as a plain function (rather than requiring a whole
    *   [[HetznerClient]]) so the polling/backoff logic can be tested without a live backend.
    */
  def poll(config: ActionPollConfig)(
      getAction: Long => IO[Either[HetznerError, Action]]
  )(actionId: Long): IO[Either[HetznerError, Action]] =
    IO.monotonic.flatMap { start =>
      def loop(interval: FiniteDuration): IO[Either[HetznerError, Action]] =
        getAction(actionId).flatMap {
          case Left(err)     => IO.pure(Left(err))
          case Right(action) =>
            action.status match {
              case ActionEnums.Status.success =>
                IO.pure(Right(action))
              case ActionEnums.Status.error =>
                val code = Option(action.error).map(_.code).getOrElse("unknown")
                val message = Option(action.error).map(_.message).getOrElse("Action failed")
                IO.pure(Left(HetznerError.ActionFailed(actionId, code, message)))
              case ActionEnums.Status.running =>
                IO.monotonic.flatMap { now =>
                  if ((now - start) >= config.timeout) {
                    IO.pure(
                      Left(
                        HetznerError.ActionTimedOut(
                          actionId,
                          s"Action $actionId did not complete within ${config.timeout}"
                        )
                      )
                    )
                  } else {
                    val nextInterval = FiniteDuration(
                      (interval.toMillis * 3) / 2,
                      MILLISECONDS
                    ).min(config.maxInterval)
                    IO.sleep(interval) *> loop(nextInterval)
                  }
                }
            }
        }
      loop(config.interval)
    }

}
