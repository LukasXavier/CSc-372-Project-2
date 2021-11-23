/**
 * @author Eric Mendoza (ericmendoza@email.arizona.edu)
 * @author Luke Ramirez (lucasxavier@email.arizona.edu)
 * <p>File: FileParser.java
 * <p>Assignment: Project 2
 * <p>Course: CSc 372, Fall 21
 * 
 * <p>Purpose: This class removes comments and 'compresses' multiline expressions to a single line
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileParser {
    
    /**
     * Condenses multiline expressions down to a single line by 'replacing' the
     * new line character with a null terminator
     * @param lines an ArrayList<String> that came from getLines()
     * @return a ArrayList<String> of single line expressions
     */
    public ArrayList<String> cleanLines(ArrayList<String> lines) {
        int curlyBraceCounter = 0;
        boolean curlyBrace = false;
        ArrayList<String> temp = new ArrayList<String>();
        String compressed = "";
        // iterates over each line in lines
        for (String line : lines) {
            // handles print statement
            if (line.contains(">{") && line.contains("}")) {
                if (!curlyBrace) {temp.add(line); }
                else { compressed += line + '\0'; }
                continue;
            }
            // handles opening curly braces
            if (line.contains("{")) {
                if (curlyBraceCounter != 0) { compressed += '\0'; }
                // a line that has both open and closing
                if (line.contains("}")) {
                    if (curlyBrace) { compressed += line + '\0'; }
                    else { temp.add(line); }
                    continue;
                }
                curlyBraceCounter++;
                curlyBrace = true;
                compressed += line;
                continue;
            }
            // handles closing braces
            if (line.contains("}")) {
                curlyBraceCounter--;
                if (curlyBraceCounter == 0) {
                    curlyBrace = false;
                    temp.add(compressed + line);
                    compressed = "";
                } else { compressed += line + '\0'; }
                continue;
            }
            // if it gets all the way down here
            if (curlyBraceCounter == 0) { temp.add(line); }
            if (curlyBrace) { compressed += line + '\0'; }
        }
        return temp;
    }

    /**
     * removes comments from the input text file
     * @param args the command line arguments from Compiler.java
     * @return a cleaned up arrayList of strings
     */
    public ArrayList<String> getLines(String[] args) {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            BufferedReader file = new BufferedReader(new FileReader(args[0])); 
            String line = file.readLine();
            int commentCounter = 0;
            boolean multiLineComment = false;
            // loops until readLine() gives a null line
            while (line != null) {
                // simple checking
                if (line.length() < 2) {
                    if (line.length() == 1) { lines.add(line); }
                    line = file.readLine();
                    continue;
                }
                // single line comments
                if (line.strip().length() > 2) {
                    if (line.strip().substring(0, 2).equals("//")) {
                        line = file.readLine();
                        continue;
                    }
                }
                // opening multiline comment
                if (line.substring(0,2).equals("/*")) {
                    multiLineComment = true;
                    commentCounter++;
                }
                else if (multiLineComment) {
                    if (commentCounter == 0) {
                        multiLineComment = false;
                        lines.add(line.strip());
                    }
                    // closing multiline comment
                    else if (line.substring(0, 2).equals("*/")) {
                        commentCounter--;
                    }
                } else {
                    lines.add(line.strip());
                }
                line = file.readLine();
            }
            file.close();
            lines = cleanLines(lines);
        } catch (FileNotFoundException e) { System.err.println(e.getMessage());
        } catch (IOException e) { System.err.println(e.getMessage()); }
        return lines;
    }
}