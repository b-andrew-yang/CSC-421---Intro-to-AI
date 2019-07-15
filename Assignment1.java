 /* 
 / Andrew Yang
 / CSC 421
 / George Tznetakis
 / V00878595
 / 05/18/18
*/
import java.util.*;
import java.lang.*;

public class Assignment1 {
    public static City[][] createMap(int [][] cityKey){
        City[][] cityMap = new City[100][100];
        Random random = new Random();
        
        char name = 'A';
        
        for(int i = 0; i< 26; i++){
            
            int x = random.nextInt(100);
            int y = random.nextInt(100);
            
            while(cityMap[x][y] != null){
                x = random.nextInt(100);
                y = random.nextInt(100);
            }
            
            //System.out.print(name);
            //System.out.print(" ");
            //System.out.print(a);
            //System.out.print(" ");
            //System.out.print(b);
            //System.out.println();
            
            int aRd = 1 + random.nextInt(4);
            
            cityMap[x][y] = new City(name++, aRd, x, y);
            cityKey[i][0] = x;
            cityKey[i][1] = y;
        }
        return cityMap;
    }
    
    // This function also counts the average number of branches
    // since we iterate through once already.
    public static void addNeighbors(City [][] cityMap){
        for(int i = 0; i< 100; i++){
            for(int j = 0; j< 100; j++){
                // Check every spot in the array for a city node.
                if(cityMap[i][j] != null){
                    // If we find a city node
                    City tmp = cityMap[i][j];
                    int branch = tmp.getBranches();
                    // Get the number of roads coming out of the city
                    
                    for(int k = 0; k< 100; k++){
                        for(int l = 0; l< 100; l++){
                            // Now search the array again for a node to // find the distance of
                            if(cityMap[k][l] != null){
                                City secondaryTmp = cityMap[k][l];
                                if(tmp.getName() != secondaryTmp.getName()){
                                    checkCloser(tmp, secondaryTmp);
                                }
                            }
                        }
                    }
                    // After adding all the closest nodes, add the roads connecting some of them.
                    addRoads(tmp);
                }
            }
        }
    }
    
    public static double getAverage(City [][] cityMap){
        int totalBranches = 0;
        
        for(int i = 0; i< cityMap.length; i++){
            for(int j = 0; j< cityMap[0].length; j++){
                if(cityMap[i][j] != null){
                    City tmp = cityMap[i][j];
                    totalBranches += tmp.getSize();
                }
            }
        }
        double numCities = 26;
        double average = totalBranches / numCities;
        return average;
    }
    
    public static int nodesGenerated(City [][] cityMap){
        int totalBranches = 0;
        
        for(int i = 0; i< cityMap.length; i++){
            for(int j = 0; j< cityMap.length; j++){
                if(cityMap[i][j] != null){
                    City tmp = cityMap[i][j];
                        totalBranches += tmp.getSize();
                }
            }
        }
        return totalBranches;
    }
    
    public static void checkCloser(City primary, City secondary){
        City [] tmp = primary.getNeighbors();
        
        for(int i = 0; i< tmp.length; i++){
            if(tmp[i] == null){
                tmp[i] = secondary;
            }
            
            if(findDistance(primary, secondary) < findDistance(primary, tmp[i])){
                tmp[i] = secondary;
            }
        }
    }
    
    public static double findDistance(City a, City b){
        int x = (a.getX() + b.getX());
        int y = (a.getY() + b.getY());
        int disX = (x * x);
        int disY = (y * y);
        double distance = Math.sqrt(disX + disY);
        
        return distance;
    }
    
    public static void addRoads(City a){
        // Called in addNeighbors method
        
        Random random = new Random();
        //System.out.println("branch" + a.getBranches());
        
        for(int i = 0; i< a.getBranches(); i++){
            int roadIndex = random.nextInt(5);
            //System.out.println(roadIndex);
            City [] neighbors = a.getNeighbors();
            a.addOutRoads(neighbors[roadIndex]);
            //System.out.println(neighbors[roadIndex].getName());
            neighbors[roadIndex].addRoads(a);
            //System.out.println(a.getName());
            //System.out.println(i);
            //System.out.println(a.getName() + " " + a.getSize());
        }
        
    }
    
