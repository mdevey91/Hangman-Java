package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by devey on 1/27/17.
 */

public class EvilHangmanGame implements IEvilHangmanGame {
    private TreeSet<String> Dictionary = new TreeSet<String>();
    private TreeSet<String> letters_guessed = new TreeSet<String>();
//    private Map<String, TreeSet<String>> partitions = new HashMap<>();
    private String word_so_far = "";
    public EvilHangmanGame() {}
    @Override
    public void startGame(File dictionary, int wordLength)
    {
        Scanner sc = null;
        try {
            sc = new Scanner(dictionary);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while(sc.hasNext())
        {
            String new_word = sc.next().toLowerCase();
            if(new_word.length() == wordLength)
                Dictionary.add(new_word);
        }
        for(int i = 0; i < wordLength; i++)
        {
            word_so_far += "-";
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException //How does this work?
    {
        Map<String, TreeSet<String>> partitions = new HashMap<>();
        String best_key = null;
        StringBuilder new_key = new StringBuilder();

        //checks if you already guessed a letter.
        for(String letter: letters_guessed)
        {
            if(guess == letter.charAt(0))
                throw new GuessAlreadyMadeException();
        }
        letters_guessed.add(Character.toString(guess)); //records that the letter has not been guessed

        for(String word: Dictionary) //creates all the keys and creates a map with those keys and the words
        {
            for(int i = 0; i < word.length(); i++)
            {
                char c = word.charAt(i);
                if(c == guess)
                    new_key.append(c);
                else
                    new_key.append(word_so_far.charAt(i));
            }
            if(!partitions.containsKey(new_key.toString())) //checks if the key is already in the map.
                partitions.put(new_key.toString(), new TreeSet<String>());  //If the key doesn't exist in the map this will insert the key into the map
            partitions.get(new_key.toString()).add(word); //partitions.get will return a set and .add(word) puts the word into that set.
            new_key.setLength(0); //deletes the string in order to have an empty StringBuilder for a new key.
        }

        for(String key : partitions.keySet())
        {
            if(best_key == null)
                best_key = key;
            else if(partitions.get(key).size() == partitions.get(best_key).size())
            {
                int key_letters = 0;
                int best_key_letters = 0;
                for(int i = 0; i < key.length(); i++)
                {
                    if(Character.isLetter(key.charAt(i)))
                        key_letters++;
                    if(Character.isLetter(best_key.charAt(i)))
                        best_key_letters++;
                }
                if(key_letters < best_key_letters)
                    best_key = key;
                else if(key_letters == best_key_letters)
                {
                    for(int i = key.length() - 1; i >= 0; i--)
                    {
                        if (key.charAt(i) == guess && best_key.charAt(i) != guess)
                        {
                            best_key = key;
                            break;
                        }
                        else if(key.charAt(i) != guess && best_key.charAt(i) == guess)
                        {
                            break;
                        }
                    }
                }
            }
            else if(partitions.get(key).size() > partitions.get(best_key).size())
                best_key = key;
        }
        System.out.println("best key after comparison: " + best_key);
        for(String key : partitions.keySet())
        {
            System.out.println("key: " + key + " has set of size: " + partitions.get(key).size() + " that contains: " + partitions.get(key).toString());
        }
        Dictionary = partitions.get(best_key);
        System.out.println("Dictionary size: " + Dictionary.size());
        word_so_far = best_key;
        return partitions.get(best_key);
    }

    public TreeSet<String> getLettersGuessed() {
        return letters_guessed;
    }

    public String getWordSoFar() {

        return word_so_far;
    }
    public String toStringWordSoFar()
    {
        StringBuilder word = new StringBuilder(word_so_far);
        for(int i = 0; i < word_so_far.length(); i++)
        {
            word.insert(i, " ");
        }
        return word.toString();
    }
    public boolean hasWordBeenGuessed()
    {
        for(int i = 0; i< word_so_far.length(); i++)
        {
            if(!Character.isLetter(word_so_far.charAt(i)))
                return false;
        }
        return true;
    }
}
