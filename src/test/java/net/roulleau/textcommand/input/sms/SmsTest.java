package net.roulleau.textcommand.input.sms;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import net.roulleau.textcommand.CommandExecutor;
import net.roulleau.textcommand.TestUtils;
import net.roulleau.textcommand.annotation.Command;
import net.roulleau.textcommand.annotation.Commands;

@Commands
public class SmsTest {

    static String who;
    
    @Command("hello $who")
    public void hello(String who) {
        SmsTest.who = who;
    }
    
    @Test
    public void testSMS() throws Exception {
        
        CommandExecutor commandExecutor = TestUtils.prepareCommandExecutor(SmsTest.class);
        
        System.setProperty("SMS_MESSAGES", "1");
        System.setProperty("SMS_1_NUMBER", "0676967258");
        System.setProperty("SMS_1_TEXT", "hello coco");
        
        new GammuHandler(commandExecutor, new ArrayList<String>()).start();;       
        
        assertThat(who).isEqualTo("coco");
    }
    
    @Test
    public void testSMSWithAuthorizedSender() throws Exception {
        
        CommandExecutor commandExecutor = TestUtils.prepareCommandExecutor(SmsTest.class);
        
        System.setProperty("SMS_MESSAGES", "1");
        System.setProperty("SMS_1_NUMBER", "0612345678");
        System.setProperty("SMS_1_TEXT", "hello john");
        
        new GammuHandler(commandExecutor, Arrays.asList("+33612345678")).start();;       
        
        assertThat(who).isEqualTo("john");
    }
    
    @Test
    public void testSMSWithUnauthorizedSender() throws Exception {
        
        CommandExecutor commandExecutor = TestUtils.prepareCommandExecutor(SmsTest.class);
        
        System.setProperty("SMS_MESSAGES", "1");
        System.setProperty("SMS_1_NUMBER", "0612345678");
        System.setProperty("SMS_1_TEXT", "hello john");
        
        SmsTest.who = "no message received";
        new GammuHandler(commandExecutor,  Arrays.asList("0612345679")).start();;       
        
        assertThat(who).isEqualTo("no message received");
    }
    
    @Test
    public void testMMS() throws Exception {
        
        CommandExecutor commandExecutor = TestUtils.prepareCommandExecutor(SmsTest.class);
        
        System.setProperty("DECODED_PARTS", "1");
        System.setProperty("DECODED_1_MMS_SENDER", "0676967258");
        System.setProperty("DECODED_1_TEXT", "hello coco");
        
        new GammuHandler(commandExecutor, new ArrayList<String>()).start();;       
        
        assertThat(who).isEqualTo("coco");
    }

}
