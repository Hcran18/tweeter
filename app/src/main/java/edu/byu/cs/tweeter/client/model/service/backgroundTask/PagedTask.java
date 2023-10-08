package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.AuthenticatedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public abstract class PagedTask extends AuthenticatedTask {
    public PagedTask(Handler messageHandler, AuthToken authToken) {
        super(messageHandler, authToken);
    }
}
