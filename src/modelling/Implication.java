package modelling;
import java.util.*;

/*Écrire une classe nommée Implication, pour représenter les contraintes 
de la forme (v1 ∈S1) → (v2 ∈ S2), où v1 et v2 sont des variables, 
et S1 et S2 des sous-ensembles de leurs domainesrespectifs. 

Une telle contrainte est satisfaite si:
    
    dès lors qu’une valeur de S1  est affectée à v1, une valeur de S2 est affectée à v2.
    càd : v1 != null AND v2 != null 
    
    et

        elle est satisfaite si ce n’est pas une valeur de S1 qui est affectée à v1, 
        càd S1.contains(valeur1) == false

    quelleque soit la valeur affectée à v2.  
*/

public class Implication implements Constraint{

    private Variable v1,v2;
    private Set<Object> S1, S2;

    public Implication(Variable v1,Set<Object> S1, Variable v2, Set<Object> S2){
        this.v1=v1;
        this.v2=v2;
        this.S1=S1;
        this.S2=S2;
    }

    public Variable getV1() {
        return v1;
    }

    public Variable getV2() {
        return v2;
    }

    public Set<Object> getS1() {
        return S1;
    }

    public Set<Object> getS2() {
        return S2;
    }

    public Set<Variable> getScope(){
        return new HashSet<>(Arrays.asList(v1, v2));
    }

    public boolean isSatisfiedBy(Map<Variable,Object> map){

        for (Variable var : getScope()) {
            if (!map.containsKey(var)) {
                throw new IllegalArgumentException("Affectation invalide pour la contrainte d'implication "+v1+"\n"+v2+"\n"+map);
            }
        }

        Object value1=map.get(v1);
        Object value2=map.get(v2);

        return (S1.contains(value1)==false || (S1.contains(value1) && S2.contains(value2)));

    }

    @Override
    public String toString() {
        return "ImplicationConstraint [ "+v1.getName()+" value belongs to "+S1+" ---implies---> "+v2.getName()+" value belongs to "+S2+" ]\n";
    }
}
