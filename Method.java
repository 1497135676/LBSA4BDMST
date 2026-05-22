import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

public class Method {
	public static int unimprove;


	public static Solution listBasedSA(Solution s,  int sampleTimes, int len) {

		PriorityQueue<Double> listOfDelta = new PriorityQueue<>();
		while (listOfDelta.size() < len) {
			double d = s.neighbor(-1);
			if (d != 0) listOfDelta.offer(new Double(-Math.abs(d/Math.log(Simulation.p0))));
		}
		double bestCost = s.getBestCost();
		int sts = sampleTimes;
		unimprove = 0;
		int evaltimes=0;

		for (int q = 0;  unimprove < 100000; q++) {
			double t = -listOfDelta.peek();
			double totalTemp = 0;
			int counter = 0;
			for (int st = 0; st < sts &&  ( unimprove < 100000); st++) {
				unimprove++;
				evaltimes++;
				double cost = s.cost;
				double prob = s.neighbor(t);

				if (s.cost - cost > 0) {//if worse solution is accepted
					totalTemp += (s.cost - cost) / Math.log(1.0/prob);
					counter++;
				}
				if (s.bestCost < bestCost) {//new best solution found
					unimprove = 0;
					bestCost = s.bestCost;
					s.lastImprove=evaltimes;

				}



			}

			if ( counter != 0) {
				listOfDelta.remove();
				listOfDelta.offer( - totalTemp/counter);
			}

		}

		return s;
	}


    private static void save(double[][] costs) {
    	if (!Simulation.SAVING_PROCESS_DATA) {
    		return;
    	}
    	
    	try {
    		String fileName = /*Simulation.getMthoedType() + " " + */ Problem.getFileName() + " convergence process.csv";
			PrintWriter printWriter = new PrintWriter(new FileWriter(fileName));
			for (int i = 0; i < costs.length; i++) {
				printWriter.print(costs[i][0]);
				for (int j = 1; j < costs[i].length; j++) {
					printWriter.print("," + costs[i][j]);
				}
				printWriter.println();
			}
			printWriter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}	     	
    }
    

}
