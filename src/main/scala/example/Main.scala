package example

import io.Source.fromFile

object Main extends App {
  val fileSource = fromFile("words.txt")
  val words: List[String] = fileSource.getLines.toList

  HangmanGame.playInLoop(words)
}
