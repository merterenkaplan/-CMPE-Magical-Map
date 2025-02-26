import java.util.ArrayList;

// The Node class represents a node in a graph.
// It implements the Comparable interface to allow nodes to be compared based on their cost.
public class Node implements Comparable<Node>{
    public int x ;// The x-coordinate of the node
    public int y ;// The y-coordinate of the node
    public int type ;// The type of the node
    public double cost = Double.POSITIVE_INFINITY ;// The cost associated with the node, initialized to infinity


    public ArrayList<Node> neighbours = new ArrayList<>();// A list to store neighboring nodes

    public HashMap<Node,Double> edges = new HashMap<>();

    // Constructor to initialize a node with its x, y coordinates and type
    public Node(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    // The compareTo method allows comparison of nodes based on their cost.
    @Override
    public int compareTo(Node o) {
        return Double.compare(cost,o.cost);// Compare the current node's cost with another node's cost
    }


}
