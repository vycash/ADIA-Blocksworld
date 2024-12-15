package cp;

import java.util.*;
import modelling.*;
/**
 * Interface ValueHeuristic
 *
 * Utilisée pour définir une stratégie d'ordre sur les valeurs d'une variable.
 * Les classes qui implémentent cette interface devront fournir une méthode pour
 * organiser les valeurs d'une variable selon un certain critère (par exemple, aléatoire, par ordre croissant, etc.).
 */

public interface ValueHeuristic {
    
    /**
     * Organise les valeurs du domaine d'une variable selon une certaine heuristique.
     *
     * @param v la variable dont on veut organiser les valeurs
     * @param domain ensemble des valeurs possibles pour la variable
     * @return une liste des valeurs ordonnées selon l'heuristique
     */

    public List<Object> ordering(Variable v, Set<Object> domain);
    
}
