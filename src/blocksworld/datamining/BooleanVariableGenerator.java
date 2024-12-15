package blocksworld.datamining;

import java.util.*;
import modelling.*;

/**
 * Classe pour gérer la propositionnalisation des états du BlocksWorld.
 */
public class BooleanVariableGenerator {
    private final int numBlocks;
    private final int numPiles;

    /**
     * Constructeur de la classe.
     * 
     * @param numBlocks Nombre de blocs dans le monde.
     * @param numPiles Nombre de piles dans le monde.
     */
    public BooleanVariableGenerator(int numBlocks, int numPiles) {
        this.numBlocks = numBlocks;
        this.numPiles = numPiles;
    }
    public Set<BooleanVariable> getAllVariables() {
    // Logique pour générer toutes les variables booléennes
    Set<BooleanVariable> variables = new HashSet<>();
    // Ajoutez ici la logique pour créer les variables
    return variables;
    }


    /**
     * Génère l'ensemble de toutes les variables booléennes correspondant aux paramètres (blocs et piles).
     *
     * @return Un ensemble de variables booléennes.
     */

    public Set<BooleanVariable> generateAllVariables() {
        Set<BooleanVariable> variables = new HashSet<>();

        // Variables on_b,b'
        for (int b1 = 0; b1 <= numBlocks; b1++) {
            for (int b2 = 0; b2 <= numBlocks; b2++) {
                if (b1 != b2) {
                    variables.add(new BooleanVariable("on_" + b1 + "," + b2));
                }
            }
        }

        // Variables on_table_b,p
        for (int b = 0; b <= numBlocks; b++) {
            for (int p = -1; p >= -numPiles; p--) {
                variables.add(new BooleanVariable("on_table_" + b + "," + p));
            }
        }

        // Variables fixed_b
        for (int b = 0; b <= numBlocks; b++) {
            variables.add(new BooleanVariable("fixed_" + b));
        }

        // Variables free_p
        for (int p = -1; p >= -numPiles; p--) {
            variables.add(new BooleanVariable("free_" + p));
        }

        return variables;
    }
    

    /**
     * Convertit un état donné en une instance de variables booléennes.
     *
     * @param state L'état sous forme de liste de piles.
     * @return Un ensemble de variables booléennes correspondant à cet état.
     */
    public Set<BooleanVariable> convertStateToInstance(List<List<Integer>> state) {
        Set<BooleanVariable> instance = new HashSet<>();
        
        // Variables on_b,b'
        for (List<Integer> pile : state) {
            // Vérifier que la pile contient au moins deux blocs
            if (pile.size() > 1) {
                for (int i = 1; i < pile.size(); i++) { // Commencer à 1 pour accéder à pile.get(i - 1)
                    int top = pile.get(i);     // Bloc au-dessus
                    int below = pile.get(i - 1); // Bloc en dessous
                    BooleanVariable variable = new BooleanVariable("on_" + top + "," + below);
                    instance.add(variable);
                }
            }
        }
    
        // Variables on_table_b,p et free_p
        for (int p = 0; p < state.size(); p++) {
            List<Integer> pile = state.get(p);
    
            if (!pile.isEmpty()) {
                // Bloc en bas de la pile est sur la table
                int bottom = pile.get(0);
                BooleanVariable variable = new BooleanVariable("on_table_" + bottom + "," + (-p - 1));
                instance.add(variable);
    
                // La pile n'est pas libre
                variable = new BooleanVariable("free_" + (-p - 1));
                instance.add(variable);
            } else {
                // Pile vide = libre
                BooleanVariable variable = new BooleanVariable("free_" + (-p - 1));
                instance.add(variable);
            }
        }
    
        // Variables fixed_b
        for (List<Integer> pile : state) {
            for (int i = 0; i < pile.size() - 1; i++) { // Tous les blocs sauf le dernier de la pile sont fixés
                BooleanVariable variable = new BooleanVariable("fixed_" + pile.get(i));
                instance.add(variable);
            }
        }
    
        return instance;
    }
    
}
