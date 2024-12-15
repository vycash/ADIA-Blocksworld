package planning;
import java.util.*;
import modelling.*;

public class BFSPlanner implements Planner{
    
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

     public BFSPlanner(Map<Variable,Object> initialState, Set<Action> actions, Goal goal){
        this.initialState = initialState;
        this.actions = actions;
        this.goal = goal;
     }

     // Implémentation de la méthode getInitialState() de l'interface Planner
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
     
     @Override
    public List<Action> plan() {
        // La file utilisée pour la BFS, elle stocke les états à explorer
        Queue<Map<Variable, Object>> queue = new LinkedList<>();
        // Map pour suivre les parents des états et les actions appliquées pour y arriver
        Map<Map<Variable, Object>, Map<Variable, Object>> parents = new HashMap<>();
        Map<Map<Variable, Object>, Action> actionsForStates = new HashMap<>();

        // Initialisation
        queue.add(initialState);
        parents.put(initialState, null); // L'état initial n'a pas de parent

        while (!queue.isEmpty()) {
            // On récupère le premier état de la file
            Map<Variable, Object> currentState = queue.poll();

            // Vérification si l'état courant satisfait le but
            if (goal.isSatisfiedBy(currentState)) {
                // Si le but est satisfait, reconstruire le plan
                return reconstructPlan(currentState, parents, actionsForStates);
            }
            // Incrémenter le compteur de nœuds si la sonde est activée
            if (isNodeCountActive) {
                count++;
            }

            // Exploration des actions applicables depuis l'état courant
            for (Action action : actions) {
                if (action.isApplicable(currentState)) {
                    Map<Variable, Object> successorState = action.successor(currentState);

                    // Si cet état n'a pas encore été visité
                    if (!parents.containsKey(successorState)) {
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
            plan.add(0, action); // On ajoute les actions au début de la liste pour respecter l'ordre
            state = parents.get(state); // On remonte à l'état parent
        }
        return plan;
    }



}