    public static Boolean BFS(City start, City end, LinkedList<City> pathBFS){
        Queue<City> Q = new LinkedList<City>();
        // Creates a new queue called "Q"
        Q.add(start);
        // Adds the start City to our queue
        start.setVisited();
        // We set the city to visited so we don't add it to the queue again
        
        while(Q.peek() != null){
            // While there are still cities in the queue
            City front = Q.poll();
            // Remove the city from the front of the queue
            // and set it equal to queue
            front.setVisited();
            // We set the city as visited
            pathBFS.add(front);
            // We add the city to our visited list
            
            if(front.equals(end)){
                return true;
            }
            
            
            // If the city that we removed from the front of
            // the queue is the goal city (end) then we return
            // true and exit the program.
            
            LinkedList<City> frontNeighbors = front.getList();
            // We get the list of connections from our current city
            
            for(int i = 0; i<front.getSize(); i++){
                // We iterate through the list of connections in the city
                if(!frontNeighbors.get(i).getVisited()){
                    // If the city at index i in the list hasn't been visited
                    Q.add(frontNeighbors.get(i));
                    // Then we add it to the list
                }
            }
        }
        return false;
    }
    
    public static LinkedList<City> runBFS(City start, City end){
        LinkedList<City> pathBFS = new LinkedList<City>();
        
        Boolean bfsValid = BFS(start, end, pathBFS);
        
        System.out.println("Is City reachable(BFS): " + bfsValid);
        
        return pathBFS;
    }
    
    public static Boolean DFS(City start, City end, LinkedList<City> pathDFS){
        City s = start;
        // Sets our start state to s
        
        Stack<City> dfsStack = new Stack<City>();
        // Creates a new stack
        dfsStack.push(s);
        // Push our first city onto the stack
        
        while(dfsStack.empty() == false){
            // While to stack isn't empty
            while(dfsStack.peek().getVisited() == true){
                // If we have previously visited cities on top
                // pop them off
                dfsStack.pop();
            }
            
            s = dfsStack.pop();
            // Pop off the top of the stack and set it equal to s
            s.setVisited();
            // Set s as visited
            pathDFS.add(s);
            // Add s to our linked list of visted cities
            
            if(s.equals(end)){
                // If s is our goal state we return true
                return true;
            }
            
            LinkedList<City> startNeighbors = s.getList();
            // We get the list of connections from s our current city
            
            for(int i = 0; i< startNeighbors.size(); i++){
                // We iterate through the list and push them onto our stack
                // if they weren't previously visited
                if(startNeighbors.get(i).getVisited() == false){
                    dfsStack.push(startNeighbors.get(i));
                }
            }
        }
        return false;
    }
    
    public static LinkedList<City> runDFS(City start, City end){
        LinkedList<City> pathDFS = new LinkedList<City>();
        // Creates our visited city linked list
        
        Boolean dfsValid = DFS(start, end, pathDFS);
        // Runs DFS and saves whether its solvable to dfsValid
        
        System.out.println("Is City reachable(DFS): " + dfsValid);
        // Prints whether its valid or not
        
        return pathDFS;
        // Returns the visited linked list
    }
    
    public static City DLS(City start, City end, int limit, LinkedList<City> pathIDS){
        if(limit == 0 && start.equals(end)){
            // If we're at our limit and our current state is equal
            // to our goal state we return current
            return start;
        }
        
        pathIDS.add(start);
        // Add start to our visited cities
        LinkedList<City> currNeighbors = start.getList();
        // Get the list of connections from our current city
        
        if (limit > 0){
            // If this level isn't our limit
            for(int i = 0; i< currNeighbors.size(); i++){
                // Iterate through out list of connections
                City found = DLS(currNeighbors.get(i), end, limit - 1, pathIDS);
                // Run DLS recursively and subtract limit by one to search our children cities
                if(found != null){
                    // If we find it, return our goal state.
                    return found;
                }
            }
        }
        
        return null;
        // Else we return null
    }
    
