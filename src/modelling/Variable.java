package modelling;
import java.util.* ;


public class Variable {

    private String name;
    private Set<Object> domain;
    
    public Variable(String name,Set<Object> domain){
        this.name = name;
        this.domain = domain;
    }
    public Variable(String name){
        this(name,null);
    }
    
    public String getName() {
        return name;
    }
    public Set<Object> getDomain() {
        return this.domain;
    }

    @Override
    public boolean equals(Object o) {
        return this.name.equals(((Variable) o).getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {    
        return "Name : "+this.name+", Domain : "+this.domain;
    }

    
}

