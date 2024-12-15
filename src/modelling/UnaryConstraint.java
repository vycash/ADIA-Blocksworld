package modelling;
import java.util.*;



/*
Écrire une classe nommée UnaryConstraint, pour représenter les contraintes 
de la forme v ∈ S, où v est une variable et S un sous-ensemble de son domaine 

càd : la contrainte est satisfaite si la valeur de var appartient à S.
      ==> S.contains(map.get(var))==true
*/
public class UnaryConstraint implements Constraint{

    private Variable var;
    private Set<Object> S;

    public UnaryConstraint(Variable var,Set<Object> S){
        this.var=var;
        this.S=S;
    }
    
    
    public Variable getVar() {
        return var;
    }

    public Set<Object> getS() {
        return S;
    }
    public Set<Variable> getScope(){
        return new HashSet<>(Arrays.asList(var));
    }

    public boolean isSatisfiedBy(Map<Variable,Object> map){

        if (!map.containsKey(var)) {
            throw new IllegalArgumentException("Affectation invalide pour la contrainte unaire");
        }
        
        return S.contains(map.get(var));
        
    }

    @Override
    public String toString(){
        return "UnaryConstraint [ "+var.getName()+" ∈  "+S+" ]";
    }
    
}