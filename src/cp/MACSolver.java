package cp;

import java.util.*;
import modelling.*;
/**
 * Classe MACSolver
 *
 * Utilise l'algorithme de Maintien de Cohérence d'Arc (MAC) pour résoudre les problèmes de satisfaction de contraintes.
 * Hérite de AbstractSolver.
 */



public class MACSolver extends AbstractSolver {

    private ArcConsistency arcConsistency;

    /**
     * Constructeur de MACSolver
     *
     * @param variables ensemble des variables du problème
     * @param contraintes ensemble des contraintes du problème
     */
    public MACSolver(Set<Variable> variables,Set<Constraint> contraintes){
        super(variables,contraintes);
        this.arcConsistency = new ArcConsistency(contraintes);
    }


    /**
     * Méthode principale pour résoudre le problème avec l'algorithme MAC.
     *
     * @return une Map contenant l'affectation finale des variables si une solution est trouvée, sinon null
     */
    @Override
    public Map<Variable, Object> solve() {

        Map<Variable, Object> partialAssignment = new HashMap<>();
        Stack<Variable> unassignedVariables = new Stack<>();
        unassignedVariables.addAll(getVariables());
        
        Map<Variable, Set<Object>> evolvingDomains = new HashMap<>();

        // Initialisation des domaines évolutifs
        for (Variable variable : getVariables()) {
            evolvingDomains.put(variable, new HashSet<>(variable.getDomain()));
        }

        return MAC(partialAssignment, unassignedVariables, evolvingDomains);
    }



    /**
     * Applique l'algorithme MAC de façon récursive pour trouver une solution au problème.
     *
     * @param partialAssignment affectation partielle actuelle des variables
     * @param unassigned pile des variables non encore affectées
     * @param evolvingDomains les domaines actuels des variables, mis à jour au fur et à mesure
     * @return une Map avec l'affectation complète si une solution est trouvée, sinon null
     */
    private Map<Variable,Object> MAC(Map<Variable,Object> partialAssignment, Stack<Variable> unassigned, Map<Variable,Set<Object>> evolvingDomains) {
        // Si toutes les variables sont assignées, retourne l'affectation actuelle 
        if ( unassigned.isEmpty() ){
            return partialAssignment;
        }

        // Applique la cohérence d'arc ; si elle échoue, retourne null pour backtrack
        if (!this.arcConsistency.ac1(evolvingDomains)){
            return null;
        }
        // Sélectionne une variable non assignée
        Variable xi = unassigned.pop();

        // Itérer à travers le domaine de xi
        for (Object vi : evolvingDomains.get(xi)) {
            
            Map<Variable, Object> newAssignment = new HashMap<>(partialAssignment);
            newAssignment.put(xi, vi);
            // Si l'affectation est cohérente continue avec la prochaine variable
            if (isConsistent(newAssignment)) {

                Map<Variable, Object> result = MAC(newAssignment, unassigned, evolvingDomains);

                if (result != null) {
                    return result;
                }
            }
        }

        // Si aucune solution n'est trouvée, backtrack
        unassigned.push(xi);
        
        return null;


    }
    
}


