package alexa.projectcharizard.Model;

import android.graphics.Bitmap;


/**
 * A class for saving the properties of a Spot
 */
public class Spot {
    private String name;
    private Double latitude;
    private Double longitude;
    private String description;
    private Category category;
    private Bitmap image;
    private String creatorId;
    private String id;
    private boolean privacy;


    /**
     * Constructor for when all variables are present
     *
     * @param name
     * @param latitude
     * @param longitude
     * @param description
     * @param category
     * @param image
     */
    public Spot(String name, Double latitude, Double longitude, String description,
                Category category, Bitmap image, String id, String creatorId, boolean privacy) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.category = category;
        this.image = image;
        this.id = id;
        this.creatorId = creatorId;
        this.privacy = privacy;
    }

    /**
     * Constructor for when creator id is missing
     *
     * @param name
     * @param latitude
     * @param longitude
     * @param description
     * @param category
     * @param image
     */
    public Spot(String name, Double latitude, Double longitude, String description,
                Category category, Bitmap image, String id, boolean privacy) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.category = category;
        this.image = image;
        this.id = id;
        this.privacy = privacy;
    }

    /**
     * Constructor for when category is missing
     *
     * @param name
     * @param latitude
     * @param longitude
     * @param description
     * @param image
     */
    public Spot(String name, Double latitude, Double longitude, String description,
                Bitmap image, String creatorId, boolean privacy) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.category = Category.OTHER;
        this.image = image;
        this.creatorId = creatorId;
        this.privacy = privacy;
    }

    public Spot() {
    }


    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getId() {
        return id;
    }

    public boolean getPrivacy() {
        return privacy;
    }
}
