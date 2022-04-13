# WordleSolver

## Overview
The following repo contains a Wordle solver that solves the game in 3-4 guesses using Java.

## Guidelines
Follow the following steps to run the program, given that Java is already installed

1. Clone this repository

2. Go to bin directory

3. Run the following command `java Run`

In case of an error, recompile the code using `javac Run.java && javac WordleSolver.java` or using an IDE (e.g. IntelliJ IDEA) and run the program normally.

## Source Code Review

Source code for the example is located in /src/WordleSolver.java. The followings overview of how the logic behinds major parts of the code

1. Obtaining all possible Wordle answers

```java
private static Set<String> getWords(String fileName, int wordLength) {
  Set<String> words = null;

  try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
    words = stream.map(word -> word.replace(" ", "")).collect(Collectors.toSet());
  } catch (IOException e) {
	  System.out.println("An Error occurred while fetching the words. \nTerminating.");
	  System.exit(1);
  }

  return words;
}
```
This code just reads from a file and filters any words that are not of the specified length. This was done to ensure that if a longer word is added by mistake, it is not included when the program is running.

2. Assignment of score

```java
private static Set<Character> bonusLetters = new HashSet<>(Arrays.asList('r', 'l', 's', 't', 'n', 'e'));
```

Any word that includes any character in the above set will have a bonus point for each character. This is done to so that the algorithm can pick which word to choose.

```java
private static String getTryWord(Set<String> words) {

  String bestWord = null;
  int maxUniqueLetter = 0;
  
  int maxScore = 10;

  Set<Character> letters = new HashSet<>();

  for (String word : words) {

    for (int i = 0; i < word.length(); i++) {
      letters.add(word.charAt(i));
    }
    int score = letters.size();

    for (char c : letters) {
      if (bonusLetters.contains(c)) {
      score++;
    }

  }

  if (score == maxScore) {
    return word;
  } else {
    if (letters.size() > maxUniqueLetter) {
      maxUniqueLetter = letters.size();
      bestWord = word;
    }
    letters.clear();
  }
 }
 
 return bestWord;
}
```

This is the main part of the algorithm that pickes the next plausible word. As mentioned earlier, if a word has the character that is present in the bonusCharacher set, it will get a +1 point. In case a word has the maximum number of point, which is 10, it will be automatically selected. Otherwise, we will have to check all the words. The algorithm will end up selecting the first word with the most number of correct unique characters. This was done in order to reduce the number of words left to select from.

3. Removal of incorrect words

```java
private static Set<String> getNewWords(Set<String> words, String tryWord, String result) {

  Set<String> newWords = new HashSet<>();

  for (String word : words) {
    boolean isValidWord = true;
    char[] wordChars = word.toCharArray();

    for (int i = 0; i < tryWord.length(); i++) {

      char r = result.charAt(i);

      if ((r == 'g' && wordChars[i] != tryWord.charAt(i))
          || (r == 'y' && (wordChars[i] == tryWord.charAt(i) || !contains(wordChars, tryWord.charAt(i))))
          || (r == 'r' && contains(wordChars, tryWord.charAt(i)))) {
           
       isValidWord = false;
        break;

      } else if (r == 'g') {
        wordChars[i] = '-';
      }

      }
      if (isValidWord) {
        System.out.println(word);
        newWords.add(word);
      }
   }
   return newWords;
}
```
The user should input the results as requested (e.g. rrgryr) as shown in the Wordle game. The following instructions demonstrates the meaning of each colour in Wordle.

- green (g); which rpresents correct letter at the correct position
- yellow (y); which represents correct letter at the incorrect position
- dark grey (r); which represents incorrect letter at the incorrect position

Thus, there are 3 cases that can happen;

- first case (green): if word character is green, we have to remove any words that does not include this character at the specified position
- second case (yellow): if word character is yellow, we have to remove any words that does include this character or does include this character but at the wrong position
- third case (dark grey): if character isn't in the word, we have to remove any words that does include this character

4.  This process is repeated 5 times or if the player wins.

## More Info
More information is present in the code as comments

## Disclaimer
* This is a starter algorithm which can be improved to enhance either performance or success rate.
* This project was done with the help of [Ghaith Chrit](https://github.com/Ghaith-Chrit)'s supervision

## License
* Check the LICENSE file
