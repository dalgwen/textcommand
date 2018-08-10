package net.roulleau.textcommand.input.sms;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.roulleau.textcommand.CommandExecutor;
import net.roulleau.textcommand.exception.InvocationExecutionException;
import net.roulleau.textcommand.exception.NoMatchingMethodFoundException;

public class GammuHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GammuHandler.class);

    private CommandExecutor textCommandExecutor;
    
    private List<String> authorizedSendersNormalized;
    
    public GammuHandler(CommandExecutor textCommandExecutor, List<String> authorizedSenders) {
        super();
        this.textCommandExecutor = textCommandExecutor;
        this.authorizedSendersNormalized = normalizeWithInternationalId(authorizedSenders);
    }
    
    private List<String> normalizeWithInternationalId(List<String> authorizedSenders) {
        return authorizedSenders.stream()
                .map(GammuHandler::normalizeWithInternationalId)
                .collect(Collectors.toList());
    }
    
    private static String normalizeWithInternationalId(String sender) {
        if (sender.startsWith("0")) {
            return sender.replaceFirst("0", "+33");
        } else {
            return sender;
        }
    }

    private Optional<String> getProperty(String property) {
        return Optional.ofNullable(System.getProperty(property));
    }
    
    public void start() {
        LOGGER.info("Starting to handle SMS and MMS (if present in system properties)");
        
        //SMS
        handleMessage("SMS_MESSAGES", "SMS_", "_TEXT", "_NUMBER");
        //MMS
        handleMessage("DECODED_PARTS", "DECODED_", "_TEXT", "_MMS_SENDER" );

    }
    
    private boolean checkNumber(String senderNumber) {
        
        String senderNumberCorrected = normalizeWithInternationalId(senderNumber);
        
        if (authorizedSendersNormalized == null || authorizedSendersNormalized.size() == 0) { //crazy but why not (who am I to judge).
            return true;
        }
        return authorizedSendersNormalized.stream()
                .anyMatch(number -> number.equals(senderNumberCorrected));
    }

    private void handleMessage(String numberString, String prefixString, String suffixTextString, String suffixTextNumber) {
        
        Integer nbSms = getProperty(numberString)
                .map(p -> Integer.parseInt(p))
                .orElse(0);
        
        for (int i = 1; i <= nbSms; i++) {

            try {
                String text = getProperty(prefixString + i + suffixTextString)
                        .orElseThrow(fail("Cannot get mandatory parameters text", null));
                String senderNumber = getProperty(prefixString + i + suffixTextNumber)
                        .orElseThrow(fail("Cannot get mandatory parameters sender number", text));
                
                if (checkNumber(senderNumber)) {
                    textCommandExecutor.findAndExecute(text);
                } else {
                    throw new GammuIncorrectParametersException("Unauthorized sender", text);
                }
            } catch (NoMatchingMethodFoundException | InvocationExecutionException e) {
                String text = e.getPartialReport().getOriginalCommand();
                LOGGER.error("Cannot execute SMS/MMS " + text, e);
            } catch (GammuIncorrectParametersException e) {
                LOGGER.error(e.getMessage() + ". Text was \'" + e.getOriginalTextCommand()+ "\'");
            }
        }
        
    }
    
    private Supplier<GammuIncorrectParametersException> fail(String message, String text) {
        return new Supplier<GammuIncorrectParametersException>() {
            @Override
            public GammuIncorrectParametersException get() {
                return new GammuIncorrectParametersException(message, text);
            }
        };
    }
}
