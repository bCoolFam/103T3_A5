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
 */

import ecs100.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class BusNetworks {

    /** YOUR DOCUMENTATION COMMENT */
    private Map<String,Town> busNetwork = new HashMap<String,Town>();

    // CORE
    /** YOUR DOCUMENTATION COMMENT */
    public void loadNetwork(String filename) {
        try {
            busNetwork.clear();
            UI.clearText();
            List<String> lines = Files.readAllLines(Path.of(filename));
            String firstLine = lines.remove(0);
            /*# YOUR CODE HERE */

            UI.println("Loaded " + busNetwork.size() + " towns:");

        } catch (IOException e) {throw new RuntimeException("Loading data.txt failed" + e);}
    }

    // CORE
    /** YOUR DOCUMENTATION COMMENT */
    public void printNetwork() {
        UI.println("The current network: \n====================");
        /*# YOUR CODE HERE */

    }

    // CORE
    /** YOUR DOCUMENTATION COMMENT */
    public void findRoute(Town town, Town dest) {
        UI.println("Looking for route between "+town.getName()+" and "+dest.getName()+":");
        /*# YOUR CODE HERE */
        
    }

    // COMPLETION
    /** YOUR DOCUMENTATION COMMENT */
    public void printReachable(Town town){
        UI.println("\nFrom "+town.getName()+" you can get to:");
        /*# YOUR CODE HERE */
        
    }

    // COMPLETION
    /** YOUR DOCUMENTATION COMMENT */
    public void printConnectedGroups() {
        UI.println("Groups of Connected Towns: \n================");
        int groupNum = 1;
        /*# YOUR CODE HERE */

    }

    // Suggested helper method for printConnectedGroups
    /** YOUR DOCUMENTATION COMMENT */
    public Set<Town> findAllConnected(Town town) {
        /*# YOUR CODE HERE */

    }

    /**  
     * Set up the GUI (buttons and mouse)
     */
    public void setupGUI() {
        UI.addButton("Load", ()->{loadNetwork(UIFileChooser.open());});
        UI.addButton("Print Network", this::printNetwork);
        UI.addButton("Find Route", () -> {findRoute(askTown("From"), askTown("Destination"));});
        UI.addButton("All Reachable", () -> {printReachable(askTown("Town"));});
        UI.addButton("All Connected Groups", this::printConnectedGroups);
        UI.addButton("Clear", UI::clearText);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1100, 500);
        UI.setDivider(1.0);
        loadNetwork("data-small.txt");
    }

    // Main
    public static void main(String[] arguments) {
        new BusNetworks().setupGUI();
    }

    // Utility method
    /**  
     * Method to get a Town from a dialog box with a list of options
     */
    public Town askTown(String question){
        Object[] possibilities = busNetwork.keySet().toArray();
        Arrays.sort(possibilities);
        String townName = (String)javax.swing.JOptionPane.showInputDialog
            (UI.getFrame(),
                question, "",
                javax.swing.JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                possibilities[0].toString());
        return busNetwork.get(townName);
    }

}
