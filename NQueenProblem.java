import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class NQueenProblem {
	
	static int populationSize,iteration;
	static ArrayList<int[]> population = new ArrayList<int[]>();
	static int[] individual = new int[8];
	static int[] individualFitness = new int[1000];
	static int[] selected_individual_index;
	static ArrayList<int[]> selectedlist = new ArrayList<int[]>();
	static String numOfPopulation;
	static String numOfGeneration;
	static String crossRate="";
	static String mutationRate = "";
	static int index=0;
	static int init_var = 0;
	static int finish_flag = 0;
	

	public static void main(String[] args) {
		input();
		for(int i = 0; i<iteration;i++)
		{
			if(finish_flag==0)
			{
				initialize();
				System.out.println("Generation:"+i);
				printPopulation();
				selected_individual_index = SUS(individualFitness,populationSize);
				Crossover(selected_individual_index);
			}
			else
			{
				System.out.println("Perfect solution found in "+i+"th generation!!!");
				break;
			}
			
			
		}
		
	}

	// take input from text file
	public static void input() {
		try {
			String file = "input.txt";
			Scanner scn = new Scanner(new File(file));
			
			numOfPopulation = scn.next();
			populationSize = Integer.parseInt(numOfPopulation);
			
			crossRate = scn.next();
			mutationRate = scn.next();
			
			numOfGeneration = scn.next();
			iteration = Integer.parseInt(numOfGeneration);
			scn.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Population Size: "+populationSize);
		System.out.println("Cross Over Rate: "+crossRate);
		System.out.println("Mutation Rate: "+mutationRate);
		System.out.println("Number of Generation for iteration: "+numOfGeneration);
	}

	// create initial generation
	public static void initialize() {
		
		index=0;
		if(init_var==0)
		{
			for(int i = 0; i< populationSize; i++)
			{
				individual = generateIndividual();
				population.add(individual);
				int fitness = fitnessFunction(individual);
				individualFitness[index] = fitness;
				index++;
				
				if(fitness==28)
				{
					finish_flag=1;
				}
			}
			init_var=1;
		}
		else
		{
			Arrays.fill(individualFitness,0);
			populationSize=selectedlist.size();
			population.clear();
			for(int i = 0; i< populationSize; i++)
			{
				individual = selectedlist.get(i);
				population.add(individual);
				int fitness = fitnessFunction(individual);
				individualFitness[index] = fitness;
				index++;
				if(fitness==28)
				{
					finish_flag=1;
				}
			}
			selectedlist.clear();
		}
	}
	
	
	

	public static int[] generateIndividual() {
		Random rand = new Random();
		int[] a = new int[8];
		for (int i = 0; i < 8; i++) {
			a[i] = rand.nextInt(8) + 0;
		}

		return a;
	}

	// return fitness of an individual
	public static int fitnessFunction(int a[]) {
		int count = 0;
		int row = 0, col = 0;
		int fitness = 0;
		String[][] board = new String[8][8];

		for (int i = 0; i < 8; i++) {
			board[a[i]][i] = "q";
			// board[row][col]
		}

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == "q") {
					row = i;
					col = j;
					count += fitnessCal(board, row, col);

				}
			}
		}

		fitness = 28 - count;

		return fitness;
	}

	// count number of attacking queens of each queen
	public static int fitnessCal(String board[][], int row, int col) {
		int i, j, count = 0;

		// to right
		for (i = col + 1; i < 8; i++)
			if (board[row][i] == "q") {
				count++;
				break;

			}
		// to left
		for (i = col - 1; i >= 0; i--)
			if (board[row][i] == "q") {
				count++;
				break;

			}

		/* Check upper diagonal on left side */
		for (i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--)
			if (board[i][j] == "q") {
				count++;
				break;
			}

		/* Check lower diagonal on left side */
		for (i = row + 1, j = col - 1; j >= 0 && i < 8; i++, j--)
			if (board[i][j] == "q") {
				count++;
				break;
			}

		/* Check upper diagonal on right side */
		for (i = row - 1, j = col + 1; i >= 0 && j < 8; i--, j++)
			if (board[i][j] == "q") {
				count++;
				break;
			}

		/* Check lower diagonal on right side */
		for (i = row + 1, j = col + 1; j < 8 && i < 8; i++, j++)
			if (board[i][j] == "q") {
				count++;
				break;
			}

		return count;
	}

	public static int[] SUS(int individualFitness[], int n) {

		double fitnessOfPopulation = 0.0, distanceOfPointers = 0.0, start = 0.0;
		double numOfOffspring = n / 2;
		for (int i = 0; i < individualFitness.length; i++) {
			fitnessOfPopulation = fitnessOfPopulation + individualFitness[i];
		}
		distanceOfPointers = fitnessOfPopulation / numOfOffspring;
		//System.out.println("distance of pointer is : " + distanceOfPointers);
		start = Math.random() * distanceOfPointers;
		//System.out.println("value of start is " + start + "");
		int[] selectedIndividualsIndex = new int[n / 2];
		int index = 0;
		double sum = individualFitness[index];
		for (int i = 0; i < n; i++) {
			// Determine pointer to a segment in the population
			double pointer = start + i * distanceOfPointers;
			//System.out.println("value of pointer is " + pointer);
			// Find segment, which corresponds to the pointer
			if (sum >= pointer) {
				selectedIndividualsIndex[i] = index;
			} else {
				for (++index; index < individualFitness.length; index++) {
					sum += individualFitness[index];
					if (sum >= pointer) {
						selectedIndividualsIndex[i] = index;
						break;
					}
				}
			}
		}

		System.out.println("Index of Selected Individuals:");
		for (int i = 0; i < selectedIndividualsIndex.length; i++) {
			System.out.println("Individual: " + selectedIndividualsIndex[i]);
		}
		return selectedIndividualsIndex;
	}

	// print generation's individuals and their fitness
	public static void printPopulation() {
		for (int i = 0; i < populationSize; i++) {
			System.out.print("Individual " + (i) + ": " + Arrays.toString(population.get(i)) + " ");

			System.out.println("Fitness: " + individualFitness[i]);
		}
	   
	}

	public static ArrayList<int[]> Crossover(int selected_individual_index[]) {
		for (int i = 0; i < selected_individual_index.length; i++) {
			int temp = selected_individual_index[i];
			selectedlist.add(population.get(temp));
		}
		int[] tempcross = new int[8];
		int len = selected_individual_index.length;
 
		for (int j = 0; j < len; j+=2) {
			tempcross = do_cross(selectedlist.get(j), selectedlist.get(j + 1));
			selectedlist.add(tempcross);
			tempcross = do_cross(selectedlist.get(j+1), selectedlist.get(j));
			selectedlist.add(tempcross);
		}
		for(int po=0;po<selectedlist.size();po++)
		{
			System.out.println(Arrays.toString(selectedlist.get(po)) + " ");
		}
		return selectedlist;
	}

	/*this function is returning a child from each pair*/
	public static int[] do_cross(int arr[], int arr1[]) {
		int[] child = new int[8];
		int[] duplicate = new int[30];
		for (int p = 0; p < duplicate.length; p++) {
			duplicate[p] = 0;
		}
			for (int i = 2; i <=5; i++) {
				child[i] = arr[i];
				duplicate[arr[i]] = 1;
			}
			int flag_1 = 0, flag_2 = 0, flag_3 = 0, flag_4 = 0;
			for (int i = 7; i >= 0; i--) {
				if (duplicate[arr1[i]] == 0) {
					if (flag_1 == 0 && flag_2 == 0 && flag_3 == 0 && flag_4 == 0) {
						child[0] = arr1[i];
						duplicate[arr1[i]] = 1;
						flag_1 = 1;
					} else if (flag_1 == 1 && flag_2 == 0 && flag_3 == 0 && flag_4 == 0) {
						child[1] = arr1[i];
						duplicate[arr1[i]] = 1;
						flag_2 = 1;
					} else if (flag_1 == 1 && flag_2 == 1 && flag_3 == 0 && flag_4 == 0) {
						child[6] = arr1[i];
						duplicate[arr1[i]] = 1;
						flag_3 = 1;
					} else if (flag_1 == 1 && flag_2 == 1 && flag_3 == 1 && flag_4 == 0) {
						child[7] = arr1[i];
						duplicate[arr1[i]] = 1;
						flag_4 = 1;					
					} else if (flag_1 == 1 && flag_2 == 1 && flag_3 == 1 && flag_4 == 1) {
						break;
					}
					else
					{
						Random rand = new Random();
						if(flag_1 == 1 && flag_2 == 1 && flag_3 == 0 && flag_4 == 0)
						{
							child[6] = rand.nextInt(8);
						}
						else if(flag_1 == 1 && flag_2 == 1 && flag_3 == 1 && flag_4 == 0)
						{
							child[7] = rand.nextInt(8);
						}
					}
				}
			}
		return child;
	}
}
