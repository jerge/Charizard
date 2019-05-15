package alexa.projectcharizard.Model;

import android.graphics.Bitmap;

import java.util.List;

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
    private boolean visibility;
    private String creatorId;
    private String id;
    private List<Comment> commentList;

    /**
     * Constructor for when all variables are present
     *
     * @param name
     * @param latitude
     * @param longitude
     * @param description
     * @param category
     * @param image
     * @param visibility
     */
    public Spot(String name, Double latitude, Double longitude, String description, Category category, Bitmap image, boolean visibility, String id, String creatorId) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.visibility = visibility;
        this.category = category;
        this.image = image;
        this.id = id;
        this.creatorId = creatorId;
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
     * @param visibility
     */
    public Spot(String name, Double latitude, Double longitude, String description, Category category, Bitmap image, boolean visibility, String id) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.category = category;
        this.image = image;
        this.visibility = visibility;
        this.id = id;
    }

    /**
     * Constructor for when category is missing
     *
     * @param name
     * @param latitude
     * @param longitude
     * @param description
     * @param image
     * @param visibility
     */
    public Spot(String name, Double latitude, Double longitude, String description, Bitmap image, boolean visibility, String creatorId) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.category = Category.OTHER;
        this.visibility = visibility;
        this.image = image;
        this.creatorId = creatorId;
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

    public boolean isVisibility() {
        return visibility;
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

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
}
