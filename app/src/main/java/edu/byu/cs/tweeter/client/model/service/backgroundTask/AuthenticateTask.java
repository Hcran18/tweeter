package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class AuthenticateTask extends BackgroundTask {
    private static final String LOG_TAG = "AuthenticateTask";
    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";
    /**
     * The user's password.
     */
    protected String password;
    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    protected String username;

    private User user;
    private AuthToken authToken;

    public AuthenticateTask(Handler messageHandler, String password, String username) {
        super(messageHandler);
        this.password = password;
        this.username = username;
    }

    @Override
    protected void doTask() {
        Pair<User, AuthToken> result = getData();

        user = result.getFirst();
        authToken = result.getSecond();
    }

    private Pair<User, AuthToken> getData() {
        User user = getFakeData().getFirstUser();
        AuthToken authToken = getFakeData().getAuthToken();
        return new Pair<>(user, authToken);
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, user);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }
}
