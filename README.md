# Hangman Game app example

[![Build Status](https://travis-ci.org/diht2016/HangmanGame.svg?branch=master)](https://travis-ci.org/diht2016/HangmanGame)

This is an example app to test integrations with other services, such as [Travis](https://travis-ci.org/).

## How to run

This app requires [sbt](https://www.scala-sbt.org/) to build and run.

Just use the command `sbt run` to run the game, the compiler will build the project if not compiled yet.

## How to play

Please ensure you have selected Latin keyboard layout before playing this game.

On the start of each game, a random word is picked from `words.txt`, which contains 1525 common well-known English words (mostly nouns).

Your aim is to find out which word was chosen by guessing a single letter. If there are any such letters in the word, all of them become visible. If there aren't any, it counts as a mistake, and you lose after making a certain amount of mistakes. So be careful and remember all misguessed letters to prevent making the same mistake twice.

You can repeat the game by pressing `y` at the end or quit by pressing `n` (or anything else). Each game, a new word is chosen on random.

Have fun!
