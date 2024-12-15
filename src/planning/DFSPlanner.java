package planning;
import java.util.*;
import modelling.*;

public class DFSPlanner implements Planner {

    private Map<Variable, Object> initialState;
    private Set<Action> actions;
    private Goal goal;

    private boolean isNodeCountActive = false;  // Par défaut, la sonde est désactivée
    private int count = 0;

    public DFSPlanner(Map<Variable, Object> initialState,Set<Action> actions,Goal goal) {
        this.initialState = initialState;
        this.actions = actions;
        this.goal = goal;
    }

    public Map<Variable, Object> getInitialState() { return this.initialState; }
    public Set<Action> getActions() { return this.actions; }
    public Goal getGoal() { return this.goal; }

    public int getNodeCount(){
        return this.count;
    }
    public void activateNodeCount(boolean val){
        this.isNodeCountActive = val;
    }


    // la classe DFSPlanner est modifiée au lieu d'utiliser la pile d'appel comme Stack pour stocker les etats
    // on a réécris l'algo en iteratif pour utiliser un objet stack pour les etats et une autre pile stack pour les plans
    public List<Action> plan() {
        // Utiliser deux piles : une pour les états et une pour les plans
        Stack<Map<Variable, Object>> stateStack = new Stack<>();
        Stack<List<Action>> planStack = new Stack<>();
        Set<Map<Variable, Object>> closed = new HashSet<>();

        // Ajouter l'état initial avec un plan vide
        stateStack.push(initialState);
        planStack.push(new ArrayList<>());

        while (!stateStack.isEmpty()) {
            // Extraire l'état et le plan associés
            Map<Variable, Object> currentState = stateStack.pop();
            List<Action> currentPlan = planStack.pop();

            // Vérifier si l'état actuel satisfait l'objectif
            if (goal.isSatisfiedBy(currentState)) {
                return currentPlan;  // Retourner le plan actuel si l'objectif est satisfait
            }

            // Ajouter l'état actuel à l'ensemble fermé
            closed.add(currentState);

            // Parcourir toutes les actions applicables
            for (Action action : getActions()) {
                if (action.isApplicable(currentState)) {
                    // Obtenir l'état successeur après avoir appliqué l'action
                    Map<Variable, Object> nextState = action.successor(currentState);

                    if (isNodeCountActive) {
                        count++;
                    }

                    // Vérifier que le prochain état n'a pas déjà été visité
                    if (!closed.contains(nextState)) {
                        // Ajouter l'action au plan actuel et empiler le nouvel état avec le nouveau plan
                        List<Action> newPlan = new ArrayList<>(currentPlan);
                        newPlan.add(action);
                        stateStack.push(nextState);
                        planStack.push(newPlan);
                    }
                }
            }
        }

        // Retourner null si aucun plan n'est trouvé
        return null;
    }
}