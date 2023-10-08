package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedTask {
    private static final String LOG_TAG = "GetFollowingTask";

    public static final String FOLLOWEES_KEY = "followees";
    public static final String MORE_PAGES_KEY = "more-pages";

    /**
     * The user whose following is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private User targetUser;
    /**
     * Maximum number of followed users to return (i.e., page size).
     */
    private int limit;
    /**
     * The last person being followed returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    private User lastFollowee;
    private List<User> followees;
    private boolean hasMorePages;

    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastFollowee = lastFollowee;
    }

    @Override
    protected void doTask() {
        Pair<List<User>, Boolean> pageOfUsers = getFollowees();

        followees = pageOfUsers.getFirst();
        hasMorePages = pageOfUsers.getSecond();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(FOLLOWEES_KEY, (Serializable) followees);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);
    }

    private Pair<List<User>, Boolean> getFollowees() {
        return getFakeData().getPageOfUsers((User) lastFollowee, limit, targetUser);
    }
}
