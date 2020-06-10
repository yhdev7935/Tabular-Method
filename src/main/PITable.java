package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PITable {
	
	private int binarySize;
	
	public Minterm[] PI;
	public Minterm[] minterm; // origin minterms
	public int[][] table;
	private HashMap<Integer, Integer> map;
	
	public int[] ignore;
	public int[] EPI;
	
	public PITable(Minterm[] PIs, Minterm[] origin_minterms, int size)
	{
		PI = PIs; minterm = origin_minterms;
		table = new int[PI.length][minterm.length];
		EPI = new int[PI.length];
		ignore = new int[minterm.length];
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
			Iterator<Integer> it = PI[i].getTermsNumber().iterator();
			while(it.hasNext()) table[i][map.get(it.next())] = 1;
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
			{
				EPI[last_location] = 1;
				Minterm epi_minterm = PI[last_location];
				Iterator<Integer> it = epi_minterm.getTermsNumber().iterator();
				while(it.hasNext()) ignore[map.get(it.next())] = 1;
			}
		}
	}
	
	public void show()
	{
		this.show(false);
	}
	
	public void show(boolean dontcare)
	{
		
		for(int i = 0; i < binarySize; i++) System.out.print(" ");
		for(int i = 0; i < minterm.length; i++)
		{
			if(dontcare && minterm[i].isDontCare()) continue;
			if(ignore[i] == 0) // epi이면 무시
					System.out.printf("|%02d", minterm[i].getTermsNumber().get(0)); // minterm은 명백히 TermNumber가 1개 있다.
		}
		System.out.println("");
		
		// pi show
		for(int i = 0; i < PI.length; i++)
		{
			if(EPI[i] == 1) continue;
			
			System.out.printf("%s", PI[i].toString());
			for(int j = 0; j < table[i].length; j++)
			{
				if(dontcare && minterm[j].isDontCare()) continue;
				if(ignore[j] == 0)
					System.out.printf("|%s", table[i][j] == 1 ? " V" : "  ");
			}
			
			System.out.println("");
		}
	}
}
