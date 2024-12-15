/**
 * Classe abstraite AbstractSolver
 *
 * Cette classe sert de base pour tous les solveurs de contraintes.
 * Elle prend un ensemble de variables et de contraintes au début.
 */
package cp;
import java.util.*;
import modelling.*;

/**
 * Classe AbstractSolver
 *
 * Casse de base pour créer des solveurs de problèmes de contraintes.
 */
public abstract class AbstractSolver implements Solver{

    private Set<Variable> variables;
    private Set<Constraint> contraintes;

    /**
     * Constructeur de AbstractSolver
     *
     * @param variables ensemble des variables du problème
     * @param contraintes ensemble des contraintes du problème
     */
    
    public AbstractSolver(Set<Variable> variables,Set<Constraint> contraintes){
        this.variables=variables;
        this.contraintes=contraintes;
    }

    /**
     * @return les variables du problème
     */
    public Set<Variable> getVariables(){
        return this.variables;
    }

    /**
     * @return les contraintes du problème
     */
    public Set<Constraint> getContraintes(){
        return this.contraintes;
    }


    /**
     * Vérifie si l'affectation partielle satisfait toutes les contraintes appliquées possibles
     *
     * @param affectationPartielle une map des variables et leurs valeurs assignées
     * @return true si toutes les contraintes possibles à tester sont satisfaites sinon false
     */
    public boolean isConsistent(Map<Variable, Object> affectationPartielle) {
        if (!contraintes.isEmpty()){
            for (Constraint contrainte : getContraintes()) {
                    // Vérifie si toutes les variables de la contrainte sont assignées
                    boolean allAssigned = true;
                    for (Variable var : contrainte.getScope()) {
                        if (!affectationPartielle.containsKey(var) || affectationPartielle.get(var) == null) {
                            allAssigned = false; // Une variable non assignée est trouvée
                            break;
                        }
                    }
    
                    // Si toutes les variables sont assignées, on évalue la contrainte
                    if (allAssigned) {
                        boolean ok = contrainte.isSatisfiedBy(affectationPartielle);
                        if (!ok) {
                            return false; // Retourne faux si une contrainte n'est pas respectée
                        }
                    }
                }
            }
        return true; // Si y a pas de contraintes, tout est ok
    }
    
    
}