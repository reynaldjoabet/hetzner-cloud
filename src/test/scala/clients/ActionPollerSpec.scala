package clients

import cats.effect.IO
import cats.effect.Ref
import configs.ActionPollConfig
import hcloud.models.Action
import hcloud.models.ActionEnums
import hcloud.models.ActionError

import java.time.OffsetDateTime
import scala.concurrent.duration.*

class ActionPollerSpec extends munit.CatsEffectSuite {

  private def action(status: ActionEnums.Status, error: ActionError = null): Action =
    Action(
      id = 1L,
      command = "test_command",
      status = status,
      started = OffsetDateTime.now(),
      finished = OffsetDateTime.now(),
      progress = 0,
      resources = Seq.empty,
      error = error
    )

  private val fastConfig =
    ActionPollConfig(interval = 1.millis, maxInterval = 5.millis, timeout = 2.seconds)

  test("returns immediately once the action reaches success") {
    val getAction: Long => IO[Either[HetznerError, Action]] =
      _ => IO.pure(Right(action(ActionEnums.Status.success)))

    ActionPoller.poll(fastConfig)(getAction)(1L).map {
      case Right(a) => assertEquals(a.status, ActionEnums.Status.success)
      case Left(e)  => fail(s"expected success, got $e")
    }
  }

  test("polls through running states until success") {
    for {
      calls <- Ref.of[IO, Int](0)
      getAction: (Long => IO[Either[HetznerError, Action]]) = _ =>
        calls.getAndUpdate(_ + 1).map { n =>
          if (n < 3) Right(action(ActionEnums.Status.running)) else Right(action(ActionEnums.Status.success))
        }
      result <- ActionPoller.poll(fastConfig)(getAction)(1L)
      n <- calls.get
    } yield {
      assertEquals(result.map(_.status), Right(ActionEnums.Status.success))
      assertEquals(n, 4)
    }
  }

  test("maps a status = error action to ActionFailed with the action's error details") {
    val err = ActionError(code = "action_failed", message = "something went wrong")
    val getAction: Long => IO[Either[HetznerError, Action]] =
      _ => IO.pure(Right(action(ActionEnums.Status.error, err)))

    ActionPoller.poll(fastConfig)(getAction)(42L).map {
      case Left(HetznerError.ActionFailed(actionId, code, message)) =>
        assertEquals(actionId, 42L)
        assertEquals(code, "action_failed")
        assertEquals(message, "something went wrong")
      case other => fail(s"expected ActionFailed, got $other")
    }
  }

  test("propagates a transport/API error from getAction without retrying") {
    val boom = HetznerError.NotFound("action not found")
    val getAction: Long => IO[Either[HetznerError, Action]] = _ => IO.pure(Left(boom))

    ActionPoller.poll(fastConfig)(getAction)(1L).map { result =>
      assertEquals(result, Left(boom))
    }
  }

  test("times out if the action never leaves running") {
    val getAction: Long => IO[Either[HetznerError, Action]] =
      _ => IO.pure(Right(action(ActionEnums.Status.running)))

    ActionPoller.poll(fastConfig)(getAction)(7L).map {
      case Left(HetznerError.ActionTimedOut(actionId, _)) => assertEquals(actionId, 7L)
      case other                                          => fail(s"expected ActionTimedOut, got $other")
    }
  }

}
