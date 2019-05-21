package alexa.projectcharizard.Model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for saving users with all it's information
 *
 * All constructors with the List<Spot> parameter present are only for
 * creating users form the database, since new users won't yet have created spots
 *
 * @author Filip Andréasson
 */

public class User {         //TODO hash passwords?
    private String username;
    private Bitmap profileImage;
    private String password;
    private List<Spot> userSpots;
    private String id;
    private String email;

    /**
     * Constructor for creating user from database that has all parameters
     */
    public User(String username, String password, Bitmap profileImage, List<Spot> userSpots, String id) {
        this.username = username;
        this.password = password;
        this.profileImage = profileImage;
        this.userSpots = userSpots;
        this.id = id;
    }

    /**
     * Constructor used when creating a profile during sign up
     */
    public User(String username, String email, String password, String id) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userSpots = new ArrayList<>();
        this.id = id;
    }

    /**
     *  Constructor for creating user from database that is missing profile image
     */
    public User(String username, String password, List<Spot> userSpots, String id) {
        this.username = username;
        this.password = password;
        this.userSpots = userSpots;
        this.id = id;
    }

    /**
     *  Constructor for when creating a user for the first time that has all parameters
     */
    public User(String username, String password, Bitmap profileImage, String id) {
        this.username = username;
  //      this.fullName = fullName;
        this.password = password;
        this.profileImage = profileImage;
        this.userSpots = new ArrayList<>();
        this.id = id;
    }

    /**
     * Constructor for when creating a user for the first time without full name
     */
    public User(String username, Bitmap profileImage, String password, String id) {
        this.username = username;
        this.profileImage = profileImage;
        this.password = password;
        this.userSpots = new ArrayList<>();
        this.id = id;
    }

    /**
     * Constructor for when creating a user for the first time without profile image and full name
     */
    public User(String username, String password, String id) {
        this.username = username;
        this.password = password;
        this.userSpots = new ArrayList<>();
        this.id = id;
    }

    public User(){}


    /**
     * ****** GETTERS *******
     */

    public String getUsername() {
        return username;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public List<Spot> getUserSpots() {
        return userSpots;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    /**
     * ****** SETTERS *******
     */

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserSpots(List<Spot> userSpots) {
        this.userSpots = userSpots;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
