package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class PagedTask<T> extends AuthenticatedTask {
    public static final String ITEMS_KEY = "items";
    public static final String MORE_PAGES_KEY = "more-pages";

    /**
     * The user whose paged data is being retrieved
     * (This can be any user, not just the currently logged-in user.)
     */
    private User targetUser;

    /**
     * Maximum number of followers to return (i.e., page size).
     */
    private int limit;

    /**
     * The last follower returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */

    private T lastItem;
    private List<T> items;
    private boolean hasMorePages;

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public int getLimit() {
        return limit;
    }

    public T getLastItem() {
        return lastItem;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public PagedTask(Handler messageHandler, AuthToken authToken, User targetUser, int limit, T lastItem) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastItem;
    }

    @Override
    protected void doTask() {
        Pair<List<T>, Boolean> pageOfStatus = getItems();

        setItems(pageOfStatus.getFirst());
        setHasMorePages(pageOfStatus.getSecond());
    }

    protected abstract Pair<List<T>, Boolean> getItems();

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(ITEMS_KEY, (Serializable) getItems());
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages());
    }
}
