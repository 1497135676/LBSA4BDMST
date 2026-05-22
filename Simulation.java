import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * 
 * @author yiwen zhong
 *
 */
public class Simulation {

	public static void main(String[] args) {

		String	filePath1 = (new File("")).getAbsolutePath() + "\\datas\\Esteins";
		String	filePath2 = (new File("")).getAbsolutePath() + "\\datas\\Rands";
		testPerformance(filePath1);
		testPerformance(filePath2);


	}

	/**
	 * This function is used to fine-tune parameter
	 * 
	 * @param filePath
	 */
	private static void parametersTunning(String filePath) {
		java.io.File dir = new java.io.File(filePath);
		java.io.File[] files = dir.listFiles();

		String pathName = filePath.substring(filePath.lastIndexOf("/", filePath.length()-2)).substring(1);
		pathName = pathName.substring(0, pathName.length()-1);
		
		String fileName = (new File("")).getAbsolutePath() + "/results/Parameters/";
		fileName += pathName + "-" + Simulation.methodType ;
		fileName += "-parameters tunning results.csv";

		List<double[]> resultsList = new ArrayList<>();
		List<Double> paras = new ArrayList<>();
		double min = 0.1, max = 1, step = 0.1;
		for (int i = 0; i < 10; i++) {
			double scale = min + i * step;
			paras.add(scale);
		}
 
		for (File file : files) {
			for (double para: paras) {
				Simulation.alpha = para;
				System.out.println("\n"+Simulation.getParaSetting());
				double[] results = testSingleInstance(file.getAbsolutePath());
				resultsList.add(results);
				Simulation.saveParasTunningResults(fileName, paras, resultsList);
			}
		}

	}
	private static void saveParasTunningResults(String fileName, List<Double> paras, List<double[]> resultsList) {
		if (!Simulation.SAVING_PARA_TUNNING) { return;	}
		try {
			PrintWriter printWriter = new PrintWriter(new FileWriter(fileName));
			for (int idx = 0; idx < resultsList.size(); idx++) {
				double[] rs = resultsList.get(idx);
				printWriter.println();
				printWriter.print(paras.get(idx % paras.size()));
				for (int j = 0; j < rs.length; j++) {
					printWriter.print(","+rs[j]);
				}
			}
			printWriter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private static double[] testSingleInstance(String fileName) {
		double[] results = run(fileName);
		for (double d : results) {
			System.out.print(d + "\t");
		}
		System.out.println();
		return results;
	}
	private static double[] testPerformance(String filePath) {
		java.io.File dir = new java.io.File(filePath);
		java.io.File[] files = dir.listFiles();
		String pathName = filePath.substring(filePath.lastIndexOf("\\", filePath.length()-2)).substring(1);
		pathName = pathName.substring(0, pathName.length());
		//System.out.println(pathName);
		String fileName = (new File("")).getAbsolutePath() + "/results/Performance/" + pathName + "-";
		fileName += Simulation.getParaSetting();
		fileName += " results.csv";
		List<double[]> results = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			//if (!file.getName().contains("Du62")) continue;
			System.out.print(file.getName()+"\n");
			double[] result = run(file.getAbsolutePath());
			results.add(result);
			System.out.println();
			System.out.print(file.getName()+"\t");
			for (double d : result) {
				System.out.print(d+"\t");
			}
			System.out.println();
		    Simulation.saveFinalResults(fileName, files, results);
		}
		
		//calculate statistics results
		double[] totals = new double[results.get(0).length];
		for (int i = 0; i < files.length; i++) {
			System.out.println();
			System.out.print(files[i].getName()+"\t");
			for (int j = 0; j < results.get(i).length; j++) {
				System.out.print(results.get(i)[j]+"\t");
				totals[j] += results.get(i)[j];
			}
		}
		System.out.println("\t");
		for (int j = 0; j < totals.length; j++) {
			totals[j] = Math.round(totals[j]/files.length*1000)/1000.0;
			System.out.print(totals[j]+"\t");
		}
		return totals; //average data for all files
	}
	private static void saveFinalResults(String fileName, File[] files, List<double[]> results) {
		if (!Simulation.SAVING_FINAL_RESULTS) return;
		
		try {
			PrintWriter printWriter = new PrintWriter(new FileWriter(fileName));
			for (int i = 0; i < results.size(); i++) {
				printWriter.println();
				printWriter.print(files[i].getName());
				for (int j = 0; j < results.get(i).length; j++) {
					printWriter.print(","+results.get(i)[j]);
				}
			}
			printWriter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private static double[] run(String fileName) {
		Problem.load(fileName);
		Problem prob = Problem.get();
		int sampleTimes = 100 + prob.getNodeNum()/3;
		System.out.println("\n"+Simulation.getParaSetting());

		double duration = (new java.util.Date()).getTime();
        Solution s = null;
        Solution best = null;
		double[] costs = new double[Simulation.TIMES];
		double[] iterations = new double[Simulation.TIMES]; //last improving iteration
		int i;

		for ( i = 0; i < Simulation.TIMES; i++) {
			Solution.init(prob.getBound());//单双中心双迭代切入点
			s = Solution.make();
			if (Simulation.methodType == EMethodType.LBSA ) {
				s = Method.listBasedSA(s, sampleTimes, len);
			} else {
				System.out.println("Cannot reach here in run method!");;
			}
            if (s != null) {
                s.getEdges();
            }
            costs[i] += s.getBestCost(); // - bValue;
			iterations[i] = s.lastImprove;
			System.out.println( i + "," + costs[i] + "," + iterations[i]);
			if (best == null || s.getBestCost() < best.getBestCost()) {
				best = s;
			}
		}

		int idx = fileName.lastIndexOf("\\");
		fileName = fileName.substring(idx+1);
		best.save((new File("")).getAbsolutePath() + "/results/" + methodType + "_" +repType+ "_" + fileName + ".txt");

        //System.out.println(best);
        
		duration = (new java.util.Date()).getTime()-duration;
		duration /= TIMES;
		duration = Math.round(duration/1000*1000)/1000.0;
		//最小，最大，平均值，中位数，平均评价次数，标准差，平均时间
		double[] stat = new double[] { Tools.min(costs), Tools.max(costs), Tools.mean(costs), Tools.median(costs), Tools.mean(iterations), Tools.standardDevition(costs), duration};
		double[] results = new double[costs.length + stat.length];
		for (int j = 0; j < stat.length; j++) {
			results[j] = stat[j];
		}
		for (int j = 0; j < costs.length; j++) {
			results[j+stat.length] = costs[j];
		}
		return results;
	}
	public static EMethodType getMthoedType() { return Simulation.methodType;}
	public static boolean isSavingFinalResults() { return Simulation.SAVING_FINAL_RESULTS;}
	public static boolean isSavingProcessData() { return Simulation.SAVING_PROCESS_DATA;}
	public static String getParaSetting() {
		String str = methodType + "-" + repType;
		if (repType == ERepType.ORDER) {
			str += "-" + decodingType + "-" + neighborType + "-" + swapProb;
		} else if (repType == ERepType.LEVEL_ARRAY || repType == ERepType.LEVEL_LIST ||
				repType == ERepType.LEVEL|| repType == ERepType.LEVEL_LIST2
				|| repType == ERepType.LEVEL_LIST10A || repType == ERepType.LEVEL_LIST10) {
			str += " +listLen=" + Simulation.len;
			str += " +swProb=" + swapProb;
			str += " +p0=" + p0;


		}
		return str;
	}
	protected static EMethodType methodType = EMethodType.LBSA;
	protected static ERepType repType = ERepType.LEVEL_LIST10;//ORDER, LEVEL_LIST, LEVEL_ARRAY, LEVEL, TREE,LEVEL_LIST2
	public static EDecodingType decodingType = EDecodingType.S_DIRE;//.B_DIRE; // for SolutionOrder only
	protected static ENeighborType neighborType = ENeighborType.HYBRID;// 
	protected static boolean isDepthGuided = true; //for SolutionOrder only
    private static final int TIMES = 50;
	public static final boolean SAVING_PROCESS_DATA = true;
	public static final boolean SAVING_FINAL_RESULTS = true;
	public static final boolean SAVING_PARA_TUNNING = true;
	public static final boolean SAVING_SPANNING_TREE = true;
	public static final boolean useNNL = false;
	public static final ETestType TEST_TYPE = ETestType.MULTIPLE_INSTANCE;//SINGLE_INSTANCE,MULTIPLE_INSTANCE,PARAMETER_TUNNING
	public static double alpha = 0.998;//0.998 for SA, 0.995 for rand


	//List-based SA
	public static int len = 600;// 100; 300, 700 ,1100, 1500
	public static double swapProb = 0.9;//0.5, 0.65, 0.8, 0.9,0.95
	public static double p0=0.1;//0.5,  1.0 / Math.E  , 0.2, 0.1, 0.01







}
