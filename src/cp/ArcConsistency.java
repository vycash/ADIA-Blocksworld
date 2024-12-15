package cp;
import java.util.*;
import modelling.*;
/**
 * Classe ArcConsistency
 *
 * Gère la cohérence d'arc pour s'assurer que les domaines de valeurs des variables respectent les contraintes.
 */

public class ArcConsistency {

    private Set<Constraint> constraints ;

    /**
     * constructeur de ArcConsistency
     *
     * @param constraints ensemble de contraintes (doit contenir seulement des contraintes unaires ou binaires)
     * @throws IllegalArgumentException si une contrainte n'est pas unaire ou binaire
     */
    public ArcConsistency(Set<Constraint> constraints){

        for (Constraint constraint : constraints){
            if ( constraint.getScope().size()>2) {
                throw new IllegalArgumentException("Ni unaire ni binaire");
            }
        }

        this.constraints = constraints;
    }

    /**
     * @return les contraintes définies
     */
    public Set<Constraint> getConstraints(){ return this.constraints; }


    public void setConstraints(Set<Constraint> constraints) { this.constraints = constraints; }

    /**
     * Vérifie la cohérence de noeud sur les domaines
     * Supprime les valeurs des domaines qui ne satisfont pas les contraintes unaires
     *
     * @param domains ensemble des domaines des variables
     * @return false si au moins un domaine est vidé, true sinon
     */

    public boolean enforceNodeConsistency(Map<Variable, Set<Object>> domains) {
        
        boolean aucunVide = true;
    
        for (Map.Entry<Variable, Set<Object>> entry : domains.entrySet()) {
            Variable var = entry.getKey();
            Set<Object> domain = entry.getValue();
            
            boolean vide = !auxNodeConsistency(var, domain);
            if (vide == true) {
                aucunVide = false;
            }
        }

        return aucunVide;
    }
    

    /**
     * vérifie la cohérence de noeud pour une variable donnée en fonction de son domaine
     * supprime les valeurs du domaine qui ne satisfont pas les contraintes unaires définies sur cette variable
     *
     * @param var la variable dont on veut vérifier la cohérence
     * @param domain le domaine des valeurs possibles pour la variable
     * @return true si le domaine n'est pas vide après la suppression des valeurs incohérentes
     *         false si le domaine est vide
     */
    private boolean auxNodeConsistency(Variable var, Set<Object> domain) {
        // Si le domaine est vide, retourne false directement
        if (!domain.isEmpty()) {
            Set<Object> toDelete = new HashSet<>();
    
            // Parcourt les contraintes et identifie les valeurs à supprimer
            for (Constraint constraint : constraints) {
                // Vérifie seulement les contraintes qui ne concernent que cette variable (contraintes unaires)
                if (constraint.getScope().contains(var) && constraint.getScope().size() == 1) {
                    for (Object v : domain) {
                        try {
                            // Si la valeur v ne satisfait pas la contrainte, on la marque pour suppression
                            if (!constraint.isSatisfiedBy(Collections.singletonMap(var, v))) {
                                toDelete.add(v);
                            }
                        } catch (Exception e) {
                            // Si y a une exception, on considère la valeur comme incohérente
                            toDelete.add(v);
                        }
                    }
                }
            }
            // Enlève toutes les valeurs incohérentes du domaine
            domain.removeAll(toDelete);
    
            // Retourne true si le domaine n'est pas vide après suppression
            return !domain.isEmpty();
        }
        return false;// Retourne false si le domaine est vide
    }

    /**
     * Méthode revise, prenant en arguments, dans cet ordre :
     * une variable v1, son domaine D1, une autre variable v2, et son domaine D2. 
     * La méthode supprime, en place, toutes les valeurs v1 de D1 pour lesquels il n’existe aucune valeur v2 de D2 supportant v1 
     * pour toutes les contraintes portant sur v1 et v2.
     * La méthode retourne true si au moins une valeur a été supprimée de D1 ( false sinon )
     * Revise:
     * ▶ Réduit la taille des domaines
     * ▶ Supprime les valeurs non viables
     * ▶ Une valeur est viable si et seulement si elle possède au moins un support pour chacune des contraintes
     * portant sur elle
     *
     * @param v1 première variable
     * @param D1 domaine de la première variable
     * @param v2 deuxième variable
     * @param D2 domaine de la deuxième variable
     * @return true si une valeur a été supprimée de D1, false sinon
     */


