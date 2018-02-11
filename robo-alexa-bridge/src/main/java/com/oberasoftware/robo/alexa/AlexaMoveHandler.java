package com.oberasoftware.robo.alexa;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
public class AlexaMoveHandler implements Speechlet {
    private static final Logger LOG = getLogger(AlexaMoveHandler.class);

    @Override
    public void onSessionStarted(SessionStartedRequest sessionStartedRequest, Session session) throws SpeechletException {
        LOG.info("Session started");
    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest launchRequest, Session session) throws SpeechletException {
        LOG.info("Launch request");

//        return SpeechletResponse.newAskResponse();
        return null;
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
        LOG.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        // Get intent from the request object.
        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        // Note: If the session is started with an intent, no welcome message will be rendered;
        // rather, the intent specific response will be returned.
//        if ("MyColorIsIntent".equals(intentName)) {
//            return setColorInSession(intent, session);
//        } else if ("WhatsMyColorIntent".equals(intentName)) {
//            return getColorFromSession(intent, session);
//        } else {
            throw new SpeechletException("Invalid Intent");
//        }
    }

    @Override
    public void onSessionEnded(SessionEndedRequest sessionEndedRequest, Session session) throws SpeechletException {

    }
}
