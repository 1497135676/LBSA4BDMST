import java.util.*;

//我的构造启发式
public class RGH1 extends Solution{
    static int[] permu;
    int[] depth;
    int[] parent;
    public RGH1() {

        if(prob.getBound()%2==0){
            constructeven();
        }else constructeven();

        //cost=0;
        cost = eval();
        bestCost = cost;
    }
    public RGH1(int[] per) {
        int nodeNum = prob.getNodeNum();
        permu = new int[nodeNum];
        depth = new int[nodeNum];
        parent = new int[nodeNum];
        permu=per.clone();
        //cost=0;
        cost = eval();
        bestCost = cost;
    }



    //    private void construct(int center1,int center2) {
//        int nodeNum = prob.getNodeNum();
//        permu = new int[nodeNum];
//        depth = new int[nodeNum];
//        parent = new int[nodeNum];
//        int optimalVertex = findOptimalVertex(prob.getNodeNum(), prob.dists);//找出图的中心
//        Point optimal = prob.getNode(optimalVertex);
//        int[] corners = new int[4];
//        int[] indexarr=new int[4];
//        for (int i = 0; i < 4; i++) {
//            corners[i]=-1;
//            indexarr[i]=-1;
//        }
//        int count = 0;  // 记录找到的角点数
//        List<Integer> list = prob.getOrderedNodes(optimalVertex);//
//
//        for (int i = 0; i < list.size(); i++) {
//            int index = list.get(i);  // 获取当前节点索引
//            Point p = prob.getNode(index);  // 获取该节点的坐标信息
//
//            // 判断该点是哪个角落
//            if (p.x < optimal.x && p.y > optimal.y && corners[0] == -1) {  // 左上角
//                corners[0] = p.id;
//                indexarr[count]=index;
//                count++;
//
//            } else if (p.x > optimal.x && p.y > optimal.y && corners[1] == -1) {  // 右上角
//                corners[1] = p.id;
//                indexarr[count]=index;
//                count++;
//            } else if (p.x < optimal.x && p.y <optimal.y && corners[2] == -1) {  // 左下角
//                corners[2] = p.id;
//                indexarr[count]=index;
//                count++;
//            } else if (p.x > optimal.x && p.y < optimal.y && corners[3] ==-1) {  // 右下角
//                corners[3] = p.id;
//                indexarr[count]=index;
//                count++;
//            }
//            // 如果已经找到四个角落，停止遍历
//            if (count == 4) {
//                break;
//            }
//        }
//        Integer[] sortedIndexes = new Integer[indexarr.length];
//        for (int i = 0; i < indexarr.length; i++) {
//            sortedIndexes[i] = indexarr[i];
//        }
//        // 排序索引数组（从大到小）
//        Arrays.sort(sortedIndexes, Collections.reverseOrder());
//
//        // 按索引从list中删除元素
//        for (int index : sortedIndexes) {
//            if(index!=-1)
//                list.remove(index);
//        }
//        int id=0;
//        permu[id++]=optimalVertex;
//
//        for (int i = 0; i < 4; i++) {
//            if(corners[i]!=-1){
//                permu[id++]=corners[i];
//            }
//        }
//
//
//
////        Collections.shuffle(list);
//        list=shuffleInSegments(list,3);
//
//
//        // 填充 permu 数组
//        for (int i = 0; i < list.size(); i++) {
//            permu[id++] = list.get(i);
//        }
//         System.out.println(Arrays.toString(permu));
//    }
    private void constructodd(int center1,int center2) {
    int nodeNum = prob.getNodeNum();
    permu = new int[nodeNum];
    depth = new int[nodeNum];
    parent = new int[nodeNum];

    int optimalVertex = findOptimalVertex(prob.getNodeNum(), prob.dists);//找出图的中心
    List<Integer> list = prob.getOrderedNodes(optimalVertex);//

    int rand1= rand.nextInt(nodeNum/3);
    int cen1= list.get(rand1);
    list.remove(rand1);
    int rand2= rand.nextInt(nodeNum/3);
    int cen2= list.get(rand2);
    list.remove(rand2);
    list.add(rand2,optimalVertex);
    list=shuffleInSegments(list,4);
    permu[0]=cen1;
    permu[1]=cen2;
    // 填充 permu 数组
    for (int i = 0; i < list.size(); i++) {
        permu[i+2] = list.get(i);
    }
        System.out.println(Arrays.toString(permu));
}

