import java.util.Random;
import java.util.Arrays;

public class Assignment2{
	public static int[] rollDice(){
		int [] diceRoll = new int[6];
		// Holds all the potential dice rolls
		int count = 0;
		// Keeps track of how many times we've rolled
		int index = 0;
		// Index for the diceRoll[]
		Dice one = new Dice();
		// Creates a new instance of Dice called one
		Dice two = new Dice();
		// Creates a new instance of Dice called two

		one.roll();
		// Get a random number from our first die
		two.roll();
		// Get a random number from our second die
		count++;
		// Iterate count

		diceRoll[index] = one.getValue();
		// Get the value from our roll
		index++;
		diceRoll[index] = two.getValue();
		// Get the value from our roll
		index++;

		while(one.getValue() == two.getValue() && count < 3){
			// This handles the case where we roll snake eyes. We can roll snake eyes a total
			// of three times max. We know this becaue if we get three snake eyes we go to jail.
			// We roll until we've rolled three times and we fill up the array with our rolls.
			one.roll();
			two.roll();

			diceRoll[index] = one.getValue();
			index++;
			diceRoll[index] = two.getValue();
			index++;

			count++;
		}
		// System.out.println(Arrays.toString(diceRoll));

		return diceRoll;
	}
	
	public static int[][] playGame(){
		GameBoard monopoly = new GameBoard();

		while(monopoly.getMoveCounter() < 100){
			int [] diceArray = rollDice();
			//System.out.println("1");
			int index = 0;
			int oneRoll = diceArray[index];
			index++;
			int twoRoll = diceArray[index];
			index++;
			int diceRoll = oneRoll + twoRoll;

			monopoly.move(diceRoll);
			//System.out.println("2");

			if(diceArray[index] != 0){
				System.out.println("Snake eyes. Roll again");
				oneRoll = diceArray[index];
				index++;
				twoRoll = diceArray[index];
				index++;
				diceRoll = oneRoll + twoRoll;

				monopoly.move(diceRoll);
				// Moves but counts an extra turn
				monopoly.removeMove();
				// Takes away the turn for rolling snake eyes. A turn is 
				// anytime in a 2+ player game we would switch players.
				//System.out.println("2");
			}

			if(diceArray[4] != 0 && diceArray[4] == diceArray[5]){
				System.out.println("Caught for speeding (Rolled three snake eyes). Go to jail");
				monopoly.jail();
			}
		}

		return monopoly.getBoardCounter();
	}

	public static void main(String [] args){
		//System.out.println(Arrays.deepToString(playGame()));
		//playGame();
		int [][] thousandGames = new int[11][11];

		for(int i = 0; i< 1000; i++){
			int [][] singleGame = playGame();
			for(int j = 0; j< 11; j++){
				for(int k = 0; k< 11; k++){
					thousandGames[j][k]+= singleGame[j][k];
				}
			}
		}

		double [][] averageGame = new double[11][11];
		double numGames = 1000.0;
		double [][] probLand = new double[11][11];

		for(int l = 0; l< 11; l++){
			for(int m = 0; m< 11; m++){
				averageGame[m][l] = thousandGames[m][l]/numGames;
				probLand[m][l] = averageGame[m][l]/100; 
			}
		}

		//System.out.println(Arrays.deepToString(probLand));
		System.out.println("Go: " + probLand[10][10]);
		System.out.println("B & O Railroad: " + probLand[0][5]);
		System.out.println("Short Line: " + probLand[5][10]);
		System.out.println("Reading Railroad: " + probLand[10][5]);
		System.out.println("Pennsylvania Railroad: " + probLand[5][0]);
		System.out.println("Mediterenean Avenue: " + probLand[10][9]);
		System.out.println("Boardwalk: " + probLand[9][10]);

	}
}

class Dice{
	private int value;
	// Holds the value of Dice

	public Dice(){
		value = 0;
	}

	public Dice(int value){
		value = this.value;
	}

	public void setValue(int newVal){
		value = newVal;
	}

	public int getValue(){
		return value;
	}

	public void roll(){
		// roll returns a pseudo-random number between 1 - 6
		Random rand = new Random();
		value = rand.nextInt(6) + 1;
	}
}

