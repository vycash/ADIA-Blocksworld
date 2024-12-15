
package cp;

import java.util.*;
import modelling.*;
/**
 * Classe DomainSizeVariableHeuristic
 *
 * Heuristique qui sert à choisir une variable en fonction de la taille de son domaine.
 * On peut choisir soit celle avec le plus grand domaine, soit celle avec le plus petit en precisant le paramètre plus, qui est à false par défaut.
 */

public class DomainSizeVariableHeuristic implements VariableHeuristic{
    /**
     * Écrire de même une classe nommée DomainSizeVariableHeuristic, ne prenant en
     * argument qu’un booléen, indiquant ici si l’on préfère les variables avec le plus grand domaine ( true) ou
     * avec le plus petit domaine ( false).
    */
    private boolean plus;

    /**
     * Constructeur de DomainSizeVariableHeuristic
     *
     * @param plus indique si on préfère les variables avec le plus grand domaine (true) ou le plus petit (false)
     */
    public DomainSizeVariableHeuristic(boolean plus){
        this.plus = plus;
    }

    /**
     * Constructeur par défaut de DomainSizeVariableHeuristic qui met plus à false, càd rend la variable avec le domaine plus petit
     *
     */
    public DomainSizeVariableHeuristic(){
        this(false);
    }

    /**
     * Définit la préférence pour plus ou moins de domaines
     *
     * @param b true pour privilégier le plus grand domaine, false pour le plus petit
     */
    public void setBoolean(boolean b ){ this.plus = b; }



    /**
     * Sélectionne la "meilleure" variable en fonction de la taille de son domaine.
     * Si `plus` est true, on prend celle avec le plus grand domaine, sinon le plus petit.
     *
     * @param variables ensemble des variables parmi lesquelles choisir
     * @param domains map des domaines de chaque variable
     * @return la variable choisie en fonction de la préférence
     */
    @Override
    public Variable best(Set<Variable> variables, Map<Variable, Set<Object>> domains) {

        Map<Variable,Integer> domainSizeVar = new HashMap<>();
        // Remplit la map avec la taille du domaine de chaque variable
        for ( Variable v : variables) {
            domainSizeVar.put(v,v.getDomain().size());
        }
        // Retourne la variable avec le plus grand domaine si `plus` est true, sinon le plus petit
        if ( plus ){ 
            return getKeyWithBiggestValue(domainSizeVar);
        }

        return getKeyWithSmallestValue(domainSizeVar);
    }

    /**
     * Trouve la variable avec le plus petit domaine dans la map
     *
     * @param map contient chaque variable et la taille de son domaine
     * @return la variable avec le plus petit domaine
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
     * Trouve la variable avec le plus grand domaine dans la map
     *
     * @param map contient chaque variable et la taille de son domaine
     * @return la variable avec le plus grand domaine
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




