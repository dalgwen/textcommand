package net.roulleau.textcommand;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CommandStore {
    
    private static CommandOrderComparator orderer = new CommandOrderComparator();
    private List<CommandMatcher> commandMatchers = new ArrayList<CommandMatcher>();
    
    public List<CommandMatcher> getAll() {
        return new ArrayList<CommandMatcher>(commandMatchers);
    }
    
    public void add(CommandMatcher cm) {
        commandMatchers.add(cm);
        commandMatchers.sort(orderer);
    }    

    public void addAll(List<CommandMatcher> commandsList) {
        commandMatchers.addAll(commandsList);
        commandMatchers.sort(orderer);
    }
    
    public static class CommandOrderComparator implements Comparator<CommandMatcher> {
        @Override
        public int compare(CommandMatcher o1, CommandMatcher o2) {
            return o2.getPriority() - o1.getPriority();
        }
    }

    public void clear() {
        commandMatchers.clear();
    }

}
