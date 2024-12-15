package blocksworld.planners;
import java.util.*;
import planning.*;
import modelling.*;


/*
 * Heuristique qui estime le cout en comptant le nombre de piles mal placées
 */
public class BadStackHeuristic implements Heuristic {
    protected Map<Variable, Object> goal;

    public BadStackHeuristic(Map<Variable, Object> goal) {
        this.goal = goal;
    }

    /**
     * 
     * Estime le coût heuristique en comptant le nombre de piles mal placées 
     * et les différences de hauteur de pile.
     * 
     * une heuristique est admissible si elle ne sur estime pas e cout réél
     * 
     * Une pile mal placée coutera au moins 1 pour bien la placer, et un bloc mal placé coutera au moins 1 pour bien le placer,
     * donc pour une pile mal placée son estimation sera : 1 + nombre de blocs dans cette pile.
     * 
     * une pile est mal placée si la valeur de sa variable free n'est pas la meme que dans goal
     * ou elle n'as pas le meme nombre de blocs que dans l'etat goal
     * 
     * L'estimation totale sera la somme de l'estimation de chaque pile.
     */
    @Override
    public float estimate(Map<Variable, Object> state) {
        int estimate = 0;

        // Calcul des hauteurs des piles pour l'état actuel et l'objectif
        Map<Integer, Integer> currentHeights = calculatePileHeights(state);
        Map<Integer, Integer> goalHeights = calculatePileHeights(goal);

        // Parcours de toutes les piles pour comparer avec l'état objectif
        for (int pileId : goalHeights.keySet()) {
            int currentHeight = currentHeights.getOrDefault(pileId, 0);
            int goalHeight = goalHeights.get(pileId);

            // Vérification de l'état "free" de la pile
            BooleanVariable freePileVar = new BooleanVariable("free" + pileId);
            boolean freeStateMatch = state.getOrDefault(freePileVar, true).equals(goal.getOrDefault(freePileVar, true));
            
            // Ajouter le coût si la pile est mal placée en fonction de la hauteur ou de l'état "free"
            if (currentHeight != goalHeight || !freeStateMatch) {
                estimate += 1 + Math.abs(currentHeight - goalHeight);
            }
        }
        return estimate;
    }

    /**
     * Méthode pour calculer la hauteur de chaque pile dans un état donné en si'inspirant de la methode afficheState.
     */
    private Map<Integer, Integer> calculatePileHeights(Map<Variable, Object> state) {
        Map<Integer, Integer> pileHeights = new HashMap<>();
        Map<Integer, Integer> blockToPile = new HashMap<>();
        
        // Remplir la map pour chaque bloc placé sur un autre bloc ou une pile
        for (Map.Entry<Variable, Object> entry : state.entrySet()) {
            Variable variable = entry.getKey();
            Object value = entry.getValue();

            if (variable.getName().startsWith("on")) {
                int blockId = Integer.parseInt(variable.getName().substring(2));
                int belowId = (Integer) value;
                
                // Construire la relation entre bloc et pile
                blockToPile.put(blockId, belowId);
            }
        }

        // Calcul de la hauteur pour chaque pile
        for (int pileId = -1; pileId >= -goal.size(); pileId--) {
            int height = 0;
            int current = pileId;

            while (blockToPile.containsValue(current)) {
                height++;
                // Récupérer le bloc au-dessus
                for (Map.Entry<Integer, Integer> entry : blockToPile.entrySet()) {
                    if (entry.getValue().equals(current)) {
                        current = entry.getKey();
                        break;
                    }
                }
            }
            pileHeights.put(pileId, height);
        }
        return pileHeights;
    }
}
