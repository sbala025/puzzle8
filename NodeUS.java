public class NodeUS implements Comparable<NodeUS> {
    private UniformSearch state;
    private NodeUS parent;
    private int cost;
    private int depth;
    private String action;

    @Override
    public int compareTo(NodeUS other) {
        return Integer.compare(this.cost, other.cost);
    }
    
    public NodeUS(UniformSearch state, NodeUS parent, int cost, int depth, String action) {
        this.state = state;
        this.parent = parent;
        this.cost = cost;
        this.depth = depth;
        this.action = action;
    }

    public NodeUS(UniformSearch state, NodeUS parent, int cost, int i) {
        this(state, parent, cost, i, null);
    }
    
    public UniformSearch getState() {
        return state;
    }
    
    public NodeUS getParent() {
        return parent;
    }
    
    public int getCost() {
        return cost;
    }
    
    public int getDepth() {
        return depth;
    }
    
    public String getAction() {
        return action;
    }
}