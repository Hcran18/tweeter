package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {

    public interface View extends PagedPresenter.PagedView<Status> {}

    User user;
    private StatusService statusService;

    public StoryPresenter (View view) {
        super(view);
        statusService = new StatusService();
    }

    @Override
    protected void load() {
        statusService.loadMoreStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItems, new StatusServiceObserver());
    }

    public void loadMoreStory(User user) {
        this.user = user;
        loadItems();
    }

    private class StatusServiceObserver implements StatusService.StatusObserver {

        @Override
        public void addMoreStatuses(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            StoryPresenter.this.hasMorePages = hasMorePages;
            lastItems = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            view.addMoreItems(statuses);
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
            view.displayMessage("Failed to get story because of exception: " + ex.getMessage());
        }

        @Override
        public void postSuccess() {

        }
    }
}
