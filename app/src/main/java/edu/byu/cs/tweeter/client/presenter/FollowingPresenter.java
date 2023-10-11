package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {

    public interface View extends PagedPresenter.PagedView<User> {}

    User user;
    private FollowService followService;

    public FollowingPresenter (View view) {
        super(view);
        followService = new FollowService();
    }

    @Override
    protected void load() {
        followService.loadMoreFollowing(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastItems, new FollowServiceObserver());
    }

    public void loadMoreItems(User user) {
        this.user = user;
        loadItems();
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
            lastItems = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
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
}
