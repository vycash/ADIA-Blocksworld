package blocksworld;
import blocksworld.planners.*;
import blocksworld.world.*;
import java.util.*;
import modelling.*;
import planning.*;


public class DemoBlocksWorld {

    public static void main(String[] args) {

        BlockWorldWithIncreasingConstraint w = new BlockWorldWithIncreasingConstraint();
        List<List<Integer>> blocks = new ArrayList<List<Integer>>();
        // définiton des empilements des blocks en piles:
        blocks.add(List.of(1,2,3));
        blocks.add(List.of(5,4));
        // dans l'exemple ci-dessus on a précisé les empilements sur les piles 1 et 2
        Map<Variable, Object> state = w.createState(blocks);
        StateVisualizer.afficheState(state);
        System.out.println("est ce que l'etat est croissant? "+w.isIncreasingSatisfiedByState(state));

        BlockWorldWithConstraints w4 = new BlockWorldWithConstraints();
        blocks = new ArrayList<List<Integer>>();
        blocks.add(List.of(2,2));
        blocks.add(List.of(4,5));
        state = w4.createState(blocks);
        StateVisualizer.afficheState(state);
        System.out.println("========================== est ce que l'etat est valide? "+w4.isStateValid(state));

        blocks = new ArrayList<List<Integer>>();
        blocks.add(List.of(1,2,3));
        blocks.add(List.of(4,5));
        state = w.createState(blocks);
        StateVisualizer.afficheState(state);
        System.out.println("est ce que l'etat est croissant? "+w.isIncreasingSatisfiedByState(state));


        BlockWorldWithRegularityConstraint w2 = new BlockWorldWithRegularityConstraint();
        blocks = new ArrayList<List<Integer>>();
        blocks.add(List.of(1,2,3));
        blocks.add(List.of(4,5));
        state = w2.createState(blocks);
        StateVisualizer.afficheState(state);
        System.out.println("est ce que l'etat est Regulier? "+w2.isRegularitySatisfiedByState(state));


        blocks = new ArrayList<List<Integer>>();
        blocks.add(List.of(1,4,3));
        blocks.add(List.of(5,2));
        state = w2.createState(blocks);
        StateVisualizer.afficheState(state);
        System.out.println("est ce que l'etat est Regulier? "+w2.isRegularitySatisfiedByState(state));


        BlockWorldWithRegularityAndIncreasingConstraint w3 = new BlockWorldWithRegularityAndIncreasingConstraint();
        blocks = new ArrayList<List<Integer>>();
        blocks.add(List.of(1,2,3));
        blocks.add(List.of(5,4));
        state = w3.createState(blocks);
        StateVisualizer.afficheState(state);
        System.out.println("est ce que l'etat est croissant et régulier?? "+w3.isSatisfiedByState(state));

        blocks = new ArrayList<List<Integer>>();
        blocks.add(List.of(1,4,3));
        blocks.add(List.of(5,2));
        state = w3.createState(blocks);
        StateVisualizer.afficheState(state);
        System.out.println("est ce que l'etat est Regulier et croissant? "+w3.isSatisfiedByState(state));

        blocks = new ArrayList<List<Integer>>();
        blocks.add(List.of(1,2,3));
        blocks.add(List.of(4,5));
        state = w3.createState(blocks);
        StateVisualizer.afficheState(state);
        System.out.println("est ce que l'etat est croissant et régulier?? "+w3.isSatisfiedByState(state));



        BlockWorld world = new BlockWorld();
        
        // Définition de l'état initial et de l'état goal avec des configurations spécifiques
        Map<Variable, Object> initialState = world.createRandomState();
        System.out.println("==== initialState");
        StateVisualizer.afficheState(initialState);
        StateVisualizer.afficheStatGUI("initialState", world, initialState);
        
        Map<Variable, Object> goalState = world.createRandomState();
        System.out.println("==== goalState");
        
        ActionGenerator generator = new ActionGenerator(world);
        //BasicGoal goal = new BasicGoal(goalState);
        BasicGoal goal = new BasicGoal(goalState);
        
        StateVisualizer.afficheState(goal.getCondition());
        StateVisualizer.afficheStatGUI("goalState", world, goal.getCondition());


        Heuristic heuristic = new BadBlockHeuristic(goal.getCondition());
        Heuristic heuristic2 = new BadStackHeuristic(goal.getCondition());
        Planner planner = new AStarPlanner(initialState, generator.generateAllActions(), goal, heuristic);

        planner.activateNodeCount(true);
        long startTime = System.nanoTime();
        List<Action> plan = planner.plan();
        long endTime = System.nanoTime();
        long durationInMilliseconds = (endTime - startTime) / 1_000_000;
        long durationInSeconds = durationInMilliseconds / 1000;

        // Affichage du résultat
        if (plan != null && !plan.isEmpty()) {
            System.out.println("Plan : " + plan);
            StateVisualizer.afficheState(ActionGenerator.executePlan(initialState, plan));
            PlanVisualizer.visualizePlan(initialState, plan, "Simulation du Plan A*");
            System.out.println("======= nombre d'états visités : " + planner.getNodeCount());
            System.out.println("======= Temps de calcul du plan (A*): " + durationInMilliseconds + "ms qui est a peu près "+durationInSeconds+"s");
        } else {
            System.out.println("Aucun plan trouvé.");
        }

    
        System.out.println("============== BFS en cours de recherche");
        planner = new BFSPlanner(initialState, generator.generateAllActions(), goal);
        planner.activateNodeCount(true);
        startTime = System.nanoTime();
        plan = planner.plan();
        endTime = System.nanoTime();
        durationInMilliseconds = (endTime - startTime) / 1_000_000;
        durationInSeconds = durationInMilliseconds / 1000;
        
        if (plan != null && !plan.isEmpty()) {
            System.out.println("Plan : " + plan);
            StateVisualizer.afficheState(ActionGenerator.executePlan(initialState,plan));
            PlanVisualizer.visualizePlan(initialState, plan, "Simulation du Plan BFS");
            System.out.println("======= nombre d'états visités : "+planner.getNodeCount());
            System.out.println("======= Temps de calcul du plan (BFS): " + durationInMilliseconds + "ms qui est a peu près "+durationInSeconds+"s");

        } else {
            System.out.println("Aucun plan trouvé.");
        }

        System.out.println("============== DFS en cours de recherche");
        planner = new DFSPlanner(initialState, generator.generateAllActions(), goal);
        planner.activateNodeCount(true);
        startTime = System.nanoTime();
        plan = planner.plan();
        endTime = System.nanoTime();
        durationInMilliseconds = (endTime - startTime) / 1_000_000;
        durationInSeconds = durationInMilliseconds / 1000;
        
        if (plan != null && !plan.isEmpty()) {
            System.out.println("Plan : " + plan);
            StateVisualizer.afficheState(ActionGenerator.executePlan(initialState,plan));
            PlanVisualizer.visualizePlan(initialState, plan, "Simulation du Plan DFS");
            System.out.println("======= nombre d'états visités : "+planner.getNodeCount());
            System.out.println("======= Temps de calcul du plan (DFS): " + durationInMilliseconds + "ms qui est a peu près "+durationInSeconds+"s");

        } else {
            System.out.println("Aucun plan trouvé.");
        }

        System.out.println("=============== Dijkstra en cours de recherche");
        generator.useRandomCosts(true);
        planner = new DijkstraPlanner(initialState, generator.generateAllActions(), goal);
        planner.activateNodeCount(true);
        startTime = System.nanoTime();
        plan = planner.plan();
        endTime = System.nanoTime();
        durationInMilliseconds = (endTime - startTime) / 1_000_000;
        durationInSeconds = durationInMilliseconds / 1000;
        
        if (plan != null && !plan.isEmpty()) {
            System.out.println("Plan : " + plan);
            StateVisualizer.afficheState(ActionGenerator.executePlan(initialState,plan));
            PlanVisualizer.visualizePlan(initialState, plan, "Simulation du Plan Dijkstra");
            System.out.println("======= nombre d'états visités : "+planner.getNodeCount());
            System.out.println("======= Temps de calcul du plan (Dijkstra): " + durationInMilliseconds + "ms qui est a peu près "+durationInSeconds+"s");

        } else {
            System.out.println("Aucun plan trouvé.");
        }
        
        
    }
}

