package com.example;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        String file_name = args[0];
        int word_length = Integer.parseInt(args[1]);
        int number_of_guesses = Integer.parseInt(args[2]);


        EvilHangmanGame my_game = new EvilHangmanGame();
        File my_file = new File(file_name);
        my_game.startGame(my_file, word_length);

        System.out.println("Welcome to Evil Hangman");



        while(number_of_guesses > 0)
        {
            System.out.println("Word so far: " + my_game.getWordSoFar());
            System.out.println("Letters guessed: " + my_game.getLettersGuessed().toString());
            System.out.println("Guesses left: " + number_of_guesses);
            System.out.print("Guess a letter: ");
            Scanner in = new Scanner(System.in);
            String user_input = in.nextLine();
            if(user_input.length() > 1)
                System.out.println("Invalid input try again.");
            else if(user_input.length() == 0)
                System.out.println("Invalid input try again.");
            else if(!Character.isLetter(user_input.charAt(0)))
                System.out.println("Invalid input try again.");
            else
            {
                try {
                    my_game.makeGuess(user_input.toLowerCase().charAt(0));
                    number_of_guesses--;
                } catch (IEvilHangmanGame.GuessAlreadyMadeException e) {
                    System.out.println("That letter has already been guessed. Guess again.");
                }
            }
            if(my_game.hasWordBeenGuessed())
                break;
        }
        if(number_of_guesses > 0)
        {
            System.out.println("Congratulations, you won!");
            System.out.println("Your word was: " + my_game.getWordSoFar());
        }
        else
            System.out.println("Sorry, you ran out of guesses!");
    }

}
