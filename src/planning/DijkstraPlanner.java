package planning;
import modelling.*;
import java.util.*;

public class DijkstraPlanner implements Planner {

    private Map<Variable, Object> initialState;
    private Set<Action> actions;
    private Goal goal;

    private boolean isNodeCountActive = false;  // Par défaut, la sonde est désactivée
    private int count = 0;

    /**
     * Constructeur pour initialiser l'état initial, les actions disponibles et le but.
     *
     * @param initialState L'état initial du problème.
     * @param actions Les actions disponibles.
     * @param goal Le but à atteindre.
     */
    public DijkstraPlanner(Map<Variable, Object> initialState, Set<Action> actions, Goal goal) {
        this.initialState = initialState;
        this.actions = actions;
        this.goal = goal;
    }

    /**
     * Implémentation de l'algorithme de Dijkstra pour trouver le plan de coût minimal.
     * 
     * @return Une liste d'actions représentant le plan optimal, ou null s'il n'y a pas de plan.
     */
    @Override
    public List<Action> plan() {
        // Map pour stocker le coût minimal pour atteindre chaque état
        Map<Map<Variable, Object>, Integer> cost = new HashMap<>();
        // Map pour suivre les parents des états et les actions qui les ont créés
        Map<Map<Variable, Object>, Map<Variable, Object>> parents = new HashMap<>();
        Map<Map<Variable, Object>, Action> actionsForStates = new HashMap<>();

        // File de priorité pour explorer les états selon leur coût (tas binaire)
        PriorityQueue<Map<Variable, Object>> queue = new PriorityQueue<>(Comparator.comparingInt(cost::get));

        // Initialisation : le coût pour atteindre l'état initial est 0
        cost.put(initialState, 0);
        queue.add(initialState);
        parents.put(initialState, null); // L'état initial n'a pas de parent

        while (!queue.isEmpty()) {
            // Extraire l'état avec le coût le plus faible
            Map<Variable, Object> currentState = queue.poll();

            
            // Si le but est satisfait, reconstruire et retourner le plan
            if (goal.isSatisfiedBy(currentState)) {
                return reconstructPlan(currentState, parents, actionsForStates);
            }
            if (isNodeCountActive) {
                count++;
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

    public int getNodeCount(){
        return this.count;
    }
    public void activateNodeCount(boolean activate) {
        this.isNodeCountActive = activate;
    }
}
