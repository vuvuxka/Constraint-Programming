



import java.util.*;

import org.jacop.constraints.*;
import org.jacop.core.*;
import org.jacop.search.*;

/*
 * After going through the receipts from your car trip through Europe this summer, you realised that the gas prices
varied between the cities you visited. Maybe you could have saved some money if you were a bit more clever
about where you filled your fuel?
To help other tourists (and save money yourself next time), you want to write a program for finding the
cheapest way to travel between cities, filling your tank on the way. We assume that all cars use one unit of fuel
per unit of distance, and start with an empty gas tank.
Figure 4.2 shows the cities number from 1 to 10 and distances between them. Price of the fuel in each city is
as presented in table 4.1. Model this problem and find the price of the cheapest trip from city 1 to city 10 using a
car with tank capacity 10. What will be the price for this trip if the car has tank capacity 15?

/********** COMENTS AT THE END OF THE DOCUMENT
*/


public class FuelTank {
	public static void main(String[] args) {
		long T1, T2, T;
		T1 = System.currentTimeMillis();
		asined();
		T2 = System.currentTimeMillis();
		T = T2 - T1;
		System.out.println("\n\t*** Execution time = " + T + " ms");
	}
	static void asined() {
		Store store = new Store();
		int N = 10;
		IntVar[] city = new IntVar[N];
		List<IntVar> circuit = new ArrayList<IntVar>();
		circuit.add(new IntVar(store, "", 1, 10));
		for(int i = 0; i < N; i++)
		{
			city[i] = new IntVar(store, Integer.toString(i+1), 1, 10);
		}
		store.impose(new Subcircuit(city));
		store.impose(new Alldiff(city));
		Iterator<IntVar> it1 = circuit.iterator();
		store.impose(new And(new XneqC(city[0], 0), new XneqC(city[9], 9)));
		for(int i = 0; i < N; i++)
		{
			if(city[i].value() == i) circuit.add(city[i]);
		}
		Iterator<IntVar> i = circuit.iterator();
		Iterator<IntVar> j = i;
		i.next();
		IntVar z = new IntVar(store, "", 0, 100);
		IntVar w = new IntVar(store, "", 0, 100);
		List<IntVar> dis = new ArrayList<IntVar>();
		int k = 0;
		/*while(i.hasNext())
		{
			store.impose(new XmulCeqZ(i.next(), 10, z));
			store.impose(new XplusYeqZ(z, j.next(), w));
			IntVar s = new IntVar(store, "", 1, 15);
			store.impose(new ElementInteger(w, distance, s, 0));
			dis.add(s);
			i.next();
			j.next();
			k++;
		}*/
		IntVar cost = new IntVar(store, "", 0, 10);
		int tank = 0;
		ListIterator<IntVar> cir = circuit.listIterator();
		ListIterator<IntVar> di= dis.listIterator();
		IntVar b[] = new IntVar[circuit.size()];
		/*for(int l = 0; l < circuit.size(); l++) 
		{
			b[l] = new IntVar(store, "pick gas at" + l +" in the circuit?", 0, 1);
			if(di.next().value() > tank) store.impose(new XeqC(b[l],1));
			tank += (10-tank)*b[l].value();
			
			//store.impose(new XmulYeqZ(cost[l]
		}*/
				
		

		// Search  
		
		Search<IntVar> label = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>( city, new SmallestDomain<IntVar>(),
				   														   new IndomainMin<IntVar>()); 
		label.setTimeOut(5); //This is not necessarily, but finds the same solution and doesn't take that much
		boolean result = label.labeling(store, select, cost);
		for(int m = 0; m < circuit.size(); m++) System.out.print(circuit.get(m).value());
		if(result) {
		System.out.println("Points at the layout = " + cost.value() + " points\n");
		//printMatrix(cost, N, N);
		}
		else System.out.println("Not solution to be found. ");
	}
	
	private static void printMatrix(IntVar[] matrix, int n, int m){
	    for (int row = 0; row < n; row++) {
	    	System.out.print("\t      |  ");
	        for (int column = 0; column < m ;column++) {
	        	if(matrix[row*n + column].value()==1) System.out.print("R  ");
	        	else System.out.print("C  ");
	        }
	        System.out.print("|\n");
	    }
	}


private static int[] distance =  // 1  2   3   4   5   6   7   8   9  10
							/*1*/  { 0, 10, 11,  6,  0,  0,  0,  0,  0,  0,
							/*2*/   10,  0,  5,  8,  0,  0,  0,  0,  0,  0,
							/*3*/   11,  5,  0,  0,  3,  5,  0,  0,  0,  0,
							/*4*/    6,  8,  0,  0,  2,  6,  7,  0,  0,  0,
							/*5*/    0,  0,  3,  2,  0,  0, 12,  0,  0,  0,
							/*6*/    0,  0,  5,  6,  0,  0,  0, 14,  0,  0,
							/*7*/    0,  0,  0,  7, 12,  0,  0,  5,  3,  0,
							/*8*/    0,  0,  0,  0,  0, 14,  5,  0,  1,  9,
							/*9*/    0,  0,  0,  0,  0,  0,  3,  1,  0,  2,
							/*10*/   0,  0,  0,  0,  0,  0,  0,  9,  2,  0};
private static int[] price = {10, 10, 8, 12, 13, 9, 10, 11, 12, 8};
}


/*
(*) 
For counting the value in each column and row, we use this system:
Since C = 0 and R = 1, this are all the possibilities:
Possibilities: 		Sum of			Value that		 
				  the values:		should be:
	C C C C C			0		  value[0] = -5			
	C C C C R			1		  value[1] = -4	
	C C C R R			2		  value[2] = -3			
	C C R R R			3		  value[3] =  3		
	C R R R R			4		  value[4] =  4		
	R R R R R			5		  value[5] =  5		 

*/