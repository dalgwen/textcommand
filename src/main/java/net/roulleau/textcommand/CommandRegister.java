package net.roulleau.textcommand;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.roulleau.textcommand.annotation.Command;
import net.roulleau.textcommand.annotation.Commands;
import net.roulleau.textcommand.exception.MethodDeclarationException;


public class CommandRegister {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandRegister.class);
    
    public static List<CommandMatcher> registerClass(Class<?> clazz) throws MethodDeclarationException {
        
        LOGGER.info("Registering class {}", clazz.getName());
        
        List<CommandMatcher> commandList = new ArrayList<CommandMatcher>();
        
        Commands annotationCommands = clazz.getAnnotation(Commands.class);
        if(annotationCommands == null) {
            throw new MethodDeclarationException("Class " + clazz.getTypeName() + "is not annotated with " + Commands.class.getTypeName());
        }
        
        Method[] methods = clazz.getMethods();
        for (Method method: methods) {
            Command[] commandAnnotations = method.getAnnotationsByType(Command.class);
            for (Command commandAnnotation : commandAnnotations) {
                LOGGER.info("Found appropriate method {}", method.getName());
                Optional<CommandMatcher> commandMatcher = createCommandMatcher(method, commandAnnotation, annotationCommands.defaultPriority());
                commandMatcher.ifPresent(cm ->commandList.add(cm) );
            }
        }
        
        return commandList;
    }    

    private static Optional<CommandMatcher> createCommandMatcher(Method method, Command commandAnnotation, int defaultPriority) {
        
        if (! commandAnnotation.enabled()) {
            return Optional.empty();
        }
        
        int priority = 0;
        String textCommand = commandAnnotation.value();
        if (commandAnnotation.priority() != -1) {
            priority = commandAnnotation.priority();
        } else if (defaultPriority != -1) {
            priority = defaultPriority;
        }
        
        return Optional.of(new CommandMatcher(method, textCommand, priority));
    }    

}
