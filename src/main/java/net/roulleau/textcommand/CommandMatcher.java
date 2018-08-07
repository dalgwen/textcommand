package net.roulleau.textcommand;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jdk.nashorn.internal.runtime.ParserException;
import net.roulleau.textcommand.parser.CommandParser;
import net.roulleau.textcommand.parser.SimpleGrammar;
import net.roulleau.textcommand.parser.Token;

public class CommandMatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandMatcher.class);
    
    private static CommandParser commandParser = new CommandParser();

    private Method method;

    private int priority;

    private String originalTextCommand;

    private Pattern compiledRegex;

    private Map<String, Integer> variableNameToGroup = new HashMap<>();
    private List<Integer> variableNumberToGroup = new ArrayList<>();

    public Integer getGroupNumberForVariable(String variableName) {
        return variableNameToGroup.get(variableName);
    }
    
    public Integer getGroupNumberForVariable(Integer variableNumber) {
        return variableNumberToGroup.get(variableNumber);
    }

    public Method getMethod() {
        return method;
    }

    public int getPriority() {
        return priority;
    }

    public String getOriginalTextCommand() {
        return originalTextCommand;
    }

    public Pattern getCompiledRegex() {
        return compiledRegex;
    }

    public CommandMatcher(Method method, String textCommand, int priority) {
        this.priority = priority;
        this.method = method;
        this.originalTextCommand = textCommand;

        parseAndCreateRegex(textCommand);
    }

    private void parseAndCreateRegex(String textCommand) {
        
        LOGGER.info("Trying to parse and create regex for command {}", textCommand);
        
        LinkedList<Token<SimpleGrammar>> tokensParsed = commandParser.parseCommand(textCommand);

        StringBuilder generatedRegex = new StringBuilder();
        int currentGroupIndex = 1;
        for (Token<SimpleGrammar> token : tokensParsed) {
            switch (token.token) {
            case OR:
                generatedRegex.append(token.sequence);
                currentGroupIndex++;
                break;
            case STRING:
                generatedRegex.append(token.sequence);
                break;
            case VARIABLE:
                generatedRegex.append("(.*)");
                variableNameToGroup.put(token.sequence.substring(1), currentGroupIndex);
                variableNumberToGroup.add(currentGroupIndex);
                currentGroupIndex++;
                break;
            default:
                throw new ParserException("Unknown token type");
            }
        }

        LOGGER.info("Parsing and regex creation finished : {}", generatedRegex);
        compiledRegex = Pattern.compile(generatedRegex.toString());
    }
}
