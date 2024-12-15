package planning;
import java.util.*;

import modelling.Variable;
public class DemoPlanning {
    public static void main(String[] args) {

        /*
        * Dans cette classe Demo nous allons faire une démonstration des differentes classes dans ce package 
        * et les differents planneurs, pour des raisons de simplification les etat seront des dictionnaires de string:int
        *
        * On a défini aussi dans cette classe de demo quelques fonctions utiles qui ne serviront pour faire de la démonstration
        * afficheMap(etat,String) : affiche les variables de l'etat et leurs valeurs
        * affichePlan(Actions) : affiche un ensemble d'actions
        *
        * les préconditions et les effets et le cout de chaque action sont définis dans le README
        */



        System.out.println("\n============== DEMO DE PLANNING ==============\n");

        // le domaine des variables qui sera de 0 à 10 inclus
        HashSet<Object> domain = new HashSet<>();
        for (int i = 0; i <= 10; i++) {
            domain.add(i);
        }
        
        // Initialisation de quelques variables
        Variable x = new Variable("x",domain);
        Variable y = new Variable("y",domain);
        Variable z = new Variable("z",domain);

        // On defini un but
        Map<Variable, Object> goalState = new HashMap<>();
        goalState.put(x, 9);
        goalState.put(y, 9);
        goalState.put(z, 9);        
        BasicGoal goal = new BasicGoal(goalState);

        // On définit l'etat initial 
        Map<Variable, Object> initialState = new HashMap<>();
        initialState.put(x, 1);
        initialState.put(y, 1);
        initialState.put(z, 1);

        // On crée un Set actions qui represent l'ensemble des actions
        Set<Action> actions = new HashSet<>();

        // on definit quelques actions

        Map<Variable, Object> preconditionAction1 = new HashMap<>();
        preconditionAction1.put(x, 1);
        Map<Variable, Object> effectAction1 = new HashMap<>();
        effectAction1.put(x, 9);
        
        Action action1 = new BasicAction(preconditionAction1, effectAction1, 5);

        Map<Variable, Object> preconditionAction2 = new HashMap<>();
        preconditionAction2.put(y, 1);
        Map<Variable, Object> effectAction2 = new HashMap<>();
        effectAction2.put(y, 9);
        
        Action action2 = new BasicAction(preconditionAction2, effectAction2, 5);

        Map<Variable, Object> preconditionAction3 = new HashMap<>();
        preconditionAction3.put(z, 1);
        Map<Variable, Object> effectAction3 = new HashMap<>();
        effectAction3.put(z, 9);
        
        Action action3 = new BasicAction(preconditionAction3, effectAction3, 5);
       
        Map<Variable, Object> preconditionAction4 = new HashMap<>();
        preconditionAction4.put(x, 1);
        preconditionAction4.put(y, 1);
        Map<Variable, Object> effectAction4 = new HashMap<>();
        effectAction4.put(x, 3);
        
        Action action4 = new BasicAction(preconditionAction4, effectAction4, 1);

        Map<Variable, Object> preconditionAction5 = new HashMap<>();
        preconditionAction5.put(y,1);
        preconditionAction5.put(z,1);
        Map<Variable, Object> effectAction5 = new HashMap<>();
        effectAction5.put(x, 8);
        effectAction5.put(y, 5);
        effectAction5.put(z, 2);

        Action action5 = new BasicAction(preconditionAction5, effectAction5, 2);

        Map<Variable, Object> preconditionAction6 = new HashMap<>();
        preconditionAction6.put(y,5);
        Map<Variable, Object> effectAction6 = new HashMap<>();
        effectAction6.put(x, 9);
        effectAction6.put(y, 9);
        effectAction6.put(z, 1);

        Action action6 = new BasicAction(preconditionAction6, effectAction6, 3);

        Map<Variable, Object> preconditionAction7 = new HashMap<>();
        preconditionAction7.put(x,3);
        preconditionAction7.put(z,9);
        Map<Variable, Object> effectAction7 = new HashMap<>();
        effectAction7.put(x, 9);
        effectAction7.put(y, 9);
        effectAction7.put(z, 9);

        Action action7 = new BasicAction(preconditionAction7, effectAction7, 3);

        Map<Variable, Object> preconditionAction8 = new HashMap<>();
        preconditionAction8.put(z,2);
        Map<Variable, Object> effectAction8 = new HashMap<>();
        effectAction8.put(x, 3);
        effectAction8.put(y, 9);
        effectAction8.put(z, 9);

        Action action8 = new BasicAction(preconditionAction8, effectAction8, 2);

        Map<Variable, Object> preconditionAction9 = new HashMap<>();
        preconditionAction9.put(x,1);
        preconditionAction9.put(y,1);
        preconditionAction9.put(z,1);
        Map<Variable, Object> effectAction9 = new HashMap<>();
        effectAction9.put(x, 9);
        effectAction9.put(y, 1);
        effectAction9.put(z, 9);

        Action action9 = new BasicAction(preconditionAction9, effectAction9, 10);


        actions.add(action1);
        actions.add(action2);
        actions.add(action3);
        actions.add(action4);
        actions.add(action5);
        actions.add(action6);
        actions.add(action7);
        actions.add(action9);
        actions.add(action8);

        // affichage de l'état inital et l'état but
        afficheMap(initialState, "initialState");
        afficheMap(goalState, "GOAL");

        // creation des planners 
        // création du planner avec l'algo dijkstra 
        Planner dijkstraPlanner = new DijkstraPlanner(initialState, actions, goal);
        // création du planner avec l'algo dfs 
        Planner dfsPlanner = new DFSPlanner(initialState, actions, goal);
        // création du planner avec l'algo bfs
        Planner bfsPlanner = new BFSPlanner(initialState, actions, goal);
        // création du planner avec l'algo A* et l'heuristic MyHeuristic
        Heuristic heuristic = new MyHeuristic(goal);
        Planner astarPlanner = new AStarPlanner(initialState,actions,goal,heuristic);

        // affichage du plan de chaque planner ainsi que le nombre de noeuds parcourus par chacun des planners

        System.out.println("\n-----BFSPLANNER------------ \n     avec comptage \n     état inital = initalState\n     goal = GOAL\n---------------------------");
        bfsPlanner.activateNodeCount(true);
        affichePlan(bfsPlanner.plan());
        System.out.println("nombre de noeuds parcourus : "+bfsPlanner.getNodeCount());

        System.out.println("\n-----DFSPLANNER------------ \n     avec comptage \n     état inital = initalState\n     goal = GOAL\n---------------------------");
        dfsPlanner.activateNodeCount(true);
        affichePlan(dfsPlanner.plan());
        System.out.println("nombre de noeuds parcourus : "+dfsPlanner.getNodeCount());
        
        System.out.println("\n-----DIJKSTRA-------------- \n     avec comptage \n     état inital = initalState\n     goal = GOAL\n---------------------------");
        dijkstraPlanner.activateNodeCount(true);
        affichePlan(dijkstraPlanner.plan());
        System.out.println("nombre de noeuds parcourus : "+dijkstraPlanner.getNodeCount());

        System.out.println("\n-----ASTARPLANNER---------- \n     avec comptage \n     état inital = initalState\n     goal = GOAL\n---------------------------");
        astarPlanner.activateNodeCount(true);
        affichePlan(astarPlanner.plan());
        System.out.println("nombre de noeuds parcourus : "+astarPlanner.getNodeCount());

        System.out.println("\n-----NOMBRE DE NOEUDS PAR COUT DU PLAN---------");
        
        Map<String,Integer> mapNode = new HashMap<>();
        mapNode.put("bfsPlanner", bfsPlanner.getNodeCount());
        mapNode.put("dfsPlanner", dfsPlanner.getNodeCount());
        mapNode.put("dijkstraPlanner", dijkstraPlanner.getNodeCount());
        mapNode.put("astarPlanner", astarPlanner.getNodeCount());


        String plannerWithHighestNodeCount = "";
        String plannerWithLowestNodeCount = "";
        int highestNodeCount = Integer.MIN_VALUE;
        int lowestNodeCount = Integer.MAX_VALUE;

        for (Map.Entry<String, Integer> entry : mapNode.entrySet()) {
            if (entry.getValue() > highestNodeCount) {
                highestNodeCount = entry.getValue();
                plannerWithHighestNodeCount = entry.getKey();
            }
            if (entry.getValue() < lowestNodeCount) {
                lowestNodeCount = entry.getValue();
                plannerWithLowestNodeCount = entry.getKey();
            }
        }

        System.out.println("Le planner le plus lent, celui qui parcours le plus de noeuds: " + plannerWithHighestNodeCount);
        System.out.println("Nb de noeuds parcourus: " + highestNodeCount);
        System.out.println("Le planner le plus rapide, celui qui parcours le moins de noeuds: " + plannerWithLowestNodeCount);
        System.out.println("Nb de noeuds parcourus: " + lowestNodeCount);  


        
    }
    // Méthode pour afficher un état
    public static void afficheMap(Map<Variable,Object> Map,String mapName){
        System.out.println(mapName+": { ");
        for (Map.Entry<Variable, Object> entry : Map.entrySet()) {
            Variable key = entry.getKey();
            Object value = entry.getValue();
            System.out.println("    { " + key + " = " + value+" }   ");
        }
        System.out.println("}");
    }
    // Méthode pour afficher un Plan
    public static void affichePlan(List<Action> plan){
        if (plan != null) {
            System.out.println("Plan found!");
            int step = 1;
            int cost = 0;
            for (Action action : plan) {
                System.out.println("Step " + step + ": " + action + " cost = "+action.getCost()+"\n");
                step++;
                cost+=action.getCost();
            }
            System.out.println("GOAL ATTAINED! \n\ntotal cost of path : "+cost);
        } else 
            System.out.println("No plan found.");
    }


        
}