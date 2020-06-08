package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main {

	private static Scanner scan = new Scanner(System.in);
	
	
	private static int binarySize;
	public static int mintermSize;
	public static int dcsize;
	public static Minterm[] minterm;
	
	public static void main(String[] args)
	{
		
		System.out.println("Tabular Method");
		
		/* Input Code ////////////////////////////////////////
		 * 
		 */
		
		// Binary Size Input
		System.out.print("Binary Size > ");
		binarySize = scan.nextInt(); // binary Size
		if(binarySize <= 0)
		{
			System.out.println("Binary Size는 양수이어야 합니다!");
			return;
		}
		
		
		// Minterm, Don't Care Input
		System.out.print("Minterm Size > ");
		mintermSize = scan.nextInt();
		if(mintermSize < 0)
		{
			System.out.print("Minterm Size는 0을 포함한 양수이어야 합니다!");
		}
		
		System.out.print("Don't Care Size > ");
		dcsize = scan.nextInt();
		
		minterm = new Minterm[mintermSize + dcsize]; 
		
		for(int i = 0; i < mintermSize; i++)
		{
			System.out.printf("Minterm [%d] > ", i + 1);
			int t = scan.nextInt();
			minterm[i] = new Minterm(t);
		}
		
		for(int i = 0; i < dcsize; i++)
		{
			System.out.printf("Don't Care [%d] > ", i + 1);
			int t = scan.nextInt();
			minterm[mintermSize + i] = new Minterm(t);
			minterm[mintermSize + i].setDontCare(true);
		}
		
		// sorting
		ArrayList<Minterm> mL = new ArrayList<Minterm>(Arrays.asList(minterm));
		Collections.sort(mL);
		minterm = mL.toArray(new Minterm[mL.size()]);
		
		/* Step 1 ////////////////////////////////////////
		 * Find Prime Implicants
		 */
		findPI();
	}
	
	public static void findPI()
	{
		System.out.println("######################################");
		System.out.println("Step 1. Find Prime Implicants");
		System.out.println("######################################\n");
		System.out.println("# of 1s | Minterm | Binary");
		for(int i = 0; i < minterm.length; i++)
			System.out.printf("%d | %s | %s\n", 
					minterm[i].getNumberOfOnes(),
					minterm[i].getTermsNumber(),
					minterm[i].toString());
	}


}
