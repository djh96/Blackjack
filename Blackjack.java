/*Daniel Hodgson
CS 0401
This code will represent playing Blackjack.
It will have features such as playing the game and storing info for each name.
*/
import java.util.Scanner; //Imports the scanner class
import java.io.*;         //Imports the input output wildcardpublic class Blackjack
import java.util.Random;	//Imports the Random class

public class Blackjack
{
  	private static Scanner keyboard = new Scanner(System.in);	//Creates scanner object called keyboard
	private static String userName = "";		//Variable to hold the user's name
	private static String userNameFile = "";   	//Variable to hold the user's file
	private static int handsWon = 0;     	  	//Variable to hold the number of hands won
	private static int handsPlayed = 0;			//Variable to hold the number of hands played
	private static double money = 100.0;			//Variable to hold the user's money
	private static boolean test = false;		//Variable to hold a test value
	private static double bet = 0.0;			//Variable to hold how much the bet is
	private static Player player = new Player();	//Creates reference to player object
	private static Card card = new Card();			//Creates reference to card object
	private static Random random = new Random();	//Creates reference to generate random number
	private static int keepPlayingCounter = 0;			//Creates variable to hold the counter in the keep playing method
	private static int blackjack = 0;					//Creates variable to hold number of blackjacks

   /**
   		The main method.
   */
	public static void main(String[] args) throws IOException
	{
		//Calls the methods that only need to be called once before entering the runProgram method
		//These methods create the cards and get the players name

		card.Card();
		player.getUserName();	
		runProgram();
	}
	/**
   		The runProgram method will allow the code to run only a certain part of the program over again
   	*/
	public static void runProgram() throws IOException
	{
		//Calls the keepPlaying method, the userHand method in the card object, and the displayValue method

		keepPlaying();
		card.userHand();
		displayValue(userName, money, blackjack, handsPlayed, handsWon);
	}
	/**
		The keepPlaying method is called to see if the user wants to play a hand.
	*/
	public static void keepPlaying() throws IOException
	{
		//If the user has not indicated that they don't want to play anymore
		if(keepPlayingCounter == 0)
		{
			System.out.print("Would you like to keep playing? (Y/N) ");
			String input = keyboard.next();

			//If the user wants to keep playing by entering a y, then get the user's bet
			if(input.equalsIgnoreCase("y"))
			{
				card.reset();
				System.out.println("Great, let's get started!");
				getBet();
			}
			//Else if the user does not want to keep playing by entering a n, then up the keepPlayuingCounter and call the saveToFile method in the player object
			else if(input.equalsIgnoreCase("n"))
			{
				keepPlayingCounter++;
				System.out.println("Thanks for playing!  Your info is being saved.");
				player.saveToFile(money, handsPlayed, handsWon, blackjack);
			}
			//Else if the user enters any invalid input it will ask the user for a valid input and start the method over
			else
			{
				System.out.println("Invalid input.  Please enter 'Y' or 'N'");
				keepPlaying();
			}
		}
		//Else if the user has indicated that they don't want to play anymore, exit the program
		else if(keepPlayingCounter > 0)
		{
			System.exit(0);		
		}
	}
	/**
		The getBet method will ask the user how much they want to bet and repeat until a valid input is typed.
	*/
	public static void getBet() 
	{
		System.out.print("How much would you like to bet? ");
		//Try getting a double input and if it doesn't work then tell the user it is invalid and restart the method
		try
		{
			bet = Double.parseDouble(keyboard.next());
		}
		catch(Exception e)
		{
			System.out.println("That is not a valid amount to bet.  Please bet between .01 and " + money);
			getBet();
		}
		//If the bet is not between the amount of money they have and 1 cent
		if(bet < 0.01 || bet > money)
		{
			System.out.println("That is not a valid amount to bet.  Please bet between .01 and " + money);
			getBet();	
		}
	}
	/**
		The whoWon method will determine if the player won or lost.
		@param userScore for the user's score, dealerScore for the dealer's score, and blackjackCounter to determine if the user had blackjack.
	*/
	public static void whoWon(int userScore, int dealerScore, int blackjackCounter) throws IOException
	{
		System.out.println("Player final score: " + userScore);
		//If the user didn't bust, display the dealer score
		if(!(userScore > 21))			
		{
			System.out.println("Dealer final score: " + dealerScore);
		}
		//If the user busted, subtract that amount of money
		if(userScore > 21)
		{
			System.out.println("Sorry, you busted");
			money -= bet;
			System.out.printf("You lost $%.2f!\n", bet);
		}
		//If the user had blackjack but the dealer also had 21
		else if(blackjackCounter == 1 && dealerScore == 21)
		{
			System.out.println("You and the dealer pushed, you lose no money.");
		}
		//If the user had blackjack, the user wins their money back and 1.5 times their bet, and the blackjack and handsWon counters will increase
		else if(blackjackCounter == 1)
		{
			bet += 1.5*bet;
			money += bet;
			System.out.printf("You had Blackjack! You won $%.2f!\n", bet);
			handsWon++;
			blackjack++;
		}
		//If the dealer busted, the user wins the amount of money they bet and the handsWon counter increases
		else if(dealerScore > 21)
		{
			System.out.println("The house busted, you win");
			money += bet;
			System.out.printf("You won $%.2f!\n", bet);
			handsWon++;
		}
		//If the user had a higher score than the dealer, they win their money back and the handsWon counter increases
		else if(userScore >  dealerScore) 
		{
			money += bet;
			System.out.printf("You won $%.2f!\n", bet);
			handsWon++;			
		}
		//If the user had a worse score than the dealer they lose their bet
		else if(userScore < dealerScore)
		{
			money -= bet;
			System.out.printf("You lost $%.2f!\n", bet);			
		}
		//If the user and the dealer had the same score
		else if(userScore == dealerScore)
		{
			System.out.println("You and the dealer pushed, you lose no money.");
		}

		//The things that need to happen regardless of who wins
		//The hands played increases, see if the user has any money left, display the user's statistics, and save their statistics to a file

		handsPlayed++;
		hasMoney(money);
		displayValue(userName, money, blackjack, handsPlayed, handsWon);
		player.saveToFile(money, handsPlayed, handsWon, blackjack);
	}
	/**
		The display value will display the values of the user's information
		@param userName2 to hold the username, money2 to hold the money, blackjack2 to hold the number of blackjacks, handsPlayed2 to hold the hands played, and handsWon2 to hold the hands won
	*/
	public static void displayValue(String userName2, double money2, int blackjack2, int handsPlayed2, int handsWon2) throws IOException
	{
		//Setting the global variables equal to the arguments from where ever the display value method is being called
		handsPlayed = handsPlayed2;
		userName = userName2;
		money = money2;
		handsWon = handsWon2;
		blackjack = blackjack2;

		//Right justify all of the code
		System.out.println("-------------------------");
		System.out.printf("Name:%20s\n", userName);
		System.out.printf("Total Hands:%13s\n",handsPlayed);
		System.out.printf("Hands Won:%15s\n",handsWon);
		System.out.printf("Money:%19s\n",money);
		System.out.printf("Blackjacks:%14s\n", blackjack);
		System.out.println("-------------------------");
	}
	/**
		The hasMoney method will see if the user has any more money to bet
		@param money2 to hold the money
	*/
	public static void hasMoney(double money2) throws IOException
	{
		//Setting the global variable equal to the local variable
		money = money2;
		//If the user has no money left, save their info to a file
		if(money < .01)
		{
			System.out.println("Sorry, you have no more money to bet!");
			System.out.println("Thanks for playing!");
			player.saveToFile(money, handsPlayed, handsWon, blackjack);
		}
	}
}
