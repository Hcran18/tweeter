package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {

    public interface View extends PagedPresenter.PagedView<Status> {}

    User user;
    private StatusService statusService;

    public FeedPresenter (View view){
        super(view);
        statusService = new StatusService();
    }

    public void loadMoreFeed(User user) {
        this.user = user;
        loadItems();
    }

    @Override
    protected void load() {
        statusService.loadMoreFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItems, new FeedPresenter.StatusServiceObserver());
    }

    private class StatusServiceObserver implements StatusService.StatusObserver {

        @Override
        public void addMoreStatuses(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            FeedPresenter.this.hasMorePages = hasMorePages;
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
            view.displayMessage("Failed to get feed because of exception: " + ex.getMessage());
        }

        @Override
        public void postSuccess() {

        }
    }
}
