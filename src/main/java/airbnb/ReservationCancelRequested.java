package airbnb;

public class ReservationCancelRequested extends AbstractEvent {
    private Long rsvId;
    private Long roomId;
    private String status;
    private Long payId;
    private Long carId; // 렌터카도 예약이 될 경우 들어옴

    public ReservationCancelRequested(){
        super();
    }

    public Long getRsvId() {
        return rsvId;
    }

    public void setRsvId(Long rsvId) {
        this.rsvId = rsvId;
    }
    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }
}
