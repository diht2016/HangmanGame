package example

import example.MockInOut.{NL, outputOf, outputOn}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers.be
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class HangmanGameSpec extends AnyFlatSpec {
  import HangmanGame._

  "maskedWord" should "hide uncovered word" in {
    maskedWord(initWordState("test")) should be ("----")
  }

  it should "show discovered word" in {
    maskedWord(fakeMask("test")) should be ("test")
  }

  "askForCharacter" should "return character from input" in {
    outputOn("x") {
      askForCharacter should be ('x')
    } should be ("Guess a letter: ")
  }

  "printGameStatus" should "print current status" in {
    outputOf {
      printGameStatus(initWordState("test"), "Test!")
    } should be (outputOf {clearScreen()} + s"The word: ----${NL}Test!${NL}")
  }

  "mistakesForWord" should "return 8 for 5-letter word" in {
    mistakesForWord("scala") should be (8)
  }

  it should "return 4 for 10-letter word" in {
    mistakesForWord("initialize") should be (4)
  }

  "play" should "print output of success" in {
    outputOn("onetip") {
      play("point", 5) should be (true)
    } should be (phaseOutputs(List(
      ("-----", ""),
      ("-o---", "Hit!"),
      ("-o-n-", "Hit!"),
      ("-o-n-", "Missed, mistake 1 out of 5."),
      ("-o-nt", "Hit!"),
      ("-oint", "Hit!"),
      ("point", "Hit!"),
    )) + s"${NL}You won!${NL}")
  }

  it should "print output of fail" in {
    outputOn("eat") {
      play("philosophy", 3) should be (false)
    } should be (phaseOutputs(List(
      ("----------", ""),
      ("----------", "Missed, mistake 1 out of 3."),
      ("----------", "Missed, mistake 2 out of 3."),
      ("----------", "Missed, mistake 3 out of 3."),
    )) + s"${NL}You lost!${NL}The word was: philosophy${NL}")
  }

  it should "print message if successful guess repeats" in {
    outputOn("eeeat") {
      play("eat", 1) should be (true)
    } should be (phaseOutputs(List(
      ("---", ""),
      ("e--", "Hit!"),
      ("e--", "Letter already present, try another one!"),
      ("e--", "Letter already present, try another one!"),
      ("ea-", "Hit!"),
      ("eat", "Hit!"),
    )) + s"${NL}You won!${NL}")
  }

  "playInLoop" should "print output of success" in {
    val playOut = (phaseOutputs(List(
      ("-", ""),
      ("a", "Hit!"),
    )) + s"${NL}You won!${NL}")
    val req = s"Play again? (y/n)$NL"
    val repeatCount = 15

    outputOn("ay".repeat(repeatCount) + "an") {
      playInLoop(List("a"))
    } should be (s"$playOut$req".repeat(repeatCount + 1))
  }

  def fakeMask(word: String): WordState =
    word.toList.map(LetterState(_, isOpen = true))

  def phaseOutputs(phases: List[(String, String)]): String =
    phases.map(t => outputOf {printGameStatus(fakeMask(t._1), t._2)}).mkString("Guess a letter: ")
}
