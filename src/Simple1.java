import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class Simple1 {
    static { System.loadLibrary("jniortools"); }

    private static MPSolver createSolver (String solverType) {
        return new MPSolver("my_program",
                MPSolver.OptimizationProblemType.valueOf(solverType));
    }

    private static void runmy_program(String solverType,
                                      boolean printModel) {
        MPSolver solver = createSolver(solverType);
        // Create the variables x and y.
        MPVariable x = solver.makeNumVar(0.0, 5.0, "x");
        MPVariable y = solver.makeNumVar(0.0, 10.0, "y");
        // Create the objective function, x + y.
        MPObjective objective = solver.objective();
        objective.setCoefficient(y, 1);
        objective.setMaximization();
        // Call the solver and display the results.
        solver.enableOutput();
        solver.solve();

        System.out.println("Solution:");
        System.out.println("x = " + x.solutionValue());
        System.out.println("y = " + y.solutionValue());

    }

    public static void main(String[] args) throws Exception {
        runmy_program("GLOP_LINEAR_PROGRAMMING", false);
    }
}