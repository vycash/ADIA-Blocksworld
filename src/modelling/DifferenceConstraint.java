package modelling;
import java.util.*;
/*Écrire une classe nommée DifferenceConstraint, pour représenter 
les contraintes de la forme v  != v2, où v1 et v2 sont des variables. */

public class DifferenceConstraint implements Constraint{
    
    private Variable v1, v2;

    public DifferenceConstraint(Variable v1, Variable v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public Variable getV1() {
        return v1;
    }

    public Variable getV2() {
        return v2;
    }

    public Set<Variable> getScope() {
        return new HashSet<>(Arrays.asList(v1, v2));
    }

    @Override
    public boolean isSatisfiedBy(Map<Variable, Object> instances) {
        // Vérification si chaque variable a une affectation correspondante
        if (!instances.containsKey(v1) || !instances.containsKey(v2)) {
            throw new IllegalArgumentException("Affectation incomplète pour la contrainte");
        }
        
        // Obtention des valeurs affectées à v1 et v2
        Object valeur1 = instances.get(v1);
        Object valeur2 = instances.get(v2);

        // Vérification si les valeurs de v1 et v2 sont différentes
        return !Objects.equals(valeur1, valeur2);
    }

    @Override
    public String toString() {
        return "DifferenceConstraint [ "+v1.getName()+" != "+v2.getName()+" ]\n";
    }
}
