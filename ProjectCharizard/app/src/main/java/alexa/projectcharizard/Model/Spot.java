package alexa.projectcharizard.Model;

/**
 * A class for saving the properties of a Spot
 */
public class Spot {
    private String name;
    private Double latitude;
    private Double longitude;
    private String description;
    private Category category;
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
     * @param privacy
     */
    public Spot(String name, Double latitude, Double longitude, String description, Category category, boolean privacy, String id, String creatorId) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.category = category;
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
     * @param privacy
     */
    public Spot(String name, Double latitude, Double longitude, String description, Category category, boolean privacy, String id) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.category = category;
        this.privacy = privacy;
        this.id = id;
    }

    /**
     * Constructor for when category is missing
     *
     * @param name
     * @param latitude
     * @param longitude
     * @param description
     * @param privacy
     */
    public Spot(String name, Double latitude, Double longitude, String description, boolean privacy, String creatorId) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.category = Category.OTHER;
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
