package main;

import java.util.ArrayList;

public class Minterm implements Comparable<Minterm>{
	
	
	private boolean dontcare = false;
	private ArrayList<Integer> termNumber = new ArrayList<Integer>();
	private String term = "";

	public Minterm(int num)
	{
		term = Integer.toBinaryString(num);
		termNumber.add(num);
	}
	
	public Minterm(Minterm m0, Minterm m1)
	{
		
	}
	
	public boolean isDontCare()
	{
		return this.dontcare;
	}
	
	public void setDontCare(boolean b)
	{
		this.dontcare = b;
	}
	
	public String toString()
	{
		return term;
	}
	
	public int getNumberOfOnes()
	{
		int ret = 0;
		for(int i = 0; i < term.length(); i++)
			if(term.charAt(i) == '1')
				ret++;
		return ret;
	}

	public String getTermsNumber()
	{
		String ret = "";
		for(int i = 0 ; i < termNumber.size(); i++)
		{
			ret += termNumber.get(i).toString();
			if(i != termNumber.size() - 1)
				ret += ", ";
		}
		return ret;
	}
	@Override
	public int compareTo(Minterm m) {
		int myOne = this.getNumberOfOnes();
		int otherOne = m.getNumberOfOnes();
		if(myOne < otherOne) return -1;
		else if(myOne == otherOne) return 0;
		else return 1;
	
	}
}
