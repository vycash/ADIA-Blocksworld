package cp;
import java.util.*;
import modelling.*;
/**
 * Class demo du package cp
 */
public class DemoCp {
    public static void main(String[] args){

        System.out.println("\n==================== Demo dela classe BackTrackSolver ====================\n");

        /*
         * le nb de variables aléatoires et de contraintes aléatoires créés est modifiable dans les parametres de la creation
         * de randomVariables et randomConstraints dans les lignes suivantes : 15/16
         */

        Set<Variable> randomVariables = generateRandomVariables(20); // nb de variables aléatoire changeable
        Set<Constraint> randomConstraints = generateRandomConstraints(randomVariables, 30); // nb de contraintes aléatoire changeable

        Variable x0 = new Variable("x0", new HashSet<>(Arrays.asList(0, 1, 2, 3, 4)));
        Variable x1 = new Variable("x1", new HashSet<>(Arrays.asList(0, 1, 2, 3, 4)));
        Variable x2 = new Variable("x2", new HashSet<>(Arrays.asList(0, 1, 2, 3, 4)));
    
        Set<Variable> variables = new HashSet<>();
        variables.add(x0);
        variables.add(x1);
        variables.add(x2);


        Constraint c1 = new DifferenceConstraint(x0,x1);
        Constraint c2 = new DifferenceConstraint(x1,x2);
        Constraint c3 = new DifferenceConstraint(x2,x1);
        Constraint c4 = new DifferenceConstraint(x2,x0);
        Constraint c5 = new DifferenceConstraint(x0,x2);
        Constraint c6 = new DifferenceConstraint(x1,x0);

        Set<Constraint> contraintesBinaires = new HashSet<>();
        contraintesBinaires.add(c1);
        contraintesBinaires.add(c2);
        contraintesBinaires.add(c3);
        contraintesBinaires.add(c4);
        contraintesBinaires.add(c5);
        contraintesBinaires.add(c6);

        afficheVariables(variables);
        afficheConstraints(contraintesBinaires);


        Solver backtrack = new BacktrackSolver(variables,contraintesBinaires);
    
        System.out.println("\n**** Resultat de l'Execution du backtrackSolver sur ces variables et ces contraintes : ****\n");
        afficheMap(backtrack.solve(),"backmap");

        
        System.out.println("\n==================== Demo dela classe ArcConsistency ====================\n");

        System.out.println("\n-------------------- Demo de enforceNodeConsistency ---------------------\n");

        
        Constraint c7 = new UnaryConstraint(x0,x0.getDomain());
        Constraint c8 = new UnaryConstraint(x1,x1.getDomain());
        Constraint c9 = new UnaryConstraint(x2,x2.getDomain());

        Set<Constraint> contraintesUnaires = new HashSet<>();
        contraintesUnaires.add(c7);
        contraintesUnaires.add(c8);
        contraintesUnaires.add(c9);

        ArcConsistency test = new ArcConsistency(contraintesUnaires);
        afficheVariables(variables);
        afficheConstraints(contraintesUnaires);


        Map<Variable,Set<Object>> map1 = new HashMap<>();
        map1.put(x0, new HashSet<>(Arrays.asList(0,1,2,3,4,5,7,8)));
        map1.put(x1, new HashSet<>(Arrays.asList(-2,-1,1,2,3,4,5,7,8)));
        map1.put(x2, new HashSet<>(Arrays.asList(-2,-1,0,1,3,5,7,8,9)));

    
        System.out.println("\n**** execution de enforceNodeConsitency sur ces variables ****\n");
        afficheDomains(map1);
        boolean ok = test.enforceNodeConsistency(map1);

        if ( ok ){ 
            System.out.println("execution effectuée sans vider de domaines:"); 
            afficheDomains(map1);
        }
        else { 
            System.out.println("execution effectuée et au moins un domaine a été vidé :"); 
            afficheDomains(map1);
        }



        Map<Variable,Set<Object>> map2 = new HashMap<>();
        map2.put(x0, new HashSet<>(Arrays.asList(0,1,2,3,4,5,7,8)));
        map2.put(x1, new HashSet<>(Arrays.asList(-2,-1,1,2,3,4,5,7,8)));
        map2.put(x2, new HashSet<>(Arrays.asList(5,7,8,9)));

        System.out.println("\n**** execution de enforceNodeConsitency sur ces variables ****\n");
        afficheDomains(map2);
        ok = test.enforceNodeConsistency(map2);

        if ( ok ){ 
            System.out.println("execution effectuée sans vider de domaines :"); 
            afficheDomains(map2);
        }
        else { 
            System.out.println("execution effectuée et au moins un domaine a été vidé : "); 
            afficheDomains(map2);
        }

        map2.put(x0, generateRandomSet(-10, 10, 10));
        map2.put(x1, generateRandomSet(-10, 10, 10));
        map2.put(x2, generateRandomSet(-10, 10, 10));

        System.out.println("\n**** execution de enforceNodeConsitency sur ces variables avec des attributions aleatoires ****\n");
        afficheDomains(map2);
        ok = test.enforceNodeConsistency(map2);

        if ( ok ){ 
            System.out.println("execution effectuée sans vider de domaines :"); 
            afficheDomains(map2);
        }
        else { 
            System.out.println("execution effectuée et au moins un domaine a été vidé : "); 
            afficheDomains(map2);
        }

        System.out.println("\n-----------------Demo de Revise---------------------\n");

        ArcConsistency test2 = new ArcConsistency(contraintesBinaires);
        afficheConstraints(contraintesBinaires);

        Map<Variable,Set<Object>> map3 = new HashMap<>();
        map3.put(x0, new HashSet<>(Arrays.asList(1,0)));
        map3.put(x1, new HashSet<>(Arrays.asList(1)));

        System.out.println("\n**** execution de revise sur ces variables ****\n");
        afficheDomains(map3);

        ok = test2.revise(x0,map3.get(x0),x1,map3.get(x1));
        if ( ok ){ 
            System.out.println("execution effectuée , au moins une valeur supprimée :"); 
            afficheDomains(map3);
        }
        else { 
            System.out.println("execution effectuée , aucune valeur supprimée :"); 
            afficheDomains(map3);
        }

        map3.put(x0,new HashSet<>(Arrays.asList(0,1,2,3,4)));
        map3.put(x1,new HashSet<>(Arrays.asList(0,1,2,3,4)));

        System.out.println("\n**** execution de revise sur ces variables ****\n");
        afficheDomains(map3);

        ok = test2.revise(x0,map3.get(x0),x1,map3.get(x1));
        if ( ok ){ 
            System.out.println("execution effectuée , au moins une valeur supprimée : "); 
            afficheDomains(map3);
        }
        else { 
            System.out.println("execution effectuée , aucune valeur supprimée : "); 
            afficheDomains(map3);
        }

        System.out.println("\n-----------------Demo de AC1---------------------\n");


        Set<Constraint> allConstraints = contraintesUnaires;
        allConstraints.addAll(contraintesBinaires);


        Map<Variable,Set<Object>> mapRandom = new HashMap<>();
        mapRandom.put(x0, generateRandomSet(-10,10,10));
        mapRandom.put(x1, generateRandomSet(-10,10,10));
        mapRandom.put(x2, generateRandomSet(-10,10,10));


        ArcConsistency test3 = new ArcConsistency(allConstraints);

        afficheConstraints(allConstraints);

        System.out.println("\n**** execution de AC1 sur ces variables avec des attributions aléatoires ****\n");
        afficheDomains(mapRandom);

        ok = test3.ac1(mapRandom);
        if ( ok ){ 
            System.out.println("execution effectuée sans vider de domaines:"); 
            afficheDomains(mapRandom);
        }
        else { 
            System.out.println("execution effectuée et au moins un domaine a été vidé :"); 
            afficheDomains(mapRandom);
        }

        System.out.println("\n==================== Demo dela classe MACSolver ====================\n");


        MACSolver macSolver = new MACSolver(variables,allConstraints);
        Map<Variable,Object> result = macSolver.solve();

        System.out.println("\n-----------------Demo de solve---------------------\n");

        System.out.println("Demo de solve de la classe MACSolver qui utilise les principes de maintenance de la cohérence d'arc\npour trouver une solution efficace à un problème de satisfaction de contraintes");
        
        System.out.println("dans ce cas, les variables sont : \n");
        afficheVariables(macSolver.getVariables());
        
        System.out.println("et les contraintes sont : \n");
        afficheConstraints(macSolver.getContraintes());
        
        afficheMap(result, "resultat de MACSolver");

        System.out.println("\n-----------------Demo de solve avec des valeurs aléatoires ---------------------\n");

        MACSolver macSolverRandom = new MACSolver(randomVariables,randomConstraints);

        System.out.println("et dans ce deuxième cas les variables sont aléatoires : \n");
        afficheVariables(macSolverRandom.getVariables());
        System.out.println("et les contraintes sont : \n");
        afficheConstraints(macSolverRandom.getContraintes());
        afficheMap(macSolverRandom.solve(), "resultat de MACSolverRandom");





        System.out.println("\n==================== Demo dela classe NbConstraintsVariableHeuristic ====================\n");

        NbConstraintsVariableHeuristic heuristicNbConstraints = new NbConstraintsVariableHeuristic(randomConstraints,false);

        System.out.println("\n-----------------Demo de best avec des valeurs aléatoires ---------------------\n");
        System.out.println("Demo de la calsse NbConstraintsVariableHeuristic dont la methode renvoie le meilleur candidant ayant le moins nb de contraintes\nou celui ayant le plus nb de contraintes selon l'attribut plus\n");
        afficheConstraints(heuristicNbConstraints.getContraintes());
        System.out.println("le meilleur candidat qui le minimum de contraintes est  "+heuristicNbConstraints.best(randomVariables, null));
        heuristicNbConstraints.setBoolean(true);
        System.out.println("le meilleur candidat qui le maximum de contraintes est  "+heuristicNbConstraints.best(randomVariables, null));

        
        
        
        
        System.out.println("\n==================== Demo dela classe DomainSizeVariableHeuristic ====================\n");

        DomainSizeVariableHeuristic heuristicDomain = new DomainSizeVariableHeuristic(false);

        System.out.println("\n-----------------Demo de best avec des valeurs aléatoires ---------------------\n");
        System.out.println("Demo de la calsse DomainSizeVariableHeuristic dont la methode renvoie le meilleur candidant ayant le plus grand domain \nou celui ayant le plus petit domain selon l'attribut plus\n");

        Set<Variable> allo = generateRandomVariablesSize(20);
        afficheVariables(allo);

        System.out.println("le meilleur candidat qui le plus petit domaine est  "+heuristicDomain.best(allo, null));
        heuristicDomain.setBoolean(true);
        System.out.println("le meilleur candidat qui le plus grand domaine est  "+heuristicDomain.best(allo, null));


        System.out.println("\n==================== Demo dela classe RandomValueHeuristic ====================\n");

        RandomValueHeuristic randomHeuristic = new RandomValueHeuristic(new Random());

        System.out.println("\n-----------------Demo de best avec des valeurs aléatoires ---------------------\n");
        System.out.println(" Demo de la calsse RandomValueHeuristic dont la methode ordering renvoie l'ordononcement aléatoire du domaine d'une variable \n");

        Variable var = new Variable("var",generateRandomSet(0,10,4));
        System.out.println(var);
        System.out.println("resultat : "+randomHeuristic.ordering(var,var.getDomain()));



        System.out.println("\n==================== Demo dela classe HeuristicMACSolver ====================\n");

        HeuristicMACSolver macSolverHeuristic = new HeuristicMACSolver(variables,allConstraints,new DomainSizeVariableHeuristic(false),randomHeuristic);
        result = macSolverHeuristic.solve();

        System.out.println("\n-----------------Demo de solve---------------------\n");

        System.out.println("Demo de solve de la classe HeuristicMACSolver qui utilise les principes de maintenance de la cohérence d'arc et duex heuristics \npour trouver une solution efficace à un problème de satisfaction de contraintes");
        System.out.println("dans cette demonstration les heuristics utilisées sont: \nDomainSizeHeuristic, qui renvoie le meilleur candidat ayant le moins ou le plus grand domaine");
        System.out.println("RandomHeuristic, qui renvoie l'ordononcement aléatoire du domaine d'une variable\n");

        System.out.println("dans ce cas, les variables sont : \n");
        afficheVariables(macSolverHeuristic.getVariables());
        
        System.out.println("et les contraintes sont : \n");
        afficheConstraints(macSolverHeuristic.getContraintes());
        
        afficheMap(result, "resultat de HeuristicMACSolver");

        System.out.println("\n-----------------Demo de solve avec des valeurs aléatoires ---------------------\n");

        randomVariables = generateRandomVariables(5);
        randomConstraints = generateRandomConstraints(randomVariables,10);

        HeuristicMACSolver macSolverHeuristicRandom = new HeuristicMACSolver(randomVariables,randomConstraints,new NbConstraintsVariableHeuristic(randomConstraints,false),randomHeuristic);

        System.out.println("et dans ce deuxième cas les variables sont aléatoires et l'heuristic de variable est celle qui renvoie le candidant ayant le moins de contraintes  : \n");
        afficheVariables(macSolverHeuristicRandom.getVariables());
        
        System.out.println("et les contraintes sont : \n");
        afficheConstraints(macSolverHeuristicRandom.getContraintes());
        
        afficheMap(macSolverHeuristicRandom.solve(), "resultat de HeuristicMACSolverRandom");



        



        System.out.println("\n==================== Fin Demo ====================\n");


    }
    public static void afficheMap(Map<Variable,Object> Map,String mapName){
        if(Map != null){
            System.out.println(mapName+": { ");
            for (Map.Entry<Variable, Object> entry : Map.entrySet()) {
                Variable key = entry.getKey();
                Object value = entry.getValue();
                System.out.println("    { " + key.getName() + " = " + value+" }   ");
            }
            System.out.println("}\n");
        }
        else{ System.out.println("MAP IS NULL"); }
    }
    public static void afficheDomains(Map<Variable,Set<Object>> map){
        System.out.println();
        for(Map.Entry<Variable, Set<Object>> entry : map.entrySet()){
            Variable v = entry.getKey();
            System.out.print(v);
            System.out.println(" /====  Domain attributed : "+entry.getValue());
        }
        System.out.println();

    }
    public static void afficheConstraints(Set<Constraint> constraints){
        System.out.println("Contraintes : ");
        for(Constraint c : constraints){
            System.out.println(c.toString());
        }
        System.out.println("");
    }
    public static void afficheVariables(Set<Variable> variables){
        System.out.println("Variables : ");
        for(Variable v : variables){
            System.out.println(v);
        }
        System.out.println("");

    }
    public static Set<Object> generateRandomSet(int min, int max, int size) {
        Set<Object> randomSet = new HashSet<>();
        Random random = new Random();
        
        while (randomSet.size() < size) {
            int randomValue = random.nextInt(max - min + 1) + min; 
            randomSet.add(randomValue);
        }
        return randomSet;
    }

