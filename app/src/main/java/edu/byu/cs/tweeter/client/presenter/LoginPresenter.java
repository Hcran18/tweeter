package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {

    public interface View {
        void displayInfoMessage(String message);
        void hideInfoMessage();
        void displayErrorMessage(String message);
        void hideErrorMessage();
        void openMainView(User user);
    }

    private  View view;

    public LoginPresenter(View view) {
        this.view = view;
    }

    public void login(String alias, String password) {
        if (validateLogin(alias, password)) {
            view.hideErrorMessage();
            view.displayInfoMessage("Logging In...");

            UserService userService = new UserService();
            userService.login(alias, password, new UserServiceObserver());
        }
    }

    public boolean validateLogin(String alias, String password) {
        if (alias.length() > 0 && alias.charAt(0) != '@') {
            view.displayErrorMessage("Alias must begin with @.");
            return false;
        }
        if (alias.length() < 2) {
            view.displayErrorMessage("Alias must contain 1 or more characters after the @.");
            return false;
        }
        if (password.length() == 0) {
            view.displayErrorMessage("Password cannot be empty.");
            return false;
        }

        return true;
    }

    private class UserServiceObserver implements UserService.LoginObserver {

        @Override
        public void loginSucceeded(AuthToken authToken, User user) {
            view.hideErrorMessage();
            view.hideInfoMessage();
            view.displayInfoMessage("Hello, " + user.getName());
            view.openMainView(user);
        }

        @Override
        public void loginFailed(String message) {
            view.displayErrorMessage(message);
        }
    }
}
