package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> {
    public interface View extends PagedPresenter.PagedView<User> {}

    User user;
    private FollowService followService;

    public FollowersPresenter(View view) {
        super(view);
        followService = new FollowService();
    }

    @Override
    protected void load() {
        followService.loadMoreFollowers(Cache.getInstance().getCurrUserAuthToken(),
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
            view.displayMessage("Failed to get followers because of exception: " + ex.getMessage());
        }

        @Override
        public void addMoreFollowers(List<User> followers, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);

            FollowersPresenter.this.hasMorePages = hasMorePages;
            lastItems = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
            view.addMoreItems(followers);
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

        @Override
        public void addMoreFollowees(List<User> followees, boolean hasMorePages) {
            // For the following class
        }
    }
}
