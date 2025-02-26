import java.io.File;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {

        StringBuilder writerToOutput = new StringBuilder();// StringBuilder for output messages
        FileWriter writer1 = new FileWriter(args[3]);
        BufferedWriter writer = new BufferedWriter(writer1);// BufferedWriter for efficient writing to file


        // Load node data from a file
        File nodeFile = new File(args[0]);
        Scanner nodeScanner = new Scanner(nodeFile);

        int width;
        int height;
        Node[][] visiblemap;// Represents the map that can "see"
        Node[][] allmap ;// Represents the full map

        HashMap<int[],Integer> nodeHash = new HashMap<>();// Maps keeps nodes

        // Read map dimensions from the file
        width = nodeScanner.nextInt();
        height = nodeScanner.nextInt();
        visiblemap = new Node[width][height];
        allmap = new Node[width][height];

        // Read nodes and populate the maps
        while (nodeScanner.hasNextLine()) {
            {
            try{
                Integer xCoordinate = nodeScanner.nextInt();
                Integer yCoordinate = nodeScanner.nextInt();
                Integer typeOfNode = nodeScanner.nextInt();
                int[] coords = new int[2];
                coords[0] = xCoordinate;
                coords[1] = yCoordinate;
                Node newnode = new Node(xCoordinate,yCoordinate,typeOfNode);


                allmap[xCoordinate][yCoordinate] = newnode;// Full map
                nodeHash.put(coords,typeOfNode);

                // Populate visible map based on node type
                if (typeOfNode>1){// Invisible nodes
                    Node unvisibleNode = new Node(xCoordinate,yCoordinate,0);
                    visiblemap[xCoordinate][yCoordinate] = unvisibleNode;
                }else {// Invisible nodes
                    Node newenode = new Node(xCoordinate,yCoordinate,typeOfNode);
                    visiblemap[xCoordinate][yCoordinate] = newenode;
                }

            }catch (NoSuchElementException e) {
                // Ignore lines that don't match the expected format
            }
            }
        }


        // Load edge data from a file
        File edgeFile = new File(args[1]);
        Scanner edgeScanner = new Scanner(edgeFile);
        HashMap<String,Double> edgeHash = new HashMap<>();// Maps edge coordinates to their coordinates


        while (edgeScanner.hasNextLine()) {
            try{
                String Line = edgeScanner.nextLine();
                String[] Linepart = Line.split(" ");
                String Nodes = Linepart[0];

                // Parse edge times
                double time = Double.parseDouble(Linepart[1]);

                // Parse edge coordinates
                String[] twoNodes = Nodes.split(",");
                String[] firstNodesCoords = twoNodes[0].split("-");
                String[] secondNodesCoords = twoNodes[1].split("-");


                int[] firstNodesCoordsInteger = new int[2];
                firstNodesCoordsInteger[0] = Integer.parseInt(firstNodesCoords[0]);
                firstNodesCoordsInteger[1] = Integer.parseInt(firstNodesCoords[1]);


                int[] secondNodesCoordsInteger = new int[2];
                secondNodesCoordsInteger[0] = Integer.parseInt(secondNodesCoords[0]);
                secondNodesCoordsInteger[1] = Integer.parseInt(secondNodesCoords[1]);

                String coords = firstNodesCoords[0] +"-"+firstNodesCoords[1]+"/"+secondNodesCoords[0]+"-"+secondNodesCoords[1];
                String coords2 = secondNodesCoords[0] +"-"+secondNodesCoords[1]+"/"+firstNodesCoords[0]+"-"+firstNodesCoords[1];


                // neighbor lists
                allmap[firstNodesCoordsInteger[0]][firstNodesCoordsInteger[1]].neighbours.add(allmap[secondNodesCoordsInteger[0]][secondNodesCoordsInteger[1]]);
                allmap[secondNodesCoordsInteger[0]][secondNodesCoordsInteger[1]].neighbours.add(allmap[firstNodesCoordsInteger[0]][firstNodesCoordsInteger[1]]);
                visiblemap[firstNodesCoordsInteger[0]][firstNodesCoordsInteger[1]].neighbours.add(visiblemap[secondNodesCoordsInteger[0]][secondNodesCoordsInteger[1]]);
                visiblemap[secondNodesCoordsInteger[0]][secondNodesCoordsInteger[1]].neighbours.add(visiblemap[firstNodesCoordsInteger[0]][firstNodesCoordsInteger[1]]);




                // Add edges to maps
                edgeHash.put(coords,time);
                edgeHash.put(coords2,time);




            }catch (NoSuchElementException e) {
            }
        }

        ArrayList<ArrayList<Integer>> allObjective = new ArrayList<>();


            // Read objectives from file
            BufferedReader br = new BufferedReader(new FileReader(args[2]));

            // Read radius and starting coordinates
            String[] firstLine = br.readLine().split(" ");
            int radius = Integer.parseInt(firstLine[0]);
            String[] secondLine = br.readLine().split(" ");

            int startingxCoordinate = Integer.parseInt(secondLine[0]);
            int startingyCoordinate = Integer.parseInt(secondLine[1]);


            // Read each objective into the list
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] values = line.split(" ");

                    ArrayList<Integer> objective = new ArrayList<>();
                    int xCoordinate = Integer.parseInt(values[0]);
                    int yCoordinate = Integer.parseInt(values[1]);

                    objective.add(xCoordinate);
                    objective.add(yCoordinate);

                    // Add all options
                    if (values.length > 2) {
                        for (int i = 2; i < values.length; i++) {
                            objective.add(Integer.parseInt(values[i]));
                        }
                    }

                    allObjective.add(objective);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                }
            }

            br.close();


            // Pathfinding for each objective
        int currentstartingX = startingxCoordinate;
        int currentstartingY = startingyCoordinate;

        int currentFinishX;
        int currentFinishY;

        for (int i = 0 ; i < allObjective.size();i++){
            currentFinishX = allObjective.get(i).get(0);
            currentFinishY = allObjective.get(i).get(1);

            control(currentstartingX,currentstartingY,currentFinishX,currentFinishY,visiblemap,allmap,edgeHash,radius,writerToOutput);

            currentstartingX = currentFinishX;
            currentstartingY = currentFinishY;

            writerToOutput.append("Objective "+(i+1)+" reached!"+ System.lineSeparator());

            // Handle visibility updates for multi-step objectives
            if (allObjective.get(i).size()>2){
                visibilityMethod(currentstartingX,currentstartingY,visiblemap,allmap,radius);
                wizzard(allObjective.get(i),visiblemap,allmap,edgeHash,currentstartingX,currentstartingY,allObjective.get(i+1).get(0),allObjective.get(i+1).get(1),radius,writerToOutput);
            }

        }

        // Write results to file
        writer.write(writerToOutput.toString());
        writer.flush();
        writer.close();



    }


    public static ArrayList<Node>  Dijkstra(int startX, int startY, int targetX, int targetY,Node[][] visiblemap,Node[][] allmap,HashMap<String, Double> edgeHash,double[] distance) {
        Heap pq = new Heap();//creating heap for djikstra

        ArrayList<ArrayList<Double>> distances = new ArrayList<>();//creating distances arraylist for djikstra
        ArrayList<ArrayList<Node>> previous = new ArrayList<>();//creating previous arraylist for djikstra

        for (int x = 0; x < visiblemap.length; x++) {
            ArrayList<Double> distanceRow = new ArrayList<>();//creating arraylist in arraylist
            ArrayList<Node> previousRow = new ArrayList<>();//creating arraylist in arraylist
            for (int y = 0; y < visiblemap[0].length; y++) {
                distanceRow.add(Double.MAX_VALUE);//adding cost values as max
                previousRow.add(null);//adding Nodes as null
                visiblemap[x][y].cost = Double.MAX_VALUE;//updating all nodes's costs to max
            }
            distances.add(distanceRow);//adding distanceRow to distances
            previous.add(previousRow);//adding previousRow to previous
        }

        distances.get(startX).set(startY, 0.0);//updating starting nodes cost as 0
        Node startNode = visiblemap[startX][startY];//creating starting node
        startNode.cost = (0.0);//updating starting nodes cost as 0
        pq.add(startNode);//adding startnode to pq
        while (!pq.isEmpty()) {
            Node current = pq.poll();//finding currnt node
            int currentX = current.x;
            int currentY = current.y;

            if (currentX == targetX && currentY == targetY) {
                Node targetNode = visiblemap[targetX][targetY];//finding target node
                targetNode.cost = distances.get(currentX).get(currentY);//finding target node's cost
                break;
            }

            Node node = visiblemap[current.x][current.y];
            for (Node neighbors : node.neighbours) {//finding nodes neighbors
                int neighbourX = neighbors.x;
                int neighbourY = neighbors.y;
                if (visiblemap[neighbourX][neighbourY].type != 0) {
                    continue;
                }

                String stringCoords = Integer.toString(current.x) +"-"+ Integer.toString(current.y)+"/" +Integer.toString(neighbourX) +"-"+Integer.toString(neighbourY);//finding edge
                double newDistance = distances.get(currentX).get(currentY) + edgeHash.get(stringCoords);//create distance as first edge
                if (newDistance < distances.get(neighbourX).get(neighbourY)) {
                    distances.get(neighbourX).set(neighbourY, newDistance);
                    previous.get(neighbourX).set(neighbourY, current);
                    Node neighborNode = visiblemap[neighbourX][neighbourY];
                    neighborNode.cost = newDistance;
                    pq.add(neighborNode);
                }
            }
        }
        distance[0] = distances.get(targetX).get(targetY);
        return reconstructPath(previous, startX, startY, targetX, targetY,visiblemap);//returns path
    }


    //finding path algorithm
    public static ArrayList<Node> reconstructPath(ArrayList<ArrayList<Node>> previous, int startX, int startY, int targetX, int targetY,Node[][] visiblemap) {
        ArrayList<Node> path = new ArrayList<>();
        int currentX = targetX;
        int currentY = targetY;

        while (currentX != startX || currentY != startY) {
            Node currentNode = previous.get(currentX).get(currentY);
            if (currentNode != null) {
                path.add(0, currentNode);
            }

            currentX = currentNode.x;
            currentY = currentNode.y;

        }
        path.add(visiblemap[targetX][targetY]);//adding elements
        return path;//returns path
    }



    //updating nodes types method
    public static void visibilityMethod(int startX, int startY,Node[][] visiblemap,Node[][] allmap,int radius){
            for (int j= startX-radius;j<=startX+radius;j++) {//finding suitable heights
                if (j<0){
                    continue;
                }if (j>=visiblemap.length){
                    continue;
                }
                for (int u = startY-radius;u<=startY+radius;u++){//finding suitable lengths
                    if (u<0){
                        continue;
                    }
                    if (u>=visiblemap[0].length){
                        continue;
                    }
                    if (Math.pow(((startX-j)*(startX-j)+(startY-u)*(startY-u)),0.5) <= radius){//finding suitable lengths by hypotenuse
                        visiblemap[j][u].type = allmap[j][u].type;//upudating visiblemap
                }
            }
        }
    }


    //control step by step method
    public static void control(int startX, int startY, int targetX, int targetY, Node[][] visiblemap, Node[][] allmap, HashMap<String, Double> edgeHash, int radius, StringBuilder writerToOutput){

        visibilityMethod(startX,startY,visiblemap,allmap,radius);//updating visiblemap
        double[] nothing = new double[1];
        ArrayList<Node> path = Dijkstra(startX,startY,targetX,targetY,visiblemap,allmap,edgeHash,nothing);//finding shortest path
        int currentX = startX;//updating current x
        int currentY = startY;//updating current y



            while ( currentX!=targetX || currentY!=targetY){//it continues until until reach to target


                for (int i = 1; i < path.size();i++){//it loops the all path
                    if (pathvalue(visiblemap,path,i,radius)){//pathvalue method
                        currentX = path.get(i).x;//updating current x
                        currentY = path.get(i).y;//updating current y

                        visibilityMethod(path.get(i).x,path.get(i).y,visiblemap,allmap,radius);//updating visiblemap
                        writerToOutput.append("Moving to "+currentX+"-"+currentY+ System.lineSeparator());//writing output

                    }
                    else {
                        writerToOutput.append("Path is impassable!"+ System.lineSeparator());//writing output
                        path = Dijkstra(currentX,currentY,targetX,targetY,visiblemap,allmap,edgeHash,nothing);//updating path
                        break;
                    }
                }
            }
    }


    //wizzard method
    public static void wizzard(ArrayList<Integer> Objective, Node[][] visiblemap, Node[][] allmap, HashMap<String, Double> edgeHash, int startX, int startY, int targetX, int targetY, int radius, StringBuilder writerToOutput){
        double minDist = Double.MAX_VALUE;//creating mindist
        ArrayList<Node> minpath = new ArrayList<>();//creating minpath

        int helpInt = 0;

        for (int i = 2 ; i<Objective.size();i++){//traversing all options
            ArrayList<Node> returnNode = new ArrayList<>();//keeps nodes which's type has changed

            for (int j = 0 ; j<visiblemap.length;j++){//traversing visiblemap's height
                for (int k = 0 ; k<visiblemap[j].length;k++){//traversing visiblemap weight
                    if (visiblemap[j][k].type== Objective.get(i)){
                        returnNode.add(visiblemap[j][k]);//adding returnNode
                        visiblemap[j][k].type = 0;//updating visiblemap

                    }
                }
            }
            double[] nothing = new double[1];

            ArrayList<Node> newminpath = Dijkstra(startX,startY,targetX,targetY,visiblemap,allmap,edgeHash,nothing);// finding new path

            //String stringCoords = Integer.toString(newminpath.get(0).x) +"-"+ Integer.toString(newminpath.get(0).y)+"/" +Integer.toString(newminpath.get(1).x)+"-" +Integer.toString(newminpath.get(1).y);

            //double newminDist = edgeHash.get(stringCoords);// updating newmin dist

            //for (int j = 1; j<newminpath.size()-1; j++){
            //    String stringCoords2 = Integer.toString(newminpath.get(j).x)+"-" + Integer.toString(newminpath.get(j).y )+"/"+ Integer.toString(newminpath.get(j+1).x)+"-" + Integer.toString(newminpath.get(j+1).y);
            //    newminDist += edgeHash.get(stringCoords2);// updating newmin dist

            //}

            if (nothing[0]<minDist){//finding shortest path
                minDist = nothing[0];// updating min dist
                minpath = newminpath;// updating min path
                helpInt = Objective.get(i);// updating helpint
            }

            for (Node node : returnNode){
                visiblemap[node.x][node.y].type = Objective.get(i);//updating visiblemap
                visiblemap[node.x][node.y].type = Objective.get(i);//updating visiblemap
            }
        }

        for (int j = 0 ; j<allmap.length;j++){
            for (int k = 0 ; k<allmap[j].length;k++){
                if (allmap[j][k].type== helpInt){
                    visiblemap[j][k].type = 0;//updating visiblemap
                    allmap[j][k].type = 0;//updating visiblemap
                }
            }
        }

        writerToOutput.append("Number "+ helpInt+ " is chosen!"+ System.lineSeparator());//writing output
    }

    //controlling path method
    public static boolean pathvalue(Node[][] visiblemap,ArrayList<Node> path,int j,int radius){

        for (int i = j+1;i<path.size();i++){//looks all path
            if (visiblemap[path.get(i).x][path.get(i).y].type!=0){//it returns false if type is not 0
                return false;
            }
        }
        return true;
    }
}












