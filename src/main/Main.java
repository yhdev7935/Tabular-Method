package main;

import java.util.Scanner;

public class Main {

	private static int binarySize;
	private static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args)
	{
		
		System.out.println("Tabular Method");
		/* Input Code
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
		int mintermSize = scan.nextInt();
		if(mintermSize < 0)
		{
			System.out.print("Minterm Size는 0을 포함한 양수이어야 합니다!");
		}
		
		System.out.print("Don't Care Size > ");
		int dcsize = scan.nextInt();
		
		Minterm[] minterm = new Minterm[mintermSize + dcsize]; 
		
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
	}


}
