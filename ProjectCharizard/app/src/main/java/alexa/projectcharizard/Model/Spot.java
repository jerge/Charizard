package alexa.projectcharizard.Model;

/**
 * A class for saving the properties of a Spot
 */
public class Spot {
    private String name;
    private Double latitude, longitude;
    private String description;
    private Category category;
    private boolean visibility;

    public Spot(String name, Double latitude, Double longitude, String description, boolean visibility) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.visibility = visibility;
    }

    public Spot(String name, Double latitude, Double longitude, String description, Category category, boolean visibility) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.category = category;
        this.visibility = visibility;
    }

    public Spot (){
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
}
