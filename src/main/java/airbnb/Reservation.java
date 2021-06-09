package airbnb;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;

@Entity
@Table(name="Reservation_table")
public class Reservation {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long rsvId;
    private Long roomId;
    private String status; // VALUE = reqReserve, reserved, reqCancel, cancelled
    private Long payId; // 결제 ID : 결재 완료시 Update, 결제 취소하는 경우 사용
    private Long carId; // 렌터카도 예약이 될 경우 들어옴

    @PrePersist
    public void onPrePersist(){

        System.out.println("######################################");
        System.out.println("######## PrePersist ");
        System.out.println("######################################");

        // 입력값 교정
        if(carId == null) carId = 0L; // 자동차 예약을 하지 않은 경우 Null을 피해 0으로 셋팅

    }
    @PostPersist
    public void onPostPersist(){

        ////////////////////////////////
        // RESERVATION에 INSERT 된 경우 
        ////////////////////////////////

        ////////////////////////////////////
        // 예약 요청(reqReserve) 들어온 경우
        ////////////////////////////////////

        // 해당 ROOM이 Available한 상태인지 체크
        boolean result = ReservationApplication.applicationContext.getBean(airbnb.external.RoomService.class).chkAndReqReserve(this.getRoomId());
        
        System.out.println("######################################");
        System.out.println("######## Check Room Result : " + result);
        System.out.println("######################################");

        if(result == false) {
            System.out.println("###########################");
            System.out.println("## Room not Available ##");
            System.out.println("###########################");
            return; // 방이 예약 가능한 상태가 아닌 경우 여기서 끝낸다
        }

        // 방이 예약 가능한 상태이면서 렌터카 예약이 들어온 경우 렌터카 체크 한번 더 한다.
        if(carId > 0) {

            result = ReservationApplication.applicationContext.getBean(airbnb.external.RentcarService.class).chkStatus(this.getCarId());
                    
            System.out.println("######################################");
            System.out.println("######## Check RentCar Result : " + result);
            System.out.println("######################################");
            
            if(result == false) {

                System.out.println("###########################");
                System.out.println("## RentCar not Available ##");
                System.out.println("###########################");
                return; // 해당 렌터카가 예약 가능한 상태가 아닌 경우 여기서 끝낸다
            }

        } else {

            System.out.println("######################################");
            System.out.println("######## No RentCar Reservation");
            System.out.println("######################################");

        }
        

        //////////////////////////////////////////
        // 이 지점까지 왔다는 것은 예약이 가능한 상태
        //////////////////////////////////////////

        //////////////////////////////
        // PAYMENT 결제 진행 (POST방식)
        //////////////////////////////
        airbnb.external.Payment payment = new airbnb.external.Payment();
        payment.setRsvId(this.getRsvId());
        payment.setRoomId(this.getRoomId());
        payment.setCarId(this.getCarId());
        payment.setStatus("paid");
        ReservationApplication.applicationContext.getBean(airbnb.external.PaymentService.class)
                .approvePayment(payment);

        /////////////////////////////////////
        // 이벤트 발행 --> ReservationCreated
        /////////////////////////////////////
        ReservationCreated reservationCreated = new ReservationCreated();
        BeanUtils.copyProperties(this, reservationCreated);
        reservationCreated.publishAfterCommit();
        
    }

    @PostUpdate
    public void onPostUpdate(){

        ////////////////////////////////
        // RESERVATION에 UPDATE 된 경우
        ////////////////////////////////
        if(this.getStatus().equals("reqCancel")) {

            ///////////////////////
            // 취소 요청 들어온 경우
            ///////////////////////

            // 이벤트 발생 --> reservationCancelRequested
            ReservationCancelRequested reservationCancelRequested = new ReservationCancelRequested();
            BeanUtils.copyProperties(this, reservationCancelRequested);
            reservationCancelRequested.publishAfterCommit();

        }

        

        if(this.getStatus().equals("reserved")) {

            ////////////////////
            // 예약 확정된 경우
            ////////////////////

            // 이벤트 발생 --> ReservationConfirmed
            ReservationConfirmed reservationConfirmed = new ReservationConfirmed();
            BeanUtils.copyProperties(this, reservationConfirmed);
            reservationConfirmed.publishAfterCommit();
        }

        if(this.getStatus().equals("cancelled")) {
            ////////////////////////
            // 예약 취소 확정된 경우
            ////////////////////////

            // 이벤트 발생 --> ReservationCancelled
            ReservationCancelled reservationCancelled = new ReservationCancelled();
            BeanUtils.copyProperties(this, reservationCancelled);
            reservationCancelled.publishAfterCommit();
        }

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