    private void constructeven1(int center1,int center2) {
        int nodeNum = prob.getNodeNum();
        permu = new int[nodeNum];
        depth = new int[nodeNum];
        parent = new int[nodeNum];

        int optimalVertex = findOptimalVertex(prob.getNodeNum(), prob.dists);//找出图的中心
        List<Integer> list = new ArrayList<>(prob.getOrderedNodes(optimalVertex));

        list=shuffleInSegments(list,5);

        permu[0]=optimalVertex;

        // 填充 permu 数组
        for (int i = 1; i < permu.length; i++) {
            permu[i] = list.get(i-1);
        }

    }

    private void constructeven() {
        int nodeNum = prob.getNodeNum();
        permu = new int[nodeNum];
        depth = new int[nodeNum];
        parent = new int[nodeNum];

        // 找到最优顶点
        int optimalVertex = findOptimalVertex(prob.getNodeNum(), prob.dists);
        permu[0] = optimalVertex;
        // 获取邻接节点列表
        List<Integer> list = new ArrayList<>(prob.getOrderedNodes(optimalVertex));
       // System.out.println(list);
        // k锦标赛选择大小
        int k = 2; // 假设k为5，可以根据需要调整

        // 使用k锦标赛选择填充permu
        for (int i = 1; i < permu.length; i++) {
            // 如果list为空，抛出异常或提前结束
            if (list.isEmpty()) {
                System.err.println("Error: The list is empty before accessing element " + i);
                break;  // 或者抛出异常，避免访问空列表
            }

            // 如果list中的剩余节点少于k，直接将剩余节点填充到permu中
            if (list.size() < k) {
                // 直接将剩余的节点加入permu
                permu[i] = list.get(0);
                list.remove(0);  // 移除已选择的节点
            } else {
                // 进行一次k锦标赛选择
                List<Integer> tournamentCandidates = new ArrayList<>();
                for (int j = 0; j < k; j++) {
                    // 随机选择k个节点中的一个
                    int randomIndex = (int) (Math.random() * list.size());
                    tournamentCandidates.add(list.get(randomIndex));
                }

                // 找到下标最小的元素
                int bestNode = tournamentCandidates.get(0);
                int bestIndex = list.indexOf(bestNode);
                for (int candidate : tournamentCandidates) {
                    int index = list.indexOf(candidate);
                    if (index < bestIndex) {
                        bestIndex = index;
                        bestNode = candidate;
                    }
                }

                // 将选择的节点加入permu
                permu[i] = bestNode;
                // 将选出的节点从list中移除，避免重复选择
                list.remove(Integer.valueOf(bestNode));

                // 再次检查是否已清空list
                if (list.isEmpty()) {
                    System.err.println("Warning: The list has become empty after removing " + bestNode);
                }
            }
        }
       // System.out.println(Arrays.toString(permu));
    }

    public List<Integer> shuffleInSegments(List<Integer> list, int k) {
        //平均切分为k段分别打乱
        int n = list.size();
        int segmentSize = n / k;
        int remainder = n % k;  // 用来处理不能完全平分的情况

        int startIndex = 0;
        for (int i = 0; i < k; i++) {
            // 计算当前段的大小，如果有余数，则当前段会比其他段多一个元素
            int currentSegmentSize = segmentSize + (i < remainder ? 1 : 0);
            int endIndex = startIndex + currentSegmentSize;

            // 对当前段进行打乱
            List<Integer> segment = list.subList(startIndex, endIndex);
            Collections.shuffle(segment);

            // 更新起始索引，处理下一段
            startIndex = endIndex;
        }
        return list;
    }

    //找到图的中心位置
    public int findOptimalVertex(int nodeNum, double[][] D) {
        double minDistSum = Double.POSITIVE_INFINITY;
        int OptimalVertex = -1;

        // 计算每个顶点和其余顶点的距离之和
        for (int i = 0; i < nodeNum; i++) {
            double distSum = 0.0;
            for (int j = 0; j < nodeNum; j++) {
                if (i != j) {
                    distSum += D[i][j];
                }
            }

            // 更新最小距离之和和对应的顶点编号
            if (distSum < minDistSum) {
                minDistSum = distSum;
                OptimalVertex = i;
            }
        }

        return OptimalVertex;
    }
    // 找到最长的边作为半径
    public double findLongestEdge(int t,double[][] D) {
        double LongestEdge = 0.0;
        for (int i = 0; i < D.length; i++) {
            LongestEdge = Math.max(LongestEdge, D[i][t]);
        }
        return LongestEdge;
    }

