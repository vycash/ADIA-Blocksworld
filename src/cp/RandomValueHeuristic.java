package cp;

import java.util.*;
import modelling.*;
/**
 * Classe RandomValueHeuristic
 *
 * Cette classe choisit une valeur aléatoirement dans le domaine d'une variable,
 * en utilisant un générateur de nombres aléatoires fourni.
 */

public class RandomValueHeuristic implements ValueHeuristic{
    private Random rand;// Générateur de nombres aléatoires

    /**
     * Constructeur de RandomValueHeuristic
     *
     * @param rand générateur aléatoire pour mélanger les valeurs
     */
    public RandomValueHeuristic(Random rand){
        this.rand=rand;
    }

     /**
     * Constructeur par défaut de RandomValueHeuristic qui initialise le generateur aleatoire à une nouvelle instance de Rand
     *
     */
    public RandomValueHeuristic(){
        this(new Random());
    }

    /**
     * Retourne les valeurs du domaine d'une variable dans un ordre aléatoire.
     *
     * @param v la variable pour laquelle on veut ordonner les valeurs
     * @param domain ensemble des valeurs possibles (domaine) pour la variable
     * @return une liste des valeurs du domaine, mélangée de façon aléatoire
     * @throws IllegalArgumentException si le domaine ou la variable est null
     */
    @Override
    public List<Object> ordering(Variable v, Set<Object> domain) {
        if (domain == null || v == null) {
            throw new IllegalArgumentException("Domain for variable " + v + " is null");
        }
        // Convertit le domaine en liste pour pouvoir le mélanger
        List<Object> list = new ArrayList<>(domain);
        Collections.shuffle(list, rand);// Mélange les valeurs de manière aléatoire
        return list;
    }


}


