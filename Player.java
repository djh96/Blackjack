/*Daniel Hodgson
CS 0401
This code will represent playing Blackjack.
It will have features such as playing the game and storing info for each name 
*/
import java.util.Scanner; //Imports the scanner class
import java.io.*;         //Imports the input output wildcardpublic class Blackjack
import java.util.Random;	//Imports the Random class
class Player
{
 	private static Scanner keyboard = new Scanner(System.in);	//Creates scanner object called keyboard
	private static String userName = "";		//Variable to hold the user's name
	private static String userNameFile = "";   	//Variable to hold the user's file
	private static int handsWon = 0;     	  	//Variable to hold the number of hands won
	private static int handsPlayed = 0;			//Variable to hold the number of hands played
	private static double money = 100.0;			//Variable to hold the user's money
	private static boolean test = false;		//Variable to hold a test value
	private static Blackjack blackjack = new Blackjack();
	private static int numberOfBlackjack = 0;
	/**
		The Player method is the constructor and saves the username and the file name	
		@param s to hold the username and str to hold the file
	*/
	public void Player(String s, String str) throws IOException
	{
		//Set the global variables equal to the local variables
		userName = s;
		userNameFile = str;

		//Call player before method to test if they are a player before
		playerBefore();
	}
	/**
		The playerBefore method will test if there has been a player before	
	*/
	public static void playerBefore() throws IOException
	{
		//Initialize the test value to false
		test = false;

		//Set the test value equal to the return value from the test player before method
		test = testPlayerBefore();
		//If test is false, they are a new player so create a file for them and initialize
		if(test == false)
		{
			PrintWriter writer = new PrintWriter(userNameFile);
			writer.printf("%.2f", money);
			writer.println("");
			writer.println(numberOfBlackjack);
			writer.println(handsPlayed);
			writer.println(handsWon);		
			writer.close();			
		}
		//If test is true, they are a returning player
		else if(test == true)
		{
			System.out.println("Welcome back, " + userName + "!");
		}
	}
	/**
		The testPlayerBefore method is an extension of the playerBefore method and returns a boolean value of if the user is new or not	
	*/
	public static boolean testPlayerBefore()
	{
		//Try to see if there is a file for the user, if one is found return true
		try
		{
			File myFile = new File(userNameFile);
			Scanner inputFile = new Scanner(myFile);
			inputFile.close();
			test = true;
			return test;
		}
		//If none is found return false
		catch(Exception e)
		{
			System.out.println("You are a new player! Welcome!");
			return test;
		}		
	}
	/**
		The saveToFile method will save the user's info to a file
		@param money to hold the money, handsPlayed to hold th enumber of hands played, handsWon to hold the number of hands won and blackjackCounter to hold the number of blackjacks		
	*/
	public static void saveToFile(double money, int handsPlayed, int handsWon, int blackjackCounter) throws IOException
	{
		//Write all of the current values to the file
		PrintWriter writer = new PrintWriter(userNameFile);
		writer.printf("%.2f", money);
		writer.println("");
		writer.println(blackjackCounter);
		writer.println(handsPlayed);
		writer.println(handsWon);
		writer.close();	
		//If the user still has money, run the program
		if(money != 0)
		{
			blackjack.runProgram();
		}
		//If the user is out of money, exit the program
		else if(money == 0)
		{
			System.exit(0);	
		}
	}
	/**
		The getUserName method will get the user's name and get the information from the file	
	*/
	public void getUserName() throws IOException
	{
		//Get the user's name and save it and the .txt extension to variables, and send these variables to the constructor
		System.out.print("What is your name? ");
		userName = keyboard.nextLine();
		userNameFile = userName + ".txt";
		Player(userName,userNameFile);

		int counter = 1;	
		File myFile = new File(userNameFile);
		Scanner inputFile = new Scanner(myFile);
		//While there is still another line in the file, get the info at that line and increase the counter to allow the next iteration to get the next line's info
		while(inputFile.hasNext())
		{
			switch(counter)
			{
				case 1:
					money = inputFile.nextDouble();
					break;
				case 2:
					numberOfBlackjack = inputFile.nextInt();
					break;
				case 3:
					handsPlayed = inputFile.nextInt();
					break;
				case 4:
					handsWon = inputFile.nextInt();
					break;
			}
			counter++;
		}

		//Send the values to the blackjack class
		blackjack.displayValue(userName, money, numberOfBlackjack, handsPlayed, handsWon);
		blackjack.hasMoney(money);
	}	
}