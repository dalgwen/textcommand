package net.roulleau.textcommand.defaultcommands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;

import org.junit.BeforeClass;
import org.junit.Test;

import net.roulleau.textcommand.CommandExecutor;
import net.roulleau.textcommand.Report;
import net.roulleau.textcommand.TestUtils;
import net.roulleau.textcommand.annotation.Command;
import net.roulleau.textcommand.annotation.Commands;
import net.roulleau.textcommand.exception.CommandExecutionException;
import net.roulleau.textcommand.exception.InvocationExecutionException;
import net.roulleau.textcommand.exception.NoMatchingMethodFoundException;

@Commands
public class CommandsTest {

    static String result;
    private static CommandExecutor commandExecutor;

    @BeforeClass
    public static void init() {
        commandExecutor = TestUtils.prepareCommandExecutor(CommandsTest.class);
    }
    
    @Command("greet $arg")
    public void firstCommand(String arg) {
        result = "hello to " + arg;
    }

    @Command(priority = 1, value = "greet $arg and $argb")
    public void firstCommand(String arg, String arg2) {
        result = "hello to " + arg + " and hello to " + arg2;
    }

    @Command(priority = 1, value = "(insult|barf at) $arg (and|or) $argb")
    public void commandWithOr(String arg, String arg2) {
        result = "CENSORED to " + arg + " and CENSORED to " + arg2;
    }

    @Command("I want a return")
    public String withReturn() {
        return "here is one";
    }

    @Command("I want a big fail")
    public String withFailure() {
        throw new RuntimeException("big failure");
    }

    @Test
    public void test() throws CommandExecutionException {
        Report report = commandExecutor.findAndExecute("greet jeanpaul");
        assertThat(report.getMethodName()).isEqualTo("net.roulleau.textcommand.defaultcommands.CommandsTest::firstCommand");
        assertThat(result).isEqualTo("hello to jeanpaul");

        report = commandExecutor.findAndExecute("greet jeanpaul and paula");
        assertThat(report.getOriginalCommand()).isEqualTo("greet jeanpaul and paula");
        assertThat(result).isEqualTo("hello to jeanpaul and hello to paula");
    }

    @Test
    public void testWithOr() throws CommandExecutionException {
        commandExecutor.findAndExecute("insult jeanpaul and michel");
        assertThat(result).isEqualTo("CENSORED to jeanpaul and CENSORED to michel");

        commandExecutor.findAndExecute("barf at jéan'paul or robert");
        assertThat(result).isEqualTo("CENSORED to jéan'paul and CENSORED to robert");

        commandExecutor.findAndExecute("greet jeanpaul and paula");
        assertThat(result).isEqualTo("hello to jeanpaul and hello to paula");
    }

    @Test
    public void test_nomatch() throws InvocationTargetException {
        try {
            commandExecutor.findAndExecute("no match");
            fail("Should not happened ! Exception should be thrown");
        } catch (CommandExecutionException ce) {
            assertThat(ce).isInstanceOf(NoMatchingMethodFoundException.class);
            assertThat(ce.getPartialReport().getOriginalCommand()).isEqualTo("no match");
        }
    }

    @Test
    public void testReturn() throws CommandExecutionException {
        Report report = commandExecutor.findAndExecute("I want a return");
        assertThat(report.getReturnedObject()).isEqualTo("here is one");
    }

    @Test
    public void testFailure() throws CommandExecutionException {
        try {
            commandExecutor.findAndExecute("I want a big fail");
            fail("Should not happened ! Exception should be thrown");
        } catch (CommandExecutionException ce) {
            assertThat(ce).isInstanceOf(InvocationExecutionException.class);
            assertThat(ce.getCause()).isInstanceOf(InvocationTargetException.class);
            assertThat(((InvocationTargetException)ce.getCause()).getTargetException()).isInstanceOf(RuntimeException.class);
            assertThat(((InvocationTargetException)ce.getCause()).getTargetException().getMessage()).isEqualTo("big failure");
            assertThat(ce.getPartialReport().getOriginalCommand()).isEqualTo("I want a big fail");
        }
    }

}
