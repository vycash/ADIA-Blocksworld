package planning;
import java.util.*;
import modelling.*;

public class MyHeuristic implements Heuristic {

    private BasicGoal goal;

    public MyHeuristic(BasicGoal goal) {
        this.goal = goal;
    }

    @Override
    public float estimate(Map<Variable, Object> state){
    
        // Si l'état courant satisfait déjà le but, heuristique = 0
        if (this.goal.isSatisfiedBy(state)) {
            return 0;
        }
    
        // Heuristique basée sur la somme des différences absolues
        float heuristicValue = 0;
    
        // Parcourir toutes les variables de l'état but
        for (Map.Entry<Variable, Object> entry : goal.getCondition().entrySet()) {
            Variable key = entry.getKey();
            int goalValue = (int) entry.getValue();
    
            // Si l'état courant contient la variable
            if (state.containsKey(key)) {
                int currentValue = (int) state.get(key);
    
                // Calculer la différence absolue entre la valeur actuelle et la valeur but
                heuristicValue += Math.abs(currentValue - goalValue);
            }
            // Sinon, une pénalité fixe
            else {
                heuristicValue += 10;
            }
        }
        return heuristicValue;
    }
}
