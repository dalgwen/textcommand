package net.roulleau.textcommand.input.http;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.roulleau.textcommand.CommandRegister;
import net.roulleau.textcommand.configuration.TextCommandParameters;
import net.roulleau.textcommand.defaultcommands.EchoCommands;
import net.roulleau.textcommand.input.http.JettyServer;
import net.roulleau.textcommand.input.http.Result;
import net.roulleau.textcommand.input.http.Result.ResultStatus;

public class HttpServerTest {

    @Test
    public void testHttp() throws Exception {
        CommandRegister.registerClass(EchoCommands.class);
        JettyServer jettyServer = new JettyServer(TextCommandParameters.DEFAULT_HTTP_PORT);
        jettyServer.start();
        int tried = 0;
        while (!jettyServer.isStarted()) {
            Thread.sleep(1000);
            if (tried > 5) {
                break;
            }
            tried++;
        }
        
        String url = "http://localhost:8080/";

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        StringEntity params = new StringEntity("echo machin");
        request.addHeader("content-type", "application/x-www-form-urlencoded");
        request.setEntity(params);
        HttpResponse response = client.execute(request);
        
        ObjectMapper mapper = new ObjectMapper();
        //JSON from file to Object
        Result result = mapper.readValue(response.getEntity().getContent(), Result.class);
        assertThat(result.getMethodName()).isEqualTo("net.roulleau.textcommand.defaultcommands.EchoCommands::echo");
        assertThat(result.getOriginalCommand()).isEqualTo("echo machin");
        assertThat(result.getResultStatus()).isEqualTo(ResultStatus.OK);
        assertThat(result.getReturnedObject()).isEqualTo("machin");
    }
    
}
