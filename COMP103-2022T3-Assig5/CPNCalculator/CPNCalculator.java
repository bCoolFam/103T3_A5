// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2022T3, Assignment 5
 * Name: yoon hong
 * Username: hongyoon
 * ID: 300441485
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;
import java.math.*;

/**
 * Calculator for Cambridge-Polish Notation expressions
 * (see the description in the assignment page)
 * User can type in an expression (in CPN) and the program
 * will compute and print out the value of the expression.
 * The template provides the method to read an expression and turn it into a
 * tree.
 * You have to write the method to evaluate an expression tree.
 * and also check and report certain kinds of invalid expressions
 */

public class CPNCalculator {

    /**
     * Setup GUI then run the calculator
     */
    public static void main(String[] args) {
        CPNCalculator calc = new CPNCalculator();
        calc.setupGUI();
        calc.runCalculator();
    }

    /**
     * Setup the GUI
     */
    public void setupGUI() {
        UI.addButton("Clear", UI::clearText);
        UI.addButton("Quit", UI::quit);
        UI.setDivider(1.0);
    }

    /**
     * KEY:
     *  (*) - indicates done
     *  (~) - indicates WIP
     *  ()  - indicates not done
     * 
     * Check list: 
     * 
     * CORE ******* (*)
     *      + - * / PI E (*)
     *      evaluate (*) 
     *      recursive (*)
     * COMPLETION* ***** (~)
     *      valid number of operands (*)
     *      extended eval   (~)
     *          log [must be 1 or 2 operands] ()
     *          ln [1 operand]  ()
     *          avg [1 operand] ()      
     *          sin, cos, and tan  [1 operand] ()
     *          sqrt  [1 oper]()
     *          ^   [2 oper]  ()
     *          dist  [6 operands]      ()
     *      
     * CHALLENGE **** (  )
     *      extend readExpr ( )
     *          empty brackets()
     *          Check invalid expression with brackets -
     *          that don't match ()
     *          printExpr()
     * 
     * 
     */

    /**
     * Run the calculator:
     * loop forever: (a REPL - Read Eval Print Loop)
     * - read an expression,
     * - evaluate the expression,
     * - print out the value
     * Invalid expressions could cause errors when reading or evaluating
     * The try-catch prevents these errors from crashing the program -
     * the error is caught, and a message printed, then the loop continues.
     */
    public void runCalculator() {
        UI.println("Enter expressions in pre-order format with spaces");
        UI.println("eg   ( * ( + 4 5 8 3 -10 ) 7 ( / 6 4 ) 18 )");
        while (true) {
            UI.println();
            try {
                GTNode<ExpElem> expr = readExpr();
                double value = evaluate(expr);
                UI.println(" -> " + value);
            } catch (Exception e) {
                UI.println("Something went wrong! " + e);
            }
        }
    }