    //树转化为序列，实验结果证明无用
    public static RGH1 EdgeTopermu(Edge[] edges ) {
        int n = edges.length + 1; // 节点数量
        int[] level = new int[n];
        int[] center= new int[2];
        int[] per= new int[n];
        int idx = 0;
        center=findCenter(edges);
        Arrays.fill(level, -1); // 初始化所有层次为-1（未访问）

        // 构建邻接列表
        List<Integer>[] adjacencyList = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adjacencyList[i] = new ArrayList<>();
        }
        for (Edge e : edges) {
            adjacencyList[e.p1].add(e.p2);
            adjacencyList[e.p2].add(e.p1);
        }

        // 创建队列并初始化中心
        Queue<Integer> queue = new LinkedList<>();
        for (int c : center) {
            if (c != -1) { // 仅当中心有效时将其加入队列
                queue.add(c);
                per[idx++]=c;
                level[c] = 0; // 中心层次设为0
            }
        }


        // 广度优先遍历
        while (!queue.isEmpty()) {
            int current = queue.poll();
            int currentLevel = level[current];

            for (int neighbor : adjacencyList[current]) {
                if (level[neighbor] == -1) { // 如果邻居未访问
                    level[neighbor] = currentLevel + 1; // 设置邻居层次
                    queue.add(neighbor);
                    per[idx++]=neighbor;// 将邻居入队
                }
            }
        }
       // System.out.println(Arrays.toString(per));

        return new RGH1(per);
    }

    public static int[] findCenter(Edge[] edges) {
        List<Integer>[] nnList = new ArrayList[prob.getNodeNum()];
        List<Integer> remained = new ArrayList<>();
        for (int i = 0; i < nnList.length; i++) {
            nnList[i] = new ArrayList<>();
            remained.add(i);
        }

        int[] degree = new int[prob.getNodeNum()];
        for (Edge e : edges) {
            nnList[e.p1].add(e.p2);
            nnList[e.p2].add(e.p1);
            degree[e.p1]++;
            degree[e.p2]++;
        }

        // 逐层删除叶节点，直到剩下1或2个中心节点
        while (remained.size() > 2) {
            List<Integer> leafs = new ArrayList<>();
            for (int i = 0; i < nnList.length; i++) {
                if (nnList[i].size() == 1) { // 找到叶节点
                    leafs.add(i);
                }
            }

            for (int leaf : leafs) {
                remained.remove(Integer.valueOf(leaf)); // 按值删除叶节点
                int neighbor = nnList[leaf].remove(0);  // 获取叶节点的唯一邻居
                nnList[neighbor].remove(Integer.valueOf(leaf)); // 从邻居的列表中移除叶节点
            }
        }

        // 检查剩下的节点数以确定中心类型
        int[] center = new int[2];
        if (remained.size() == 1) {
            center[0] = remained.get(0);
            center[1] = -1; // 单中心情况
        } else {
            center[0] = remained.get(0);
            center[1] = remained.get(1); // 双中心情况
        }

        return center;
    }


    @Override
    public double eval() {
        Arrays.fill(depth, Integer.MAX_VALUE -1);
        int idx = 0;
        int node = permu[idx];
        depth[node] = 0;
        parent[node] = -1;
        double cost = 0;
        if (Solution.maxDiameter % 2 == 1) {
            idx++;
            node = permu[idx];
            depth[node] = 0;
            parent[node] = permu[0];
            cost += prob.dists[node][parent[node]];
        }
        while (++idx < permu.length) {
            node = permu[idx];
            int p = -1;
            for (int v : prob.getOrderedNodes(node)) {
                if (depth[v] < Solution.maxDepth) {
                    p = v;
                    break;
                }
            }
            parent[node] = p;
            depth[node] = depth[p] + 1;
            cost += prob.dists[node][p];
        }

        return cost;
    }

    @Override

    public Edge[] getEdges() {
        Edge[] edges = new Edge[permu.length - 1];
        int index = 0;
        for (int n1 = 0; n1 < parent.length; n1++) {
            if (parent[n1] != -1) {
                int n2 = parent[n1];
                edges[index++] = new Edge(n1, n2, prob.getDistance(n1, n2));
            }
        }
        return edges;
    }
    @Override
    public double neighbor(double t) {
        return 0;
    }
}
