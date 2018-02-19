



import java.util.*;

import org.jacop.constraints.*;
import org.jacop.core.*;
import org.jacop.search.*;


/*
 * 
 * Problem:
The data-flow graph for the auto regression filter is depicted on Figure 4.3. It consists of 16 multiplications
and 12 additions. These operations need to be scheduled on multipliers and adders. Write a program which will optimize the schedule length for different amount of resources as specified in table 4.2. Fill the table with results
obtained from your program, Assume that multiplication takes two clock cycles and addition only one, but write
your program in such a way that you can easily specify otherwise. Make your program data independent, so if4. LAB ASSIGNMENTS 12
new operation or new operation type is added the program does not have to be changed but only the database of
facts.

/********** COMENTS AT THE END OF THE DOCUMENT
*/


public class Calculations {
	public static void main(String[] args) {
		long T1, T2, T;
		T = System.currentTimeMillis();
		long[][] resoults = new long[numbersAvaliable.length][2];
		for(int k = 0; k < numbersAvaliable.length; k++)
		{
			T1 = System.currentTimeMillis();
			resoults[k][0] = asined(numbersAvaliable[k]);
			T2 = System.currentTimeMillis();
			resoults[k][1] = T2 - T1;
		}
		System.out.println("\n\t*** Execution time = " + (System.currentTimeMillis() - T) + " ms");
		System.out.println("Configuration\t\t\tClock cicles\tRuntime(ms)");
		for(int j = 0; j < numbersAvaliable.length; j++)
		{
			System.out.println(numbersAvaliable[j][0] + " adder(s), " + numbersAvaliable[j][1] + " multiplier(s)\t"
			+ resoults[j][0] + "\t\t" + resoults[j][1]);
		}
	}
	static int asined(int[] numberAvaliable) {
		
		Store store = new Store();
		ArrayList<IntVar[]> starts = new ArrayList<IntVar[]>();
		ArrayList<Operation> ops = new ArrayList<Operation>();
		int operTotal = 0;
		IntVar end = new IntVar(store, "End", 0, maxTime);
		for(int i = 0; i < numberOperations; i++)
		{
			operTotal += numberOperationsEach[i];
			IntVar[] var = new IntVar[numberOperationsEach[i]];
			for(int j = 0; j < numberOperationsEach[i]; j++)
				{ var[j] = new IntVar(store, Integer.toString(i)+"x"+Integer.toString(j), 0, maxTime);}
			starts.add(var);
			
		}

		int[] op = new int[numberOperations];
			for(int k = 0; k < operTotal; k++)
				{
				Operation p1 = null, p2 = null;
				int[][] fathers = entrance[k][0];
				if(fathers[0][0] >= 0 && fathers[0][0] < numberOperations)
				{
					int ip1 = self.index(self.new Operation(fathers[0][0], 0, Integer.toString(fathers[0][1]), null, null), ops);
					if(ip1 != -1) p1 = ops.get(ip1);
				}
				if(fathers[1][0] >= 0 && fathers[1][0] < numberOperations)
				{
					int ip2 = self.index(self.new Operation(fathers[1][0], 0, Integer.toString(fathers[1][1]), null, null), ops);
					if(ip2 != -1) p2 = ops.get(ip2);
				}
				int n = op[entrance[k][1][0][0]];
				Operation o = self.new Operation(entrance[k][1][0][0], n, Integer.toString(entrance[k][2][0][0]), p1, p2);
				ops.add(o);
				op[entrance[k][1][0][0]] += 1;
				}
		IntVar[][] dur = new IntVar[numberOperations][]; 	
		for(int i = 0; i < numberOperations; i++)
		{
			dur[i] = new IntVar[numberOperationsEach[i]];
			IntVar[] res = new IntVar[numberOperationsEach[i]];
			for(int k = 0; k < numberOperationsEach[i]; k++) 
				{ dur[i][k] = new IntVar(store, "", ciclesPerOperation[i], ciclesPerOperation[i]);
				  res[k] = new IntVar(store, "", 1, 1);}
			store.impose(new Cumulative(starts.get(i), dur[i], res, new IntVar(store, "limit", numberAvaliable[i], numberAvaliable[i])));
		}
	ListIterator<Operation> it = ops.listIterator();
	while(it.hasNext())
	{
		Operation o = it.next();
		if(o.before1 != null) store.impose(new XplusYlteqZ(starts.get(o.before1.operation)[o.before1.indexOperation], dur[o.before1.operation][o.indexOperation], starts.get(o.operation)[o.indexOperation]));
		if(o.before2 != null) store.impose(new XplusYlteqZ(starts.get(o.before2.operation)[o.before2.indexOperation], dur[o.before2.operation][o.indexOperation], starts.get(o.operation)[o.indexOperation]));
		store.impose(new XplusYlteqZ(starts.get(o.operation)[o.indexOperation], dur[o.operation][o.indexOperation], end));
	}
		

		// Search 
		
		IntVar[] global = new IntVar[operTotal];
		for(int j = 0; j < operTotal; j++) 	global[j] = new IntVar(store, "", 0, maxTime);
		int id = 0;
		for(int k = 0; k < numberOperations; k++)
		{
			IntVar[] current = starts.get(k);
			global[id] = new IntVar(store, "", 0, maxTime);
			for(int i = 0; i < numberOperationsEach[k]; i++) {store.impose(new XeqY(global[id],current[i])); id++;}
		}
			Search<IntVar> label = new DepthFirstSearch<IntVar>();
			SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>( global , new SmallestDomain<IntVar>(),
					   														   new IndomainMin<IntVar>()); 
		boolean	result = label.labeling(store, select, end);
		if(result) {
		System.out.println("Cicles in total = " + end.value() + " cicles\n");
		System.out.println("\t\t** Option: " + numberAvaliable[0] + " ADDER(S), " + numberAvaliable[1] + " MULTIPLIER(S)");
		// Print the whole tree with all the operations, the start of that operation and the fathers of that operation. 
		self.printSolution(end, starts, ops, operTotal);
		}
		else System.out.println("Not solution to be found. ");
		return end.value();
	}
	
