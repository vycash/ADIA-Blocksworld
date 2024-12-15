package datamining;

import java.util.*;
import modelling.*;

/**
 * Classe implémentant l'algorithme Apriori pour extraire les itemsets fréquents d'une base de données.
 */
public class Apriori extends AbstractItemsetMiner {

    /**
     * Constructeur de la classe Apriori.
     * 
     * @param database la base de données utilisée.
     */
    public Apriori(BooleanDatabase database) {
        super(database);
    }

    /**
     * Identifie tous les singletons fréquents (itemsets de taille 1) dans la base de données
     * en fonction de la fréquence minimale spécifiée.
     * 
     * @param minFrequency la fréquence minimale pour qu'un singleton soit considéré comme fréquent.
     * @return un ensemble d'itemsets fréquents de taille 1 (singletons) respectant la fréquence minimale.
     */
    public Set<Itemset> frequentSingletons(float minFrequency) {
        Set<Itemset> frequentSingletons = new HashSet<>();

        for (BooleanVariable item : getDatabase().getItems()) {
            Set<BooleanVariable> singleton = new HashSet<>();
            singleton.add(item);
            float frequency = frequency(singleton);

            if (frequency >= minFrequency) {
                frequentSingletons.add(new Itemset(singleton, frequency));
            }
        }
        return frequentSingletons;
    }

    /**
     * Combine deux ensembles triés d'éléments (BooleanVariable) en un seul ensemble trié.
     * Les ensembles combinés doivent avoir la même taille et les mêmes k-1 premiers éléments,
     * mais des derniers éléments différents. Si ces conditions ne sont pas remplies, la méthode renvoie null.
     * 
     * @param set1 le premier ensemble trié d'éléments.
     * @param set2 le second ensemble trié d'éléments.
     * @return un ensemble trié combiné si les conditions sont remplies, sinon null.
     */
    public static SortedSet<BooleanVariable> combine(SortedSet<BooleanVariable> set1, SortedSet<BooleanVariable> set2) {
        if (set1.size() != set2.size() || set1.size() == 0 || set2.size() == 0) {
            return null;
        }

        BooleanVariable[] arr1 = set1.toArray(new BooleanVariable[0]);
        BooleanVariable[] arr2 = set2.toArray(new BooleanVariable[0]);

        for (int i = 0; i < arr1.length - 1; i++) {
            if (!arr1[i].equals(arr2[i])) {
                return null;
            }
        }
        if (arr1[arr1.length - 1].equals(arr2[arr2.length - 1])) {
            return null;
        }

        SortedSet<BooleanVariable> combinedSet = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);
        combinedSet.addAll(set1);
        combinedSet.add(arr2[arr2.length - 1]);

        return combinedSet;
    }

    /**
     * Vérifie que tous les sous-ensembles d'un itemset donné sont présents dans une collection
     * d'itemsets fréquents. Utilisé pour assurer que seuls les candidats dont tous les sous-ensembles
     * sont fréquents passent à l'étape suivante.
     * 
     * @param itemset l'ensemble d'éléments à vérifier.
     * @param frequentSubsets la collection des sous-ensembles fréquents.
     * @return true si tous les sous-ensembles de l'itemset sont fréquents, sinon false.
     */
    public static boolean allSubsetsFrequent(Set<BooleanVariable> itemset, Collection<SortedSet<BooleanVariable>> frequentSubsets) {
        for (BooleanVariable item : itemset) {
            SortedSet<BooleanVariable> subset = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);
            subset.addAll(itemset);
            subset.remove(item);
            if (!frequentSubsets.contains(subset)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Identifie tous les ensembles d'items fréquents dans la base de données en utilisant l'algorithme Apriori.
     * Cette méthode génère de manière itérative des ensembles d'items candidats plus grands en appliquant le principe d'Apriori.
     * 
     * @param minFrequency la fréquence minimale pour qu'un itemset soit considéré comme fréquent.
     * @return un ensemble d'itemsets fréquents respectant la fréquence minimale.
     */
    @Override
    public Set<Itemset> extract(float minFrequency) {
        Set<Itemset> result = new HashSet<>();
        List<SortedSet<BooleanVariable>> frequentSets = new ArrayList<>();
        Set<SortedSet<BooleanVariable>> allFrequentSubsets = new HashSet<>();

        for (Itemset itemset : frequentSingletons(minFrequency)) {
            SortedSet<BooleanVariable> singleton = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);
            singleton.addAll(itemset.getItems());
            frequentSets.add(singleton);
            result.add(itemset);
            allFrequentSubsets.add(singleton);
        }

        while (!frequentSets.isEmpty()) {
            List<SortedSet<BooleanVariable>> candidates = generateCandidates(frequentSets);
            frequentSets.clear();

            for (SortedSet<BooleanVariable> candidate : candidates) {
                if (allSubsetsFrequent(candidate, allFrequentSubsets)) {
                    float frequency = frequency(candidate);

                    if (frequency >= minFrequency) {
                        Itemset itemset = new Itemset(candidate, frequency);
                        result.add(itemset);
                        frequentSets.add(candidate);
                        allFrequentSubsets.add(candidate);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Génère toutes les combinaisons possibles d'ensembles d'items candidats
     * à partir de l'ensemble des itemsets fréquents de la taille précédente.
     * 
     * @param previousFrequentSets la liste des ensembles d'items fréquents de la taille précédente.
     * @return une liste de candidats pour l'étape suivante.
     */
    private List<SortedSet<BooleanVariable>> generateCandidates(List<SortedSet<BooleanVariable>> previousFrequentSets) {
        List<SortedSet<BooleanVariable>> candidates = new ArrayList<>();

        for (int i = 0; i < previousFrequentSets.size(); i++) {
            for (int j = i + 1; j < previousFrequentSets.size(); j++) {
                SortedSet<BooleanVariable> candidate = combine(previousFrequentSets.get(i), previousFrequentSets.get(j));
                if (candidate != null) {
                    candidates.add(candidate);
                }
            }
        }
        return candidates;
    }
}
