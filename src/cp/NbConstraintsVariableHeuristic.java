package cp;

import modelling.*;
import java.util.*;
/**
 * Classe NbConstraintsVariableHeuristic
 *
 * Utilisée pour choisir une variable en fonction du nombre de contraintes
 * dans lesquelles elle apparaît. On peut choisir soit celle qui apparaît dans le plus
 * de contraintes, soit dans le moins.
 */

public class NbConstraintsVariableHeuristic implements VariableHeuristic {

    private Set<Constraint> contraintes;
    private boolean plus;

    /**
     * Constructeur pour initialiser l'heuristique
     *
     * @param contraintes ensemble des contraintes à prendre en compte
     * @param plus si true, on privilégie les variables avec le plus de contraintes,
     *             sinon celles avec le moins
     */
    public NbConstraintsVariableHeuristic(Set<Constraint> contraintes,boolean plus){
        this.contraintes = contraintes;
        this.plus = plus;
    }

    /**
     * Constructeur par défaut pour initialiser l'heuristique, qui met plus à false, càd rend la variable avec le moins de contraintes
     *
     * @param contraintes ensemble des contraintes à prendre en compte
     */
    public NbConstraintsVariableHeuristic(Set<Constraint> contraintes){
        this(contraintes,false);
    }

    /**
     * @return les contraintes utilisées par l'heuristique
     */
    public Set<Constraint> getContraintes(){ return this.contraintes; }

    /**
     * Définit la préférence pour plus ou moins de contraintes
     *
     * @param b true pour privilégier plus de contraintes, false pour moins
     */
    public void setBoolean(boolean b ){ this.plus = b; }


    /**
     * Choisit la "meilleure" variable en fonction du nombre de contraintes
     * qui la concernent. Soit celle avec le plus de contraintes, soit avec le moins.
     *
     * @param variables ensemble des variables parmi lesquelles choisir
     * @param domains les domaines (pas vraiment utilisé ici)
     * @return la variable choisie en fonction de la préférence
     */
    @Override
    public Variable best(Set<Variable> variables, Map<Variable, Set<Object>> domains) {

        Map<Variable,Integer> nbConstraintVar = new HashMap<>();
        // Pour chaque variable, on compte combien de contraintes elle a
        for ( Variable v : variables ) {
            for ( Constraint c : contraintes ){
                if( c.getScope().contains(v) ){
                    nbConstraintVar.put(v, nbConstraintVar.getOrDefault(v, 0) + 1);
                }
            }
        }
        // Si on veut la variable avec le plus de contraintes
        if ( plus ){ 
            return getKeyWithBiggestValue(nbConstraintVar);
        }
        // Sinon, celle avec le moins de contraintes
        return getKeyWithSmallestValue(nbConstraintVar);
    }

    /**
     * Trouve la variable avec le moins de contraintes dans la map
     *
     * @param map contient chaque variable et son nombre de contraintes
     * @return la variable avec le moins de contraintes
     */
    private Variable getKeyWithSmallestValue(Map<Variable, Integer> map) {
        Variable minKey = null;
        int minValue = Integer.MAX_VALUE;
    
        for (Map.Entry<Variable, Integer> entry : map.entrySet()) {
            if (entry.getValue() < minValue) {
                minValue = entry.getValue();
                minKey = entry.getKey();
            }
        }
    
        return minKey;
    }

    /**
     * Trouve la variable avec le plus de contraintes dans la map
     *
     * @param map contient chaque variable et son nombre de contraintes
     * @return la variable avec le plus de contraintes
     */
    private Variable getKeyWithBiggestValue(Map<Variable, Integer> map) {
        Variable maxKey = null;
        int maxValue = Integer.MIN_VALUE;
    
        for (Map.Entry<Variable, Integer> entry : map.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
                maxKey = entry.getKey();
            }
        }
    
        return maxKey;
    }
    
}
