package example

import io.Source.fromFile

object Main extends App {
  val fileSource = fromFile("words.txt")
  val wordsRaw: List[String] = fileSource.getLines.toList
  val words: List[String] = wordsRaw.filter(!_.isEmpty).map(_.toLowerCase)

  HangmanGame.playInLoop(words)
}
