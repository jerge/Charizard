package alexa.projectcharizard.Model;

public class EasySpot {
    private String spotId;
    private String name;
    private String latitude;
    private String longitude;
    private String description;
    private String category;
    private String visibility;

    public EasySpot(){

    }

    public EasySpot (String spotId, String name, String latitude, String longitude, String description, String category, String visibility) {
        this.spotId = spotId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.category = category;
        this.visibility = visibility;
    }

    public EasySpot (String spotId, String name, String latitude, String longitude, String description, String visibility) {
        this.spotId = spotId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.category = "Standard";
        this.visibility = visibility;
    }

    public  String getSpotId() {return  spotId; }

    public void  setSpotId(String spotId) { this.spotId = spotId; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}

