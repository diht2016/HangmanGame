package example

import scala.annotation.tailrec
import scala.util.Random

object HangmanGame {
  case class LetterState(letter: Char, isOpen: Boolean)
  type WordState = List[LetterState]

  def maskedWord(wordState: WordState): String = {
    wordState.map(ls => if (ls.isOpen) ls.letter else '-').mkString("")
  }

  def clearScreen(): Unit =
    print("\u001b[2J\u001b[H")

  def printGameStatus(wordState: WordState, action: String): Unit = {
    clearScreen()
    println(s"The word: ${maskedWord(wordState)}")
    println(s"$action")
  }

  def askForCharacter: Char = {
    print("Guess a letter: ")
    Console.in.read.toChar.toLower
  }

  @tailrec
  final def playIteration(wordState: WordState, mistakesDone: Int, mistakesMax: Int, lastAction: String): Boolean = {
    printGameStatus(wordState, lastAction)
    if (mistakesDone == mistakesMax) {
      false
    } else if (wordState.forall(_.isOpen)) {
      true
    } else {
      val charGot = askForCharacter
      wordState.find(_.letter == charGot) match {
        case Some(LetterState(_, true)) =>
          playIteration(wordState, mistakesDone, mistakesMax,
            "Letter already present, try another one!")
        case Some(LetterState(_, _)) =>
          playIteration(
            wordState.map(ls => {
              if (ls.letter == charGot) LetterState(ls.letter, isOpen = true) else ls
            }), mistakesDone, mistakesMax, "Hit!")
        case _ =>
          val mistakesDoneNow = mistakesDone + 1
          playIteration(wordState, mistakesDoneNow, mistakesMax,
            s"Missed, mistake $mistakesDoneNow out of $mistakesMax.")
      }
    }
  }

  def initWordState(word: String): WordState =
    word.toList.map(LetterState(_, isOpen = false))

  def play(word: String, mistakesMax: Int): Boolean = {
    val wordState: WordState = initWordState(word)
    val gameResult = playIteration(wordState, 0, mistakesMax, "")

    println()
    println(s"You ${if (gameResult) "won" else "lost"}!")
    if (!gameResult) {
      println(s"The word was: $word")
    }
    gameResult
  }

  def pickRandom[T](list: List[T]): T =
    list(new Random().nextInt(list.length))

  def mistakesForWord(word: String): Int = Math.ceil(40 / word.length).toInt

  @tailrec
  def playInLoop(words: List[String]): Unit = {
    val word = pickRandom(words)
    val mistakesAllowed = mistakesForWord(word)
    play(word, mistakesAllowed)

    println("Play again? (y/n)")
    if (Console.in.read.toChar == 'y') {
      playInLoop(words)
    }
  }
}
