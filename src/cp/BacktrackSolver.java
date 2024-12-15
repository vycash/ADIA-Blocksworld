package cp;
import java.util.*;
import modelling.*;

/**
 * Classe BacktrackSolver
 *
 * Cette classe utilise un algorithme de backtracking pour résoudre les problèmes
 * de satisfaction de contraintes et herite d'AbstractSolver.
 */

public class BacktrackSolver extends AbstractSolver {
    /**
     * Constructeur de BacktrackSolver
     *
     * @param variables ensemble des variables du problème
     * @param contraintes ensemble des contraintes du problème
     */
    public BacktrackSolver(Set<Variable> variables, Set<Constraint> contraintes) {
        super(variables, contraintes);
    }
    
    /**
     * Lance la résolution du problème
     *
     * @return une Map avec les variables et leurs valeurs si une solution est trouvée
     *         sinon null si pas de solution possible
     */
    public Map<Variable, Object> solve(){

        Map<Variable, Object> affectation = new HashMap<>();
        Stack<Variable> unassignedVariables = new Stack<>();
        unassignedVariables.addAll(getVariables());
        
        return backtrack(affectation,unassignedVariables);
    }


    /**
     * Fonction récursive de backtracking pour essayer de trouver une affectation des variables qui satisfait toutes les contraintes
     *
     * @param affectation l'affectation actuelle des variables
     * @param unassignedVariables la pile des variables qui n'ont pas encore de valeur
     * @return la solution complète si trouvée, sinon null
     * 
     */

    private Map<Variable, Object> backtrack(Map<Variable, Object> affectation, Stack<Variable> unassignedVariables){

        // Si y a plus de variables à affecter
        if (unassignedVariables.isEmpty()) {
            // Si oui, on a trouvé une affectation valide pour toutes les variables
            return affectation; // Retourne l'affectation
        }
    
        // Prend une variable de la liste des variables non affectées
        Variable var = unassignedVariables.pop();
    
        // Itère sur les valeurs du domaine de la variable
        for(Object value : var.getDomain()){
            // Affecte à la variable une valeur du domaine
            affectation.put(var, value);
            
            // Teste si cette affectation est cohérente
            if(isConsistent(affectation)){
                // Si oui, appelle récursivement la fonction backtrack avec l'affectation partielle et les variables non affectées
                Map<Variable, Object> result = backtrack(affectation, unassignedVariables);
                
                // Si toutes les variables sont affectées et toutes les contraintes sont satisfaites
                if(result != null){
                    return result; // Retourne la solution trouvée
                }
            }
            // Réinitialise l'affectation de la variable pour essayer une autre valeur
            affectation.remove(var);
        }
    
        // Si aucune valeur ne marche, remet la variable dans les non affectées
        unassignedVariables.push(var);
    
        // Retourne null si aucune solution n'a marché
        return null;
    }
    
}