class GameBoard{
	/*
		Go = [10][10]
		Reading Railroad = [10][5]
		Pennsylvania Railroad = [5][0]
		B & O Railroad = [0][5]
		Short Line = [5][10]
		Mediterenean Avenue = [10][9]
		Boardwalk = [9][10]
		Chance = {[10][3], [0][2], [10][6]}
		Community Chest = {[10][8], [3][0], [10][3]}
	*/

	private String[][] monopoly;
	// Have this in there but it's not used.
	private int[][] boardCounter;
	// Keeps track of the number of times we land on each spot
	private int moveCounter;
	// Keeps track of the number of turns we've used
	private int [] current;
	// Keeps track of our current position [0][1]
	private int mode;
	// Keeps track of how we travel on the board
	private Chance gameChance;
	// Creates a new instance of chance
	private boolean jailCard;
	// Keeps track of our get out of jail free cards
	private CommunityChest gameCommChest;
	// Creates a new instance of Community Chest
	// current[0] = row
	// current[1] = col

	public GameBoard(){
		monopoly = new String[11][11];
		boardCounter = new int[11][11];
		current = new int[2];
		current[0] = 10;
		current[1] = 10;
		moveCounter = 0;
		mode = 0;
		gameChance = new Chance();
		gameCommChest = new CommunityChest();
		jailCard = false;
	}

	public boolean checkChance(){
		// Check if our current square is a chance square
		boolean landChance = false;

		if(current[0] == 10 && current[1] == 3){
			landChance = true;
		}else if(current[0] == 3 && current[1] == 0){
			landChance = true;
		}else if(current[0] == 10 && current[1] == 3){
			landChance = true;
		}

		return landChance;
	}

	public boolean checkCommChest(){
		boolean isCommChest = false;

		if(current[0] == 10 && current[1] == 8){
			isCommChest = true;
		}else if(current[0] == 3 && current[1] == 0){
			isCommChest = true;
		}else if(current[0] == 10 && current[1] == 3){
			isCommChest = true;
		}

		return isCommChest;
	}

	public void move(int diceRoll){
		//System.out.println(diceRoll);
		
		while(diceRoll > 0){
			checkMode();
			// System.out.println(mode);

			if(mode == 0){
				current[1]--;
			}else if(mode == 1){
				current[0]--;
			}else if(mode == 2){
				current[1]++;
			}else if(mode == 3){
				current[0]++;
			}

			//System.out.println(current[0] + " " + current[1]);
			diceRoll--;
		}
		//System.out.println("Move done.");

		if(current[0] >= 0 && current[0] <= 10){
			if(current[1] >= 0 && current[1] <= 10){
				boardCounter[current[0]][current[1]]++;
			}else{
				System.out.println("Error: Current[1] = " + current[1]);
				System.out.println(moveCounter);
			}
		}else{
			System.out.println("Error: Current[0] = " + current[0]);
			System.out.println(moveCounter);
		}
		//System.out.println("Board counter done.");

		if(checkChance()){
			SpecialCard newPull = gameChance.getChance();
			readChance(newPull.getValue());
		}
		//System.out.println("Check chance done.");

		if(checkCommChest()){
			System.out.println("Enter check Comm Chest");
			SpecialCard newPull = gameCommChest.getCommChest();
			System.out.println("Drew comm chest");
			readCommChest(newPull.getValue());
		}
		//System.out.println("Check comm done.");

		if(checkJail()){
			jail();
		}
		//System.out.println("Check jail done.");

		moveCounter++;
		//System.out.println(current[0] + " " + current[1]);
		System.out.println(moveCounter);
		//System.out.println(Arrays.deepToString(boardCounter));
	}

	public void moveBack(int numMoves){
		int dirMode = 0;

		while(numMoves > 0){
			checkMode();

			if(mode == 0){
				current[1]++;
			}else if(mode == 1){
				current[0]++;
			}else if(mode == 2){
				current[1]--;
			}else if(mode == 3){
				current[0]--;
			}
			numMoves--;
		}
		boardCounter[current[0]][current[1]]++;
	}

