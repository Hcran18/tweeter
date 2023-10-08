package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthenticatedTask {
    private static final String LOG_TAG = "LogoutTask";

    public LogoutTask(AuthToken authToken, Handler messageHandler) {
        super(messageHandler, authToken);
        this.authToken = authToken;
    }

    @Override
    protected void doTask() {
        // TODO: Currently using dummy data will implement later - milestone 4
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        //TODO Using dummy data
    }
}
