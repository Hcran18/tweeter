package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends BackgroundTask {
    private static final String LOG_TAG = "GetFollowersCountTask";
    public static final String COUNT_KEY = "count";

    /**
     * Auth token for logged-in user.
     */
    private AuthToken authToken;
    /**
     * The user whose follower count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private User targetUser;

    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(messageHandler);
        this.authToken = authToken;
        this.targetUser = targetUser;
    }

    @Override
    public void run() {
        try {

            sendSuccessMessage(20);

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    private void sendSuccessMessage(int count) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putInt(COUNT_KEY, count);

        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }
}