	private void printSolution(IntVar end, ArrayList<IntVar[]> starts, ArrayList<Operation> ops, int operTotal)
	{
		System.out.println("Operation\tStarting\tFathers");
		for(int i = 0; i < operTotal ; i++)
		{
			Operation o = ops.get(i);
			System.out.print("\n"+characterOperation[o.operation]
					+" "+o.name+"\t\t"+starts.get(o.operation)[o.indexOperation].value());
			if(o.before1!=null) System.out.print("\t\t"+characterOperation[o.before1.operation]+o.before1.name);
			if(o.before2!=null) System.out.print(" \t" + characterOperation[o.before2.operation]+o.before2.name);
		}
		System.out.println("");
	}
	
	private int index(Operation o, List<Operation> ops)
	{
		ListIterator<Operation> it = ops.listIterator();
		int i =  -1;
		boolean found = false;
		while(it.hasNext() && !found)
		{
			found = it.next().equals(o);
		}
		if(found) i = it.previousIndex();
		return i;
	}


	class Operation
	{
		
		public Operation(int o, int io, String n, Operation b1, Operation b2)
		{
			operation = o;
			indexOperation = io;
			name = n;
			before1 = b1;
			before2 = b2;
		}
		int operation;
		String name;
		Operation before1, before2;
		int indexOperation;
		public boolean equals(Operation o)
		{
			return  o.operation == this.operation && this.name.equals(o.name);
		}
	}
	// DATA
	static Calculations self = new Calculations();
	static int[][] numbersAvaliable = {{1, 1}, {1, 2}, {1, 3}, {2, 2}, {2, 3}, {2, 4}};
	static int[] numberOperationsEach = {12, 16};
	static int numberOperations = 2;
	static int[] ciclesPerOperation = {1, 2};
	static char[] characterOperation = {'+', '*'};
	static int[][][][] entrance = { //BEFORE  		//Operation 		//Number  	//int[#Total][parents+op+name][2 max][2]
								{ {{-1, 0}, {-1, 0}},	{{1}},		{{2}}},			//int[k][0][i] = father_i[2]
								{ {{-1, 0}, {-1, 0}},	{{1}},		{{3}}},
								{ {{-1, 0}, {-1, 0}},	{{1}},		{{4}}},
								{ {{-1, 0}, {-1, 0}},	{{1}},		{{5}}},
								{ {{ 1, 2}, { 1, 3}},	{{0}},		{{9}}},
								{ {{ 1, 4}, { 1, 5}},	{{0}},		{{10}}},
								{ {{ 0, 9}, {-1, 0}},	{{0}},		{{12}}},
								{ {{ 0,10}, {-1, 0}},	{{0}},		{{13}}},
								{ {{ 0,12}, {-1, 0}},	{{1}},		{{14}}},
								{ {{ 0,12}, {-1, 0}},	{{1}},		{{16}}},
								{ {{ 0,13}, {-1, 0}},	{{1}},		{{15}}},
								{ {{ 0,13}, {-1, 0}},	{{1}},		{{17}}},
								{ {{ 1,14}, { 1,15}},	{{0}},		{{18}}},
								{ {{ 1,16}, { 1,17}},	{{0}},		{{19}}},
								{ {{ 0,18}, {-1, 0}},	{{1}},		{{22}}},
								{ {{ 0,18}, {-1, 0}},	{{1}},		{{20}}},
								{ {{ 0,19}, {-1, 0}},	{{1}},		{{23}}},
								{ {{ 0,19}, {-1, 0}},	{{1}},		{{21}}},
								{ {{-1, 0}, {-1, 0}},	{{1}},		{{6}}},
								{ {{-1, 0}, {-1, 0}},	{{1}},		{{7}}},
								{ {{-1, 0}, {-1, 0}},	{{1}},		{{1}}},
								{ {{-1, 0}, {-1, 0}},	{{1}},		{{0}}},
								{ {{ 1, 6}, { 1, 7}},	{{0}},		{{11}}},
								{ {{ 1,22}, { 1,23}},	{{0}},		{{25}}},
								{ {{ 1, 1}, { 1, 0}},	{{0}},		{{8}}},
								{ {{ 1,20}, { 1,21}},	{{0}},		{{24}}},
								{ {{ 0,11}, { 0,25}},	{{0}},		{{27}}},
								{ {{ 0,24}, { 0,8}},	{{0}},		{{26}}},
								}; 

	static int maxTime = 100;

}

/* ***** COMENTS
I didn't used the constraint Diff2 because it was not reaching the solution, so I substitute it for a equivalent using XplusYlteqZ. 
*/