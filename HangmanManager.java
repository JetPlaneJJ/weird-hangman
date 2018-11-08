// Jia-Jia Lin
// CSE 143 AP with Zachary Chun
// Homework 4
// A HangmanManager class keeps track of the status of a Hangman game. This
// manager cheats in traditional hangman by not choosing a word until 
// it needs to.

import java.util.*;

public class HangmanManager
{
   private Collection<String> dictionary; // current list of dictionary words
   private Set<Character> alreadyGuessed; // characters already guessed
   private String currentPattern; // the current pattern being displayed
   private int maxWrong; // maximum number of wrong guesses allowed

   // Pre: Takes in a dictionary of words, word length, and the max number 
   //      of wrong guesses.
   // Post: Throws an IllegalArgumentException if the given word length is 
   //       less than 1 or if the number of wrong guesses is less than 0.
   //       Creates a new dictionary containing only the words of the specified
   //       length.
   public HangmanManager(Collection<String> dictionary, int length, int max)
   {
      if (length < 1 || maxWrong < 0)
      {
         throw new IllegalArgumentException("Error.");
      }
      this.dictionary = new TreeSet<String>();
      for (String s : dictionary)
      {
         if (s.length() == length)
         {
            this.dictionary.add(s);
         }
      }
      this.alreadyGuessed = new TreeSet<Character>();
      this.currentPattern = "";
      while (length > 1)
      {
         this.currentPattern += "- ";
         length--;
      }
      this.currentPattern += "-";
      this.maxWrong = max;
   }

   // Post: Returns the current set of words being considered.
   public Set<String> words()
   {
      Set<String> currSet = new TreeSet<String>();
      Iterator<String> it = dictionary.iterator();
      while (it.hasNext())
      {
         currSet.add(it.next());
      }
      return currSet;
   }
   
   // Post: Returns the remaining number of guesses.
   public int guessesLeft()
   {
      return maxWrong;
   }
   
   // Post: Returns the current set of letters already guessed by the user.
   public Set<Character> guesses()
   {
      return alreadyGuessed;
   }
   
   // Post: Return the current display for the hangman game. 
   //       Letters not yet guessed are displayed as a dash and spaces 
   //       separate all letters.  
   //       Throws an IllegalStateException if the set of words is empty.
   public String pattern()
   {
      if (dictionary.isEmpty())
      {
         throw new IllegalStateException("Empty set of words.");
      }
      return this.currentPattern;
   }
   
   // Pre:  Takes in a character to guess for the hangman game.
   // Post: Returns the number of occurrences of the guessed letter in the new 
   //       pattern and decrement number of guesses left. 
   //       Throws IllegalStateException if the number of guesses left is not
   //       at least 1 or if the set of words is empty.  
   //       Throws IllegalArgumentException if the set of words is not empty
   //       but the character being guessed was already guessed.
   public int record(char guess)
   {
      if (guessesLeft() < 1 || dictionary.isEmpty())
      {
         throw new IllegalStateException("No guesses possible at the moment.");
      }
      if (alreadyGuessed.contains(guess))
      {
         throw new IllegalArgumentException("Letter already guessed.");
      }
      alreadyGuessed.add(guess);
      Map<String, Set<String>> map = makeMap(guess);
      int mostKeys = 0;
      String keyToKeep = "";
      Iterator<String> itr = map.keySet().iterator();
      while (itr.hasNext())
      {
         String key = itr.next();
         if (map.get(key).size() > mostKeys)
         {            
            keyToKeep = key;
            mostKeys = map.get(key).size();
         }
      }
      if (this.currentPattern.equals(keyToKeep))
      {
         this.maxWrong--; 
      }
      updateDictionary(map.get(keyToKeep));
      this.currentPattern = keyToKeep;
      return howManyTimesLetterOccurs(this.currentPattern, guess);
   }
   
   // Pre: Takes in a whole word and the character to find within the word.
   // Post: Returns the number of times the letter occurs in the word.
   private int howManyTimesLetterOccurs(String word, char guess)
   {
      int times = 0;
      for (int i = 0; i < word.length(); i++)
      {
         if (word.charAt(i) == guess)
         {
            times++;
         }
      }
      return times;
   }
   
   // Pre: Takes in a word and the character to find within word.
   // Post: Changes the current pattern to the new pattern.
   private String makePattern(String word, char guess)
   {
      String pattern = "";
      word = format(word);
      for (int i = 0; i < word.length()-1; i+=2)
      {
         if (word.charAt(i) == guess)
         {
            pattern += "" + guess;
         }
         else
         {
            if (!Character.isLetter(currentPattern.charAt(i)))
            {
               pattern += "-";
            }
            else
            {
               pattern += "" + currentPattern.charAt(i);
            }
         }
         pattern += " ";
      }
      if (word.charAt(word.length()-1) != guess)
      {
         if (!Character.isLetter(currentPattern.charAt(currentPattern.length()-1))) 
         {
            pattern += "-";
         }
         else
         {
            pattern += "" + word.charAt(word.length()-1);
         }
      }
      else
      {
         pattern += ("" + guess);
      }
      return pattern;
   }
   
   // Pre: Takes in the letter the user guessed
   // Post: Returns a map of all the possible patterns and their
   //       corresponding words.
   private Map<String, Set<String>> makeMap(char guess)
   {
      Map<String, Set<String>> map = new TreeMap<String, Set<String>>();
      Iterator<String> itr = dictionary.iterator();
      for (int i = 0; i < dictionary.size(); i++)
      {
         String currentWord = itr.next();          
         String key = makePattern(currentWord, guess);
         if (!map.containsKey(key))
         {
            Set<String> newSet = new TreeSet<String>();
            newSet.add(currentWord);
            map.put(key, newSet);
         }
         else
         {
            map.get(key).add(currentWord);
         }
      }
      return map;
   }
   
   // Pre: Takes in a string representing a word.
   // Post: Returns a String separating each letter with a space.
   private String format(String word)
   {
      String result = "";
      for (int i = 0; i < word.length()-1; i++)
      {
         result += "" + word.charAt(i) + " ";
      }
      result += word.charAt(word.length()-1);
      return result;
   }
   
   // Pre: Takes in a key to represent the words being kept in the 
   //      new dictionary for the next round.
   // Post: Updates the dictionary to only containing the words
   //       corresponding to the given key.
   private void updateDictionary(Set<String> keepWords)  
   {
      Collection<String> newDictionary = new TreeSet<String>();
      Iterator<String> itr = keepWords.iterator();
      while (itr.hasNext())
      {
         newDictionary.add(itr.next());
      }
      dictionary = newDictionary;
   }
}
