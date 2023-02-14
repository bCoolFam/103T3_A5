// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2022T3, Assignment 5
 * Name: yoon hong
 * Username: hongyoon
 * ID: 300441485
 * 
 * code can also be found at
 * https://github.com/bCoolFam/103T3_A5/
 * 
 * 
 * 
 * [1] wrong way error : HYPOTHETICALLY say a town named 'Hamilton' may have a neighbor "napier" to the north 
 *                              and another neighbor to the south called "palmerston north", it would be wrong of me
 *                              to assume that "napier" and "palmerston north" were neighbors because they were 
 *                              neighbors in the "wrong way".
 *                          
 */

import ecs100.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class BusNetworks {

    /**
     * CheckList
     * CORE:
     * loadnetwork() ()
     * printNetwork() ()
     * findRoute() ()
     * 
     * COMPLETION:
     * 
     * 
     * 
     * 
     */

    /**
     * BUS NETWORKS
     * Fields to access globally
     * 
     * 
     */
    private Map<String, Town> busNetwork = new HashMap<String, Town>();
    private Map<String, List<Town>> originToDest = new HashMap<String, List<Town>>();
    private List<Town> destTowns = new ArrayList<>();
    private Queue<Town> townQueue = new LinkedList<Town>() ;


    // CORE
     /** loadNetwork() -----     ()
     * printNetwork() -------       ()
     * findRoute()--------       ()
     * 
     */




    /**LoadNetwork
     * opens file and parses information into busNetwork
     * 
     * 
     * 
     * @param filename passed down filename from UI File Chooser from user interaction
     * 
     * 
     * 
     */

    public void loadNetwork(String filename) {
        try { //ioexception
            busNetwork.clear();
            originToDest.clear();
            destTowns.clear(); // empty list/ reinitialize
            townQueue.clear();
            UI.clearText();
            List<String> lines = Files.readAllLines(Path.of(filename));
            

            // IDEA: could use recursion for below

            // while the list of lines is not empty... iterate
            while (!lines.isEmpty()) {

                String line = lines.remove(0); // removes the 'top' of the line
                /** Initialising the scanner and all the variables for line grabbed */
                destTowns = new ArrayList<Town>(); // resets list of destination towns
                Scanner sc = new Scanner(line);
                String originStation = sc.next(); // origin station AKA first token of line
                Town originTown = new Town(null);

                for (int i = 0; i < lines.size(); i++) {
                    String currentLine = lines.get(i); // current line ie. AKL HML NPR NP etc..
                
                        // execute below
                        originTown = townObjectGetter(originStation); // creates new Town obj if Map bN does not contain the
                                                                      // first token aka origin
                        busNetwork.put(originStation, originTown); // creates 'root' nodes with children
                        originToDest.put(originStation, null); // and creates a new object
                        originTown = busNetwork.get(originStation); //retrieve town obj
                        townQueue.offer(originTown); //offer the origin Town obj to the queue (first item in this loop)
                        String destinationStation = sc.next(); // first destination AKA NEIGHBOR

                        /**
                         * below block will scan for the non-neighbor stations
                         */

                        while (sc.hasNext()) { // while loop for the rest of the destinations in line if it exists
                            destinationStation = sc.next(); // string value for for next stop in line
                            /** check if town exists in network */
                            if (!busNetwork.containsKey(destinationStation)) { // if this string doesnt exist in the
                                                                               // busNetwork
                                Town destionTown = new Town(destinationStation);// create the town object
                                busNetwork.put(destinationStation, destionTown);// link it to the busNetwork
                                // DONT LINK AS NEIGHBORS for wrong way^[1] error
                                //
                                //
                                //

                            } else { // else must exist in bus network
                                     // get the town node from busnetwork
                                     // 

                            }

                            Town destinationSTown = new Town(destinationStation);
                            destTowns.add(destinationSTown);

                        } // rest of destinations while end **

                    } // for ea line in lines end***

                    originToDest.put(originStation, destTowns);

                    sc.close();
                }

                UI.println("Loaded " + busNetwork.size() + " towns:");

            } // while each line end

         catch (IOException e) {
            throw new RuntimeException("Loading data.txt failed" + e);
     }
    }
    

    /**DEBUGGER** 
     * TESTER METHOD
     * called by pushing TEST button in UI Button panel in use
     * the idea is to output the origin and its destination black and redn texts
     * respectively
     * 
     * 
     */
    public void testLoadData() {
        Set<String> testerSet = new HashSet<String>();
        UI.println("BusNetwork Stored objects: ");
        for(String a : busNetwork.keySet()){
            UI.println(" " + a);
        }
        UI.println("origin set: ");
        for(String b : originToDest.keySet()){
            UI.print("======== " + b);

        }


    }


    /**recursive
     * town getter or creator; 
     * also adds neighbors by checking the queue of last visited 
     * town (if applicable; handles null values) 
     * @param townName parsing current string token from scan (should be a town name)
     * @return  the town object (aka node) that exists in the network *OR*
     * @return a NEW town object (aka node) and adds to the network
     */
    public Town townObjectGetter(String townName){
        try {
            if(busNetwork.containsKey(townName)){ //if exists in busnetwork
                
                if(busNetwork.get(townName).equals(null)){   //odd case if key exists but town object is 'null'
                    Town createTown = new Town(townName); //create a new town object/node 
                    busNetwork.replace(townName, null, createTown); //replace null value with real object and node
                }
                    /**
                     * townQueue implementation
                     * for adding neighbors by peeking and polling from a list of visited stops
                     * on the line, only adds the last/closest stop from parsed town
                     * 
                     */
                if(!(townQueue.peek()==null)){//if the queue of towns passed along the line is not empty
                    if(townQueue.peek().getName().equalsIgnoreCase(townName)){ //if the last visited node is the same as this (which is an error)
                        townQueue.remove();//we fix it
                        if(!(townQueue.peek()==null)){ //if theres still a value after we skip the current town in queue error
                            Town neighborTown = townObjectGetter(townQueue.peek().getName());//must be a neighbor
                            busNetwork.get(townName).addNeighbour(neighborTown); //adds this newly retreived neighbor as a new 
                            //no need to worry about adding duplicates as neighbors as stores as SET and any duplicate values will be rejected
                            }   //if null block catch end***
                        
                    }else{//if head of townQ is not thesame ,ergo must be neighbor
                        busNetwork.get(townName).addNeighbour(townQueue.poll());//add the last town/node as a neighbor to this town obj/node
                    }//else end**
                    
                    }/**end of  townQ neighbor adding implentation. 
                    *no action required if has no neighbors
                    */
                    return busNetwork.get(townName);
                }else{//if town doest exist in network
                Town createTown = new Town(townName);  //create a new town object with string
                 if(!(townQueue.peek()==null)){//if the queue of towns passed along the line is not empty
                    
                        if(townQueue.peek().getName().equalsIgnoreCase(townName)){ //if the last visited node is the same as this (which is an error)
                            townQueue.remove();//we fix it
                        
                            if(!(townQueue.peek()==null)){ //if theres still a value after we skip the current town in queue error
                                Town neighborTown = townObjectGetter(townQueue.peek().getName());//must be a neighbor
                                busNetwork.get(townName).addNeighbour(neighborTown); //adds this newly retreived
                                }   //if null block catch end***
                            
                        }else{//if head of townQ is not thesame ,ergo must be neighbor
                            busNetwork.get(townName).addNeighbour(townQueue.poll());//add the last town/node as a neighbor to this town obj/node
                        }//else end**
                        
                        }/**end of  townQ neighbor adding implentation. 
                        *no action required if has no neighbors
                        */

                busNetwork.put(townName, createTown);//link to network
                return busNetwork.get(townName);//return town
            }
        } catch (NullPointerException e) {
            UI.println("NullPExc. townObjecGetter():     "+e.toString());
        }
        
        return null; //CHANGE
    }


    // CORE
    /** YOUR DOCUMENTATION COMMENT */
    public void printNetwork() {
        UI.println("The current network: \n====================");
        /* # YOUR CODE HERE */

    }

    // CORE
    /** YOUR DOCUMENTATION COMMENT */
    public void findRoute(Town town, Town dest) {
        UI.println("Looking for route between " + town.getName() + " and " + dest.getName() + ":");
        /* # YOUR CODE HERE */

    }

    // COMPLETION
    /** YOUR DOCUMENTATION COMMENT */
    public void printReachable(Town town) {
        UI.println("\nFrom " + town.getName() + " you can get to:");
        /* # YOUR CODE HERE */

    }

    // COMPLETION
    /** YOUR DOCUMENTATION COMMENT */
    public void printConnectedGroups() {
        UI.println("Groups of Connected Towns: \n================");
        int groupNum = 1;
        /* # YOUR CODE HERE */

    }

    // Suggested helper method for printConnectedGroups
    /** YOUR DOCUMENTATION COMMENT */
    // public Set<Town> findAllConnected(Town town) {
    /* # YOUR CODE HERE */

    // }

    /**
     * Set up the GUI (buttons and mouse)
     */
    public void setupGUI() {
        UI.addButton("Load", () -> {
            loadNetwork(UIFileChooser.open());
        });
        UI.addButton("Print Network", this::printNetwork);
        UI.addButton("Find Route", () -> {
            findRoute(askTown("From"), askTown("Destination"));
        });

        UI.addButton("test", this::testLoadData);
        UI.addButton("All Reachable", () -> {
            printReachable(askTown("Town"));
        });
        UI.addButton("All Connected Groups", this::printConnectedGroups);
        UI.addButton("Reset", () -> {
            UI.clearText();
        });
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1100, 500);
        UI.setDivider(1.0);
        loadNetwork("data-small.txt");
    }

    // Main
    public static void main(String[] arguments) {
        BusNetworks bN = new BusNetworks();
        bN.setupGUI();
    }

    // Utility method
    /**
     * Method to get a Town from a dialog box with a list of options
     */
    public Town askTown(String question) {
        Object[] possibilities = busNetwork.keySet().toArray();
        Arrays.sort(possibilities);
        String townName = (String) javax.swing.JOptionPane.showInputDialog(UI.getFrame(),
                question, "",
                javax.swing.JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                possibilities[0].toString());
        return busNetwork.get(townName);
    }

}
