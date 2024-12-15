package blocksworld.csp;
import blocksworld.world.*;
import cp.*;
import java.util.*;
import modelling.*;

/**
 *  classe representant un démonstrateur de rechere d'état qui satisfait les contraintes d'un monde donné
 */
public class DemoCSP {

    private BlockWorldWithConstraints world;
    private String worldName;

    public DemoCSP(BlockWorldWithConstraints world,String worldName){
        this.world = world;
        this.worldName=worldName;
    }

    public DemoCSP(int numBlocks,int numStacks){
        this(new BlockWorldWithRegularityConstraint(numBlocks, numStacks),"regularity constraint");
    }

    public DemoCSP(){
        this(6,8);
    }

    public void demo() {

        System.out.println("========================= Démo de CSP sur un monde "+worldName);

        //BlockWorldWithRegularityAndIncreasingConstraint world = new BlockWorldWithRegularityAndIncreasingConstraint(numBlocks,numStacks);

        Set<Variable> variables = world.getVariables();
        Set<Constraint> allConstraints = world.getConstraints();

        BacktrackSolver solver = new BacktrackSolver(variables, allConstraints);
        Map<Variable, Object> result = calculateSolve(solver);
        StateVisualizer.afficheState(result);
        StateVisualizer.afficheStatGUI("BackTrackSolver "+worldName, world, result);
        System.out.println("\n======= est ce que l'état result satisfait les contraintes? "+world.isStateValid(result));


        MACSolver macSolver = new MACSolver(variables, allConstraints);
        result = calculateSolve(macSolver);
        StateVisualizer.afficheState(result);
        StateVisualizer.afficheStatGUI("MACSolver "+worldName, world, result);
        System.out.println("\n======= est ce que l'état result satisfait les contraintes? "+world.isStateValid(result));


        //HeuristicMACSolver macSolverHeuristic = new HeuristicMACSolver(variables,allConstraints,new NbConstraintsVariableHeuristic(false),new RandomValueHeuristic());
        HeuristicMACSolver macSolverHeuristic = new HeuristicMACSolver(variables,allConstraints,new DomainSizeVariableHeuristic(false),new RandomValueHeuristic());
        result = calculateSolve(macSolverHeuristic);
        StateVisualizer.afficheState(result);
        StateVisualizer.afficheStatGUI("HeuristicMACSolver "+worldName, world, result);
        System.out.println("\n======= est ce que l'état result satisfait les contraintes? "+world.isStateValid(result));


    }

    private static Map<Variable, Object> calculateSolve(AbstractSolver solver){
        long startTime = System.nanoTime();
        Map<Variable, Object> result = solver.solve();
        long endTime = System.nanoTime();
        long durationInMilliseconds = (endTime - startTime) / 1_000_000;
        long durationInSeconds = durationInMilliseconds / 1000;
        System.out.println("======= Temps de recherche d'un état valide : " + durationInMilliseconds + "ms qui est a peu près "+durationInSeconds+"s");
        return result;
    }
    
    
}
