package jsf.start.model.data;

import java.io.Serializable;
import java.util.*;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import jsf.start.model.Plan;

@ManagedBean(eager=true)
@ApplicationScoped
public class Plans implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final Plan PLAN500 = new Plan("Plan 500", 500);
    public static final Plan PLAN1000 = new Plan("Plan 1000", 1000);
    public static final Plan PLAN5000 = new Plan("Plan 5000", 5000);
    public static final Plan PLAN10000 = new Plan("Plan 10000", 10000);
    private static Map<String, Plan> plans;
    
    static {
        plans = new LinkedHashMap<>();
        plans.put(PLAN500.getPlanName(), PLAN500);
        plans.put(PLAN1000.getPlanName(), PLAN1000);
        plans.put(PLAN5000.getPlanName(), PLAN5000);
        plans.put(PLAN10000.getPlanName(), PLAN10000);
                        
    }
    
    public static Map<String, Plan> getPlans() {
        return plans;
    }
    
    public static Plan findPlanByName(String name) {
        return plans.get(name);
    }
}
