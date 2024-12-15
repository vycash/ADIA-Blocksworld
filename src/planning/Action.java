package planning;
import java.util.*;
import modelling.*;

public interface Action {
    public boolean isApplicable(Map<Variable,Object> map);
    public Map<Variable,Object> successor(Map<Variable,Object> map);
    public int getCost();
}
