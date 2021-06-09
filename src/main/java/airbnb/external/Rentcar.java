package airbnb.external;

public class Rentcar {

    private Long carId;
    private String carName;
    private String status;
    private String lastAction;
    private Long roomId;

    public Long getCarId() {
        return carId;
    }
    public void setCarId(Long carId) {
        this.carId = carId;
    }
    public String getCarName() {
        return carName;
    }
    public void setCarName(String carName) {
        this.carName = carName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getLastAction() {
        return lastAction;
    }
    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }
    public Long getRoomId() {
        return roomId;
    }
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

}
