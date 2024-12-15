package modelling;

import java.util.*;
public class DemoModelling {
    public static void main(String[] args) {

        HashSet<Object> domain = new HashSet<>(); // creation du domaine [0,100]
        for (int i = 0; i <= 100; i++) {
            domain.add(i);
        }

        // Création des variables
        Variable v1 = new Variable("v1", domain);
        Variable v2 = new Variable("v2", domain);
        Variable v3 = new Variable("v3", domain);

        // Création des ensembles de contraintes (S1, S2, etc.)
        Set<Object> S1 = Set.of(2); // v1 doit être égal à 2 pour l'implication
        Set<Object> S2 = Set.of(4); // v2 doit être égal à 4 pour l'implication

        // Instanciation des contraintes
        UnaryConstraint unaryConstraint = new UnaryConstraint(v1, Set.of(1, 2)); // v1 doit être dans {1, 2}
        Implication implicationConstraint = new Implication(v1, S1, v2, S2); // Si v1 == 2 alors v2 doit être 4
        DifferenceConstraint differenceConstraint = new DifferenceConstraint(v2, v3); // v2 != v3

        // Création des affectations (assignations de valeurs)
        Map<Variable, Object> assignment1 = new HashMap<>();
        assignment1.put(v1, 1);
        assignment1.put(v2, 3);
        assignment1.put(v3, 10);

        Map<Variable, Object> assignment2 = new HashMap<>();
        assignment2.put(v1, 2);
        assignment2.put(v2, 4);
        assignment2.put(v3, 20);

        Map<Variable, Object> assignment3 = new HashMap<>();
        assignment3.put(v1, 2);
        assignment3.put(v2, 3);
        assignment3.put(v3, 3);

        Map<Variable, Object> assignment4 = new HashMap<>();
        assignment4.put(v1, 4);
        assignment4.put(v2, 3);
        assignment4.put(v3, 20);

        Map<Variable, Object> assignment5 = new HashMap<>();
        assignment4.put(v2, 3);
        assignment4.put(v3, 20);

        Map<Variable, Object> assignment6 = new HashMap<>();
        assignment4.put(v1, 3);
        assignment4.put(v3, 20);

        Map<Variable, Object> assignment7 = new HashMap<>();
        assignment4.put(v1, 3);
        assignment4.put(v2, 20);

        System.out.println("\n============== DEMO DE MODELLING ==============\n");

        System.out.println("UnaryConstraint définie avec la variable "+v1+" sur l'ensemble "+unaryConstraint.getS());
        System.out.println("Condition de Satisfaction de UnaryConstraint : v1 ∈  unaryConstraint.getS()");
        System.out.println("la valeur de v1 doit appartenir à l'ensemble défini à UnaryConstraint\n");

        System.out.println("UnaryConstraint sur v1 avec assignment1 (v1=1, v2=3, v3=10)");
        System.out.println("Contrainte satisfaite? " + unaryConstraint.isSatisfiedBy(assignment1) + "\n");

        System.out.println("UnaryConstraint sur v1 avec assignment2 (v1=2, v2=4, v3=20)");
        System.out.println("Contrainte satisfaite? " + unaryConstraint.isSatisfiedBy(assignment2) + "\n");

        System.out.println("UnaryConstraint sur v1 avec assignment4 (v1=4, v2=3, v3=20)");
        System.out.println("Contrainte satisfaite? "+unaryConstraint.isSatisfiedBy(assignment4)+"\n");

        System.out.println("UnaryConstraint sur v1 avec assignment5 (v2=3, v3=20)");
        System.out.print("Contrainte satisfaite? ");

        try {  unaryConstraint.isSatisfiedBy(assignment5); }
        catch (IllegalArgumentException e) { System.out.println("false\n"+e.getMessage() + "\n"); } 
 
        
        System.out.println("----------------------------------------------------------------\n");

        System.out.println("ImplicationConstraint définie avec les variables "+v1+" "+v2+" sur les ensembles "+implicationConstraint.getS1()+","+implicationConstraint.getS2());
        System.out.println("Condition de Satisfaction de ImplicationConstraint : \n(v1 ∈  ImplicationConstraint.getS1()) → (v2 ∈ ImplicationConstraint.getS2) OU (v1 ∈/  ImplicationConstraint.getS1())");
        System.out.println("si v1 appartient à S1 -> v2 appartient à S2 OU v1 n'appartient pas à S1 et v2 est ignorée\n");

        System.out.println("ImplicationConstraint sur assignment1 (v1=1, v2=3, v3=10)");
        System.out.println("Contrainte satisfaite? " + implicationConstraint.isSatisfiedBy(assignment1) + "\n");

        System.out.println("ImplicationConstraint sur assignment2 (v1=2, v2=4, v3=20)");
        System.out.println("Contrainte satisfaite? " + implicationConstraint.isSatisfiedBy(assignment2) + "\n");

        System.out.println("ImplicationConstraint sur assignment3 (v1=2, v2=3, v3=3)");
        System.out.println("Contrainte satisfaite? " + implicationConstraint.isSatisfiedBy(assignment3) + "\n");

        System.out.println("ImplicationConstraint sur assignment5 (v2=3, v3=20)");
        System.out.print("Contrainte satisfaite? ");

        try {  implicationConstraint.isSatisfiedBy(assignment5); }
        catch (IllegalArgumentException e) { System.out.println("false\n"+e.getMessage() + "\n"); } 

        System.out.println("ImplicationConstraint sur assignment6 (v1=3, v3=20)");
        System.out.print("Contrainte satisfaite? ");

        try {  implicationConstraint.isSatisfiedBy(assignment6); }
        catch (IllegalArgumentException e) { System.out.println("false\n"+e.getMessage() + "\n"); } 

        

        System.out.println("----------------------------------------------------------------\n");

        System.out.println("differenceConstraint définie avec la variable "+v2+" "+v3);
        System.out.println("Condition de Satisfaction de differenceConstraint : v2 != v3");
        System.out.println("la valeur de v2 est differente de v3\n");

        System.out.println("DifferenceConstraint sur assignment1 (v1=1, v2=3, v3=10)");
        System.out.println("Contrainte satisfaite? " + differenceConstraint.isSatisfiedBy(assignment1) + "\n");

        System.out.println("DifferenceConstraint sur assignment2 (v1=2, v2=4, v3=20)");
        System.out.println("Contrainte satisfaite? " + differenceConstraint.isSatisfiedBy(assignment2) + "\n");

        System.out.println("DifferenceConstraint sur assignment3 (v1=2, v2=3, v3=3)");
        System.out.println("Contrainte satisfaite? " + differenceConstraint.isSatisfiedBy(assignment3) + "\n");

        System.out.println("DifferenceConstraint sur assignment6 (v1=3, v3=20)");
        System.out.print("Contrainte satisfaite? ");

        try {  differenceConstraint.isSatisfiedBy(assignment6); }
        catch (IllegalArgumentException e) { System.out.println("false\n"+e.getMessage() + "\n");}

        System.out.println("DifferenceConstraint sur assignment7 (v1=3, v2=20)");
        System.out.print("Contrainte satisfaite? ");

        try {  differenceConstraint.isSatisfiedBy(assignment7); }
        catch (IllegalArgumentException e) { System.out.println("false\n"+e.getMessage() + "\n");}


    }
}
