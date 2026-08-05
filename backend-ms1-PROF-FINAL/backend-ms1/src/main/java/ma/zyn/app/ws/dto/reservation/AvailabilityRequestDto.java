package ma.zyn.app.ws.dto.reservation;

import java.time.LocalDate;

/** Requête utilisée par /api/admin/reservation/check-availability. */
public class AvailabilityRequestDto {

    private Long propertyId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long excludeReservationId;

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Long getExcludeReservationId() {
        return excludeReservationId;
    }

    public void setExcludeReservationId(Long excludeReservationId) {
        this.excludeReservationId = excludeReservationId;
    }
}
