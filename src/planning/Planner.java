package planning;
import java.util.*;
import modelling.*;

public interface Planner {
    public List<Action> plan();
    public Map<Variable, Object> getInitialState();
    public Set<Action> getActions();
    public Goal getGoal();
    public int getNodeCount();
    public void activateNodeCount(boolean activate);
}
