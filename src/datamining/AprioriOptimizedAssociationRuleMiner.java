package datamining;
import java.util.*;
import modelling.*;


/*
 * AprioriOptimizedAssociationRuleMiner :

*  -S'appuie sur l'algorithme Apriori pour extraire les itemsets fréquents, ce qui réduit considérablement le nombre d'itemsets candidats.
*  -Génère des prémisses fréquentes en respectant l'anti-monotonie (si un ensemble est non fréquent, ses supers-ensembles ne le sont pas non plus).
*  -Met en cache les fréquences des sous-ensembles pour éviter les calculs redondants.
*  -Plus efficace et optimisé pour les grandes bases de données.
*/
public class AprioriOptimizedAssociationRuleMiner extends AbstractAssociationRuleMiner {

    private Map<Set<BooleanVariable>, Float> frequencyCache;

    public AprioriOptimizedAssociationRuleMiner(BooleanDatabase database) {
        super(database);
        frequencyCache = new HashMap<>();
    }

    @Override
    public Set<AssociationRule> extract(float minFrequency, float minConfidence) {

        Set<Itemset> frequentItemsets = new Apriori(getDatabase()).extract(minFrequency);
        Set<AssociationRule> associationRules = new HashSet<>();

        // Parcourir chaque itemset fréquent pour générer les règles possibles
        for (Itemset itemset : frequentItemsets) {
            Set<BooleanVariable> items = itemset.getItems();

            // Générer toutes les prémisses fréquentes possibles
            Set<Set<BooleanVariable>> premises = allCandidatePremises(items, frequentItemsets, minFrequency);

            for (Set<BooleanVariable> premise : premises) {
                
                Set<BooleanVariable> conclusion = new HashSet<>(items);
                conclusion.removeAll(premise);

                float ruleFrequency = itemset.getFrequency();
                float ruleConfidence = confidence(premise, conclusion, frequentItemsets);

                if (ruleFrequency >= minFrequency && ruleConfidence >= minConfidence) {
                    AssociationRule rule = new AssociationRule(premise, conclusion, ruleFrequency, ruleConfidence);
                    associationRules.add(rule);
                }
            }
        }

        return associationRules;
    }

    // méthode pour générer tous les sous-ensembles candidats fréquents
    public Set<Set<BooleanVariable>> allCandidatePremises(Set<BooleanVariable> items, Set<Itemset> frequentItemsets, float minFrequency) {
        Set<Set<BooleanVariable>> candidatePremises = new HashSet<>();
        generateFrequentSubsets(items, new HashSet<>(), candidatePremises, frequentItemsets, minFrequency);
        return candidatePremises;
    }

    public Set<Set<BooleanVariable>> allCandidatePremises(Set<BooleanVariable> items, float minFrequency) {
        return allCandidatePremises(items,new Apriori(getDatabase()).extract(minFrequency), minFrequency);
    }

    // Génère les sous-ensembles fréquents récursivement qui ont une fréquence >= minFrequency en respectant le principe d'antie-monotonie d'Apriori
    private void generateFrequentSubsets(Set<BooleanVariable> remainingItems, Set<BooleanVariable> currentSubset, 
                                         Set<Set<BooleanVariable>> candidatePremises, Set<Itemset> frequentItemsets, float minFrequency) {
        
        if (!currentSubset.isEmpty()) {
            // Vérifie si le sous-ensemble actuel est fréquent
            float subsetFrequency = getFrequency(currentSubset, frequentItemsets);
            if (subsetFrequency >= minFrequency) {
                candidatePremises.add(new HashSet<>(currentSubset));
            } else {
                return; // Si le sous-ensemble n'est pas fréquent, on ne génère pas ses super-ensembles
            }
        }

        // Continue de générer des sous-ensembles fréquents pour chaque item restant
        for (BooleanVariable item : remainingItems) {
            Set<BooleanVariable> newRemainingItems = new HashSet<>(remainingItems);
            newRemainingItems.remove(item);

            Set<BooleanVariable> newSubset = new HashSet<>(currentSubset);
            newSubset.add(item);

            generateFrequentSubsets(newRemainingItems, newSubset, candidatePremises, frequentItemsets, minFrequency);
        }
    }

    // Méthode pour calculer ou récupérer la fréquence depuis le cache
    private float getFrequency(Set<BooleanVariable> subset, Set<Itemset> frequentItemsets) {
        if (frequencyCache.containsKey(subset)) {
            return frequencyCache.get(subset);
        }

        // Calcule la fréquence de ce sous-ensemble dans les itemsets fréquents
        float frequency = 0.0f;
        for (Itemset itemset : frequentItemsets) {
            if (itemset.getItems().containsAll(subset)) {
                frequency = Math.max(frequency, itemset.getFrequency());
            }
        }

        frequencyCache.put(subset, frequency);
        return frequency;
    }

}
