package planning;
import modelling.*;
import java.util.*;

public class AStarPlanner implements Planner {
    private Map<Variable, Object> initialState;
    private Set<Action> actions;
    private Goal goal;
    private Heuristic heuristic;
    
    private boolean isNodeCountActive = false;  // Par défaut, la sonde est désactivée
    private int nodeCount = 0;  // Nombre de nœuds explorés

    /**
     * Constructeur pour initialiser l'état initial, les actions disponibles, le but et l'heuristique.
     *
     * @param initialState L'état initial du problème.
     * @param actions Les actions disponibles.
     * @param goal Le but à atteindre.
     * @param heuristic L'heuristique utilisée pour estimer le coût restant.
     */
    public AStarPlanner(Map<Variable, Object> initialState, Set<Action> actions, Goal goal, Heuristic heuristic) {
        this.initialState = initialState;
        this.actions = actions;
        this.goal = goal;
        this.heuristic = heuristic;
    }

    /**
     * Active ou désactive le comptage des nœuds explorés.
     * 
     * @param activate Si true, active le comptage des nœuds, sinon le désactive.
     */
    public void activateNodeCount(boolean activate) {
        this.isNodeCountActive = activate;
    }

    /**
     * Retourne le nombre de nœuds explorés après l'exécution de l'algorithme.
     * 
     * @return Le nombre de nœuds explorés.
     */
    public int getNodeCount() {
        return nodeCount;
    }

    /**
     * Implémentation de l'algorithme A* pour trouver le plan de coût minimal en utilisant une heuristique.
     * 
     * @return Une liste d'actions représentant le plan optimal, ou null s'il n'y a pas de plan.
     */
    @Override
    public List<Action> plan() {
        // Réinitialiser le compteur de nœuds
        if (isNodeCountActive) {
            nodeCount = 0;
        }
        
        // Map pour stocker le coût minimal pour atteindre chaque état
        Map<Map<Variable, Object>, Integer> cost = new HashMap<>();
        // Map pour suivre les parents des états et les actions qui les ont créés
        Map<Map<Variable, Object>, Map<Variable, Object>> parents = new HashMap<>();
        Map<Map<Variable, Object>, Action> actionsForStates = new HashMap<>();
        
        // File de priorité pour explorer les états selon le coût estimé (f = g + h)
        PriorityQueue<Map<Variable, Object>> queue = new PriorityQueue<>(
            Comparator.comparingDouble(state -> cost.get(state) + heuristic.estimate(state))
        );
        
        // Initialisation : le coût pour atteindre l'état initial est 0
        cost.put(initialState, 0);
        queue.add(initialState);
        parents.put(initialState, null); // L'état initial n'a pas de parent

        while (!queue.isEmpty()) {
            // Extraire l'état avec le coût estimé le plus faible
            Map<Variable, Object> currentState = queue.poll();

            
            // Si le but est satisfait, reconstruire et retourner le plan
            if (goal.isSatisfiedBy(currentState)) {
                return reconstructPlan(currentState, parents, actionsForStates);
            }
            // Incrémenter le compteur de nœuds si la sonde est activée
            if (isNodeCountActive) {
                nodeCount++;
            }

            // Explorer les actions applicables depuis cet état
            for (Action action : actions) {
                if (action.isApplicable(currentState)) {
                    Map<Variable, Object> successorState = action.successor(currentState);
                    int newCost = cost.get(currentState) + action.getCost();

                    // Si le successeur n'a pas encore été visité ou si un meilleur coût est trouvé
                    if (!cost.containsKey(successorState) || newCost < cost.get(successorState)) {
                        cost.put(successorState, newCost);
                        queue.add(successorState);
                        parents.put(successorState, currentState);
                        actionsForStates.put(successorState, action);
                    }
                }
            }
        }
        
        // Si aucun plan n'a été trouvé, retourner null
        return null;
    }

    /**
     * Méthode auxiliaire pour reconstruire le plan à partir des parents et des actions.
     *
     * @param state L'état final (qui satisfait le but).
     * @param parents Map des états et de leurs parents.
     * @param actionsForStates Map des états et des actions appliquées pour les atteindre.
     * @return La liste d'actions qui constitue le plan.
     */
    private List<Action> reconstructPlan(Map<Variable, Object> state, 
                                         Map<Map<Variable, Object>, Map<Variable, Object>> parents,
                                         Map<Map<Variable, Object>, Action> actionsForStates) {
        List<Action> plan = new LinkedList<>();
        while (parents.get(state) != null) {
            Action action = actionsForStates.get(state);
            plan.add(0, action); // Ajouter l'action au début du plan pour respecter l'ordre
            state = parents.get(state); // Remonter à l'état parent
        }
        return plan;
    }

    @Override
    public Map<Variable, Object> getInitialState() {
        return initialState;
    }

    @Override
    public Set<Action> getActions() {
        return actions;
    }

    @Override
    public Goal getGoal() {
        return goal;
    }
}
