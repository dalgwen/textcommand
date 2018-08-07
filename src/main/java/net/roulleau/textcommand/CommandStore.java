package net.roulleau.textcommand;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CommandStore {
    
    private static CommandOrderComparator orderer = new CommandOrderComparator();
    private static List<CommandMatcher> commandMatchers = new ArrayList<CommandMatcher>();
    
    public static List<CommandMatcher> get() {
        return new ArrayList<CommandMatcher>(commandMatchers);
    }
    
    public static void add(CommandMatcher cm) {
        commandMatchers.add(cm);
        commandMatchers.sort(orderer);
    }
    
    public static class CommandOrderComparator implements Comparator<CommandMatcher> {

        @Override
        public int compare(CommandMatcher o1, CommandMatcher o2) {
            return o2.getPriority() - o1.getPriority();
        }
        
    }

    public static void clear() {
        commandMatchers.clear();
    }
}
