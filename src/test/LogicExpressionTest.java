package test;

import java.util.Arrays;

import main.LogicExpression;

public class LogicExpressionTest {

	public static void main(String args[])
	{
		Integer a[] = {1, 2};
		LogicExpression texp1 = new LogicExpression(a);
		Integer b[] = {2, 4};
		LogicExpression texp2 = new LogicExpression(b);
		Integer c[] = {3, 5};
		LogicExpression texp3 = new LogicExpression(c);
		Integer d[] = {4, 5};
		LogicExpression texp4 = new LogicExpression(d);
		
		texp1 = LogicExpression.multiply(texp1, texp2);
		texp1 = LogicExpression.multiply(texp1, texp3);
		texp1 = LogicExpression.multiply(texp1, texp4);
		
		texp1.show();
	}
}
