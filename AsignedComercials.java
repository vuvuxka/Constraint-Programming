import java.util.ArrayList;

import org.jacop.constraints.*;
import org.jacop.core.*;
import org.jacop.search.*;

/*
 * Urban planning requires careful placement and distribution of commercial and residential lots. Too many commercial
lots in one area leave no room for residential shoppers. Conversely, too many residential lots in one area
leave no room for shops or restaurants.
Your job is to reorder the 12 residential lots and 13 commercial lots to maximize the quality of the layout.
The quality of the layout is determined by a points system. Points are awarded as follows:
• Any column or row that has 5 Residential lots = +5 points
• Any column or row that has 4 Residential lots = +4 points
• Any column or row that has 3 Residential lots = +3 points
• Any column or row that has 5 Commercial lots 	= -5 points
• Any column or row that has 4 Commercial lots 	= -4 points
• Any column or row that has 3 Commercial lots 	= -3 points

Question: What is the maximum number of points you can achieve for the layout? Give the layout for this
case.
/********** COMENTS AT THE END OF THE DOCUMENT
*/


public class AsignedComercials {
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
		int N = 5, C = 13;
		IntVar[] layout = new IntVar[N*N];
		for(int i = 0; i < N; i++)
		{
			for(int j = 0; j < N; j++) {
				layout[i*N + j] = new IntVar(store, Integer.toString(i+1) + "x" + Integer.toString(j+1), 0, 1);
				//RESIDENTIAL = 1; COMERCIAL = 0;
			}
		}

		IntVar[] valuesR = new IntVar[N];
		IntVar[] valuesC = new IntVar[N];
		IntVar auxR, auxC;
		int[] value = {-5, -4, -3, 3, 4, 5};
		for(int i = 0; i < N; i++)
		{
			valuesR[i] = new IntVar(store, "", -5, 5);
			valuesC[i] = new IntVar(store, "", -5, 5);
			auxR = new IntVar(store, "", 0, 5);
			auxC = new IntVar(store, "", 0, 5);
			ArrayList<IntVar> row = new ArrayList<IntVar>();
			ArrayList<IntVar> col = new ArrayList<IntVar>();
			for(int j = 0; j < N; j++)
			{
				row.add(layout[i*N+j]);
				col.add(layout[j*N+i]);
			}
			// Add the Constraints
			store.impose(new Sum(row, auxR));
			store.impose(new Sum(col, auxC));
			store.impose(new Element(auxR, value, valuesR[i], -1));
			store.impose(new Element(auxC, value, valuesC[i], -1));
		}
		store.impose(new Sum(layout, new IntVar(store, "", N*N - C, N*N - C))); //Is there exact 12 Residence				
		IntVar total = new IntVar(store, "total", -50, 50);
		IntVar totalInverted = new IntVar(store, "totalIn", -50, 50);
		IntVar[] scores = new IntVar[2*N];
		for (int p = 0; p < N; p++) {scores[p] = valuesC[p]; scores[N + p] = valuesR[p];}
		store.impose(new Sum(scores, total));
		store.impose(new XplusYeqC(total, totalInverted, 0)); // total + totalInverted = 0 -> total = -totalInverted
		
		// Search  
		
		Search<IntVar> label = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(layout, new SmallestDomain<IntVar>(),
				   														   new IndomainMin<IntVar>()); 
		label.setTimeOut(5); //This is not necessarily, but finds the same solution and doesn't take that much
		boolean result = label.labeling(store, select, totalInverted);
		if(result) {
		System.out.println("Points at the layout = " + total.value() + " points\n");
		printMatrix(layout, N, N);
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