	public void checkMode(){
			if(current[0] == 10){
				if(current[1] > 0 && current[1] <= 10){
					// Move left
					mode = 0;
				}
			}
			if(current[1] == 0){
				if(current[0] > 0 && current[0] <= 10){
					// Move up
					mode = 1;
				}
			}
			if(current[0] == 0){
				if(current[1] >= 0 && current[1] < 10){
					// Move right
					mode = 2;
				}
			}
			if(current[1] == 10){
				if(current[0] >= 0 && current[0] < 10){
					// Move down
					mode = 3;
				}
			}
	}

	public void jail(){
		// Simulate landing in jail by skipping 3 turns.
		current[0] = 0;
		current[1] = 1;

		if(!jailCard){
			for(int i = 0; i< 3; i++){
				moveCounter++;
			}
		}else{
			jailCard = false;
			System.out.println("You get out of jail with a jail free card");
		}	
	}

	public boolean checkJail(){
		// Check if player in go to jail square
		if(current[0] == 0 && current[1] == 10){
			return true;
		}
		return false;
	}

	public void readChance(int value){
		if(value == 0){
			System.out.println("Chance: Get out of jail free");
			jailCard = true;
		}else if(value == 1){
			current[0] = 0;
			current[1] = 0;
			jail();
			System.out.println("Chance: Go to jail!");
			boardCounter[current[0]][current[1]]++;
		}else if(value == 2){
			current[0] = 10;
			current[1] = 10;
			System.out.println("Chance: Proceed directly to Go");
			boardCounter[current[0]][current[1]]++;
		}else if(value == 3){
			current[0] = 0;
			current[1] = 4;
			System.out.println("Chance: Proceed to Illinois Avenue");
			boardCounter[current[0]][current[1]]++;
		}else if(value == 4){
			current[0] = 9;
			current[1] = 0;
			System.out.println("Chance: Proceed to St. Charles Place");
			boardCounter[current[0]][current[1]]++;
		}else if(value == 5){
			current[0] = 9;
			current[1] = 10;
			System.out.println("Chance: Proceed to Boardwalk");
			boardCounter[current[0]][current[1]]++;
		}else if(value == 6){
			// Reading Railroad
			current[0] = 10;
			current[1] = 5;
			boardCounter[current[0]][current[1]]++;
		}else if(value == 7){
			if(current[0] == 0){
				// B. & O. Railroad
				current[0] = 0;
				current[1] = 5;
			}else if(current[1] == 10){
				// Short Line
				current[0] = 5;
				current[1] = 10;
			}else if(current[0] == 10){
				// Reading Railroad
				current[0] = 10;
				current[1] = 5;
			}else if(current[1] == 0){
				// Pennsylvania Railroad
				current[0] = 5;
				current[1] = 0;
			}
			boardCounter[current[0]][current[1]]++;
			System.out.println("Chance: Proceed to the nearest railroad");
		}else if(value == 8){
			// Move back three spaces
			moveBack(3);
			// Count of landing in square done in moveBack method
		}else if(value == 9){
			// Nearest Utility
			nearestUtility();
			// Count of landing in square done in nearestUtility method
		}else if(value == 10){
			// Pay money
			System.out.println("You owe $200 for failing your exams");
		}else if(value == 11){
			// Collect money
			System.out.println("You won your NBA playoff bet. Collect $100");
		}
	}

	public void readCommChest(int value){
		if(value == 0){
			// Get out of jail card
			System.out.println("Community Chest: Get out of jail free");
			jailCard = true;
		}else if(value == 1){
			// Go to jail
			System.out.println("Community Chest: Go directly to jail");
			current[0] = 0;
			current[1] = 0;
			jail();
			boardCounter[current[0]][current[1]]++;
		}else if(value == 2){
			// Proceed to go
			System.out.println("Community Chest: Proceed to go");
			current[0] = 10;
			current[1] = 10;
			boardCounter[current[0]][current[1]]++;
		}else if(value == 3){
			// Pay money
			System.out.println("Community Chest: You owe back taxes. Pay $200");
		}else if(value == 4){
			// Collect money
			System.out.println("Community Chest: You win the lottery. Collect $100");
		}
	}

