import java.util.Scanner;

public class Project2 {
    static String[][] parseTable;
    static String[][] grammar;
    static StringBuilder input;
    static StringBuilder stack;
    static final int maxRows = 12;
    static final int maxColumns = 10;

    public static void quit() {
        System.exit(0);
    }

    public static void error() {
        System.out.println("Error: Empty entry in parse table reached.");
        System.exit(-1);
    }

    public static void initializeTable() {
        parseTable = new String[][]{
                {"0", "s5", "emp", "emp", "s4", "emp", "emp", "1", "2", "3"},
                {"1", "emp", "s6", "emp", "emp", "emp", "acc", "emp", "emp", "emp"},
                {"2", "emp", "r2", "s7", "emp", "r2", "r2", "emp", "emp", "emp"},
                {"3", "emp", "r4", "r4", "emp", "r4", "r4", "emp", "emp", "emp"},
                {"4", "s5", "emp", "emp", "s4", "emp", "emp", "8", "2", "3"},
                {"5", "emp", "r6", "r6", "emp", "r6", "r6", "emp", "emp", "emp"},
                {"6", "s5", "emp", "emp", "s4", "emp", "emp", "emp", "9", "3"},
                {"7", "s5", "emp", "emp", "s4", "emp", "emp", "emp", "emp", "10"},
                {"8", "emp", "s6", "emp", "emp", "s11", "emp", "emp", "emp", "emp"},
                {"9", "emp", "r1", "s7", "emp", "r1", "r1", "emp", "emp", "emp"},
                {"10", "emp", "r3", "r3", "emp", "r3", "r3", "emp", "emp", "emp"},
                {"11", "emp", "r5", "r5", "emp", "r5", "r5", "emp", "emp", "emp",},
        };
    }

    public static void initializeGrammar() {
        grammar = new String[][]{
                {"num", "grammar"},
                {"1", "E>E+T"},
                {"2", "E>T"},
                {"3", "T>T*F"},
                {"4", "T>F"},
                {"5", "F>(E)"},
                {"6", "F>id"}
        };
    }

    public static void handleAction(String action, boolean isId) {
        System.out.println(String.format("%-30s%-30s%-10s", stack.toString(), input.toString(), action));
        String temp;
        if (action.equals("acc")) {
            quit();
        } else if (action.equals("emp")) {
            error();
        } else if (action.charAt(0) == 's') {
            int actionNum = Integer.parseInt(action.substring(1));
            if (isId) {
                temp = input.substring(0, 2);
                stack.append(temp);
                input.delete(0, 2);
            } else {
                temp = input.substring(0, 1);
                stack.append(temp);
                input.delete(0, 1);
            }
            stack.append(actionNum);
        } else {
            String rule = grammar[Character.getNumericValue(action.charAt(1))][1]; //get the rule
            int index;
            switch (rule) { //parse the rule
                case "E>E+T":
                    index = stack.indexOf("E", stack.length() - 7);
                    stack.delete(index, stack.length());
                    stack.append('E');
                    stack.append(parseTable[Character.getNumericValue(stack.charAt(stack.length() - 2))][7]);
                    break;
                case "E>T":
                    index = stack.indexOf("T", stack.length() - 4);
                    stack.delete(index, stack.length());
                    stack.append('E');
                    stack.append(parseTable[Character.getNumericValue(stack.charAt(stack.length() - 2))][7]);
                    break;
                case "T>T*F":
                    index = stack.indexOf("T", stack.length() - 7);
                    stack.delete(index, stack.length());
                    stack.append('T');
                    stack.append(parseTable[Character.getNumericValue(stack.charAt(stack.length() - 2))][8]);
                    break;
                case "T>F":
                    index = stack.indexOf("F", stack.length() - 4);
                    stack.delete(index, stack.length());
                    stack.append('T');
                    stack.append(parseTable[Character.getNumericValue(stack.charAt(stack.length() - 2))][8]);
                    break;
                case "F>(E)":
                    index = stack.indexOf("(", stack.length() - 8);
                    stack.delete(index, stack.length());
                    stack.append('F');
                    stack.append(parseTable[Character.getNumericValue(stack.charAt(stack.length() - 2))][9]);
                    break;
                case "F>id":
                    index = stack.indexOf("i", stack.length() - 4);
                    stack.delete(index, stack.length());
                    stack.append('F');
                    stack.append(parseTable[Character.getNumericValue(stack.charAt(stack.length() - 2))][9]);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        int stackNum; //for storing the num at the end of the stack
        String action; //for storing the current action from the table
        int actionNum; //only used if the action state is double digits
        initializeTable();
        initializeGrammar();
        Scanner kb = new Scanner(System.in);
        System.out.print("Enter sentence: ");
        input = new StringBuilder(kb.nextLine());
        stack = new StringBuilder("0"); //stack always starts with 0
        System.out.println(String.format("%-30s%-30s%-10s", "Stack", "Input", "Action"));
        while (true) {
            switch (input.charAt(0)) {
                case 'i':
                    if (input.charAt(1) == 'd') { //checking for id
                        if (stack.length() > 1) {
                            if (Character.isDigit(stack.charAt(stack.length() - 2))) {
                                stackNum = Integer.parseInt(stack.substring(stack.length() - 2));
                            } else {
                                stackNum = Character.getNumericValue(stack.charAt(stack.length() - 1));
                            }
                        } else {
                            stackNum = Character.getNumericValue(stack.charAt(stack.length() - 1));
                        }
                        action = parseTable[stackNum][1];
                        handleAction(action, true);
                    }
                    break;
                case '+':
                    if (Character.isDigit(stack.charAt(stack.length() - 2))) {
                        stackNum = Integer.parseInt(stack.substring(stack.length() - 2));
                    } else {
                        stackNum = Character.getNumericValue(stack.charAt(stack.length() - 1));
                    }
                    action = parseTable[stackNum][2];
                    handleAction(action, false);
                    break;
                case '*':
                    if (Character.isDigit(stack.charAt(stack.length() - 2))) {
                        stackNum = Integer.parseInt(stack.substring(stack.length() - 2));
                    } else {
                        stackNum = Character.getNumericValue(stack.charAt(stack.length() - 1));
                    }
                    action = parseTable[stackNum][3];
                    handleAction(action, false);
                    break;
                case '(':
                    if (Character.isDigit(stack.charAt(stack.length() - 2))) {
                        stackNum = Integer.parseInt(stack.substring(stack.length() - 2));
                    } else {
                        stackNum = Character.getNumericValue(stack.charAt(stack.length() - 1));
                    }
                    action = parseTable[stackNum][4];
                    handleAction(action, false);
                    break;
                case ')':
                    if (Character.isDigit(stack.charAt(stack.length() - 2))) {
                        stackNum = Integer.parseInt(stack.substring(stack.length() - 2));
                    } else {
                        stackNum = Character.getNumericValue(stack.charAt(stack.length() - 1));
                    }
                    action = parseTable[stackNum][5];
                    handleAction(action, false);
                    break;
                case '$':
                    if (Character.isDigit(stack.charAt(stack.length() - 2))) {
                        stackNum = Integer.parseInt(stack.substring(stack.length() - 2));
                    } else {
                        stackNum = Character.getNumericValue(stack.charAt(stack.length() - 1));
                    }
                    action = parseTable[stackNum][6];
                    handleAction(action, false);
                    break;
            }
        }
    }
}
