import com.google.ortools.graph.MinCostFlow;
import com.google.ortools.graph.MinCostFlowBase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

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
        String jobname;
        ArrayList<Requirement> requirements = new ArrayList<>();
        Job()
        {

        }
        Job(String jobname) { this.jobname = jobname;}
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
                    if (gap<0.0f) gap=0.0f;
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
    public void prepPersonsFromFileEx(String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new BufferedReader(new FileReader(filename)));
        int personsCnt;
        personsCnt=in.nextInt();
        for (int i=0; i<personsCnt; i++)
        {
            String personUid = in.next();
            int personQuals = in.nextInt();
            Person aPerson = new Person(personUid);
            for (int j=0; j<personQuals; j++)
            {
                String competencyId = in.next();
                int competencyLevel = in.nextInt();
                aPerson.addQual(competencyId, competencyLevel);
            }
            persons.add(aPerson);
        }
    }
    public void prepPersonsFromFile(String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new BufferedReader(new FileReader(filename)));
        int personsCnt;
        personsCnt=in.nextInt();
        for (int i=0; i<personsCnt; i++)
        {
            int personUid = in.nextInt();
            int personQuals = in.nextInt();
            Person aPerson = new Person("U" + personUid);
            for (int j=0; j<personQuals; j++)
            {
                int competencyId = in.nextInt();
                int competencyLevel = in.nextInt();
                aPerson.addQual("C" + competencyId, competencyLevel);
            }
            persons.add(aPerson);
        }
    }
    public void prepPersons()
    {
        persons.add(new Person("HAGANTA BREMA BANGUN").addQual("SC003",3).addQual("SC017",2).addQual("SC004",4).addQual("SC005",2).addQual("SC016",2).addQual("SC007",2).addQual("SC008",3).addQual("SC011",1).addQual("SC009",2).addQual("SC020",3).addQual("SC012",2).addQual("SC013",1).addQual("SC025",3).addQual("SC014",2).addQual("SC018",1).addQual("SC019",3).addQual("SC030",2).addQual("SC033",1).addQual("SC035",2).addQual("HC001",1).addQual("HC003",1).addQual("HC008",1).addQual("HC009",2).addQual("HC019",1).addQual("HC005",2).addQual("HC010",2).addQual("HC018",2));
        persons.add(new Person("AULIA NURI IKHSANATI").addQual("SC003",2).addQual("SC017",2).addQual("SC004",2).addQual("SC005",3).addQual("SC016",2).addQual("SC007",3).addQual("SC008",3).addQual("SC011",2).addQual("SC009",1).addQual("SC020",3).addQual("SC012",3).addQual("SC013",2).addQual("SC025",3).addQual("SC014",3).addQual("SC018",2).addQual("SC019",2).addQual("SC030",3).addQual("SC033",3).addQual("SC035",2).addQual("HC001",2).addQual("HC003",2).addQual("HC008",3).addQual("HC009",2).addQual("HC019",1).addQual("HC005",4).addQual("HC010",3).addQual("HC018",2));
        persons.add(new Person("HANIF RAMADHAN ABDILLAH").addQual("SC003",2).addQual("SC017",3).addQual("SC004",3).addQual("SC005",3).addQual("SC016",2).addQual("SC007",2).addQual("SC008",2).addQual("SC011",3).addQual("SC009",2).addQual("SC020",3).addQual("SC012",3).addQual("SC013",3).addQual("SC025",3).addQual("SC014",2).addQual("SC018",2).addQual("SC019",3).addQual("SC030",3).addQual("SC033",2).addQual("SC035",3).addQual("HC001",3).addQual("HC003",3).addQual("HC008",2).addQual("HC009",3).addQual("HC019",2).addQual("HC005",3).addQual("HC010",3).addQual("HC018",2));
        persons.add(new Person("RIZKA FADHILAH").addQual("SC003",1).addQual("SC017",3).addQual("SC004",2).addQual("SC005",4).addQual("SC016",1).addQual("SC007",4).addQual("SC008",3).addQual("SC011",3).addQual("SC009",2).addQual("SC020",3).addQual("SC012",2).addQual("SC013",3).addQual("SC025",4).addQual("SC014",2).addQual("SC018",3).addQual("SC019",3).addQual("SC030",3).addQual("SC033",3).addQual("SC035",2).addQual("HC001",3).addQual("HC003",2).addQual("HC008",3).addQual("HC009",1).addQual("HC019",2).addQual("HC005",1).addQual("HC010",2).addQual("HC018",3));
        persons.add(new Person("ADITYO SATRIO BAGASKORO").addQual("SC003",3).addQual("SC017",3).addQual("SC004",3).addQual("SC005",2).addQual("SC016",2).addQual("SC007",3).addQual("SC008",3).addQual("SC011",2).addQual("SC009",2).addQual("SC020",3).addQual("SC012",2).addQual("SC013",2).addQual("SC025",2).addQual("SC014",1).addQual("SC018",2).addQual("SC019",3).addQual("SC030",3).addQual("SC033",2).addQual("SC035",1).addQual("HC001",2).addQual("HC003",2).addQual("HC008",2).addQual("HC009",2).addQual("HC019",2).addQual("HC005",2).addQual("HC010",2).addQual("HC018",2));
        persons.add(new Person("MUHAMMAD RAFIDAN").addQual("SC003",3).addQual("SC017",3).addQual("SC004",3).addQual("SC005",3).addQual("SC016",2).addQual("SC007",3).addQual("SC008",3).addQual("SC011",2).addQual("SC009",2).addQual("SC020",3).addQual("SC012",3).addQual("SC013",2).addQual("SC025",3).addQual("SC014",2).addQual("SC018",2).addQual("SC019",3).addQual("SC030",3).addQual("SC033",3).addQual("SC035",3).addQual("HC001",3).addQual("HC003",3).addQual("HC008",2).addQual("HC009",3).addQual("HC019",3).addQual("HC005",3).addQual("HC010",3).addQual("HC018",2));
        persons.add(new Person("RINALDY JABAR SETIA").addQual("SC003",3).addQual("SC017",4).addQual("SC004",3).addQual("SC005",3).addQual("SC016",3).addQual("SC007",3).addQual("SC008",3).addQual("SC011",3).addQual("SC009",4).addQual("SC020",3).addQual("SC012",2).addQual("SC013",2).addQual("SC025",3).addQual("SC014",1).addQual("SC018",2).addQual("SC019",3).addQual("SC030",2).addQual("SC033",2).addQual("SC035",3).addQual("HC001",2).addQual("HC003",1).addQual("HC008",3).addQual("HC009",1).addQual("HC019",1).addQual("HC005",2).addQual("HC010",2).addQual("HC018",3));
        persons.add(new Person("APRILIA ARI WIDYASTUTI").addQual("SC003",2).addQual("SC017",3).addQual("SC004",1).addQual("SC005",2).addQual("SC016",3).addQual("SC007",3).addQual("SC008",4).addQual("SC011",2).addQual("SC009",3).addQual("SC020",2).addQual("SC012",3).addQual("SC013",3).addQual("SC025",3).addQual("SC014",2).addQual("SC018",3).addQual("SC019",3).addQual("SC030",2).addQual("SC033",2).addQual("SC035",2).addQual("HC001",3).addQual("HC003",3).addQual("HC008",3).addQual("HC009",2).addQual("HC019",2).addQual("HC005",2).addQual("HC010",4).addQual("HC018",2));
        persons.add(new Person("ANISYA ANGGITA FEBRYANTI").addQual("SC003",2).addQual("SC017",2).addQual("SC004",3).addQual("SC005",3).addQual("SC016",2).addQual("SC007",2).addQual("SC008",3).addQual("SC011",2).addQual("SC009",3).addQual("SC020",2).addQual("SC012",3).addQual("SC013",3).addQual("SC025",3).addQual("SC014",2).addQual("SC018",2).addQual("SC019",3).addQual("SC030",2).addQual("SC033",3).addQual("SC035",3).addQual("HC001",3).addQual("HC003",1).addQual("HC008",2).addQual("HC009",2).addQual("HC019",2).addQual("HC005",3).addQual("HC010",2).addQual("HC018",3));
        persons.add(new Person("SEVIRA HENDARI PRATIWI").addQual("SC003",1).addQual("SC017",3).addQual("SC004",2).addQual("SC005",3).addQual("SC016",2).addQual("SC007",3).addQual("SC008",4).addQual("SC011",2).addQual("SC009",2).addQual("SC020",3).addQual("SC012",3).addQual("SC013",2).addQual("SC025",3).addQual("SC014",1).addQual("SC018",2).addQual("SC019",3).addQual("SC030",2).addQual("SC033",1).addQual("SC035",1).addQual("HC001",1).addQual("HC003",1).addQual("HC008",3).addQual("HC009",2).addQual("HC019",1).addQual("HC005",2).addQual("HC010",1).addQual("HC018",1));
        persons.add(new Person("DANTI PUJI YUHERISNA").addQual("SC003",2).addQual("SC017",2).addQual("SC004",2).addQual("SC005",2).addQual("SC016",1).addQual("SC007",3).addQual("SC008",3).addQual("SC011",2).addQual("SC009",2).addQual("SC020",3).addQual("SC012",2).addQual("SC013",2).addQual("SC025",3).addQual("SC014",3).addQual("SC018",2).addQual("SC019",2).addQual("SC030",1).addQual("SC033",1).addQual("SC035",2).addQual("HC001",2).addQual("HC003",1).addQual("HC008",2).addQual("HC009",2).addQual("HC019",1).addQual("HC005",2).addQual("HC010",2).addQual("HC018",2));
        persons.add(new Person("AZOLLA DHIGO PRASETYA").addQual("SC003",3).addQual("SC017",2).addQual("SC004",3).addQual("SC005",3).addQual("SC016",2).addQual("SC007",3).addQual("SC008",3).addQual("SC011",3).addQual("SC009",2).addQual("SC020",3).addQual("SC012",3).addQual("SC013",3).addQual("SC025",3).addQual("SC014",2).addQual("SC018",3).addQual("SC019",3).addQual("SC030",3).addQual("SC033",2).addQual("SC035",2).addQual("HC001",3).addQual("HC003",3).addQual("HC008",2).addQual("HC009",3).addQual("HC019",3).addQual("HC005",4).addQual("HC010",4).addQual("HC018",3));
        persons.add(new Person("DENNIS NIGEL CUNONG").addQual("SC003",2).addQual("SC017",3).addQual("SC004",2).addQual("SC005",3).addQual("SC016",3).addQual("SC007",3).addQual("SC008",3).addQual("SC011",2).addQual("SC009",3).addQual("SC020",3).addQual("SC012",3).addQual("SC013",3).addQual("SC025",3).addQual("SC014",2).addQual("SC018",3).addQual("SC019",2).addQual("SC030",1).addQual("SC033",3).addQual("SC035",3).addQual("HC001",3).addQual("HC003",1).addQual("HC008",3).addQual("HC009",1).addQual("HC019",1).addQual("HC005",2).addQual("HC010",4).addQual("HC018",2));
        persons.add(new Person("FAHRI HAMZALAH").addQual("SC003",3).addQual("SC017",3).addQual("SC004",2).addQual("SC005",2).addQual("SC016",3).addQual("SC007",3).addQual("SC008",2).addQual("SC011",3).addQual("SC009",3).addQual("SC020",2).addQual("SC012",3).addQual("SC013",3).addQual("SC025",3).addQual("SC014",2).addQual("SC018",3).addQual("SC019",3).addQual("SC030",2).addQual("SC033",2).addQual("SC035",2).addQual("HC001",4).addQual("HC003",2).addQual("HC008",3).addQual("HC009",2).addQual("HC019",1).addQual("HC005",4).addQual("HC010",2).addQual("HC018",3));
        persons.add(new Person("ARIEF RYAN RISBAYA").addQual("SC003",1).addQual("SC017",2).addQual("SC004",1).addQual("SC005",2).addQual("SC016",2).addQual("SC007",2).addQual("SC008",2).addQual("SC011",3).addQual("SC009",2).addQual("SC020",2).addQual("SC012",2).addQual("SC013",2).addQual("SC025",3).addQual("SC014",2).addQual("SC018",2).addQual("SC019",3).addQual("SC030",2).addQual("SC033",2).addQual("SC035",3).addQual("HC001",2).addQual("HC003",2).addQual("HC008",2).addQual("HC009",2).addQual("HC019",2).addQual("HC005",3).addQual("HC010",2).addQual("HC018",2));
        persons.add(new Person("FIRDHA UTAMI").addQual("SC003",2).addQual("SC017",3).addQual("SC004",1).addQual("SC005",3).addQual("SC016",3).addQual("SC007",2).addQual("SC008",3).addQual("SC011",2).addQual("SC009",3).addQual("SC020",3).addQual("SC012",3).addQual("SC013",2).addQual("SC025",3).addQual("SC014",1).addQual("SC018",2).addQual("SC019",2).addQual("SC030",2).addQual("SC033",1).addQual("SC035",2).addQual("HC001",2).addQual("HC003",1).addQual("HC008",2).addQual("HC009",1).addQual("HC019",2).addQual("HC005",1).addQual("HC010",2).addQual("HC018",2));
        persons.add(new Person("IAN DZILLAN MALIK").addQual("SC003",3).addQual("SC017",3).addQual("SC004",5).addQual("SC005",2).addQual("SC016",2).addQual("SC007",3).addQual("SC008",3).addQual("SC011",2).addQual("SC009",2).addQual("SC020",3).addQual("SC012",1).addQual("SC013",2).addQual("SC025",3).addQual("SC014",3).addQual("SC018",2).addQual("SC019",2).addQual("SC030",2).addQual("SC033",1).addQual("SC035",3).addQual("HC001",3).addQual("HC003",3).addQual("HC008",2).addQual("HC009",3).addQual("HC019",2).addQual("HC005",2).addQual("HC010",2).addQual("HC018",2));
        persons.add(new Person("MUHAMMAD FARHAN ZUHDY").addQual("SC003",1).addQual("SC017",3).addQual("SC004",2).addQual("SC005",2).addQual("SC016",2).addQual("SC007",2).addQual("SC008",3).addQual("SC011",2).addQual("SC009",2).addQual("SC020",3).addQual("SC012",3).addQual("SC013",2).addQual("SC025",3).addQual("SC014",2).addQual("SC018",3).addQual("SC019",2).addQual("SC030",2).addQual("SC033",2).addQual("SC035",2).addQual("HC001",2).addQual("HC003",2).addQual("HC008",2).addQual("HC009",2).addQual("HC019",2).addQual("HC005",2).addQual("HC010",2).addQual("HC018",2));
        persons.add(new Person("M LUTHFI RIDHWAN").addQual("SC003",2).addQual("SC017",3).addQual("SC004",3).addQual("SC005",4).addQual("SC016",4).addQual("SC007",4).addQual("SC008",4).addQual("SC011",4).addQual("SC009",3).addQual("SC020",4).addQual("SC012",3).addQual("SC013",4).addQual("SC025",4).addQual("SC014",2).addQual("SC018",3).addQual("SC019",4).addQual("SC030",4).addQual("SC033",2).addQual("SC035",4).addQual("HC001",3).addQual("HC003",3).addQual("HC008",3).addQual("HC009",3).addQual("HC019",3).addQual("HC005",4).addQual("HC010",4).addQual("HC018",3));
        persons.add(new Person("ANISA AGUSTINA").addQual("SC003",2).addQual("SC017",3).addQual("SC004",2).addQual("SC005",2).addQual("SC016",2).addQual("SC007",2).addQual("SC008",2).addQual("SC011",2).addQual("SC009",2).addQual("SC020",2).addQual("SC012",2).addQual("SC013",3).addQual("SC025",3).addQual("SC014",2).addQual("SC018",2).addQual("SC019",3).addQual("SC030",3).addQual("SC033",2).addQual("SC035",2).addQual("HC001",3).addQual("HC003",1).addQual("HC008",2).addQual("HC009",2).addQual("HC019",2).addQual("HC005",2).addQual("HC010",2).addQual("HC018",2));
        persons.add(new Person("ICHWAN HABIBIE").addQual("SC003",2).addQual("SC017",3).addQual("SC004",3).addQual("SC005",3).addQual("SC016",3).addQual("SC007",3).addQual("SC008",3).addQual("SC011",2).addQual("SC009",3).addQual("SC020",2).addQual("SC012",3).addQual("SC013",3).addQual("SC025",3).addQual("SC014",2).addQual("SC018",2).addQual("SC019",2).addQual("SC030",3).addQual("SC033",2).addQual("SC035",2).addQual("HC001",2).addQual("HC003",3).addQual("HC008",2).addQual("HC009",2).addQual("HC019",2).addQual("HC005",2).addQual("HC010",3).addQual("HC018",3));
        persons.add(new Person("ZENITHO MADYAGANTANG HAKIKI").addQual("SC003",2).addQual("SC017",3).addQual("SC004",2).addQual("SC005",2).addQual("SC016",3).addQual("SC007",2).addQual("SC008",3).addQual("SC011",2).addQual("SC009",3).addQual("SC020",3).addQual("SC012",4).addQual("SC013",3).addQual("SC025",4).addQual("SC014",3).addQual("SC018",3).addQual("SC019",3).addQual("SC030",3).addQual("SC033",3).addQual("SC035",3).addQual("HC001",2).addQual("HC003",2).addQual("HC008",3).addQual("HC009",3).addQual("HC019",1).addQual("HC005",3).addQual("HC010",5).addQual("HC018",3));
        persons.add(new Person("TUBAGUS RAHMAN RAMADAN").addQual("SC003",2).addQual("SC017",3).addQual("SC004",3).addQual("SC005",3).addQual("SC016",3).addQual("SC007",3).addQual("SC008",2).addQual("SC011",3).addQual("SC009",2).addQual("SC020",3).addQual("SC012",3).addQual("SC013",3).addQual("SC025",4).addQual("SC014",2).addQual("SC018",2).addQual("SC019",3).addQual("SC030",3).addQual("SC033",2).addQual("SC035",3).addQual("HC001",3).addQual("HC003",2).addQual("HC008",3).addQual("HC009",2).addQual("HC019",2).addQual("HC005",2).addQual("HC010",2).addQual("HC018",2));
        persons.add(new Person("RAHKMAH NABILA").addQual("SC003",3).addQual("SC017",3).addQual("SC004",3).addQual("SC005",3).addQual("SC016",4).addQual("SC007",3).addQual("SC008",3).addQual("SC011",3).addQual("SC009",2).addQual("SC020",3).addQual("SC012",3).addQual("SC013",2).addQual("SC025",3).addQual("SC014",1).addQual("SC018",3).addQual("SC019",3).addQual("SC030",2).addQual("SC033",2).addQual("SC035",3).addQual("HC001",3).addQual("HC003",1).addQual("HC008",2).addQual("HC009",2).addQual("HC019",1).addQual("HC005",3).addQual("HC010",3).addQual("HC018",3));
        persons.add(new Person("ALIF JAFAR FATKHURROHMAN").addQual("SC003",3).addQual("SC017",3).addQual("SC004",3).addQual("SC005",3).addQual("SC016",3).addQual("SC007",2).addQual("SC008",3).addQual("SC011",2).addQual("SC009",3).addQual("SC020",3).addQual("SC012",2).addQual("SC013",2).addQual("SC025",3).addQual("SC014",3).addQual("SC018",2).addQual("SC019",2).addQual("SC030",2).addQual("SC033",2).addQual("SC035",2).addQual("HC001",2).addQual("HC003",4).addQual("HC008",2).addQual("HC009",3).addQual("HC019",1).addQual("HC005",3).addQual("HC010",3).addQual("HC018",2));
        persons.add(new Person("GANAR NUGRAHA MAHENDRASTA").addQual("SC003",2).addQual("SC017",2).addQual("SC004",2).addQual("SC005",2).addQual("SC016",2).addQual("SC007",2).addQual("SC008",2).addQual("SC011",2).addQual("SC009",2).addQual("SC020",3).addQual("SC012",3).addQual("SC013",2).addQual("SC025",3).addQual("SC014",2).addQual("SC018",2).addQual("SC019",3).addQual("SC030",2).addQual("SC033",2).addQual("SC035",2).addQual("HC001",2).addQual("HC003",2).addQual("HC008",2).addQual("HC009",2).addQual("HC019",2).addQual("HC005",2).addQual("HC010",2).addQual("HC018",2));
        persons.add(new Person("INDAH NUR FITRI ASTUTI").addQual("SC003",1).addQual("SC017",3).addQual("SC004",2).addQual("SC005",3).addQual("SC016",3).addQual("SC007",2).addQual("SC008",3).addQual("SC011",3).addQual("SC009",2).addQual("SC020",3).addQual("SC012",2).addQual("SC013",3).addQual("SC025",3).addQual("SC014",3).addQual("SC018",2).addQual("SC019",3).addQual("SC030",3).addQual("SC033",3).addQual("SC035",3).addQual("HC001",2).addQual("HC003",2).addQual("HC008",3).addQual("HC009",2).addQual("HC019",2).addQual("HC005",2).addQual("HC010",3).addQual("HC018",3));
        persons.add(new Person("VERA ADELIA").addQual("SC003",1).addQual("SC017",3).addQual("SC004",2).addQual("SC005",2).addQual("SC016",3).addQual("SC007",3).addQual("SC008",3).addQual("SC011",2).addQual("SC009",3).addQual("SC020",3).addQual("SC012",3).addQual("SC013",3).addQual("SC025",3).addQual("SC014",1).addQual("SC018",2).addQual("SC019",3).addQual("SC030",3).addQual("SC033",1).addQual("SC035",2).addQual("HC001",2).addQual("HC003",1).addQual("HC008",2).addQual("HC009",2).addQual("HC019",1).addQual("HC005",2).addQual("HC010",1).addQual("HC018",2));
        persons.add(new Person("KURNIA DWI WIRANTI RAHAYU").addQual("SC003",3).addQual("SC017",4).addQual("SC004",3).addQual("SC005",4).addQual("SC016",3).addQual("SC007",3).addQual("SC008",3).addQual("SC011",2).addQual("SC009",4).addQual("SC020",3).addQual("SC012",3).addQual("SC013",3).addQual("SC025",3).addQual("SC014",2).addQual("SC018",2).addQual("SC019",3).addQual("SC030",3).addQual("SC033",2).addQual("SC035",2).addQual("HC001",3).addQual("HC003",2).addQual("HC008",2).addQual("HC009",2).addQual("HC019",2).addQual("HC005",3).addQual("HC010",2).addQual("HC018",3));
        persons.add(new Person("RIZKY RAHMAT HAKIKI").addQual("SC003",2).addQual("SC017",4).addQual("SC004",3).addQual("SC005",4).addQual("SC016",3).addQual("SC007",3).addQual("SC008",3).addQual("SC011",4).addQual("SC009",4).addQual("SC020",4).addQual("SC012",4).addQual("SC013",3).addQual("SC025",4).addQual("SC014",2).addQual("SC018",4).addQual("SC019",4).addQual("SC030",3).addQual("SC033",3).addQual("SC035",3).addQual("HC001",3).addQual("HC003",2).addQual("HC008",3).addQual("HC009",2).addQual("HC019",1).addQual("HC005",3).addQual("HC010",3).addQual("HC018",3));
        persons.add(new Person("GHALIB MAHENDRA").addQual("SC003",2).addQual("SC017",2).addQual("SC004",3).addQual("SC005",2).addQual("SC016",3).addQual("SC007",2).addQual("SC008",3).addQual("SC011",3).addQual("SC009",2).addQual("SC020",4).addQual("SC012",4).addQual("SC013",2).addQual("SC025",2).addQual("SC014",3).addQual("SC018",1).addQual("SC019",3).addQual("SC030",2).addQual("SC033",2).addQual("SC035",2).addQual("HC001",2).addQual("HC003",1).addQual("HC008",2).addQual("HC009",2).addQual("HC019",2).addQual("HC005",2).addQual("HC010",2).addQual("HC018",3));
        persons.add(new Person("AYUVIRA KUSUMA MULADI").addQual("SC003",1).addQual("SC017",3).addQual("SC004",2).addQual("SC005",2).addQual("SC016",3).addQual("SC007",2).addQual("SC008",2).addQual("SC011",4).addQual("SC009",4).addQual("SC020",3).addQual("SC012",2).addQual("SC013",2).addQual("SC025",2).addQual("SC014",2).addQual("SC018",2).addQual("SC019",3).addQual("SC030",3).addQual("SC033",1).addQual("SC035",1).addQual("HC001",2).addQual("HC003",2).addQual("HC008",3).addQual("HC009",1).addQual("HC019",2).addQual("HC005",2).addQual("HC010",1).addQual("HC018",2));
        persons.add(new Person("FERNANDO REDONDO").addQual("SC003",1).addQual("SC017",3).addQual("SC004",2).addQual("SC005",3).addQual("SC016",2).addQual("SC007",3).addQual("SC008",2).addQual("SC011",2).addQual("SC009",2).addQual("SC020",3).addQual("SC012",2).addQual("SC013",2).addQual("SC025",3).addQual("SC014",2).addQual("SC018",2).addQual("SC019",3).addQual("SC030",2).addQual("SC033",3).addQual("SC035",3).addQual("HC001",3).addQual("HC003",2).addQual("HC008",2).addQual("HC009",2).addQual("HC019",3).addQual("HC005",2).addQual("HC010",3).addQual("HC018",2));
        persons.add(new Person("ATIKA RAHMAWATI HUSNA").addQual("SC003",1).addQual("SC017",2).addQual("SC004",1).addQual("SC005",2).addQual("SC016",1).addQual("SC007",2).addQual("SC008",2).addQual("SC011",1).addQual("SC009",2).addQual("SC020",3).addQual("SC012",2).addQual("SC013",2).addQual("SC025",3).addQual("SC014",1).addQual("SC018",2).addQual("SC019",2).addQual("SC030",2).addQual("SC033",1).addQual("SC035",2).addQual("HC001",1).addQual("HC003",1).addQual("HC008",1).addQual("HC009",1).addQual("HC019",1).addQual("HC005",2).addQual("HC010",1).addQual("HC018",1));
        persons.add(new Person("JANNATUL RAHMADIANI").addQual("SC003",1).addQual("SC017",4).addQual("SC004",3).addQual("SC005",2).addQual("SC016",2).addQual("SC007",3).addQual("SC008",4).addQual("SC011",3).addQual("SC009",3).addQual("SC020",3).addQual("SC012",2).addQual("SC013",2).addQual("SC025",3).addQual("SC014",3).addQual("SC018",3).addQual("SC019",2).addQual("SC030",2).addQual("SC033",2).addQual("SC035",1).addQual("HC001",2).addQual("HC003",2).addQual("HC008",2).addQual("HC009",3).addQual("HC019",2).addQual("HC005",3).addQual("HC010",3).addQual("HC018",3));
        persons.add(new Person("BEN HUGHIE REZANDA").addQual("SC003",2).addQual("SC017",4).addQual("SC004",3).addQual("SC005",3).addQual("SC016",2).addQual("SC007",3).addQual("SC008",3).addQual("SC011",2).addQual("SC009",3).addQual("SC020",3).addQual("SC012",3).addQual("SC013",3).addQual("SC025",4).addQual("SC014",3).addQual("SC018",2).addQual("SC019",4).addQual("SC030",3).addQual("SC033",2).addQual("SC035",2).addQual("HC001",3).addQual("HC003",1).addQual("HC008",2).addQual("HC009",2).addQual("HC019",2).addQual("HC005",2).addQual("HC010",2).addQual("HC018",3));
        persons.add(new Person("AVISA GAVRILLA").addQual("SC003",3).addQual("SC017",3).addQual("SC004",3).addQual("SC005",3).addQual("SC016",3).addQual("SC007",3).addQual("SC008",3).addQual("SC011",3).addQual("SC009",2).addQual("SC020",3).addQual("SC012",3).addQual("SC013",2).addQual("SC025",3).addQual("SC014",3).addQual("SC018",2).addQual("SC019",4).addQual("SC030",3).addQual("SC033",2).addQual("SC035",3).addQual("HC001",1).addQual("HC003",1).addQual("HC008",2).addQual("HC009",2).addQual("HC019",2).addQual("HC005",2).addQual("HC010",2).addQual("HC018",3));
        persons.add(new Person("MUHAMMAD FIKRI MAKMUR").addQual("SC003",1).addQual("SC017",4).addQual("SC004",4).addQual("SC005",3).addQual("SC016",2).addQual("SC007",3).addQual("SC008",3).addQual("SC011",3).addQual("SC009",4).addQual("SC020",3).addQual("SC012",3).addQual("SC013",3).addQual("SC025",4).addQual("SC014",4).addQual("SC018",3).addQual("SC019",3).addQual("SC030",2).addQual("SC033",3).addQual("SC035",2).addQual("HC001",2).addQual("HC003",2).addQual("HC008",3).addQual("HC009",3).addQual("HC019",3).addQual("HC005",3).addQual("HC010",3).addQual("HC018",3));
        persons.add(new Person("ILHAM SATRIA AJI PRATAMA").addQual("SC003",2).addQual("SC017",2).addQual("SC004",2).addQual("SC005",3).addQual("SC016",3).addQual("SC007",2).addQual("SC008",2).addQual("SC011",2).addQual("SC009",2).addQual("SC020",3).addQual("SC012",2).addQual("SC013",2).addQual("SC025",3).addQual("SC014",1).addQual("SC018",2).addQual("SC019",2).addQual("SC030",2).addQual("SC033",1).addQual("SC035",2).addQual("HC001",2).addQual("HC003",1).addQual("HC008",2).addQual("HC009",1).addQual("HC019",2).addQual("HC005",2).addQual("HC010",2).addQual("HC018",1));
        persons.add(new Person("MUHAMMAD ISHAK SYUKRI").addQual("SC003",2).addQual("SC017",3).addQual("SC004",3).addQual("SC005",3).addQual("SC016",3).addQual("SC007",3).addQual("SC008",3).addQual("SC011",3).addQual("SC009",3).addQual("SC020",3).addQual("SC012",3).addQual("SC013",3).addQual("SC025",3).addQual("SC014",2).addQual("SC018",2).addQual("SC019",3).addQual("SC030",2).addQual("SC033",2).addQual("SC035",4).addQual("HC001",2).addQual("HC003",2).addQual("HC008",3).addQual("HC009",2).addQual("HC019",2).addQual("HC005",2).addQual("HC010",2).addQual("HC018",2));
        persons.add(new Person("SYLVIA").addQual("SC003",2).addQual("SC017",3).addQual("SC004",3).addQual("SC005",2).addQual("SC016",3).addQual("SC007",3).addQual("SC008",4).addQual("SC011",3).addQual("SC009",3).addQual("SC020",3).addQual("SC012",3).addQual("SC013",3).addQual("SC025",3).addQual("SC014",3).addQual("SC018",2).addQual("SC019",3).addQual("SC030",3).addQual("SC033",3).addQual("SC035",3).addQual("HC001",2).addQual("HC003",3).addQual("HC008",3).addQual("HC009",3).addQual("HC019",3).addQual("HC005",3).addQual("HC010",3).addQual("HC018",3));
        persons.add(new Person("NAUFAL AL HAKIM").addQual("SC003",2).addQual("SC017",3).addQual("SC004",2).addQual("SC005",2).addQual("SC016",3).addQual("SC007",3).addQual("SC008",4).addQual("SC011",4).addQual("SC009",3).addQual("SC020",4).addQual("SC012",3).addQual("SC013",2).addQual("SC025",3).addQual("SC014",1).addQual("SC018",3).addQual("SC019",3).addQual("SC030",3).addQual("SC033",2).addQual("SC035",3).addQual("HC001",3).addQual("HC003",2).addQual("HC008",2).addQual("HC009",2).addQual("HC019",4).addQual("HC005",2).addQual("HC010",2).addQual("HC018",3));
    }
    public void prepPositionsFromFile(String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new BufferedReader(new FileReader(filename)));
        int jobtargetCount;
        jobtargetCount=in.nextInt();
        for (int i=0; i<jobtargetCount; i++)
        {
            int jobTargetId = in.nextInt();
            int jobTargetReqs = in.nextInt();
            Job aJob = new Job("J"+ jobTargetId);
            for (int j=0; j<jobTargetReqs; j++)
            {
                int competencyId = in.nextInt();
                int competencyLevel = in.nextInt();
                aJob.addReq("C" + competencyId, competencyLevel);
            }
            positions.add(new Position("P"+jobTargetId, aJob));
        }
    }
    public void prepPositionsFromFileEx(String filename, HashMap<String,Job> jobs) throws FileNotFoundException {
        Scanner in = new Scanner(new BufferedReader(new FileReader(filename)));
        int positionCount;
        positionCount=in.nextInt();
        for (int i=0; i<positionCount; i++)
        {
            String positionId = in.next();
            String jobId = in.next();
            positions.add(new Position(positionId,jobs.get(jobId) ));
        }
    }
    public HashMap<String,Job> prepJobsFromFileEx(String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new BufferedReader(new FileReader(filename)));
        int jobtargetCount;
        jobtargetCount=in.nextInt();
        HashMap<String,Job> jobHashMap = new HashMap<String, Job>();
        for (int i=0; i<jobtargetCount; i++)
        {
            String jobId = in.next();
            int jobTargetReqs = in.nextInt();
            Job aJob = new Job(jobId);
            for (int j=0; j<jobTargetReqs; j++)
            {
                String competencyId = in.next();
                int competencyLevel = in.nextInt();
                aJob.addReq(competencyId, competencyLevel);
            }
            jobHashMap.put(jobId,aJob);
        }
        return jobHashMap;
    }



    public void prepPositions()
    {

        Job pm = new Job("Project Manager")
            .addReq("SC008",3)
            .addReq("SC017",3)
            .addReq("SC016",3)
            .addReq("SC004",3)
            .addReq("SC011",3)
            .addReq("SC003",3)
            .addReq("SC005",3)
            .addReq("SC018",3)
            .addReq("SC013",3)
            .addReq("SC009",3)
            .addReq("SC030",1)
            .addReq("SC033",2)
            .addReq("SC007",2)
            .addReq("SC012",3)
            .addReq("SC020",3)
            .addReq("SC019",3)
            .addReq("SC025",4)
            .addReq("SC035",3)
            .addReq("SC014",2)
            .addReq("HC001",3)
            .addReq("HC019",3)
            .addReq("HC009",1)
            .addReq("HC003",4)
            .addReq("HC008",4);
        Job pgm =  new Job("Programmer")
                .addReq("SC008",3)
                .addReq("SC017",4)
                .addReq("SC016",1)
                .addReq("SC004",2)
                .addReq("SC011",1)
                .addReq("SC003",1)
                .addReq("SC005",2)
                .addReq("SC018",1)
                .addReq("SC013",2)
                .addReq("SC009",2)
                .addReq("SC030",2)
                .addReq("SC033",1)
                .addReq("SC007",3)
                .addReq("SC012",3)
                .addReq("SC020",2)
                .addReq("SC019",2)
                .addReq("SC025",3)
                .addReq("SC035",1)
                .addReq("SC014",1)
                .addReq("HC001",1)
                .addReq("HC019",1)
                .addReq("HC009",2)
                .addReq("HC003",3)
                .addReq("HC008",1);
        Job analyst =  new Job("Analyst")
                .addReq("SC008",3)
                .addReq("SC017",3)
                .addReq("SC016",4)
                .addReq("SC004",2)
                .addReq("SC011",2)
                .addReq("SC003",2)
                .addReq("SC005",2)
                .addReq("SC018",2)
                .addReq("SC013",3)
                .addReq("SC009",3)
                .addReq("SC030",1)
                .addReq("SC033",2)
                .addReq("SC007",2)
                .addReq("SC012",2)
                .addReq("SC020",3)
                .addReq("SC019",3)
                .addReq("SC025",4)
                .addReq("SC035",2)
                .addReq("SC014",1)
                .addReq("HC001",2)
                .addReq("HC019",2)
                .addReq("HC009",3)
                .addReq("HC003",3)
                .addReq("HC008",3);
        for (int teamno=1; teamno<=3; teamno++) {
            positions.add(new Position("PM-T" + teamno, pm));
            positions.add(new Position("PGM1-T" + teamno, pgm));
            positions.add(new Position("PGM2-T" + teamno, pgm));
            positions.add(new Position("PGM3-T" + teamno, pgm));
            positions.add(new Position("ANA1-T" + teamno, analyst));
            positions.add(new Position("ANA2-T" + teamno, analyst));
        }

    }
    HashMap<Integer,String> nameMap = new HashMap<Integer, String>();
    public MinCost.NodeMaster prepare(MinCostFlow minCostFlow)
    {
        MinCost.NodeMaster nm = new MinCost.NodeMaster();
        persons.forEach(person -> {
                    person.setNode(nm.addNode(10000));
                    nameMap.put(person.refNode.id, person.fullName);
        });
        MinCost.Node root = nm.addSupplyNode(positions.size());
        MinCost.Node sink = nm.addDemandNode(positions.size());
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
        TeamAssignmentCase me = new TeamAssignmentCase();
        if (args.length < 2) {
            System.out.println("Error : invalid arguments, at least: A|B|T runidentifier");
        }
        String persons_filename = args[1] + "_persons.in";
        String positions_filename = args[1] + "_positions.in";
        String jobs_filename = args[1] + "_jobs.in";
        try {
            if (args[0].equalsIgnoreCase("A")) {

                me.prepPersonsFromFile(persons_filename);
                me.prepPositionsFromFile(positions_filename);
            } else if (args[0].equalsIgnoreCase("B"))
            {
                me.prepPersonsFromFileEx(persons_filename);
                HashMap<String, Job> jobHashMap = me.prepJobsFromFileEx(jobs_filename);
                me.prepPositionsFromFileEx(positions_filename,jobHashMap);
            } else if (args[0].equalsIgnoreCase("T"))
            {
                me.prepPersons();
                me.prepPositions();
            } else
            {
                System.out.println("Error : invalid argument [0] => must be A or B, but it is "+args[0]);
                return;
            }
            MinCostFlow minCostFlow = new MinCostFlow();
            MinCost.NodeMaster nm = me.prepare(minCostFlow);
            MinCostFlowBase.Status status = minCostFlow.solve();
            if (status == MinCostFlow.Status.OPTIMAL)
                nm.printoutEx(minCostFlow,me.nameMap,false);
            else
                System.out.println("Failed : " + status.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
