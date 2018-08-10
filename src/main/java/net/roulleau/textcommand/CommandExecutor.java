package net.roulleau.textcommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.roulleau.textcommand.exception.CommandExecutionException;
import net.roulleau.textcommand.exception.InvalidParameterException;
import net.roulleau.textcommand.exception.InvocationExecutionException;
import net.roulleau.textcommand.exception.NoMatchingMethodFoundException;

public class CommandExecutor {
    
    public static final Logger LOGGER = LoggerFactory.getLogger(CommandExecutor.class);
    
    public CommandStore commandStore;

    public CommandExecutor(CommandStore commandStore) {
        super();
        this.commandStore = commandStore;
    }

    public CommandStore getCommandStore() {
        return commandStore;
    }

    public Report findAndExecute(String textCommand) throws CommandExecutionException {
        
        Report report = new Report();
        report.setOriginalCommand(textCommand);

        LOGGER.debug("Trying to find a match for command {}", textCommand);
        
        for(CommandMatcher cm : commandStore.getAll()) {
            LOGGER.debug("Trying to match {} against {}", textCommand, cm.getOriginalTextCommand());
            Matcher matcher = cm.getCompiledRegex().matcher(textCommand);
            if (matcher.matches()) {
                LOGGER.info("Find a match for \"{}\" against \"{}\"", textCommand, cm.getOriginalTextCommand());
                Method method = cm.getMethod();
                String methodName = method.getDeclaringClass().getName() + "::" + method.getName();
                report.setMethodName(methodName);
                Object[] parametersValue = buildParameters(matcher, cm);
                LOGGER.info("Invoking {} with params {}", methodName, parametersValue);
                try {
                    Object instance = null;
                    if (! Modifier.isStatic(method.getModifiers())) {
                        instance = method.getDeclaringClass().newInstance();
                    }
                    Object returnedObject = method.invoke(instance, parametersValue);
                    report.setReturnedObject(returnedObject);
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    CommandExecutionException commandExecutionException = new CommandExecutionException("Cannot call", e);
                    commandExecutionException.setPartialReport(report);
                    throw commandExecutionException;
                } catch (InstantiationException e) {
                    CommandExecutionException commandExecutionException = new CommandExecutionException("Cannot instantiate class for the method " + method.getName
                            ()+ ". Maybe you should use the static modifier");
                    commandExecutionException.setPartialReport(report);
                    throw commandExecutionException;
                } catch (InvocationTargetException e) {
                    InvocationExecutionException commandExecutionException = new InvocationExecutionException("Cannot execute the method " + method.getName(), e);
                    commandExecutionException.setPartialReport(report);
                    throw commandExecutionException;
                } 
                return report;
            }
        }
        
        NoMatchingMethodFoundException noMatchingMethodFoundException = new NoMatchingMethodFoundException();
        noMatchingMethodFoundException.setPartialReport(report);
        throw noMatchingMethodFoundException;
        
    }
    
    private Object[] buildParameters(Matcher matcher, CommandMatcher cm) throws CommandExecutionException {
        
        Method method = cm.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] paramatersValue = new Object[parameters.length];
        
        int paramPosition = 0;
        for(Parameter param : parameters) {
            String parameterName = param.getName();
            
            LOGGER.debug("Building parameters {}", parameterName);
            
            Integer groupNumberCorrespondingToThisParameter = cm.getGroupNumberForVariable(parameterName) != null ?
                            cm.getGroupNumberForVariable(parameterName)
                            :cm.getGroupNumberForVariable(paramPosition);
            
            if (groupNumberCorrespondingToThisParameter != null) {
                String parameterValue = matcher.group(groupNumberCorrespondingToThisParameter);
                LOGGER.debug("Building parameters {}, value found {}", parameterName, parameterValue);
                paramatersValue[paramPosition] = parameterValue;
                paramPosition++;
            } else {
                throw new InvalidParameterException(parameterName);
            }            
        }
        
        return paramatersValue;
    }
    
}
