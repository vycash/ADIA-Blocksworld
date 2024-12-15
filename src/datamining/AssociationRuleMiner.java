package datamining;
import java.util.*;

/**
 * interface precisant deux méthodes que doit implementer un miner de règles d'association
 */
public interface AssociationRuleMiner {
    public BooleanDatabase getDatabase();
    public Set<AssociationRule> extract(float minFrequency,float minConfidence);
}
