package net.roulleau.textcommand;
import net.roulleau.textcommand.CommandExecutor;
import net.roulleau.textcommand.CommandRegister;
import net.roulleau.textcommand.CommandStore;

public class TestUtils {

    public static CommandExecutor prepareCommandExecutor(Class<?>... clazzs) {
        
        CommandStore commandStore = new CommandStore();
        for (Class<?> clazz :clazzs) {
            commandStore.addAll(CommandRegister.registerClass(clazz));
        }
         return new CommandExecutor(commandStore); 
    }
    
}
