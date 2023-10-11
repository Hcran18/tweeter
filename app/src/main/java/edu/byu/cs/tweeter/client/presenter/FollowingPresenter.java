package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends Presenter {
    private static final int PAGE_SIZE = 10;
    private User lastItem;
    private boolean hasMorePages;
    private boolean isLoading;

    public interface View extends Presenter.MainView {
        void setLoadingFooter(boolean setOrRemove);
        void addMoreItems(List<User> items);
        void startingNewActivity(User user);
    }

    private View view;

    private FollowService followService;

    private UserService userService;

    public FollowingPresenter (View view) {
        this.view = view;
        followService = new FollowService();
        userService = new UserService();
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);

            followService.loadMoreFollowing(Cache.getInstance().getCurrUserAuthToken(),
                    user, PAGE_SIZE, lastItem, new FollowServiceObserver());
        }
    }

    public void getUser(String aliasString) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), aliasString, new UserServiceObserver());
    }

    private class FollowServiceObserver implements FollowService.FollowObserver {
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
            view.displayMessage("Failed to get following because of exception: " + ex.getMessage());
        }

        @Override
        public void addMoreFollowees(List<User> followees, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            FollowingPresenter.this.hasMorePages = hasMorePages;
            lastItem = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            view.addMoreItems(followees);
        }

        @Override
        public void addMoreFollowers(List<User> followers, boolean hasMorePages) {
            // For the Followers Class
        }

        @Override
        public void setIsFollower(boolean isFollower) {

        }

        @Override
        public void followEnable(boolean enable) {

        }

        @Override
        public void displayMessage(String message) {

        }

        @Override
        public void updateFollow(boolean update) {

        }

        @Override
        public void followerCount(int count) {

        }

        @Override
        public void followingCount(int count) {

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