    /**
     * Evaluate method
     * pulls the <EXPELEM> GTNode and operates on all the children of the nodes//
     * recurse into evaluate for
     * 
     */
    public double evaluate(GTNode<ExpElem> expr) {
        if (expr == null) {
            return Double.NaN;
        }
        try {

            String expression = expr.getItem().getOperator();
            switch (expression) {
                case "E":
                    return Math.E;

                case "PI":
                    return Math.PI;

                case "#":
                    return expr.getItem().getValue();

                /**
                 * addition
                 * variable sum set to 0
                 * for each (next) child of addition subtree
                 * add them up
                 * returns the sum of subtree
                 */
                case "+":
                    if (expr.numberOfChildren() < 1) {
                        UI.println("ERROR YOU NEED AT LEAST ONE OPERANDS FOR +");
                        break;
                    } // operand catcher

                    double sum = evaluate(expr.getChild(0));
                    for (int i = 1; i < expr.numberOfChildren(); i++) {
                        sum += evaluate(expr.getChild(i));
                    }
                    return sum;

                /**
                 * subtraction
                 * variable subtract is initially is set to evaulation of first child of
                 * subtraction tree
                 * subtracts the next child in subtree FROM the subtract first value
                 * does that for each child using evaluate() recursion
                 * then returns subtraction of subtree (order sensitive)
                 */
                case "-":
                    if (expr.numberOfChildren() < 1) {
                        UI.println("ERROR YOU NEED AT LEAST ONE OPERANDS FOR -");
                        break;
                    } // operand catcher
                    double subtract = evaluate(expr.getChild(0)); // subtract var set to first item of subtree
                    for (int i = 1; i < expr.numberOfChildren(); i++) {
                        subtract = subtract - evaluate(expr.getChild(i));
                    }
                    return subtract;

                /**
                 * Multiplication
                 * var mulitply is set to evaulation of first child of multiplication tree
                 * multiplies next number to previous value to the var multiply
                 * returns that value
                 */
                case "*":
                    if (expr.numberOfChildren() <= 1) {
                        UI.println("ERROR YOU NEED AT LEAST TWO OPERANDS FOR *");
                        break;
                    } // operand catcher
                    double multiply = evaluate(expr.getChild(0));
                    for (int i = 1; i < expr.numberOfChildren(); i++) {
                        multiply = multiply * evaluate(expr.getChild(i));
                    }
                    return multiply;

                /**Division
                 * var division is set to the evaluation of the first child
                 * then divides the evaulation of each children of each child
                 * stored as var division
                 * returns var division
                 */

                case "/":
                    if (expr.numberOfChildren() <= 1) {
                        UI.println("ERROR YOU NEED AT LEAST TWO OPERANDS FOR /");
                        break;
                    } // operand catcher
                    double division = evaluate(expr.getChild(0));
                    for (int i = 1; i < expr.numberOfChildren(); i++) {
                        division = division / evaluate(expr.getChild(i));
                    }
                    return division;


                /**Exponent
                 * checks if two operands are present; breaks if not
                 * if pass: evaluates and assigns the two operands as variables:
                 *  operand1 and operand2
                 * returns the operand1 to the power of operand2
                */    
                
                case "^":
                if (!(expr.numberOfChildren() == 2)) {
                    UI.println("ERROR YOU NEED AT LEAST TWO OPERANDS FOR ^");break;} // operand catcher
                    
                    double operand1 = evaluate(expr.getChild(0));
                    double operand2 = evaluate(expr.getChild(1));
                    return Math.pow(operand1, operand2);
                
                /**Square Root
                 * checks for operand criterion
                 * evaluates and assigns the ONE operand to variable
                 *  sqrtOperation
                 * then passes the var into Math.sqrt() method
                 * returns that result
                 */
                case "sqrt":
                    if (!(expr.numberOfChildren() == 1)) {
                        UI.println("ERROR YOU NEED ONLY ONE OPERANDS FOR SQRT");break;} // operand catcher
                    
                    double sqrtOperation = evaluate(expr.getChild(0));
                    return Math.sqrt(sqrtOperation);
                
                /**Log
                 * checks operand criteria (1 or 2) using XOR
                 * has two modes:
                 *       first one for one operand and the second for two
                 * if ONE operand
                 * finds and returns Math.log10(operand)
                 * if TWO operand
                 * finds and returns Math.log(first operand) DIVIDED by MATH.log(second operand)
                 * NB: (above) is equal to he log of Log first to base second is 
                 */
                case "log":
                    if (!   ((expr.numberOfChildren() == 1) ^ (expr.numberOfChildren() == 2))   ) { //if number of operand are neither 1 nor 2: break;
                    UI.println("ERROR YOU NEED AT LEAST TWO OPERANDS");break;} // operand catcher
                    double firstOperand = evaluate(expr.getChild(0));
                    if(expr.numberOfChildren() == 1 ){
                        firstOperand = Math.log10(firstOperand);
                        return firstOperand;
                    }
                    if(expr.numberOfChildren() == 2){
                        double secondOperand = evaluate(expr.getChild(1)); //we can use absolute positions as theres only 2 operands and we know this location
                        return Math.log(firstOperand) / Math.log(secondOperand);
                    }

                case "ln":
                    


                case "sin":
                case "cos":
                case "tan":
                case "dist":
                case "avg":


                default:
                UI.println("ERROR IN PROCESSING");
                break;

            }

        } catch (IndexOutOfBoundsException e) {
            UI.println(e.toString());
        }

        return Double.NaN;
    }

    /**
     * Reads an expression from the user and constructs the tree.
     */
    public GTNode<ExpElem> readExpr() {
        String expr = UI.askString("expr:");
        return readExpr(new Scanner(expr)); // the recursive reading method
    }

    /**
     * Recursive helper method.
     * Uses the hasNext(String pattern) method for the Scanner to peek at next token
     */
    public GTNode<ExpElem> readExpr(Scanner sc) {
        if (sc.hasNextDouble()) { // next token is a number: return a new node
            return new GTNode<ExpElem>(new ExpElem(sc.nextDouble()));
        } else if (sc.hasNext("\\(")) { // next token is an opening bracket
            sc.next(); // read and throw away the opening '('
            ExpElem opElem = new ExpElem(sc.next()); // read the operator
            GTNode<ExpElem> node = new GTNode<ExpElem>(opElem); // make the node, with the operator in it.
            while (!sc.hasNext("\\)")) { // loop until the closing ')'
                GTNode<ExpElem> child = readExpr(sc); // read each operand/argument
                node.addChild(child); // and add as a child of the node
            }
            sc.next(); // read and throw away the closing ')'
            return node;
        } else { // next token must be a named constant (PI or E)
                 // make a token with the name as the "operator"
            return new GTNode<ExpElem>(new ExpElem(sc.next()));
        }
    }

}