    public boolean revise(Variable v1, Set<Object> D1, Variable v2, Set<Object> D2) {

        boolean deleted = false;
        Set<Object> toDelete = new HashSet<>();
    
        // Parcourt chaque valeur dans le domaine de v1
        for (Object val1 : D1) {
            // Utilise la fonction auxiliaire pour vérifier si val1 est supportée
            if (!isSupported(v1, val1, v2, D2)) {
                toDelete.add(val1);
            }
        }
    
        // Supprime toutes les valeurs non supportées de D1
        if (!toDelete.isEmpty()) {
            D1.removeAll(toDelete);
            deleted = true;
        }
    
        return deleted;
        
    }
    


    /**
     * Vérifie si une valeur dans le domaine de v1 est supportée par une valeur
     * dans le domaine de v2, en fonction des contraintes binaires entre v1 et v2
     *
     * @param v1 la première variable
     * @param val1 la valeur de la première variable qu'on souhaite vérifier
     * @param v2 la deuxième variable
     * @param D2 le domaine de la deuxième variable
     * @return true si val1 est supportée par au moins une valeur dans D2, false sinon
     */
    private boolean isSupported(Variable v1, Object val1, Variable v2, Set<Object> D2) {
    // Parcourt chaque valeur dans le domaine de v2
        for (Object val2 : D2) {
            Map<Variable, Object> partialAssignment = new HashMap<>();
            partialAssignment.put(v1, val1);
            partialAssignment.put(v2, val2);
    
            // Vérifie si cette assignation satisfait toutes les contraintes entre v1 et v2
            boolean isSupported = true;
            for (Constraint constraint : constraints) {
                // Vérifie si la contrainte concerne à la fois v1 et v2
                if (constraint.getScope().contains(v1) && constraint.getScope().contains(v2)) {
                    try {
                        // Teste si la contrainte est satisfaite pour l'assignation partielle v1, v2
                        if (!constraint.isSatisfiedBy(partialAssignment)) {
                            isSupported = false;
                            break;
                        }
                    } catch (Exception e) {
                        // Si une exception est lancée, considère que la valeur n'est pas supportée
                        isSupported = false;
                        break;
                    }
                }
            }
    
            // Si une valeur supporte val1, retourne true
            if (isSupported) {
                return true;
            }
        }
    
        // Retourne false si aucune valeur dans D2 ne supporte val1
        return false;
    }

    /**
     * méthode nommée ac1, qui prend en argument un ensemble de domaines (de type Map<Variable, Set<Objec>>), 
     * filtre tous les domaines en place en utilisant ac1, jusqu’à stabilité, 
     * et retourne false si au moins un domaine a été vidé (true sinon).
     *
     * AC1 :
     * ▶ Applique autant que possible Revise à tous les arcs sur lesquels il y a une contrainte
     * ▶ Si aucun domaine n’a été modifié, la procédure prend fin
     * ▶ Réapplique Revise à tous les arcs, même aux arcs non modifiés par la passe précédente
     *
     * @param domains ensemble des domaines des variables
     * @return false si un domaine est vide après filtrage, true sinon
     */
    public boolean ac1(Map<Variable, Set<Object>> domains) {
        
        // si l'un des domaines est vide lors du filtrage des contraintes unaires
        if (!enforceNodeConsistency(domains)) {
            // renvoyer false
            return false;
        }
        
        // variables pour pour suivre si y'as un changement
        boolean change = true;
        // stocker les variables ici pour faciliter la manipulation
        Set<Variable> variables = domains.keySet();
    
        // tant que y'as des changements la procedure continue
        while (change) {
            change = false;

            // applique revise pour chaque couple de variables 
            for (Variable v1 : variables ) {
                Set<Object> domain1 = domains.get(v1);
                for (Variable v2 : variables) {
                    Set<Object> domain2 = domains.get(v2);
                    // si les deux variables sont différentes
                    // et que revise a modifié au moins un de leurs domaines
                    // met change à true
                    if (v1!=v2 && revise(v1, domain1, v2, domain2)) {
                        change = true;
                    }
                }
            }
        }
    
        // teste que chaque domaine de chaque variable n'est pas vide
        for (Variable v : variables) {
            if (domains.get(v).isEmpty()) {
                // si l'un est vide renvoie false
                return false;
            }
        }
        // si tout les domaines sont vides et qui sont tous filtrés
        // renvoie true
        return true;
    }


    
    
    



}


