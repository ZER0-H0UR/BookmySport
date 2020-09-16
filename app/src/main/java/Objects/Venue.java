package Objects;

public class Venue {
    public int background;
    public String venueName;
    public String venueDescription;
    public String location;
    public int sportTypeLogo;
    boolean hasBathroom=false,hasCarPark=false,hasFirstAid=false,hasLocker=false,hasResturant=false,hasWifi=false;



    public Venue() {
    }

    public Venue(int background, String venueName, String venueDescription, String location, int sportTypeLogo, boolean hasBathroom, boolean hasCarPark, boolean hasFirstAid, boolean hasLocker, boolean hasResturant, boolean hasWifi) {
        this.background = background;
        this.venueName = venueName;
        this.venueDescription = venueDescription;
        this.location = location;
        this.sportTypeLogo = sportTypeLogo;
        this.hasBathroom = hasBathroom;
        this.hasCarPark = hasCarPark;
        this.hasFirstAid = hasFirstAid;
        this.hasLocker = hasLocker;
        this.hasResturant = hasResturant;
        this.hasWifi = hasWifi;

    }
    public Venue(int background, String venueName, String venueDescription, String location, int sportTypeLogo) {
        this.background = background;
        this.venueName = venueName;
        this.venueDescription = venueDescription;
        this.location = location;
        this.sportTypeLogo = sportTypeLogo;
    }
    public Venue(int background, String venueName, String venueDescription, String location) {
        this.background = background;
        this.venueName = venueName;
        this.venueDescription = venueDescription;
        this.location = location;
    }

    public Venue(int background, String s, String venueName, String venueDescription, int soccer_icon, boolean b1, boolean b) {
        this.background = background;
        this.venueName = venueName;
        this.venueDescription = venueDescription;
    }


    public boolean isHasBathroom() {
        return hasBathroom;
    }

    public void setHasBathroom(boolean hasBathroom) {
        this.hasBathroom = hasBathroom;
    }

    public boolean isHasCarPark() {
        return hasCarPark;
    }

    public void setHasCarPark(boolean hasCarPark) {
        this.hasCarPark = hasCarPark;
    }

    public boolean isHasFirstAid() {
        return hasFirstAid;
    }

    public void setHasFirstAid(boolean hasFirstAid) {
        this.hasFirstAid = hasFirstAid;
    }

    public boolean isHasLocker() {
        return hasLocker;
    }

    public void setHasLocker(boolean hasLocker) {
        this.hasLocker = hasLocker;
    }

    public boolean isHasResturant() {
        return hasResturant;
    }

    public void setHasResturant(boolean hasResturant) {
        this.hasResturant = hasResturant;
    }

    public boolean isHasWifi() {
        return hasWifi;
    }

    public void setHasWifi(boolean hasWifi) {
        this.hasWifi = hasWifi;
    }


    public int getSportTypeLogo() {
        return sportTypeLogo;
    }

    public void setSportTypeLogo(int sportTypeLogo) {
        this.sportTypeLogo = sportTypeLogo;
    }

    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueDescription() {
        return venueDescription;
    }

    public void setVenueDescription(String venueDescription) {
        this.venueDescription = venueDescription;
    }
}
