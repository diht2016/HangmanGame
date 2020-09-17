package example

import scala.annotation.tailrec
import scala.util.Random

class HangmanGame(word: String, mistakesAllowed: Int) {
  private type WordState = List[(Char, Boolean)]
  private val initWordState: WordState = word.toList.map((_, false))

  private def visibleWord(wordState: WordState): String = {
    wordState.map(ct => if (ct._2) ct._1 else '-').mkString("")
  }

  private def clearScreen(): Unit =
    print("\u001b[2J\u001b[H")

  private def printGameStatus(wordState: WordState, action: String): Unit = {
    clearScreen()
    println(s"The word: ${visibleWord(wordState)}")
    println(s"$action")
  }

  private def askForCharacter: Char = {
    print("Guess a letter: ")
    Console.in.read.toChar
  }

  @tailrec
  private def playIteration(wordState: WordState, mistakesDone: Int, lastAction: String): Boolean = {
    printGameStatus(wordState, lastAction)
    if (mistakesDone == mistakesAllowed) {
      false
    } else if (wordState.forall(_._2)) {
      true
    } else {
      val charGot = askForCharacter
      if (wordState.exists(_._1 == charGot)) {
        if (wordState.contains((charGot, true))) {
          playIteration(wordState, mistakesDone, "Letter already present, try another one!")
        } else {
          playIteration(
            wordState.map({
              case (c, _) if c == charGot => (c, true)
              case (c, open) => (c, open)
            }), mistakesDone, "Hit!")
        }
      } else {
        val mistakesDoneNow = mistakesDone + 1
        playIteration(wordState, mistakesDoneNow, s"Missed, mistake $mistakesDoneNow out of $mistakesAllowed.")
      }
    }
  }

  def play: Boolean = {
    val gameResult = playIteration(initWordState, 0, "")
    println()
    println(s"You ${if (gameResult) "won" else "lost"}!")
    if (!gameResult) {
      println(s"The word was: $word")
    }
    gameResult
  }
}

object HangmanGame {
  def pickRandom[T](list: List[T]): T =
    list(new Random().nextInt(list.length))

  def mistakesForWord(word: String): Int = Math.ceil(40 / word.length).toInt

  @tailrec
  def playInLoop(words: List[String]): Unit = {
    val word = pickRandom(words)
    val mistakesAllowed = Math.ceil(40 / word.length).toInt
    val game = new HangmanGame(word, mistakesAllowed)
    game.play

    println("Play again? (y/n)")
    if (Console.in.read.toChar == 'y') {
      playInLoop(words)
    }
  }
}
