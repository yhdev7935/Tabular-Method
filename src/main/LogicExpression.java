package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;

public class LogicExpression {

	ArrayList<ArrayList<Integer>> exp = new ArrayList<ArrayList<Integer>>();
	
	public LogicExpression()
	{
		
	}
	
	public LogicExpression(Integer[] SOP)
	{
		this(new ArrayList<Integer>(Arrays.asList(SOP)));
	}
	
	public LogicExpression(ArrayList<Integer> SOP)
	{
		//System.out.println("SOP Check: " + SOP.toString());
		// ArrayList + ArrayList + ArrayList 이 하나의 expression를 구성한다.
		// 이 Constructor에서는 각 ArrayList의 size가 1이다. 그러한 size가 1인 ArrayList를 SOP에 담은 것이다.
		for(Integer v : SOP)
		{
			ArrayList<Integer> number = new ArrayList<Integer>();
			number.add(v);
			exp.add(number);
		}
	}
	
	public void show()
	{
		ArrayList<Integer> expArray[] = exp.toArray(new ArrayList[exp.size()]);
		System.out.print("(");
		for(int i = 0; i < expArray.length; i++)
		{
			for(Integer v : expArray[i])
				System.out.printf("P%d", v);
			if(i != expArray.length - 1)
				System.out.print(" + ");
		}
		System.out.print(")");
	}
	
	public static void show(Deque<LogicExpression> dq)
	{
		System.out.print("F(with NEPIs) = ");
		for(LogicExpression v : dq) v.show();
		System.out.println();
	}
	
	public ArrayList<Integer> getShortestMinterm()
	{
		int minimumLength = 0x7FFFFFFF; int idx = -1;
		ArrayList<Integer> expArray[] = exp.toArray(new ArrayList[exp.size()]);
		for(int i = 0; i < expArray.length; i++)
		{
			if(minimumLength > expArray[i].size())
			{
				minimumLength = expArray[i].size();
				idx = i;
			}
		}
		
		return expArray[idx];
	}
	
	public ArrayList<ArrayList<Integer>> getExp()
	{
		return exp;
	}
	
	public void add(ArrayList<Integer> v)
	{
		exp.add(v);
	}
	
	public void sort()
	{
		Comparator<ArrayList<Integer>> comp = new Comparator<ArrayList<Integer>>()
		{
			public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
				return Integer.compare(o1.size(), o2.size());
			};
		};
		
		exp.sort(comp); // 길이순으로 정렬
	}
	
	public static LogicExpression multiply(LogicExpression exp1, LogicExpression exp2)
	{
		LogicExpression ret = new LogicExpression();
		
		for(ArrayList<Integer> exp1_v : exp1.getExp())
			for(ArrayList<Integer> exp2_v : exp2.getExp())
				ret.add(multiply(exp1_v, exp2_v));
		ret.sort();
		
		ret.minimize();
		return ret;
	}
	
	private static ArrayList<Integer> multiply(ArrayList<Integer> a, ArrayList<Integer> b)
	{
		HashSet<Integer> set = new HashSet<Integer>();

		for(Integer v : a) set.add(v);
		for(Integer v : b) set.add(v);
		
		return new ArrayList<Integer>(set);
	}
	
	private void minimize()
	{
		/* X + XY = X를 이용
		 * X + X 도 내포되어 있고, X * X 는 multiply() 과정에서 제거
		 */
		
		ArrayList<ArrayList<Integer>> removeSet = new ArrayList<ArrayList<Integer>>();
		
		ArrayList<Integer> expArray[] = exp.toArray(new ArrayList[exp.size()]);
		for(int i = 0; i < expArray.length; i++)
			for(int j = 0; j < expArray.length; j++)
				if(i != j && expArray[j].containsAll(expArray[i]))
					removeSet.add(expArray[j]);

		for(ArrayList<Integer> remove : removeSet)
			exp.remove(remove);
		
	}
}
