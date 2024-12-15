package blocksworld.planners;
import java.util.*;
import planning.*;
import modelling.*;


/*
 * Heuristique qui estime le cout en comptant le nombre de blocs mal placés.
 */
public class BadBlockHeuristic implements Heuristic {

    protected Map<Variable, Object> goal;

    public BadBlockHeuristic(Map<Variable, Object> goal) {
        this.goal = goal;
    }

    /**
     * Estime le coût heuristique en comptant le nombre de blocs mal placés.
     * cette heuristique est admissible car l'estimation ne serait jamais supérieure au cout réél,
     * elle se contente de compter les blocs mal placés sans ajouter de coûts supplémentaires pour les repositionner, 
     * ce qui signifie qu'elle sous-estime ou est égale au coût réel
     * 
     * @param state L'état actuel.
     * @return Le coût heuristique estimé.
     */
    @Override
    public float estimate(Map<Variable, Object> state) {
        int nbBlocks = 0;
        
        for (Map.Entry<Variable, Object> entry : state.entrySet()) {
            Variable v = entry.getKey();
            Object value = entry.getValue();
            
            // Vérification si la variable commence par "on"
            if (v.getName().startsWith("on")) {
                // Vérification si l'état actuel est différent de l'objectif
                if (!value.equals(this.goal.get(v))) {
                    nbBlocks++;
                }
            }
        }
        return nbBlocks;    
    }
}


