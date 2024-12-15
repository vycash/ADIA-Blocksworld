package datamining;
import java.util.*;
import modelling.*;

/**
 * Class demo du package datamining avec des données aléatoires
 */
public class DemoDataminingRandom {
    public static void main(String[] args) {

        System.out.println("\n==================== Demo de la classe Apriori avec données aléatoires ====================\n");

        // Créer la base de données
        BooleanDatabase database = new BooleanDatabase(randomBooleanVariableSet());
        Set<BooleanVariable> variables = database.getItems();
        addRandomTransactions(database, 100);

        // Afficher la base de données
        System.out.println(database);

        // Utiliser l'algorithme Apriori
        Apriori aprioriMiner = new Apriori(database);
        float minFrequency = 0.3f;

        // Extraire les singletons fréquents
        System.out.println("\n-------------------- Demo de frequentSingletons ---------------------\n");
        System.out.println("Frequent Singletons with min frequency " + minFrequency + " : \n");
        Set<Itemset> frequentSingletons = aprioriMiner.frequentSingletons(minFrequency);
        for (Itemset itemset : frequentSingletons) {
            System.out.println(itemset);
        }

        // Extraire tous les itemsets fréquents
        System.out.println("\n-------------------- Demo de extract ---------------------\n");
        System.out.println("Extracting all frequent itemsets:\n");
        Set<Itemset> allFrequentItemsets = aprioriMiner.extract(minFrequency);
        for (Itemset itemset : allFrequentItemsets) {
            System.out.println(itemset);
        }

        // Exécuter les démonstrations pour la classe BruteForceAssociationRuleMiner
        System.out.println("\n==================== Demo de la classe BruteForceAssociationRuleMiner avec des données aléatoires ====================\n");
        BruteForceAssociationRuleMiner bruteForceMiner = new BruteForceAssociationRuleMiner(database);

        System.out.println("\n-------------------- Demo de allCandidatePremises ---------------------\n");
        System.out.println("Generating all possible candidate premises from the set:\n");
        System.out.println("In this case the set is: " + variables + "\n");
        System.out.println(BruteForceAssociationRuleMiner.allCandidatePremises(variables) + "\n");

        System.out.println("\n-------------------- Demo de extract ---------------------\n");
        System.out.println("Extracting all Association Rules with minFrequency = 0.3, minConfidence = 0.5:\n");
        System.out.println(bruteForceMiner.extract(0.3f, 0.5f));

        System.out.println("\n==================== Fin Demo ====================\n");
    }

    public static Set<BooleanVariable> randomBooleanVariableSet() {
        // Liste de toutes les lettres de l'alphabet
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        Set<BooleanVariable> resultSet = new HashSet<>();

        // Créer une instance de Random
        Random random = new Random();

        // Déterminer un nombre aléatoire d'items à ajouter
        int numberOfItems = random.nextInt(10) + 1; // Entre 1 et maxItems

        // Mélanger les lettres de l'alphabet
        List<Character> letters = new ArrayList<>();
        for (char c : alphabet) {
            letters.add(c);
        }
        Collections.shuffle(letters);

        // Ajouter les lettres mélangées au Set
        for (int i = 0; i < numberOfItems; i++) {
            char letter = letters.get(i);
            resultSet.add(new BooleanVariable(String.valueOf(letter)));
        }

        return resultSet;
    }

    public static void addRandomTransactions(BooleanDatabase database, int numberOfTransactions) {
        // Récupérer les items de la base de données
        Set<BooleanVariable> items = database.getItems();

        // Créer un Random pour la génération aléatoire
        Random random = new Random();

        for (int i = 0; i < numberOfTransactions; i++) {
            // Créer une transaction aléatoire
            Set<BooleanVariable> transaction = generateRandomTransaction(items, random);
            // Ajouter la transaction à la base de données
            database.add(transaction);
        }
    }

    // Méthode pour générer une transaction aléatoire à partir des items
    private static Set<BooleanVariable> generateRandomTransaction(Set<BooleanVariable> items, Random random) {
        // Convertir l'ensemble d'items en liste pour un accès aléatoire
        List<BooleanVariable> itemList = new ArrayList<>(items);
        int transactionSize = random.nextInt(itemList.size()) + 1; // Taille de la transaction entre 1 et items.size()

        Set<BooleanVariable> transaction = new HashSet<>();
        // Sélectionner des items aléatoires pour la transaction
        for (int j = 0; j < transactionSize; j++) {
            // Choisir un index aléatoire
            int index = random.nextInt(itemList.size());
            transaction.add(itemList.get(index));
        }

        return transaction;
    }


}