	public void nearestUtility(){
		if(current[0] == 0){
			current[0] = 0;
			current[1] = 8;
			System.out.println("Chance: Proceed to nearest utility (Water Works)");
		}else if(current[0] == 10){
			current[0] = 8;
			current[1] = 0;
			System.out.println("Chance: Proceed to nearest utility (Electric Company)");
		}else if(current[1] == 10){
			current[0] = 0;
			current[1] = 8;
			System.out.println("Chance: Proceed to nearest utility (Water Works)");
		}else if(current[1] == 0){
			current[0] = 8;
			current[1] = 0;
			System.out.println("Chance: Proceed to nearest utility (Electric Company)");
		}
		boardCounter[current[0]][current[1]]++;
	}

	public int getMoveCounter(){
		return moveCounter;
	}

	public void removeMove(){
		moveCounter--;
	}

	public int[][] getBoardCounter(){
		return boardCounter;
	}
}

class CommunityChest{
	private SpecialCard[] commChest;
	private boolean[] visited;

	public CommunityChest(){
		commChest = new SpecialCard[16];
		visited = new boolean[16];

		initializeCommChest();
	}

	public void initializeCommChest(){
		/*
			0 = Get out of jail free (1/16) 1
			1 = Jail (1/16) 2
			2 = Proceed to Go (1/16) 3
			3 = Pay money (4/16) 7
			4 = Collect money (9/16) 16
		*/

			int value = 0;

			for(int i = 0; i< 3; i++){
				commChest[i] = new SpecialCard(value);
				value++;
			}

			value = 3;
			for(int j = 3; j< 7; j++){
				commChest[j] = new SpecialCard(value);
			}

			value = 4;
			for(int k = 7; k < 16; k++){
				commChest[k] = new SpecialCard(value);
			}
	}

	public SpecialCard getCommChest(){
		Random rand = new Random();

		int randComm = rand.nextInt(16);

		while(visited[randComm]){
			randComm = rand.nextInt(16);
		}

		SpecialCard newPull = commChest[randComm];
		visited[randComm] = true;

		return newPull;
	}

	public boolean checkVisited(int index){
		return visited[index];
	}

	public void setVisited(int index){
		visited[index] = true;
	}
}

class Chance{
	private SpecialCard[] chance;
	private boolean[] visited;

	public Chance(){
		chance = new SpecialCard[16];
		visited = new boolean[16];

		initializeChance();

	}

	public void initializeChance(){
		// Fills the chance array with SpecialCard values of the
		// corresponding table below.

		/*
			0 = Get out of jail (1/16) 1
			1 = Jail (1/16) 2
			2 = Go (1/16) 3 
			3 = Illinois Avenue (1/16) 4
			4 = St. Charles Place (1/16) 5
			5 = Boardwalk (1/16) 6
			6 = Reading Railroad (1/16) 7
			7 = Nearest railroad (1/16) 8
			8 = Three spaces back (1/16) 9
			9 = Nearest Utility (1/16) 10
			10 = Pay money (3/16)  13
			11 = Collect money (3/16) 16
		*/
		int value = 0;

		for(int i = 0; i< 10; i++){
			SpecialCard a = new SpecialCard(value);
			chance[i] = a;
			value++;
		}
		value++;

		for(int j = 10; j< 13; j++){
			SpecialCard a = new SpecialCard(value);
			chance[j] = a;
		}
		value++;

		for(int k = 13; k< 16; k++){
			SpecialCard a = new SpecialCard(value);
			chance[k] = a;
		}
	}

	public SpecialCard getChance(){
		// Returns a random chance card from our 16 card array.
		// Marks the visited array if we are able to return that card.
		Random rand = new Random();

		int randChance = rand.nextInt(16);

		while(visited[randChance]){
			randChance = rand.nextInt(16);
		}

		SpecialCard newPull = chance[randChance];
		visited[randChance] = true;

		return newPull;
	}

	public boolean checkVisited(int index){
		// Check if that card has been used
		return visited[index];
	}

	public void assUsed(){
		boolean check = true;

		for(int i = 0; i< 16; i++){
			if(visited[i] == false){
				check = false;
			}
		}

		if(check == true){
			for(int j = 0; j< 16; j++){
				visited[j] = false;
			}
		}
	}

}

class SpecialCard{
	// Small class which just holds a value
	private int cardValue;

	public SpecialCard(int value){
		cardValue = value;
	}

	public int getValue(){
		return cardValue;
	}

	public void setValue(int value){
		cardValue = value;
	}
}