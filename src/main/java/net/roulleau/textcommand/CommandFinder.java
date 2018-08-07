package net.roulleau.textcommand;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.roulleau.textcommand.annotation.Command;
import net.roulleau.textcommand.annotation.Commands;
import net.roulleau.textcommand.exception.MethodDeclarationException;


public class CommandFinder {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandFinder.class);
    
    public static void registerClass(Class<?> clazz) throws MethodDeclarationException {
        
        LOGGER.info("Registering class {}", clazz.getName());
        
        Commands annotationCommands = clazz.getAnnotation(Commands.class);
        if(annotationCommands == null) {
            throw new MethodDeclarationException("Class " + clazz.getTypeName() + "is not annotated with " + Commands.class.getTypeName());
        }
        
        Method[] methods = clazz.getMethods();
        for (Method method: methods) {
            Command[] commandAnnotations = method.getAnnotationsByType(Command.class);
            for (Command commandAnnotation : commandAnnotations) {
                LOGGER.info("Found appropriate method {}", method.getName());
                createCommandMatcher(method, commandAnnotation, annotationCommands.defaultPriority());
            }
        }
    }
    
    public static void clear() {
        CommandStore.clear();
    }

    private static void createCommandMatcher(Method method, Command commandAnnotation, int defaultPriority) {
        
        if (! commandAnnotation.enabled()) {
            return;
        }
        
        int priority = 0;
        String textCommand = commandAnnotation.value();
        if (commandAnnotation.priority() != -1) {
            priority = commandAnnotation.priority();
        } else if (defaultPriority != -1) {
            priority = defaultPriority;
        }
        
        CommandMatcher commandMatcher = new CommandMatcher(method, textCommand, priority);
        CommandStore.add(commandMatcher);        
    }    

}
