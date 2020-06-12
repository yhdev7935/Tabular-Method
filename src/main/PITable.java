package main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;

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
	
	public void setEPI(int loc)
	{
		EPI[loc] = 1;
		Minterm epi_minterm = PI[loc];
		Iterator<Integer> it = epi_minterm.getTermsNumber().iterator();
		while(it.hasNext()) ignore[map.get(it.next())] = 1;
	}
	
	public void refreshAllEPI()
	{
		for(int loc = 0; loc < PI.length; loc++)
		{
			if(EPI[loc] == 1)
			{
				Minterm epi_minterm = PI[loc];
				Iterator<Integer> it = epi_minterm.getTermsNumber().iterator();
				while(it.hasNext()) ignore[map.get(it.next())] = 1;
			}
		}
	}
	
	public boolean findEPI()
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
				setEPI(last_location);
		}
		
		// EPI print
		System.out.print("EPI: ");
		for(int i = 0; i < EPI.length; i++)
			if(EPI[i] == 1)
				System.out.printf("P%d ", i);
		System.out.println();
			
		// 모두 ignore일 때
		for(int i = 0; i < minterm.length; i++)
		{
			if(minterm[i].isDontCare()) continue;
			if(ignore[i] == 0)
				return false;
		}
	
		return true; 
	}
	
	public void show()
	{
		this.show(false);
	}
	
	public void show(boolean dontcare)
	{
		System.out.print("     ");  // PX = X
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
			
			System.out.printf("P%d = %s", i, PI[i].toString());
			for(int j = 0; j < table[i].length; j++)
			{
				if(dontcare && minterm[j].isDontCare()) continue;
				if(ignore[j] == 0)
					System.out.printf("|%s", table[i][j] == 1 ? " V" : "  ");
			}
			
			System.out.println("");
		}
	}
	
	public void columnDominace()
	{
		for(int i = 0; i < minterm.length; i++)
		{
			for(int j = 0; j < minterm.length; j++)
			{
				if(i != j) // 달라야 함.
				{
					if(ignore[i] == 1 || ignore[j] == 1) continue;
					
					if(minterm[i].isDontCare() || minterm[j].isDontCare()) continue;
					
					boolean dominanced = true; // i가 j를 dominance 했는가
					for(int k = 0; k < PI.length; k++)
					{
						if(table[k][i] == 0 && table[k][j] == 1) // 유일하게 dominance가 안되는 경우!
						{
							dominanced = false; break;
						}
					}
					
					if(dominanced)
					{
						//System.out.printf("cD: (%d dominates %d)\n", i, j);
						ignore[i] = 1;
					}
				}
			}
		}
	}
	
	public void rowDominace()
	{
		// greedy solution
		class tempPI implements Comparable<tempPI>
		{
			int idx; int val;
			
			public tempPI(int i, int v) {
				idx = i; val = v;
			}
			@Override
			public int compareTo(tempPI o) {
				
				if(val < o.val) return -1;
				else if(val == o.val) return 0;
				else return 1;
			}
		};
		
		ArrayList<tempPI> tPI = new ArrayList<tempPI>();
		for(int i = 0; i < PI.length; i++)
		{
			int ret = 0;
			for(int j = 0; j < minterm.length; j++)
				if(!minterm[j].isDontCare() && ignore[j] == 0)
					if(table[i][j] == 1)
						ret += 1;
			tPI.add(new tempPI(i, ret));
		}
		Collections.sort(tPI);
		
		
		Iterator<tempPI> tpi_itr = tPI.iterator();
		while(tpi_itr.hasNext())
		{
			int i = tpi_itr.next().idx;
			for(int j = 0; j < PI.length; j++)
			{
				if(i != j)
				{	
					boolean iempty = true;
					boolean jempty = true;
					boolean dominanced = true;
					for(int k = 0; k < minterm.length; k++)
					{
						if(!minterm[k].isDontCare() && ignore[k] == 0)
						{
							if(table[i][k] == 1) iempty = false;
							if(table[j][k] == 1) jempty = false;
							if(table[i][k] == 0 && table[j][k] == 1)
							{
								dominanced = false; break;
							}
						}
					}
					
					if(iempty || jempty) continue;
					
					if(dominanced)
					{
						//System.out.printf("NEPI: P%d는 NEPI: P%d를 Dominance한다.\n", i, j);
						EPI[i] = 1; EPI[j] = 0; // 쉽게 표기하기 위해 EPI라 표기 , setEPI를 쓰지 않는다.
					}
				}
			}
		}
		
		refreshAllEPI(); // ignore에 등록하기 위해서
	}
	
	public boolean printResult()
	{
		// Result Validation
		int[] validation = new int[minterm.length];
		for(int i = 0; i < PI.length; i++)
		{
			if(EPI[i] == 1)
			{
				Minterm term = PI[i];
				Iterator<Integer> it = term.getTermsNumber().iterator();
				while(it.hasNext()) validation[map.get(it.next())] = 1;
			}
		}
		
		for(int i = 0; i < validation.length; i++)
		{
			if(minterm[i].isDontCare()) continue;
			if(validation[i] == 0)
				return false; // 실패 한것 => Petrick's Method로 해야함
		}
		
		// print Result
		System.out.print("F = ");
		ArrayList<Integer> EPI_list = new ArrayList<Integer>();
		for(int i = 0; i < PI.length; i++)
			if(EPI[i] == 1)
				EPI_list.add(i);
		
		Integer[] EPI_array = EPI_list.toArray(new Integer[EPI_list.size()]);
		for(int i = 0; i < EPI_array.length; i++)
		{
			System.out.printf("P%d", EPI_array[i]);
			if(i != EPI_array.length - 1) System.out.print(" + ");
		}
		
		return true;
	}
	
	public void petrick()
	{
		int ret = 0; Deque<LogicExpression> dq = new LinkedList<LogicExpression>();
		for(int i = 0; i < minterm.length; i++)
		{
			if(ignore[i] == 1) continue;
			
			ArrayList<Integer> numbers = new ArrayList<Integer>();
			for(int j = 0; j < PI.length; j++)
			{
				// EPI인 PI를 탐색해야 하는것인가? => 있었으면 ignore[i]는 1이었을 것이므로 탐색할 필요가 없다.
				if(EPI[j] == 1) continue;
				if(table[j][i] == 1)
					numbers.add(j);
			}
			
			dq.addLast(new LogicExpression(numbers));
		}
		
		LogicExpression.show(dq);
		
		while(dq.size() > 1)
		{
			LogicExpression exp1 = dq.getFirst(); dq.pop();
			LogicExpression exp2 = dq.getFirst(); dq.pop();
			dq.addFirst(LogicExpression.multiply(exp1, exp2));
			LogicExpression.show(dq);
		}
		
		// print Result
		
		System.out.print("F = ");
		ArrayList<Integer> EPI_list = new ArrayList<Integer>();
		for(int i = 0; i < PI.length; i++)
			if(EPI[i] == 1)
				EPI_list.add(i);
		
		Integer[] EPI_array = EPI_list.toArray(new Integer[EPI_list.size()]);
		for(int i = 0; i < EPI_array.length; i++)
			System.out.printf("P%d + ", EPI_array[i]);
		
		ArrayList<Integer> petrickresult = dq.getFirst().getShortestMinterm();
		for(Iterator<Integer> it = petrickresult.iterator(); it.hasNext();)
			System.out.printf("P%d", it.next());
		
	}
}
