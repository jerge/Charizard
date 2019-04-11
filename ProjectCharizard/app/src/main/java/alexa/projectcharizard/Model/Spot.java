package alexa.projectcharizard.Model;

import com.google.android.gms.maps.model.LatLng;


public class Spot {
    private String name;
    private LatLng location;
    private String description;
    private Category category;
    private boolean visibility;

    public Spot (String name, LatLng location, String description, Category category, boolean visibility) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.category = category;
        this.visibility = visibility;
    }

    public Spot (String name, LatLng location, String description, boolean visibility) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.category = Category.OTHER;
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
