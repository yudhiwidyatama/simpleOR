import com.google.ortools.graph.MinCostFlow;
import com.google.ortools.graph.MinCostFlowBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class TeamAssignmentCase {
    static { System.loadLibrary("jniortools"); }
    static class Position
    {
        String positionName;
        Job job;
        MinCost.Node refNode;
        Position(String name,Job job)
        {
            this.job = job;
            this.positionName = name;
        }

        public void setNode(MinCost.Node n) {
            this.refNode = n;
        }
    }
    static class Job
    {
        ArrayList<Requirement> requirements = new ArrayList<>();
        Job()
        {

        }
        Job addReq(String cc,float level)
        {
            String inCc = cc.intern();
            requirements.add(new Requirement(inCc,level));
            return this;
        }
    }
    static class Requirement {
        String competencyCode;
        float level;
        static long Penalty = 400; // 4 points
        Requirement(String cc, float level) {
            this.competencyCode = cc;
            this.level = level;
        }
    }
    static class Qualification{
        String competencyCode;
        float level;

        public Qualification(String cc, float level) {
            this.competencyCode = cc;
            this.level = level;
        }
    }
    static class Person
    {
        MinCost.Node refNode;
        String fullName;
        HashMap<String,Qualification> qualifications = new HashMap<String,Qualification>();
        Person(String fullname) { this.fullName = fullname; }
        public Person addQual(String cc, float level) {
            String inCc = cc.intern();
            qualifications.put(inCc,new Qualification(inCc,level));
            return this;
        }
        long calculateGap(Position p)
        {
            return calculateGap(p.job);
        }
        long calculateGap(Job j)
        {
            long totalGap = 0;
            for (Requirement requirement : j.requirements)
            {
                String cc = requirement.competencyCode;
                long currGap = 0;
                if (qualifications.containsKey(cc))
                {
                    float gap = -100 * (qualifications.get(cc).level - requirement.level);
                    currGap = (long)gap;
                    totalGap = currGap + totalGap;
                } else
                    totalGap = totalGap + (long)(100 * requirement.level);
            }
            return totalGap / (j.requirements.size());
        }

        public void setNode(MinCost.Node n) {
            this.refNode = n;
        }
    }
    List<Person> persons = new ArrayList<Person>();
    List<Position> positions = new ArrayList<Position>();
    public void prepPersons()
    {

        persons.add(new Person("jack")
                .addQual("LEAD", 3.1f)
                .addQual("PROG", 2.0f)
                .addQual("DSGN", 0.5f)
        );

        persons.add(new Person("jill")
                .addQual("LEAD", 0.5f)
                .addQual("PROG", 1.1f)
                .addQual("DSGN", 0.8f)
        );
        persons.add(new Person("jim")
                .addQual("LEAD", 0.5f)
                .addQual("PROG", 2.1f)
                .addQual("DSGN", 0.5f)
        );
        persons.add(new Person("josh")
                .addQual("LEAD", 0.5f)
                .addQual("PROG", 2.3f)
                .addQual("DSGN", 1.5f)
        );
        persons.add(new Person("jerry")
                .addQual("LEAD", 2.5f)
                .addQual("PROG", 1.3f)
                .addQual("DSGN", 2.0f)
        );
        persons.add(new Person("julia")
                .addQual("LEAD", 0.5f)
                .addQual("PROG", 2.3f)
                .addQual("DSGN", 1.5f)
        );

    }
    public void prepPositions()
    {
        Job pgm = new Job().addReq("PROG",3.0f);
        Job pm = new Job().addReq( "LEAD", 3.0f);
        Job analyst = new Job().addReq( "DSGN", 3.0f);
        positions.add(new Position("PM-T1",pm));
        positions.add(new Position("PGM-T1",pgm));
        positions.add(new Position("ANA-T1",analyst));
        positions.add(new Position("PM-T2",pm));
        positions.add(new Position("PGMA-T2",pgm));
        positions.add(new Position("PGMB-T2",pgm));
    }
    HashMap<Integer,String> nameMap = new HashMap<Integer, String>();
    public MinCost.NodeMaster prepare(MinCostFlow minCostFlow)
    {
        MinCost.NodeMaster nm = new MinCost.NodeMaster();
        persons.forEach(person -> {
                    person.setNode(nm.addNode(10000));
                    nameMap.put(person.refNode.id, person.fullName);
        });
        var root = nm.addSupplyNode(positions.size());
        var sink = nm.addDemandNode(positions.size());
        persons.forEach(person ->
        {
           root.addEdgeTo(person.refNode,1,0);
           // each person can be assigned once or not at all
        });
        positions.forEach(position -> {
            position.setNode(nm.addNode(20000));
            position.refNode.addEdgeTo(sink,1,0);
            nameMap.put(position.refNode.id, position.positionName);
        });
        persons.forEach(person ->
        {
            positions.forEach( position -> {
                long cost = person.calculateGap(position);
                person.refNode.addEdgeTo(position.refNode,1,cost);
            });
            // each person can be assigned once or not at all
        });
        nm.prepare(minCostFlow);
        return nm;
    }
    public static void main(String args[])
    {
        var me = new TeamAssignmentCase();
        me.prepPersons();
        me.prepPositions();
        var minCostFlow = new MinCostFlow();
        var nm =  me.prepare(minCostFlow);
        var status = minCostFlow.solve();
        if (status == MinCostFlow.Status.OPTIMAL)
            nm.printoutEx(minCostFlow,me.nameMap);
        else
            System.out.println("Failed : " + status.toString());

    }
}
