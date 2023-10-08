package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

public abstract class AuthenticatedTask extends BackgroundTask {
    public AuthenticatedTask(Handler messageHandler) {
        super(messageHandler);
    }
}
