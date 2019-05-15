package alexa.projectcharizard.Model;

import java.util.ArrayList;
import java.util.List;

public class CurrentRun {

    private static CurrentRun instance;

    // A list of all spots loaded from the database
    private static List<Spot> spots = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private static User activeUser;


    // A list containing all spots added during current run
    private static List<Spot> currentRunAddedSpots = new ArrayList<>();
    private static List<User> currentRunUsers = new ArrayList<>();

    private CurrentRun() {
        spots = new ArrayList<>();
    }

    public static CurrentRun getInstance() {
        if (instance == null) {
            return new CurrentRun();
        }
        return instance;
    }

    /**
     * A list of spots added during current run, in static context so that the list stays the same
     * no matter what object is currently in use
     *
     * @return The list of spots added during the current run
     */
    public static List<Spot> getCurrentRunAddedSpots() {
        return currentRunAddedSpots;
    }

    public static List<User> getCurrentRunUsers() {
        return currentRunUsers;
    }

    public static void setCurrentRunUsers(List<User> currentRunUsers) {
        CurrentRun.currentRunUsers = currentRunUsers;
    }

    public List<Spot> getSpots() {
        return spots;
    }

    public List<User> getUsers() {
        return users;
    }

    /**
     * Gets the user that is logged in to the application
     * @return the logged in user
     */
    public static User getActiveUser() {
        return activeUser;
    }

    /**
     * Sets what user is logged in to the application
     * @param activeUser the user to be set to active
     */
    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }
}
