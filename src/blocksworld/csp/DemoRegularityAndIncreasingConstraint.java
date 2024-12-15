package blocksworld.csp;
import blocksworld.world.*;


/**
 *  classe qui fait démonstration de recherche d'un état qui satisfait les contraintes de Régularité et de Croissance
 */
public class DemoRegularityAndIncreasingConstraint {
    public static void main(String[] args){

        int b = 6;
        int s = 4;

        BlockWorldWithConstraints w = new BlockWorldWithRegularityAndIncreasingConstraint(b, s);

        DemoCSP d = new DemoCSP(w,"Regularity+Increasing contrainte");
        d.demo();
    }   
}
