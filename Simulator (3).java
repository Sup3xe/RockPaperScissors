import java.util.Random; // Allows us of random numbers to represent player choice
import java.util.Scanner; 		// Allows integer input for numebr of players
public class Simulator
{
	 //Gets the numer of players and creates an array of players
	private int[] playersChoice = new int[10];
	

	private int rock = 0;		// Stores the number of rock wins
	private int paper = 0;		// Stores the number of paper wins
	private int scissors = 0;	// Stores the number of scissor wins
	private int draw = 0;		// allocates memory for number of draws
	private int winner = 11;	// allocates memory for number of wins
	private int count = 0;		// the number thread the program is on
	private int numberOfPlayers;
	private int round = 0;
	private int i = 0;

	public static void main(String[]args)
	{

		Simulator game = new Simulator();	//creates new smulation game
		game.Simulation();					//calls the simulation
	}
	
	public void Simulation()				// Simulation function
	{
		do
		{
			System.out.println("Please enter the number of players 2-10: ");
			Scanner in = new Scanner(System.in);
			numberOfPlayers = in.nextInt();
		} while(numberOfPlayers < 2 || numberOfPlayers > 10);

		Thread[] player = new Thread[numberOfPlayers];	//new thread array for all players
		for(i = 0; i < numberOfPlayers; ++i)		// Loop for number of games
		{
			player[i] = new Thread(new Runnable() 		// runnable for thread i
			{
				public synchronized void run()	//runnable
				{
					while(round < 1000)	// to loop throug 1000 games
					{
						Random rand = new Random();
						playersChoice[count] = rand.nextInt(3) + 1; // random number between 1 and 3
						
						//makes threads wait for last thread
						if(count < numberOfPlayers - 1)
						{
							++ count;	//incerments the number of threads 
							synchronized(this)
							{
								try
								{
									this.wait(10);
								}
								catch(InterruptedException e)
								{
									e.printStackTrace();
								}
							}
						}

						//after last thread arrives
						else
						{	

							for(int q = 0; q < numberOfPlayers; ++q)
							{

								//prints current game states
								if(playersChoice[q] == 1)
									System.out.println("Round " + round + " player " + q + " picks: rock.");
								else if(playersChoice[q] == 2)
									System.out.println("Round " + round + " player " + q + " picks: paper.");
								else
									System.out.println("Round " + round + " player " + q + " picks: scissors.");
							}

							++round;
							count = 0;		// sets count back to 0 for next round
							
							// loops to check who is winner of this round
							for(int k = 0; k < numberOfPlayers; ++k)	// loops through every player
							{
								for (int l = 0; l < numberOfPlayers; ++l)	// loops through every palyer to compar
								{
									if(k != l)
									{
										switch(playersChoice[k])
										{
											case 1: if(playersChoice[l] != 3) 
														l = numberOfPlayers;
													else if(l == numberOfPlayers - 1 || (k == numberOfPlayers - 1 && l == numberOfPlayers - 2))
													{
														winner = k;
														k = numberOfPlayers;
														l = numberOfPlayers;
														rock();
													}
											break;
											case 2: if(playersChoice[l] != 1) 
														l = numberOfPlayers;
													else if(l == numberOfPlayers - 1 || (k == numberOfPlayers - 1 && l == numberOfPlayers - 2))
													{
														winner = k;
														k = numberOfPlayers;
														l = numberOfPlayers;
														paper();
													}
											break;
											case 3: if(playersChoice[l] != 2) 
														l = numberOfPlayers;
													else if(l == numberOfPlayers - 1 || (k == numberOfPlayers - 1 && l == numberOfPlayers - 2))
													{
														winner = k;
														k = numberOfPlayers;
														l = numberOfPlayers;
														scissors();
													}
											break;
											default: break;
										}
									}
								}
							}
							if(winner != 11 )
							{
								System.out.println("Round " + (round-1) + " player " + winner + " Wins!");
								winner = 11;
							}
							else
							{
								draw();
								System.out.println("Round " + (round-1) + " was a draw. ");
							}
							
							synchronized(this)
							{							
								notifyAll();
							}
						}
					}
				}
			});
		
			player[i].start();
		}
		//for(int m = 0; m < numberOfPlayers; ++m)		// Loop for number of games


		for(int n = 0; n < numberOfPlayers; ++n)
{
		try
		{
				player[n].join();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
}
		System.out.println("Rock wins: " + rock + " times");
		System.out.println("Paper wins: " + paper + " times");
		System.out.println("Scissors wins: " + scissors + " times"); 
		System.out.println("Draws: " + draw + " times"); 

	}
	public synchronized void rock() 	//Function incrementing rock's wins
	{
		++rock;
	}
	public synchronized void paper() 	//Function incrementing paper's wins
	{
		++paper;
	}
	public synchronized void scissors()	//Function incrementing scissor's wins
	{
		++scissors;
	}
	public synchronized void draw()	//Function incrementing number of draws
	{
		++draw;
	}
}