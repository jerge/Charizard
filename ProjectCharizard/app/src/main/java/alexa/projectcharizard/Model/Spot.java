package alexa.projectcharizard.Model;

import android.media.Image;

import com.google.android.gms.maps.model.LatLng;

/**
 * A class for saving the properties of a Spot
 */
public class Spot {
    private String name;
    private LatLng location;
    private String description;
    private Category category;
    private Image image;
    private boolean visibility;
    private final String id;

    public Spot (String name, LatLng location, String description, Category category, Image image, boolean visibility, String id) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.category = category;
        this.visibility = visibility;
        this.image = image;
        this.id = id;
    }

    public Spot (String name, LatLng location, String description, Image image, boolean visibility, String id) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.category = Category.OTHER;
        this.visibility = visibility;
        this.image = image;
        this.id = id;
    }

    public Spot (String name, LatLng location, String description, Category category, boolean visibility, String id) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.category = category;
        this.visibility = visibility;
        this.id = id;
    }

    public Spot (String name, LatLng location, String description, boolean visibility, String id) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.category = Category.OTHER;
        this.visibility = visibility;
        this.id = id;
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }


}
