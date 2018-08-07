package net.roulleau.textcommand.defaultcommands;

import net.roulleau.textcommand.annotation.Command;
import net.roulleau.textcommand.annotation.Commands;

@Commands
public class EchoCommands {

    @Command(priority=-100000, value="echo $coco")
    public String echo(String what) {
        return what;
    }
    
}
