package alexa.projectcharizard.Model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * A class for saving the properties of a Spot
 */
public class Spot {
    private String spotId;
    private String name;
    private Double latitude;
    private Double longitude;
    private String description;
    private String id;
    private Category category;
    private Bitmap image;
    private boolean visibility;

    /**
     * Constructor for when all variables are present
     * @param name
     * @param latitude
     * @param longitude
     * @param description
     * @param category
     * @param image
     * @param visibility
     * @param id
     */
    public Spot(String name, Double latitude, Double longitude, String description, Category category, Bitmap image, boolean visibility, String id) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.visibility = visibility;
        this.image = image;
        this.id = id;
    }

    /**
     * Constructor for when category is not present
     * @param name
     * @param latitude
     * @param longitude
     * @param description
     * @param image
     * @param visibility
     * @param id
     */
    public Spot(String name, Double latitude, Double longitude, String description, Bitmap image, boolean visibility, String id) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.category = category;
        this.visibility = visibility;
        this.image = image;
        this.id = id;
    }

    public Spot() {
    }

    /**
     * Constructor for when neither image nor category is present
     * @param name
     * @param latitude
     * @param longitude
     * @param description
     * @param visibility
     * @param id
     */
    public Spot(String name, Double latitude, Double longitude, String description, boolean visibility, String id) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.visibility = visibility;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }
}
