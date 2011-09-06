import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class token implements Serializable
{
	Vector<Integer> tokvec = new Vector<Integer>();
	Queue<Integer> tokq = new LinkedList<Integer>();
	String output;
	public token(int nofc)
	{
		output="Member ID\tSequence Vector\t\tToken Vector\t\tToken Queue\n";
		output=output.concat("==========\t==============\t\t============\t\t===========\n");
		for(int i=0;i<nofc;i++)
			tokvec.add(0);
	}

}
