import cptests.AbstractSolverTests;
import cptests.ArcConsistencyTests;
import cptests.BacktrackSolverTests;
import cptests.DomainSizeVariableHeuristicTests;
import cptests.HeuristicMACSolverTests;
import cptests.MACSolverTests;
import cptests.NbConstraintsVariableHeuristicTests;
import cptests.RandomValueHeuristicTests;


public class TestCp {
    public static void main(String[] args) {
        boolean ok = true;
        ok = ok && AbstractSolverTests.testIsConsistent();
        ok = ok && BacktrackSolverTests.testSolve();
        ok = ok && ArcConsistencyTests.testEnforceNodeConsistency();
        ok = ok && ArcConsistencyTests.testRevise();
        ok = ok && ArcConsistencyTests.testAC1();
        ok = ok && MACSolverTests.testSolve();
        ok = ok && NbConstraintsVariableHeuristicTests.testBest();
        ok = ok && DomainSizeVariableHeuristicTests.testBest();
        ok = ok && RandomValueHeuristicTests.testOrdering();
        ok = ok && HeuristicMACSolverTests.testSolve();
        System.out.println(ok ? "All tests OK" : "At least one test KO");
    }
}