    public static Set<Variable> generateRandomVariables(int count,int domainsize) {

        Set<Variable> variables = new HashSet<>();
        for (int i = 0; i < count; i++) {
            String varName = "x" + i; // Créer le nom de la variable (x0, x1, ..., x20)
            variables.add(new Variable(varName, generateRandomSet(-10,10,domainsize)));
        }
        return variables;
    }

    public static Set<Variable> generateRandomVariablesSize(int count) {

        Set<Variable> variables = new HashSet<>();
        for (int i = 0; i < count; i++) {
            String varName = "x" + i; // Créer le nom de la variable (x0, x1, ..., x20)
            variables.add(new Variable(varName, generateRandomSet(-10,10,new Random().nextInt(20))));
        }
        return variables;
    }

    public static Set<Variable> generateRandomVariables(int count) {
        return generateRandomVariables(count,10);
    }

    public static Set<Constraint> generateRandomConstraints(Set<Variable> variables, int numConstraints) {

        Set<Constraint> constraints = new HashSet<>();
        List<Variable> varList = new ArrayList<>(variables);
        Random random = new Random();

        for (int i = 0; i < numConstraints; i++) {
            // Choisir un type de contrainte aléatoire (0 pour DifferenceConstraint, 1 pour UnaryConstraint)
            int constraintType = random.nextInt(2); // 0 ou 1
            
            if (constraintType == 0) { // Créer une DifferenceConstraint
                Variable var1 = varList.get(random.nextInt(varList.size()));
                Variable var2 = varList.get(random.nextInt(varList.size()));

                // Éviter de créer une contrainte entre la même variable
                while (var1.equals(var2)) {
                    var2 = varList.get(random.nextInt(varList.size()));
                }

                constraints.add(new DifferenceConstraint(var1, var2));
            } else { // Créer une UnaryConstraint
                Variable var = varList.get(random.nextInt(varList.size()));
                constraints.add(new UnaryConstraint(var, var.getDomain())); // Utiliser le domaine de la variable
            }
        }
        return constraints;
    }




}
