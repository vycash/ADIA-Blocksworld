package blocksworld.datamining;
import blocksworld.world.*;
import datamining.*;
import modelling.*;
import java.util.*;
import bwgeneratordemo.Demo;

public class DemoDataMining {
    public static void main(String[] args) {
        // paramètres
        int numBlocks = 8; // nbr de blocs
        int numPiles = 10; // nbr de piles
        int numInstances = 10000; // nbr d'instances dans la base de données
        float minFrequency = 2.0f / 3.0f; // fréquence min (2/3)
        float minConfidence = 95.0f / 100.0f; // confiance min (95% = 95/100)


        // base de données booléenne
        BooleanDatabase database = generateDatabase(numBlocks, numPiles, numInstances);

        // extraction des motifs fréquents
        System.out.println("\n==== Extraction des motifs fréquents ====");
        Apriori apriori = new Apriori(database);
        Set<Itemset> frequentItemsets = apriori.extract(minFrequency);
        for (Itemset itemset : frequentItemsets) {
            System.out.println(itemset);
        }

        // extraction des règles d'association
        System.out.println("\n==== Extraction des règles d'association ====");
        BruteForceAssociationRuleMiner ruleMiner = new BruteForceAssociationRuleMiner(database);
        Set<AssociationRule> rules = ruleMiner.extract(minFrequency, minConfidence);
        for (AssociationRule rule : rules) {
            System.out.println(rule);
        }

        System.out.println("\n==============================================\n");
        System.out.println("nb d'itemSets de fréquence >= 2/3 et de confiance >= 95% extraits : "+frequentItemsets.size());
        System.out.println("nb de règles d'associations de fréquence >= 2/3 et de confiance >= 95% extraits : "+rules.size());
        System.out.println("\n==============================================\n");

        AprioriOptimizedAssociationRuleMiner miner = new AprioriOptimizedAssociationRuleMiner(database);
        rules = miner.extract(minFrequency, minConfidence);
        System.out.println("\n==============================================\n");
        System.out.println("nb de règles d'associations de fréquence >= 2/3 et de confiance >= 95% extraits en utilisant AprioriOptimizedAssociationRuleMiner : "+rules.size());
        System.out.println("\n==============================================\n");
    }


    private static BooleanDatabase generateDatabase(int numBlocks, int numPiles, int numInstances) {

        Random random = new Random();
        BooleanVariableGenerator generator = new BooleanVariableGenerator(numBlocks, numPiles);
        Set<BooleanVariable> allVariables = generator.generateAllVariables();
        BooleanDatabase db = new BooleanDatabase(allVariables);
        for (int i = 0; i < numInstances; i++) {
            List<List<Integer>> state = Demo.getState(random);
            Set<BooleanVariable> instance = generator.convertStateToInstance(state);
            db.add(instance);
        }
        return db;
    }



    //méthode pour générer des états aléatoires
    private static List<List<Integer>> generateRandomState(Random random, int numBlocks, int numPiles) {
        List<List<Integer>> state = new ArrayList<>();
        for (int i = 0; i < numPiles; i++) {
            state.add(new ArrayList<>());
        }

        // répartition aléatoire des blocs sur les piles
        for (int b = 0; b < numBlocks; b++) {
            int pileIndex = random.nextInt(numPiles);
            state.get(pileIndex).add(b);
        }

        return state;
    }


}
