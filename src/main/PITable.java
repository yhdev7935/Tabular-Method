package main;

import java.util.ArrayList;
import java.util.HashMap;

public class PITable {
	
	private int binarySize;
	
	public Minterm[] PI;
	public Minterm[] minterm; // origin minterms
	public int[][] table;
	private HashMap<Integer, Integer> map;
	
	public int[] EPI;
	
	public PITable(Minterm[] PIs, Minterm[] origin_minterms, int size)
	{
		PI = PIs; minterm = origin_minterms;
		table = new int[PI.length][minterm.length];
		EPI = new int[PI.length];
		binarySize = size;
		createTable();
	}
	
	private void createTable()
	{
		// create Table, it can be called by constructors.
		map = new HashMap<Integer, Integer>();
		for(int i = 0; i < minterm.length; i++)
			map.put(minterm[i].getTermsNumber().get(0), i);
		
		for(int i = 0; i < PI.length; i++)
		{
			ArrayList<Integer> terms = PI[i].getTermsNumber();
			Integer[] termsArray = terms.toArray(new Integer[terms.size()]);
			for(int j = 0; j < termsArray.length; j++)
				table[i][map.get(termsArray[j])] = 1;
		}
	}
	
	public void findEPI()
	{
		for(int i = 0; i < minterm.length; i++)
		{
			if(minterm[i].isDontCare()) continue; // don't care이면 무시!
			
			int check = 0; int last_location = -1;
			for(int j = 0; j < PI.length; j++)
				if(table[j][i] == 1)
				{
					check += 1;
					last_location = j;
				}
			if(check == 1)
				EPI[last_location] = 1;
		}
	}
	
	public void show()
	{
		
		for(int i = 0; i < binarySize; i++) System.out.print(" ");
		for(int i = 0; i < minterm.length; i++)
			System.out.printf("|%02d", minterm[i].getTermsNumber().get(0)); // minterm은 명백히 TermNumber가 1개 있다.
		System.out.println("");
		
		// pi show
		for(int i = 0; i < PI.length; i++)
		{
			System.out.printf("%s", PI[i].toString());
			for(int j = 0; j < table[i].length; j++)
				System.out.printf("|%s", table[i][j] == 1 ? " V" : "  ");
			
			if(EPI[i] == 1)
				System.out.print("  <- EPI");
			
			System.out.println("");
		}
	}
}
