import com.google.ortools.graph.MinCostFlow;
import com.google.ortools.graph.MinCostFlowBase;
import com.google.ortools.linearsolver.MPSolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MinCost {
    static { System.loadLibrary("jniortools"); }
    static class Node {
        int id;
        int supply;
        LinkedList<Edge> edges = new LinkedList<Edge>();
        Node addEdgeTo(Node n, int cap, long cst)
        {
            Edge e=new Edge(cap,cst);
            edges.add(e);
            e.fromNode = this; e.toNode = n;
            return this;
        }
        Node(NodeMaster nm)
        {

            nm.addNode(this,1000);
        }
        Node(NodeMaster nm, int prefix)
        {
            nm.addNode(this,prefix);
        }
    }
    static class Edge {
        int capacity;
        long cost;
        Node fromNode,toNode;
        Edge(int cap, long cst) {
            this.capacity=cap;
            this.cost = cst;
        }
    }
    static class NodeMaster {
        ArrayList<Node> nodes = new ArrayList<Node>();
         void addNode(Node n, int prefix)
        {
            nodes.add(n);
            n.id = nodes.size()+prefix;
        }
        private void addNode(Node n)
        {
            this.addNode(n,1000);
        }
        public Node addSupplyNode(int amount) {
            Node s = new SupplyNode(this,amount);
            return s;
        }
        public Node addDemandNode(int amount) {
            Node s = new DemandNode(this,amount);
            return s;
        }
        public Node addNode(int prefix) {
            Node n = new Node(this,prefix);
            return n;
        }
        public Node addNode() {
            Node n = new Node(this);
            return n;
        }
        public void prepare(MinCostFlow minCostFlow)
        {
            nodes.forEach(node -> {
                node.edges.forEach(edge ->
                {
                    minCostFlow.addArcWithCapacityAndUnitCost(
                            edge.fromNode.id,
                            edge.toNode.id,
                            edge.capacity,
                            edge.cost);
                });
            });

            nodes.forEach(node -> {
                minCostFlow.setNodeSupply(node.id,node.supply);
            });
        }
        public void printoutEx(MinCostFlow minCostFlow, HashMap<Integer, String> nameMap, boolean printAll)
        {
            long optimalCost = minCostFlow.getOptimalCost();
            System.out.println("Minimum cost: " + optimalCost);
            System.out.println("");
            System.out.println(" Edge   Flow / Capacity  Cost");
            int numArcs = minCostFlow.getNumArcs();
            for (int i = 0; i < numArcs; ++i)
            {
                long cost = minCostFlow.getFlow(i) * minCostFlow.getUnitCost(i);
                var flow1= minCostFlow.getFlow(i);
                if (flow1==0) continue;
                var id1= minCostFlow.getTail(i);
                String name1 = String.valueOf(id1);
                if (nameMap.containsKey(id1))
                    name1= nameMap.get(id1);
                else if (!printAll) continue;
                var id2=minCostFlow.getHead(i);
                String name2 = String.valueOf(id2);
                if (nameMap.containsKey(id2))
                    name2= nameMap.get(id2);
                else if (!printAll) continue;
                System.out.println(name1 + "," +
                        name2 + "," +
                        String.format("%3d",  cost));
            }
        }
        public void printout(MinCostFlow minCostFlow)
        {
            long optimalCost = minCostFlow.getOptimalCost();
            System.out.println("Minimum cost: " + optimalCost);
            System.out.println("");
            System.out.println(" Edge   Flow / Capacity  Cost");
            int numArcs = minCostFlow.getNumArcs();
            for (int i = 0; i < numArcs; ++i)
            {
                long cost = minCostFlow.getFlow(i) * minCostFlow.getUnitCost(i);
                System.out.println(minCostFlow.getTail(i) + " -> " +
                        minCostFlow.getHead(i) + "  " +
                        String.format("%3d",  minCostFlow.getFlow(i))
                        + "  / " +
                        String.format("%3d",  minCostFlow.getCapacity(i)) + "       " +
                        String.format("%3d",  cost));
            }
        }
    }
    static class SupplyNode extends Node {
        SupplyNode(NodeMaster nm,int amount)
        {
            super(nm);
            if (amount < 0) throw new RuntimeException("amount must be non negative");
            this.supply=amount;
        }

    }
    static class DemandNode extends Node {
        DemandNode(NodeMaster nm,int amount)
        {
            super(nm);
            if (amount < 0) throw new RuntimeException("amount must be non negative");
            this.supply=-amount;

        }
    }
    public static void setup(NodeMaster n)
    {
        Node s = n.addSupplyNode(20);
        Node n1 = n.addNode();
        Node n2 = n.addNode();
        s
                .addEdgeTo(n1,15,4)
                .addEdgeTo(n2,8,4);
        Node n3 = n.addDemandNode(5);
        Node n4 = n.addDemandNode(15);
        n2
                .addEdgeTo(n3,15,1)
                .addEdgeTo(n4, 4,3);
        n4.addEdgeTo(n2,5,3);
        n1.addEdgeTo(n2, 20,2)
          .addEdgeTo(n4,10,6)
          .addEdgeTo(n3,4,2);
        n3.addEdgeTo(n4,20,2);

    }
    public static void main(String[] args) throws Exception {
        MinCostFlow minCostFlow = new MinCostFlow();
        NodeMaster n =new NodeMaster();
        setup(n);
        n.prepare(minCostFlow);
        MinCostFlow.Status solveStatus = minCostFlow.solve();
        if (solveStatus == MinCostFlow.Status.OPTIMAL)
        {
            n.printout(minCostFlow);
        }
        else
        {
            System.out.println("Solving the min cost flow problem failed. Solver status: " +
                    solveStatus);
        }
    }


}
