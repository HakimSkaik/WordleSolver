import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Set;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordleSolver {
	// Creating bonus points set instantiated with the letters r,l,s,t,n,e (same as
	// wheel of fortune)
	private static Set<Character> bonusLetters = new HashSet<>(Arrays.asList('r', 'l', 's', 't', 'n', 'e'));

	public static void play() {

		// creating a set of type String to display dictionary of words using a path
		// called index.txt
		Set<String> dictionary = getWords("file.txt", 5);

		// have a set of words and each time new information is received after making a
		// guess
		// continue to narrow it down so the size keeps on getting smaller until
		// solution is achieved

		// Opening a Scanner to take user input
		try (Scanner scanner = new Scanner(System.in)) {

			// loop for the 6 guesses allowed
			for (int i = 0; i < 6; i++) {

				// calling a method called getTryWord using the current set of words available
				String tryWord = getTryWord(dictionary);
				String result;

				// printing message to the user after the first guess and iterating the guess
				// number every time
				// including a message which indicates if user enters an input out of a
				// possible number of words
				System.out.println("#" + (i + 1) + "Try: " + tryWord + "(out of" + dictionary.size() + "words)");

				// help information for user
				System.out.println("(g = green, y = yellow, r = dark grey )");

				// showing result
				result = scanner.next();

				if (result.equalsIgnoreCase("ggggg")) {
					System.out.println("Congrats! You won!");
					break;
				}

				dictionary = getNewWords(dictionary, tryWord, result);

			}
		}

	}

	private static String getTryWord(Set<String> words) {

		// prefer more unique letters if user tries guess with two of the same letters
		String bestWord = null;
		int maxUniqueLetter = 0;

		// based on the number of unique letters, if that letter is in bonus letters
		// then 2 points are given to add to the score
		int maxScore = 10;// words.iterator().next().length() * 2;// 5 * 2 = 10

		// unique letters in the word we're looking at
		Set<Character> letters = new HashSet<>();

		// iterating over all of the words
		for (String word : words) {

			// get the unique letters
			for (int i = 0; i < word.length(); i++) {
				// add each letter
				letters.add(word.charAt(i));
			}
			// current score
			int score = letters.size();

			// iterating through the letters
			for (char c : letters) {
				// if bonus letter contains the same letter c then we'll add another one to the
				// score
				if (bonusLetters.contains(c)) {
					score++;
				}

			}
			// break out of the loop and return the max word
			if (score == maxScore) {
				return word;
			} else {
				if (letters.size() > maxUniqueLetter) {
					maxUniqueLetter = letters.size();
					bestWord = word;
				}
				// after each word we look at, we'll clear it
				letters.clear();
			}
		}
		return bestWord;
	}

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

	private static Set<String> getNewWords(Set<String> words, String tryWord, String result) {

		Set<String> newWords = new HashSet<>();

		// iterating over all the words
		for (String word : words) {
			boolean isValidWord = true;
			char[] wordChars = word.toCharArray();

			// iterate over the letters
			for (int i = 0; i < tryWord.length(); i++) {

				// result at the current index
				char r = result.charAt(i);

				// green represents correct letter at the correct position
				// yellow represents correct letter at the incorrect position
				// dark grey represents incorrect letter at the incorrect position

				// first case (green): if word character is green, we know not to add to the
				// new words set/ remove from set
				// second case (yellow): if word character is yellow but is incorrect position
				// then fail
				// third case: if character isn't in the word then fail
				// fourth case(red): if character is in the target word then fail
				if ((r == 'g' && wordChars[i] != tryWord.charAt(i))
						|| (r == 'y' && (wordChars[i] == tryWord.charAt(i) || !contains(wordChars, tryWord.charAt(i))))
						|| (r == 'r' && contains(wordChars, tryWord.charAt(i)))) {

					isValidWord = false;
					break;

				} else if (r == 'g') {
					// replace with '-' character to remove word from being used later
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

	private static boolean contains(char[] letters, char targetChar) {

		for (char c : letters) {
			if (c == targetChar) {
				return true;
			}
		}
		return false;
	}
}