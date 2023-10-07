package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter {

    public interface View {

        void displayErrorMessage(String message);

        void hideErrorMessage();

        void displayInfoMessage(String message);

        void hideInfoMessage();

        void openMainView(User user);
    }
    
    private View view;
    
    public RegisterPresenter(View view) {
        this.view = view;
    }

    public void register(String firstName, String lastName, String alias, String password, ImageView imageToUpload, String imageBytesBase64) {
        if (validateRegistration(firstName, lastName, alias, password, imageToUpload)) {
            view.hideErrorMessage();
            view.displayInfoMessage("Registering...");

            UserService userService = new UserService();
            userService.register(firstName, lastName, alias, password, imageBytesBase64, new UserServiceObserver());
        }
    }

    public boolean validateRegistration(String firstName, String lastName, String alias, String password, ImageView imageToUpload) {
        if (firstName.length() == 0) {
            view.displayErrorMessage("First Name cannot be empty.");
            return false;
        }
        if (lastName.length() == 0) {
            view.displayErrorMessage("Last Name cannot be empty.");
            return false;
        }
        if (alias.length() == 0) {
            view.displayErrorMessage("Alias cannot be empty.");
            return false;
        }
        if (alias.charAt(0) != '@') {
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

        if (imageToUpload.getDrawable() == null) {
            view.displayErrorMessage("Profile image must be uploaded.");
            return false;
        }
        
        return true;
    }
    
    private class UserServiceObserver implements UserService.RegisterObserver {

        @Override
        public void registerSucceeded(User user) {
            view.hideErrorMessage();
            view.hideInfoMessage();
            view.displayInfoMessage("Hello " + Cache.getInstance().getCurrUser().getName());
            view.openMainView(user);
        }

        @Override
        public void registerFailed(String message) {
            view.displayErrorMessage(message);
        }
    }
}
