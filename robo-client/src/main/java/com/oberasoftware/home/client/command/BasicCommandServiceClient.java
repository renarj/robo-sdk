package com.oberasoftware.home.client.command;

import com.oberasoftware.home.client.api.ClientResponse;
import com.oberasoftware.home.client.api.CommandServiceClient;
import com.oberasoftware.robo.api.commands.BasicCommand;
import com.oberasoftware.robo.core.model.BasicCommandImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

/**
 * @author Renze de Vries
 */
@Component
public class BasicCommandServiceClient implements CommandServiceClient {
    private static final Logger LOG = LoggerFactory.getLogger(BasicCommandServiceClient.class);

    @Value("${command_svc_url:}")
    private String commandServiceBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Future<ClientResponse> sendAsyncCommand(BasicCommand basicCommand) {
        return null;
    }

    @Override
    public ClientResponse sendCommand(BasicCommand basicCommand) {
        if(!StringUtils.isEmpty(commandServiceBaseUrl)) {
            LOG.debug("Doing request to endpoint: {} for command: {}", commandServiceBaseUrl, basicCommand);
            ResponseEntity<BasicCommandImpl> response = restTemplate.postForEntity(getCommandUrl(), basicCommand, BasicCommandImpl.class);

            return () -> response.getStatusCode() == HttpStatus.OK ? ClientResponse.RESPONSE_STATUS.OK : ClientResponse.RESPONSE_STATUS.FAILED;
        } else {
            LOG.error("Cannot complete request, no command service url specified");
            return () -> ClientResponse.RESPONSE_STATUS.FAILED;
        }
    }

    private String getCommandUrl() {
        String url = commandServiceBaseUrl;
        if(!commandServiceBaseUrl.endsWith("/")) {
            url += "/";
        }

        return url + "command/";
    }
}
