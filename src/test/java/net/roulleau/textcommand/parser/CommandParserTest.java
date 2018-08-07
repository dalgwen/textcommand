package net.roulleau.textcommand.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedList;

import org.junit.Test;

import net.roulleau.textcommand.parser.CommandParser;
import net.roulleau.textcommand.parser.SimpleGrammar;
import net.roulleau.textcommand.parser.Token;

public class CommandParserTest {

    @Test
    public void testParseCommand() throws Exception {
        CommandParser cp = new CommandParser();
        LinkedList<Token<SimpleGrammar>> tokens = cp.parseCommand("coucou $caca couca");
        
        assertThat(tokens.size()).isEqualTo(3);
        assertThat(tokens.get(0).token).isEqualTo(SimpleGrammar.STRING);
        assertThat(tokens.get(1).token).isEqualTo(SimpleGrammar.VARIABLE);
        assertThat(tokens.get(2).token).isEqualTo(SimpleGrammar.STRING);
    }

}
