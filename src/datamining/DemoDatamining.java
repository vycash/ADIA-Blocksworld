package datamining;
import java.util.*;
import modelling.*;

/**
 * Class demo du package datamining
 */
public class DemoDatamining {
    public static void main(String[] args) {

        System.out.println("\n==================== Demo dela classe Apriori ====================\n");


        BooleanVariable milk = new BooleanVariable("milk");
        BooleanVariable bread = new BooleanVariable("bread");
        BooleanVariable cucumber = new BooleanVariable("cucumber");
        BooleanVariable denimjeans = new BooleanVariable("denim jeans");
        BooleanVariable eggs = new BooleanVariable("eggs");

        Set<BooleanVariable> variables = Set.of(milk, bread, cucumber, denimjeans, eggs);

        BooleanDatabase database = new BooleanDatabase(variables);
        
        database.add(Set.of(milk, bread));              
        database.add(Set.of(milk, cucumber, denimjeans));      
        database.add(Set.of(bread, denimjeans));              
        database.add(Set.of(milk, bread, cucumber));       
        database.add(Set.of(bread, cucumber));              
        database.add(Set.of(cucumber, denimjeans));              
        database.add(Set.of(milk, bread, denimjeans));       
        database.add(Set.of(milk, bread, cucumber, denimjeans));
        database.add(Set.of(bread));                     
        database.add(Set.of(milk, denimjeans));     
        database.add(Set.of(milk,eggs));
        database.add(Set.of(eggs, bread));              
              
         

        System.out.println(database);

        Apriori aprioriMiner = new Apriori(database);

        float minFrequency = 0.3f;

        //Extract frequent singletons
        System.out.println("\n-------------------- Demo de frequentSingletons ---------------------\n");
        System.out.println("Frequent Singletons with min frequency " + minFrequency + " : \n");

        Set<Itemset> frequentSingletons = aprioriMiner.frequentSingletons(minFrequency);
        for (Itemset itemset : frequentSingletons) {
            System.out.println(itemset);
        }

        // Demonstrate the combination of itemsets using the Apriori 'combine' method
        System.out.println("\n-------------------- Demo de combine ---------------------\n");
        System.out.println("Combining Itemsets:\n");

        SortedSet<BooleanVariable> set1 = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);
        set1.add(cucumber);
        set1.add(denimjeans);
        
        SortedSet<BooleanVariable> set2 = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);;
        set2.add(milk);
        set2.add(cucumber);

        SortedSet<BooleanVariable> combinedSet = Apriori.combine(set1, set2);
        System.out.println("Set1: " + set1);
        System.out.println("Set2: " + set2);
        System.out.println("Combined Set (if valid): " + (combinedSet != null ? combinedSet : "Combination invalid") + "\n");

        SortedSet<BooleanVariable> set3 = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);
        set3.add(cucumber);
        set3.add(denimjeans);
        
        SortedSet<BooleanVariable> set4 = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);
        set4.add(eggs);
        set4.add(bread);

        SortedSet<BooleanVariable> combinedSet2 = Apriori.combine(set3, set4);
        System.out.println("Set3: " + set3);
        System.out.println("Set4: " + set4);
        System.out.println("Combined Set (if valid): " + (combinedSet2 != null ? combinedSet2 : "Combination invalid"));

        // Check if all subsets of an itemset are frequent using 'allSubsetsFrequent' method
        System.out.println("\n-------------------- Demo de allSubsetsFrequent ---------------------\n");
        System.out.println("Checking if all subsets are frequent:\n");

        List<SortedSet<BooleanVariable>> frequentSubsets = List.of(
            new TreeSet<>(AbstractItemsetMiner.COMPARATOR),
            new TreeSet<>(AbstractItemsetMiner.COMPARATOR),
            new TreeSet<>(AbstractItemsetMiner.COMPARATOR)
        );

        frequentSubsets.get(0).add(milk);
        frequentSubsets.get(0).add(bread);
        frequentSubsets.get(1).add(milk);
        frequentSubsets.get(1).add(cucumber);
        frequentSubsets.get(2).add(bread);
        frequentSubsets.get(2).add(cucumber);

        Set<BooleanVariable> itemsetToCheck = Set.of(milk, bread, cucumber);

        boolean allFrequent = Apriori.allSubsetsFrequent(itemsetToCheck, frequentSubsets);
        System.out.println("Itemset: " + itemsetToCheck);
        System.out.println("\nfrequent subsets : "+frequentSubsets);
        System.out.println("\nAll subsets frequent: " + allFrequent);

        // Extract all frequent itemsets based on the minFrequency
        System.out.println("\n-------------------- Demo de extract ---------------------\n");
        System.out.println("Extracting all frequent itemsets:\n");
        
        Set<Itemset> allFrequentItemsets = aprioriMiner.extract(minFrequency);
        for (Itemset itemset : allFrequentItemsets) {
            System.out.println(itemset);
        }

        System.out.println("\n\n==================== Demo dela classe BruteForceAssociationRuleMiner ====================\n");

        BruteForceAssociationRuleMiner bruteForceMiner = new BruteForceAssociationRuleMiner(database);

        System.out.println("\n-------------------- Demo de allCandidatePremises ---------------------\n");
        System.out.println("this method generate all the possible subsets from a set meaning all the candidate premises from a set :\n");
        System.out.println("in this case the set is : "+variables+"\n");
        System.out.println(BruteForceAssociationRuleMiner.allCandidatePremises(variables)+"\n");

        System.out.println("\n-------------------- Demo de extract ---------------------\n");
        System.out.println("this method extracts all the Association Rules above a minimum frequency and confidence.");
        System.out.println("In this case minFrequency = 0.3 , minConfidence = 0.5 : \n");
        System.out.println(bruteForceMiner.extract((float) 0.3, (float) 0.5));



        System.out.println("\n==================== Fin Demo ====================\n");
    }
}