    public static City IDS(City start, City end, LinkedList<City> pathIDS){
        for(int limit = 0; limit< Integer.MAX_VALUE; limit++){
            // Iterate from 0 to infinity or MAX_VALUE
            // This starts our search from a small basis to get bigger
            // This helps us get a better runtime is our city is close.
            
            City found = DLS(start, end, limit, pathIDS);
            // Run DLS and save its result as found
            
            if(found != null){
                // If we find the city, then we return found and exit the program
                return found;
            }
        }
        return null;
        // Else we return null
    }
    
    public static LinkedList<City> runIDS(City start, City end){
        LinkedList<City> pathIDS = new LinkedList<City>();
        // Create our visited linked list
        
        City result = IDS(start, end, pathIDS);
        // Store the result from running IDS
        
        Boolean idsValid = false;
        // Create a boolean variable to tell if the search was successful
        
        if(result != null){
            // If we found the goal state set idsValid as true
            idsValid = true;
        }
        
        System.out.println("Is City reachable(IDS): " + idsValid);
        // Prints whether the search was successful
        
        return pathIDS;
        // Return the visited linked list
    }
    
    public static City[][] initialize(int[][] cityKey){
        City [][] cityMap = createMap(cityKey);
        // Initializes the map, places the cities on the map.
        
        addNeighbors(cityMap);
        //Adds the neighbor nodes and then creates the roads that link the cities together.
        
        return cityMap;
    }
    
    public static void resetMap(City [][] cityMap){
        for(int i = 0; i< cityMap.length; i++){
            for(int j = 0; j< cityMap[0].length; j++){
                if(cityMap[i][j] != null){
                    cityMap[i][j].resetVisited();
                }
            }
        }
    }
    
    public static double distanceTraveled(LinkedList<City> list){
        City prev = list.get(0);
        City next = list.get(0);
        double distance = 0;
        
        for(int i = 1; i< list.size(); i++){
            next = list.get(i);
            distance+= findDistance(prev, next);
            prev = next;
        }
        return distance;
    }
        
    public static void main(String [] args){
        int [][] cityKey = new int[26][2];
        // This array keeps references to the city x and y coordinates to
        // allow for easy searching of cities. (0 == A ... 25 == Z)
        // cityKey[][0] == x and cityKey[][1] == y
        
        City[][] cityMap = initialize(cityKey);
        // Calls the initialize method and creates our map with cities.
        
        Random random = new Random();
        // Creates a new instance of Random
        
        int beg = random.nextInt(26);
        int fin = random.nextInt(26);
        // Generates two random integers between 0-25 which represents random cities
        
        while(fin == beg){
            fin = random.nextInt(26);
        }
        // If our two random variables are the same, we generate random
        // numbers between 0-25 until they are different. We then set
        // fin to that number
        
        City start = cityMap[cityKey[beg][0]][cityKey[beg][1]];
        City end = cityMap[cityKey[fin][0]][cityKey[fin][1]];
        // We take the integers generated from before and call cityKey. We then 
        // search cityMap with the coordinates retrieved from cityKey to get our
        // two cities.
        
        long startTimeBFS = System.nanoTime();
        LinkedList<City> pathBFS = runBFS(start, end);
        long endTimeBFS = System.nanoTime();
        // Logs the runtime of the algorithm and runs runBFS which calls
        // BFS and returns a linked list of visted cities
        
        System.out.println("BFS runtime: " + (endTimeBFS - startTimeBFS));
        // Prints out the runtime of BFS
        
        /*for(int i = 0; i< pathBFS.size(); i++){
            System.out.print(pathBFS.get(i).toString() + " ");
        }*/
        // Prints out the visited cities
        
        System.out.println("Cities visited: " + pathBFS.size());
        // Prints out the cities visited
        double distanceBFS = distanceTraveled(pathBFS);
        System.out.println("Distance Traveled " + distanceBFS);
        
        resetMap(cityMap);
        // Sets all the cities to unvisited so that the next algorithm can run
        
        System.out.println();
        long startTimeDFS = System.nanoTime();
        LinkedList<City> pathDFS = runDFS(start, end);
        long endTimeDFS = System.nanoTime();
        System.out.println("DFS runtime: " + (endTimeDFS - startTimeDFS));
        // Logs the runtime of the algorithm and runs runDFS which calls
        // DFS and returns a linked list of visted cities
        
        /*for(int j = 0; j< pathDFS.size(); j++){
            System.out.print(pathDFS.get(j).toString() + " ");
        }*/
        // Prints out the visited cities
        
        System.out.println("Cities visited: " + pathDFS.size());
        // Prints out the nubmer of cities visited
        double distanceDFS = distanceTraveled(pathDFS);
        System.out.println("Distance Traveled " + distanceDFS);
        resetMap(cityMap);
        // Resets the cities to unvisited
        System.out.println();
        long startTimeIDS = System.nanoTime();
        LinkedList<City> pathIDS = runIDS(start, end);
        long endTimeIDS = System.nanoTime();
        
        
        System.out.println("IDS runtime: " + (endTimeIDS - startTimeIDS));
        // Prints runtime of IDS
        pathIDS.add(end);
        
        /*for(int k = 0; k< pathIDS.size(); k++){
            System.out.print(pathIDS.get(k).toString() + " ");
        }*/
        // Prints the visited cities in IDS
        
        System.out.println("Cities Visited: " + pathIDS.size());
        // Prints the number of cities visited
        
        double distanceIDS = distanceTraveled(pathIDS);
        System.out.println("Distance Traveled: " + distanceIDS);
        
        System.out.println("average " + getAverage(cityMap));
        // Prints the average connections per city
        
        System.out.println("Nodes Generated " + nodesGenerated(cityMap));
        // Prints the number of nodes generated by the problem
    }
}

