import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private String roomName;
    private boolean available;
    private List<LocalTime> availabletimes;
    private List<LocalTime> bookedtimes;

    public void setAvailable(boolean available) {
        this.available = available;
    }
    public void addavailabletime(LocalTime time){
        availabletimes.add(time);
    }

    public Room(String roomName , LocalTime time) {
        this.roomName = roomName;
        this.available = true;
        this.availabletimes = new ArrayList<>();
        this.bookedtimes = new ArrayList<>();
    }
    public String getRoomName() {
        return roomName;
    }

    public boolean isAvailable(LocalTime time) {
        return availabletimes.contains(time);

    }

    public void book() {
        this.available = false;
    }

    public void addAvailableTime(LocalTime time) {
        if (!availabletimes.contains(time) && !bookedtimes.contains(time)) {
            availabletimes.add(time);
        }
    }
    public void removeAvailableTime(LocalTime time){
        availabletimes.remove(time);
    }
    @Override
    public String toString() {
        return roomName + " (Available: " + available + ")";
    }

}
