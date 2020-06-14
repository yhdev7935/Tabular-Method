package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class Minterm implements Comparable<Minterm>{
	
	
	private boolean dontcare = false;
	private ArrayList<Integer> termNumber = new ArrayList<Integer>();
	private String term = "";
	private int numberOfOnes = -1;

	public Minterm(int num, int binarySize)
	{
		term = Integer.toBinaryString(num);
		termNumber.add(num);
		
		if(term.length() != binarySize)
		{
			String s = "";
			for(int i = 0; i < binarySize - term.length(); i++) s += "0";
			term = s + term;
		}
	}
	
	public Minterm(Minterm m0, Minterm m1)
	{
		String m0_str = m0.toString();
		String m1_str = m1.toString();
		for(int i = 0; i < m0_str.length(); i++)
		{
			char m0i = m0_str.charAt(i);
			char m1i = m1_str.charAt(i);
			if(m0i == m1i) term += m0i;
			else term += '-';
		}
		
		HashSet<Integer> hs = new HashSet<Integer>();
		for(int i = 0; i < m0.getTermsNumber().size(); i++)
			hs.add(m0.getTermsNumber().get(i));
		for(int i = 0; i < m1.getTermsNumber().size(); i++)
			hs.add(m1.getTermsNumber().get(i));
		
		for(Integer val : hs) termNumber.add(val);
		
		Collections.sort(termNumber);
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
		if(this.numberOfOnes == -1) // 처음만 갱신
		{
			int ret = 0;
			for(int i = 0; i < term.length(); i++)
				if(term.charAt(i) == '1')
					ret++;
			this.numberOfOnes = ret;
			return ret;
		}
		else return this.numberOfOnes;
	}

	public ArrayList<Integer> getTermsNumber()
	{
		return this.termNumber;
	}
	
	public String getTermsNumberString()
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
	
	public int sumOfTermsNumber()
	{
		int ret = 0;
		for(Integer v : termNumber) ret += v;
		return ret;
	}
	
	@Override
	public int compareTo(Minterm m) {
		int myOne = this.getNumberOfOnes();
		int otherOne = m.getNumberOfOnes();
		if(myOne < otherOne) return -1;
		else if(myOne == otherOne)
		{
			int myTermSum = this.sumOfTermsNumber();
			int otherTermSum = m.sumOfTermsNumber();
			if(myTermSum < otherTermSum) return -1;
			else if(myTermSum == otherTermSum) return 0; // 사실 일어나면 안되는 경우다.
			else return 1;
		}
		else return 1;
	
	}
	
	public static int getHammingDistance(Minterm m0, Minterm m1)
	{
		int ret = 0;
		
		String m0_str = m0.toString();
		String m1_str = m1.toString();
		for(int i = 0; i < m0_str.length(); i++)
		{
			char m0i = m0_str.charAt(i);
			char m1i = m1_str.charAt(i);
			if(m0i == '-' && m1i == '-') ret += 0;
			else if(m0i != m1i) ret += 1;
		}
		
		return ret;
	}
	
	public static Minterm[] getMinterms(Minterm[] minterms, int ones)
	{
		ArrayList<Minterm> ret = new ArrayList<Minterm>();
		for(int i = 0; i < minterms.length; i++)
			if(minterms[i].getNumberOfOnes() == ones)
				ret.add(minterms[i]);
		return ret.toArray(new Minterm[ret.size()]);
	}
	
	public static Minterm[] sort(Minterm[] minterms)
	{
		if(minterms == null || minterms.length == 0) return minterms;
		Arrays.sort(minterms);
		return minterms;
	}
	
	public static int getIndexOfMintermGroups(Minterm[] minterms, int ones, int idx)
	{
		int ret = 0;
		for(int i = 0; i < minterms.length; i++)
		{
			if(minterms[i].getNumberOfOnes() == ones)
			{
				if(ret == idx) return i;
				ret++;
			}
		}
		return -1;
	}
}