class City{
    private char name;
    // the name from A-Z
    private int X;
    // X-coordinate of our city
    private int Y;
    // Y-coordinate of our city
    private City[] neighbors = new City[5];
    // Array containing the closest 5 neighbors
    private int branches;
    // The number of roads coming out of the city
    private LinkedList<City> roads;
    // A linked list keeping track of which cities are connected to this city
    private City[] outRoads;
    // An array keeping for selecting the roads generated by this city
    private Boolean visited;
    // A variable to keep track if it was visited or not
    private int size;
    // For keeping track of the size of the linkedList
    
    public City(char name){
        this.name = name;
    }
    
    public City(char name, int branches, int X, int Y){
        this.name = name;
        this.branches = branches;
        this.X = X;
        this.Y = Y;
        roads = new LinkedList<City>();
        outRoads = new City[branches];
        visited = false;
        size = 0;
    }
    
    public char getName(){
        return name;
    }
    
    public int getX(){
        return X;
    }
    
    public int getY(){
        return Y;
    }
    
    public int getBranches(){
        return branches;
    }
    
    public void addRoads(City a){
        roads.add(a);
        size++;
    }
    
    public void addOutRoads(City a){
        for(int i = 0; i< outRoads.length; i++){
            if(outRoads[i] == null){
                outRoads[i] = a;
            }
        }
        addRoads(a);
        // Adds this road to our linked list
    }
    
    public LinkedList<City> getList(){
        return roads;
    }
    
    public int getSize(){
        return size;
    }
    
    public void addNeighbor(City a){
        for(int i = 0; i< neighbors.length; i++){
            if(neighbors[i] == null){
                neighbors[i] = a;
            }
        }
    }
    
    public City[] getNeighbors(){
        return neighbors;
    }
    
    public void setVisited(){
        visited = true;
    }
    
    public void resetVisited(){
        visited = false;
    }
    
    public Boolean getVisited(){
        return visited;
    }
    
    public Boolean equals(City comp){
        if(this.X == comp.getX()){
            if(this.Y == comp.getY()){
                return true;
            }
        }
        return false;
    }
    
    public String toString(){
        return Character.toString(name);
    }
}