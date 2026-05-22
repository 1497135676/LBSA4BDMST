import java.io.File;
import java.util.*;

public class SolutionLevelList10 extends Solution {
	int[] level;
	List<Integer>[] levelList;
	int[] parent;
	int[] bestParent;
	//对每个顶点的近邻列表搜索，加三顶点交换

	public SolutionLevelList10() {
		level = new int[prob.getNodeNum()];
		for (int i = 0; i < level.length; i++) {
			level[i] = 1 + i % (Solution.maxDepth); //
		}

		int i = rand.nextInt(level.length);
		level[i] = 0;
		if (Solution.maxDiameter % 2 == 1) {
			int j = rand.nextInt(level.length);
			while(j == i) {
				j = rand.nextInt(level.length);
			}
			level[j] = 0;
		}

	    parent = new int[level.length];
		levelList = new ArrayList[Solution.maxDepth+1];
		for (int ii = 0; ii < levelList.length; ii++) {
			levelList[ii] = new ArrayList<>();
		}
		for (int node = 0; node < level.length; node++) {
			levelList[level[node]].add(node);
		}
		cost = eval();//
        bestCost = cost;
		bestParent = parent.clone();
	}



	@Override
	public double eval() {
		return evalLevelList();
	}



    /**
     * Eval method may link a node to lower level nodes and change its level, but the levelList will not be adjusted.
     * @return
     */
	private double evalLevelList() {
		for (int i = 0; i < levelList.length; i++) {
			for (int node : levelList[i]) {
				level[node] = i;
			}
		}

		for (int node : levelList[0]) {
			parent[node] = -1;
		}
		double cost = 0;
		if (levelList[0].size() == 2) {
			int p = levelList[0].get(0);
			int node = levelList[0].get(1);
			//parent[node] = p;
			cost += prob.getDistance(node, p);
		}
		for (int d = 1; d < levelList.length; d++) {
			//for (int d = levelList.length-1; d >=0; d--) {
				for (int node : levelList[d]) {
					int p = -1;
					for (int v : prob.getOrderedNodes(node)) {
						if (level[v] < d) {// == d - 1) {
							p = v;
							break;
						}
					}
					parent[node] = p;
					level[node] = level[p] + 1;
					cost += prob.getDistance(node, p);
				}
			}


		return cost;
	}

	@Override
	public Edge[] getEdges() {
		int edgeCount = 0;
		// 先统计 bestParent 中非 -1 的元素的个数
		for (int i = 0; i < bestParent.length; i++) {
			if (bestParent[i] != -1) {
				edgeCount++;
			}
		}

		// 使用统计到的数量创建数组
		Edge[] edges = new Edge[edgeCount];
		int index = 0;

		// 现在可以安全地添加元素，而不用担心 index 越界
		for (int n1 = 0; n1 < bestParent.length; n1++) {
			if (bestParent[n1] != -1) {
				int n2 = bestParent[n1];
				edges[index++] = new Edge(n1, n2, prob.getDistance(n1, n2));
			}
		}
		return edges;
	}


	public double neighbor(double t) {
        //return neighborLevelList(t);
		double ran=rand.nextDouble();
		if(ran < 1.0/levelList.length) {
			return neighbor_CenterOfSwap(t);
		} else if (ran< Simulation.swapProb) {
			return neighbor_Swap_notCenter(t);
		} else  {
			return neighbor_Insert(t);
		}
	}
	

	private double neighbor_CenterOfSwap(double t) {
		double oldCost = this.cost;
		int level1=0, level2=0,temp;
		int pos1=0, pos2=0;
		int node1=0, node2 = 0;int level3;
		List<Integer>[] copiedLevelList = new ArrayList[levelList.length];


		level2 = 1 + rand.nextInt(levelList.length - 1);
		pos1 = rand.nextInt(levelList[level1].size());
		node1 = levelList[level1].get(pos1);
		pos2 = rand.nextInt(levelList[level2].size());
		node2 = levelList[level2].get(pos2);
		levelList[level1].set(pos1, node2);
		levelList[level2].set(pos2, node1);
		cost = evalLevelList();
		double d =  this.cost - oldCost;
		double prob = rand.nextDouble();
		if (d < 0 || (t > 0 && prob < 1.0/ Math.exp(d/t))) {
			if (cost < bestCost) {
				bestCost = cost;
				bestParent = parent.clone();
			}
		} else {//restore
			cost = oldCost;
			levelList[level1].set(pos1, node1);
			levelList[level2].set(pos2, node2);
		}
		if (t < 0) {
			return d;
		} else {
			return prob;
		}
	}

	private double neighbor_Swap_notCenter(double t) {
		double oldCost = this.cost;
		int level1=0, level2=0,temp;
		int pos1=0, pos2=0;
		int node1=0, node2 = 0;int level3;

		level1 = 1 + rand.nextInt(levelList.length - 1);
		level2 = 1 + rand.nextInt(levelList.length - 1);
		pos1 = rand.nextInt(levelList[level1].size());
		node1 = levelList[level1].get(pos1);
		pos2 = rand.nextInt(levelList[level2].size());
		node2 = levelList[level2].get(pos2);
		levelList[level1].set(pos1, node2);
		levelList[level2].set(pos2, node1);
		cost = evalLevelList();

		double d =  this.cost - oldCost;
		double prob = rand.nextDouble();
		if (d < 0 || (t > 0 && prob < 1.0 / Math.exp(d/t))) {
			if (cost < bestCost) {
				bestCost = cost;
				bestParent = parent.clone();
			}
		} else {//restore
			cost = oldCost;
			levelList[level1].set(pos1, node1);
			levelList[level2].set(pos2, node2);

		}
		if (t < 0) {
			return d;
		} else {
			return prob;
		}
	}

	private double neighbor_Insert(double t) {
		double oldCost = this.cost;
		int level1=0, level2=0,temp;
		int pos1=0, pos2=0;
		int node1=0, node2 = 0;int level3;


		//Move a node not in level 0 to another position not in level 0
		level1 = 1 + rand.nextInt(levelList.length - 1);
		while (levelList[level1].size() < 2) {//levelList[level1].size() < 3
			level1 = 1 + rand.nextInt(levelList.length - 1);
		}
		level2 = 1 + rand.nextInt(levelList.length - 1);
		pos1 = rand.nextInt(levelList[level1].size());
		node1 = levelList[level1].get(pos1);
		pos2 = rand.nextInt(levelList[level2].size());

		if (level1 != level2) {
			levelList[level1].remove(pos1);
			levelList[level2].add(pos2, node1);
		} else if (pos1 < pos2) {
			levelList[level2].add(pos2, node1);
			levelList[level1].remove(pos1);
		} else if (pos1 > pos2) {
			levelList[level1].remove(pos1);
			levelList[level2].add(pos2, node1);
		}


		cost = evalLevelList();
		double d =  this.cost - oldCost;
		double prob = rand.nextDouble();
		if (d < 0 || (t > 0 && prob < 1.0 / Math.exp(d/t))) {
			if (cost < bestCost) {
				bestCost = cost;
				bestParent = parent.clone();
			}
		} else {//restore
			cost = oldCost;
			if (level1 != level2) {
				levelList[level1].add(pos1, node1);
				levelList[level2].remove(pos2);
			} else if (pos1 < pos2) {
				levelList[level1].remove(pos2-1);
				levelList[level2].add(pos1, node1);
			} else if (pos1 > pos2) {
				levelList[level1].remove(pos2);
				levelList[level2].add(pos1, node1);
			}
		}
		if (t < 0) {
			return d;
		} else {
			return prob;
		}
	}


}

