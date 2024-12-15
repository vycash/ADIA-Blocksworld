package cp;

import modelling.*;
import java.util.*;
/**
 * Classe HeuristicMACSolver
 *
 * Utilise l'algorithme MAC (Maintenance de Cohérence d'Arc) avec des heuristiques sur les variables
 * et les valeurs pour résoudre des problèmes de satisfaction de contraintes.
 * On peut spécifier les heuristiques pour choisir la variable et la valeur à traiter en premier.
 */

public class HeuristicMACSolver extends AbstractSolver {
    /*
    Écrire une classe nommée HeuristicMACSolver, étendant la classe AbstractSolver,
    et dont le constructeur prend en arguments, dans cet ordre, un ensemble de variables, un ensemble de
    contraintes, une heuristique sur les variables, et une heuristique sur les valeurs.
    */
    private VariableHeuristic varHeuristic;
    private ValueHeuristic valHeuristic;
    private ArcConsistency arcConsistency;

    /**
     * Constructeur de HeuristicMACSolver
     *
     * @param variables ensemble des variables du problème
     * @param constraints ensemble des contraintes du problème
     * @param varHeuristic stratégie pour choisir la prochaine variable
     * @param valHeuristic stratégie pour ordonner les valeurs d'une variable
     */
    public HeuristicMACSolver(Set<Variable> variables,Set<Constraint> constraints, VariableHeuristic varHeuristic, ValueHeuristic valHeuristic){
        super(variables,constraints);
        this.varHeuristic = varHeuristic;
        this.valHeuristic = valHeuristic;
        this.arcConsistency = new ArcConsistency(constraints);
    }

    /**
     * Constructeur par défaut de HeuristicMACSolver qui initalise la varHeuristic à une instanec de DomainSizeVariableHeuristic,
     * et la valHeuristic à une instance de RandomValueHeuristic
     *
     * @param variables ensemble des variables du problème
     * @param constraints ensemble des contraintes du problème
     * 
     */
    public HeuristicMACSolver(Set<Variable> variables,Set<Constraint> constraints){
        this(variables,constraints,new DomainSizeVariableHeuristic(),new RandomValueHeuristic());
    }
    // Getters et setters pour les heuristiques et la cohérence d'arc
    /**
     * getter pour l'heuristique de variable actuellement utilisée.
     * 
     * @return l'heuristique de variable (VariableHeuristic)
     */
    public VariableHeuristic getVarHeuristic() {
        return varHeuristic;
    }

    /**
     * reDéfinit l'heuristique de variable à utiliser.
     * 
     * @param varHeuristic la nouvelle heuristique de variable (VariableHeuristic)
     */
    public void setVarHeuristic(VariableHeuristic varHeuristic) {
        this.varHeuristic = varHeuristic;
    }

    /**
     * getter pour l'heuristique de valeur actuellement utilisée.
     * 
     * @return l'heuristique de valeur (ValueHeuristic)
     */
    public ValueHeuristic getValHeuristic() {
        return valHeuristic;
    }

    /**
     * reDéfinit l'heuristique de valeur à utiliser.
     * 
     * @param valHeuristic la nouvelle heuristique de valeur (ValueHeuristic)
     */
    public void setValHeuristic(ValueHeuristic valHeuristic) {
        this.valHeuristic = valHeuristic;
    }

    /**
     * getter pour l'instance d'ArcConsistency actuellement utilisée.
     * 
     * @return la méthode d'arc-consistance (ArcConsistency)
     */
    public ArcConsistency getArcConsistency() {
        return arcConsistency;
    }

    /**
     * reDéfinit l'instance d'ArcConsistency actuellement à utiliser.
     * 
     * @param arcConsistency la nouvelle méthode d'arc-consistance (ArcConsistency)
     */
    public void setArcConsistency(ArcConsistency arcConsistency) {
        this.arcConsistency = arcConsistency;
    }
    
    /**
     * Méthode principale pour résoudre le problème en utilisant l'algorithme MAC avec les heuristiques pour récupérer les meilleurs candidats à traiter en premier.
     *
     * @return une map avec l'affectation complète des variables si une solution est trouvée, sinon null
     */
    @Override
    public Map<Variable, Object> solve() {

        Map<Variable, Object> partialAssignment = new HashMap<>();
        Set<Variable> unassignedVariables = new HashSet<>(getVariables());
        Map<Variable, Set<Object>> evolvingDomains = new HashMap<>();

        // Initialisation des domaines évolutifs pour chaque variable
        for (Variable variable : getVariables()) {
            evolvingDomains.put(variable, new HashSet<>(variable.getDomain()));
        }

        return MAC(partialAssignment, unassignedVariables, evolvingDomains);
    }

    /**
     * Algorithme MAC récursif pour essayer de trouver une solution avec les heuristiques pour récuperer les meilleurs candidats.
     *
     * @param partialAssignment affectation partielle des variables
     * @param unassigned ensemble des variables non assignées
     * @param evolvingDomains domaines actuels des variables, mis à jour au fur et à mesure
     * @return une map avec l'affectation complète si une solution est trouvée, sinon null
     */
    private Map<Variable, Object> MAC(Map<Variable, Object> partialAssignment, Set<Variable> unassigned, Map<Variable, Set<Object>> evolvingDomains) {
        
        // Si toutes les variables sont assignées, on a trouvé une solution
        if (unassigned.isEmpty()) {
            return partialAssignment;
        }
        
        // Applique la cohérence d'arc ; si échec, retourne null pour faire un backtrack
        if (!this.arcConsistency.ac1(evolvingDomains)) {
            return null;
        }
        
        
        Variable xi = varHeuristic.best(new HashSet<>(unassigned), evolvingDomains);
    
        /// Vérifie que la variable et son domaine ne sont pas nuls
        if (xi == null || evolvingDomains.get(xi) == null) {
            return null;
        }
    
        unassigned.remove(xi);
        // Obtenez les valeurs dans un ordre défini par l'heuristique
        List<Object> orderedValues = valHeuristic.ordering(xi, evolvingDomains.get(xi));
    
        // Essaye chaque valeur dans le domaine de `xi`
        for (Object vi : orderedValues) {
            Map<Variable, Object> newAssignment = new HashMap<>(partialAssignment);
            newAssignment.put(xi, vi);
            
            // Vérifie si l'affectation est cohérente
            if (isConsistent(newAssignment)) {
                Map<Variable, Object> result = MAC(newAssignment, unassigned, evolvingDomains);
    
                if (result != null) {
                    return result;
                }
            }
        }
    
        // Si aucune solution n'est trouvée, on remet xi dans les variables non assignées
        unassigned.add(xi);
    
        return null;
    }
    
    

}


