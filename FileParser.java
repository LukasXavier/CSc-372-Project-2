import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileParser {
    
    public ArrayList<String> cleanLines(ArrayList<String> lines) {
        int curlyBraceCounter = 0;
        boolean curlyBrace = false;
        ArrayList<String> temp = new ArrayList<String>();
        String compressed = "";
        for (String line : lines) {
            if (line.contains("{")) {
                if (curlyBraceCounter != 0) {
                    compressed += '\0';
                }
                if (line.contains("}")) {
                    if (curlyBrace) {
                        compressed += line + '\0';
                    } else {
                        temp.add(line);
                    }
                    continue;
                }
                curlyBraceCounter++;
                curlyBrace = true;
                compressed += line;
                continue;
            }
            if (line.contains("}")) {
                curlyBraceCounter--;
                if (curlyBraceCounter == 0) {
                    curlyBrace = false;
                    temp.add(compressed + line);
                    compressed = "";
                } else { 
                    compressed += line + '\0';
                }
                continue;
            }
            if (curlyBraceCounter == 0) {
                temp.add(line);
            }
            if (curlyBrace) {
                compressed += line;
            }
        }
        return temp;
    }

    public ArrayList<String> getLines(String[] args) {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            BufferedReader file = new BufferedReader(new FileReader(args[0])); 
            String line = file.readLine();
            int commentCounter = 0;
            boolean multiLineComment = false;

            while (line != null) {
                if (line.length() < 2) {
                    if (line.length() == 1) { lines.add(line); }
                    line = file.readLine();
                    continue;
                }
                if (line.substring(0,2).equals("/*")) {
                    multiLineComment = true;
                    commentCounter++;
                }
                else if (multiLineComment) {
                    if (commentCounter == 0) {
                        multiLineComment = false;
                        lines.add(line);
                    }
                    else if (line.substring(0, 2).equals("*/")) {
                        commentCounter--;
                    }
                } else {
                    lines.add(line);
                }
                line = file.readLine();
            }
            file.close();
            lines = cleanLines(lines);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return lines;
    }
    
}
