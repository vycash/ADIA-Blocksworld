package datamining;
import java.util.*;

/**
 * interface precisant deux méthodes que doit implementer un miner d'items
 */
public interface ItemsetMiner {
    BooleanDatabase getDatabase();
    public Set<Itemset> extract(float minFrequency);
}
