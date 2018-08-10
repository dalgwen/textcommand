package net.roulleau.textcommand.input.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.roulleau.textcommand.CommandExecutor;
import net.roulleau.textcommand.Report;
import net.roulleau.textcommand.exception.CommandExecutionException;
import net.roulleau.textcommand.exception.InvalidParameterException;
import net.roulleau.textcommand.exception.InvocationExecutionException;
import net.roulleau.textcommand.exception.NoMatchingMethodFoundException;
import net.roulleau.textcommand.exception.TextCommandTechnicalException;
import net.roulleau.textcommand.input.http.Result.ResultStatus;

public class Handler extends AbstractHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(Handler.class);

    private static ObjectMapper mapper = new ObjectMapper();
    
    private static TemplateEngine templateEngine;
    
    private CommandExecutor commandExecutor;
    
    public Handler(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
        
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(new ClassLoaderTemplateResolver());
    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        LOGGER.debug("Receiving HTTP call");

        PrintWriter out = response.getWriter();
        Result HTTPexecutionResultDTO = new Result();

        boolean hasCommand = false;

        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                hasCommand = true;
                LOGGER.info("Trying to execute http call \"{}\"...", line);
                Report execute = commandExecutor.findAndExecute(line);
                HTTPexecutionResultDTO = new Result(execute);
                LOGGER.debug("Execution by http invocation success");
            }

        } catch (CommandExecutionException | TextCommandTechnicalException e) {

            LOGGER.error("Cannot execute the command {}", e.getPartialReport().getOriginalCommand());
            HTTPexecutionResultDTO = new Result();
            if ((e.getPartialReport()) != null) {
                HTTPexecutionResultDTO.setMethodName(e.getPartialReport().getMethodName());
                HTTPexecutionResultDTO.setOriginalCommand(e.getPartialReport().getOriginalCommand());
            }
            if (e instanceof InvocationExecutionException) {
                HTTPexecutionResultDTO.setDetailedMessage(((InvocationExecutionException)e).getDetailedMessage());
                HTTPexecutionResultDTO.setResultStatus(ResultStatus.EXECUTION_FAILED);
            } else if (e instanceof NoMatchingMethodFoundException) {
                HTTPexecutionResultDTO.setResultStatus(ResultStatus.NO_MATCH);
            } else if (e instanceof InvalidParameterException) {
                HTTPexecutionResultDTO.setResultStatus(ResultStatus.EXECUTION_FAILED);
                HTTPexecutionResultDTO.setDetailedMessage("Parameter error");
            } else {
                HTTPexecutionResultDTO.setResultStatus(ResultStatus.EXECUTION_FAILED);
            }
        }

        if (hasCommand) {
            responseWithCommandReports(response, out, HTTPexecutionResultDTO);
        }
        else {
            responseWithHtmlPage(response.getWriter());
        }
        out.close();
        baseRequest.setHandled(true);
    }

    private void responseWithHtmlPage(Writer out) {
        templateEngine.process("homepage.tl", new Context(), out);
        
    }

    private void responseWithCommandReports(HttpServletResponse response, PrintWriter out, Result HTTPexecutionResultDTO)
            throws JsonProcessingException {
        response.setContentType("text/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String jsonResponse = mapper.writeValueAsString(HTTPexecutionResultDTO);
        out.println(jsonResponse);
    }

}
