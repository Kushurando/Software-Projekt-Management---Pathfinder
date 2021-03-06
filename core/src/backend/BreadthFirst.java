package backend;

import API.APIManager;
import API.Models.Node;
import API.Models.NodeType;
import util.Util;

import java.util.ArrayList;

/**
 * Breadth First Algorithm Class
 *
 * @author backend
 */
public class BreadthFirst extends SearchAlgorithm {

    private ArrayList<Node> relevantNodes;

    /**
     * Initializes all fields
     *
     * @param manager APIManager that handles the communication between frontend and backend
     */
    public BreadthFirst(APIManager manager) {
        super(manager);
        this.relevantNodes = new ArrayList<Node>();
    }

    /** Starts the recursive algorithm and runs until a path is found or there is no valid path */
    @Override
    public void run() {
        findLocations();
        boolean found = advance(this.start);
        if (found) {
            Node node = end;

            // update the state for all nodes that are part of the found path
            while (node != null) {
                if (node.getType() != NodeType.END && node.getType() != NodeType.START) {
                    node.setType(NodeType.PATH);

                    // update frontend
                    manager.sendToFrontend(node);
                }

                node = node.getPrev();
            }
        } else {
            System.out.println("Es wurde leider kein Weg gefunden!");
        }
    }

    /**
     * advances the next node
     *
     * @param node The node that is supposed to be advanced
     * @return boolean Descrives whether a valid path was found or not
     */
    private boolean advance(Node node) {
        // Coordinates of the neighbours that are supposed to be updated
        int[][] coords = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        if (relevantNodes.contains(node)) {
            relevantNodes.remove(node);
        }

        // Check if the given node is the start or end node
        if (node.getType() != NodeType.START && node.getType() != NodeType.END) {
            // mark the given node as visited
            node.setType(NodeType.VISITED);

            // update frontend
            manager.sendToFrontend(node);
        }

        // Iterate over all neighbour coordinates
        for (int[] coord : coords) {
            int i = coord[0];
            int j = coord[1];

            boolean leftInBound = node.getHorIndex() + j >= 0;
            boolean rightInBound = node.getHorIndex() + j < field[0].length;
            boolean topInBound = node.getVertIndex() + i >= 0;
            boolean bottomInBound = node.getVertIndex() + i < field.length;

            // Check if the neighbour coordinates are still in bound
            if (leftInBound && rightInBound && topInBound && bottomInBound) {

                Node neighbour = field[node.getVertIndex() + i][node.getHorIndex() + j];

                // skip the current neighbour if the node is blocked, already visited or the
                // start node
                if (neighbour.getType() == NodeType.START
                        || neighbour.getType() == NodeType.BLOCKED
                        || neighbour.getType() == NodeType.VISITED) {
                    continue;
                }

                neighbour.setPrev(node);

                Util.printField(field);

                // end node was found
                if (neighbour.getType() == NodeType.END) {
                    System.out.println("Ziel gefunden!");
                    return true;
                }

                if (!relevantNodes.contains(neighbour)) this.relevantNodes.add(neighbour);
            }
        }

        if (relevantNodes.size() != 0) {
            return advance(relevantNodes.get(0));
        }

        return false;
    }
}
