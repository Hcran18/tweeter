package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter {
    private static final int PAGE_SIZE = 10;
    private Status lastStatus;
    private boolean hasMorePages;
    private boolean isLoading;

    public interface View {

        void setLoadingFooter(boolean b);

        void addMoreFollowers(List<Status> statuses);

        void displayMessage(String message);

        void startingNewActivity(User user);
    }

    private View view;

    private StatusService statusService;

    private UserService userService;

    public FeedPresenter (View view){
        this.view = view;
        statusService = new StatusService();
        userService = new UserService();
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void loadMoreFeed(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);

            statusService.loadMoreFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new StatusServiceObserver());
        }
    }

    public void getUser(String givenString) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), givenString, new UserServiceObserver());
    }

    private class StatusServiceObserver implements StatusService.StatusObserver {

        @Override
        public void addMoreStatuses(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            FeedPresenter.this.hasMorePages = hasMorePages;
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            view.addMoreFollowers(statuses);
        }

        @Override
        public void displayError(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get feed because of exception: " + ex.getMessage());
        }

        @Override
        public void postSuccess() {

        }
    }

    private class UserServiceObserver implements UserService.UserObserver {

        @Override
        public void displayGettingProfile(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayError(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to get user's profile because of exception: " + ex.getMessage());
        }

        @Override
        public void startingNewActivity(User user) {
            view.startingNewActivity(user);
        }

        @Override
        public void logOutCancel() {

        }
    }
}