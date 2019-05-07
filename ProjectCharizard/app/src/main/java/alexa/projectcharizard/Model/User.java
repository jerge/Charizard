package alexa.projectcharizard.Model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private Bitmap profileImage;
    private String password;
    private List<Spot> userSpots;

    public User(String name, String password, Bitmap profileImage, List<Spot> userSpots) {
        this.name = name;
        this.password = password; //TODO hash password?
        this.profileImage = profileImage;
        this.userSpots = userSpots;
    }

    public User(String name, String password, List<Spot> userSpots) {
        this.name = name;
        this.password = password;
        this.userSpots = userSpots;
        this.profileImage = null;
    }

    public User(String name, String password, Bitmap profileImage) {
        this.name = name;
        this.password = password;
        this.profileImage = profileImage;
        this.userSpots = new ArrayList<>();
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.profileImage = null;
        this.userSpots = new ArrayList<>();
    }

    public List<Spot> getUserSpots() {
        return userSpots;
    }

    public String getName() {
        return name;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }
}
