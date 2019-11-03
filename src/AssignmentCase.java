import com.google.ortools.graph.MinCostFlow;
import com.google.ortools.graph.MinCostFlowBase;

public class AssignmentCase {
    static { System.loadLibrary("jniortools"); }
    public static void main(String args[])
    {
        MinCost.NodeMaster nm = new MinCost.NodeMaster();
        MinCost.Node root = nm.addSupplyNode(4);

        MinCost.Node w1 = nm.addNode(2000);
        MinCost.Node w2 = nm.addNode(2000);
        MinCost.Node w3 = nm.addNode(2000);
        MinCost.Node w4 = nm.addNode(2000);
        root    .addEdgeTo(w1,1,0)
                .addEdgeTo(w2,1,0)
                .addEdgeTo(w3,1,0)
                .addEdgeTo(w4,1,0);
        MinCost.Node t5 = nm.addNode(3000);
        MinCost.Node t6 = nm.addNode(3000);
        MinCost.Node t7 = nm.addNode(3000);
        MinCost.Node t8 = nm.addNode(3000);
        w1
                .addEdgeTo(t5,1,50)
                .addEdgeTo(t6,1,76)
                .addEdgeTo(t7,1,75)
                .addEdgeTo(t8,1,70);
        w2
                .addEdgeTo(t5,1,35)
                .addEdgeTo(t6,1,85)
                .addEdgeTo(t7,1,55)
                .addEdgeTo(t8,1,65);
        w3
                .addEdgeTo(t5,1,125)
                .addEdgeTo(t6,1,95)
                .addEdgeTo(t7,1,90)
                .addEdgeTo(t8,1,105);
        w4
                .addEdgeTo(t5,1,45)
                .addEdgeTo(t6,1,110)
                .addEdgeTo(t7,1,95)
                .addEdgeTo(t8,1,115);
        MinCost.Node sink = nm.addDemandNode(4);
        t5.addEdgeTo(sink,1,0);
        t6.addEdgeTo(sink,1,0);
        t7.addEdgeTo(sink,1,0);
        t8.addEdgeTo(sink,1,0);
        MinCostFlow minCostFlow = new MinCostFlow();
        nm.prepare(minCostFlow);
        MinCostFlowBase.Status status = minCostFlow.solve();
        if (status == MinCostFlow.Status.OPTIMAL)
            nm.printout(minCostFlow);
        else
            System.out.println("Failed : " + status.toString());

    }
}
