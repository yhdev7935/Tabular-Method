package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

public class Main {

	private static Scanner scan = new Scanner(System.in);
	
	
	private static int binarySize;
	public static int mintermSize;
	public static int dcsize;
	public static Minterm[] minterm;
	public static Minterm[] origin_minterm;
	public static ArrayList<Integer> dontCareNumbers = new ArrayList<Integer>();
	public static Minterm[] notCombinedMinterm;
	public static int[] Combined;
	
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
		Combined = new int[mintermSize + dcsize];
		
		for(int i = 0; i < mintermSize; i++)
		{
			System.out.printf("Minterm [%d] > ", i + 1);
			int t = scan.nextInt();
			minterm[i] = new Minterm(t, binarySize);
			dontCareNumbers.add(i);
		}
		
		for(int i = 0; i < dcsize; i++)
		{
			System.out.printf("Don't Care [%d] > ", i + 1);
			int t = scan.nextInt();
			minterm[mintermSize + i] = new Minterm(t, binarySize);
			minterm[mintermSize + i].setDontCare(true);
		}
		
		// origin minterms
		origin_minterm = new Minterm[minterm.length];
		for(int i = 0; i < minterm.length; i++)
			origin_minterm[i] = minterm[i];
		
		// sorting
		Collections.sort(dontCareNumbers);
		minterm = Minterm.sort(minterm);
		
		/* Step 1 ////////////////////////////////////////
		 * Find Prime Implicants
		 */
		System.out.println("######################################");
		System.out.println("Step 1. Find Prime Implicants");
		System.out.println("######################################\n");
		System.out.println("\n##############Initialize##############\n");
		findImplicants();
		System.out.println("\n###############Combine################\n");
		boolean flag = true;
		while(flag)
			flag = combineImplicants(flag);
		
		/* Step 2 ////////////////////////////////////////
		 * Find Essential Prime Implicants
		 */
		System.out.println("######################################");
		System.out.println("Step 2. Find Essential Prime Implicants");
		System.out.println("######################################\n");
		System.out.println("\n###############PI Table###############\n");
		PITable pit = new PITable(notCombinedMinterm, origin_minterm, binarySize);
		pit.show();
		System.out.println("\n##############REMOVE EPI##############\n");
		pit.findEPI();
		pit.show(true);
	}
	
	public static void findImplicants()
	{
		System.out.println("######################################");
		System.out.println("# of 1s | Minterm | Binary | Combined");
		for(int i = 0; i < minterm.length; i++)
			System.out.printf("%d | %s | %s | %s\n", 
					minterm[i].getNumberOfOnes(),
					minterm[i].getTermsNumberString(),
					minterm[i].toString(),
					Combined[i] == 1 ? "V" : "");
		System.out.println("######################################");
	}
	
	public static boolean combineImplicants(boolean flag)
	{
		if(minterm.length == 0) return false;
		
		boolean escape = false;
		Combined = new int[minterm.length];
		int max_ones = minterm[minterm.length - 1].getNumberOfOnes();
		
		ArrayList<Minterm> CombinedMinterm = new ArrayList<Minterm>(); HashSet<String> duplicate = new HashSet<String>();
		for(int i = 0; i < max_ones; i++)
		{
			Minterm[] minterms_a = Minterm.getMinterms(minterm, i);
			Minterm[] minterms_b = Minterm.getMinterms(minterm, i + 1);
			for(int ai = 0; ai < minterms_a.length; ai++)
			{
				for(int bi = 0; bi < minterms_b.length; bi++)
				{
					Minterm minterm_ai = minterms_a[ai];
					Minterm minterm_bi = minterms_b[bi];
					//System.out.printf("합치기중... %s[idx: %d] + %s[idx: %d] , HD: %d\n", minterm_ai.toString(),
					//		Minterm.getIndexOfMintermGroups(minterm, i, ai), minterm_bi.toString(),
					//		Minterm.getIndexOfMintermGroups(minterm, i + 1, bi),
					//		Minterm.getHammingDistance(minterm_ai, minterm_bi));
					if(Minterm.getHammingDistance(minterm_ai, minterm_bi) == 1)
					{
						escape = true;
						Minterm newMinterm = new Minterm(minterm_ai, minterm_bi);
						Combined[Minterm.getIndexOfMintermGroups(minterm, i, ai)] = 1;
						Combined[Minterm.getIndexOfMintermGroups(minterm, i + 1, bi)] = 1;
						
						if(!duplicate.contains(newMinterm.toString()))
						{
							duplicate.add(newMinterm.toString());
							CombinedMinterm.add(newMinterm);
						}
					}
				}
			}
		}
		
		duplicate = new HashSet<String>();
		for(int i = 0; i < minterm.length; i++)
			if(Combined[i] == 0 && !duplicate.contains(minterm[i].toString()))
			{
				duplicate.add(minterm[i].toString());
				
				ArrayList<Minterm> mL;
				if(notCombinedMinterm != null && notCombinedMinterm.length != 0) mL = new ArrayList<Minterm>(Arrays.asList(notCombinedMinterm));
				else mL = new ArrayList<Minterm>();
				mL.add(minterm[i]);
				notCombinedMinterm = mL.toArray(new Minterm[mL.size()]);
			}
		
		Minterm.sort(notCombinedMinterm);
		
		if(escape == false) return false;
		
		System.out.println("\n################Before################\n");
		findImplicants(); // before
		
		minterm = CombinedMinterm.toArray(new Minterm[CombinedMinterm.size()]);
		Combined = new int[minterm.length];
		
		System.out.println("\n################After#################\n");
		findImplicants(); // after
		System.out.print("\n\n\n");
		
		return escape;
		
	}
	
	public static int countCombinedImplicants()
	{
		int ret = 0;
		for(int i = 0; i < Combined.length; i++)
			if(Combined[i] == 1)
				ret += 1;
		return ret;
	}


}
