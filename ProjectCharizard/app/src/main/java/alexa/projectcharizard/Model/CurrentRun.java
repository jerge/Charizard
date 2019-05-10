package alexa.projectcharizard.Model;

import java.util.ArrayList;
import java.util.List;

public class CurrentRun {

    private static CurrentRun instance;
    private static List<Spot> spots = new ArrayList<>();

    // A list containting all spots added during current run
    private static List<Spot> currentRunAddedSpots = new ArrayList<>();

    private CurrentRun(){
        spots = new ArrayList<>();
    }

    public static CurrentRun getInstance(){
        if (instance == null){
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

    public List<Spot> getSpots() {
        return spots;
    }
}
