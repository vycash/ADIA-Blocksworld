
package cp;
import java.util.*;
import modelling.*;
/**
 * Interface VariableHeuristic
 *
 * Représente une heuristique pour sélectionner la meilleure variable à assigner
 * dans un problème de satisfaction de contraintes. Les implémentations de cette interface
 * devront fournir une méthode qui choisit la variable "idéale" en fonction des domaines
 * des variables et d'autres critères spécifiques.
 */

public interface VariableHeuristic {
    /**
     * Sélectionne la meilleure variable à assigner parmi un ensemble de variables,
     * en se basant sur les domaines de chaque variable.
     *
     * @param variables l'ensemble des variables à évaluer
     * @param domains une map associant chaque variable à son domaine possible de valeurs
     * @return la variable jugée la meilleure à assigner en premier
     */
    public Variable best(Set<Variable> variables,Map<Variable,Set<Object>> domains);

    
}

