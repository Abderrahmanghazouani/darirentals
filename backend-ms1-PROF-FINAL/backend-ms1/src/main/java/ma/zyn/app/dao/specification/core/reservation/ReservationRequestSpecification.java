package  ma.zyn.app.dao.specification.core.reservation;

import ma.zyn.app.dao.criteria.core.reservation.ReservationRequestCriteria;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class ReservationRequestSpecification extends  AbstractSpecification<ReservationRequestCriteria, ReservationRequest>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicateFk("client","id", criteria.getClient()==null?null:criteria.getClient().getId());
        addPredicateFk("client","id", criteria.getClients());
        addPredicateFk("client","email", criteria.getClient()==null?null:criteria.getClient().getEmail());
        addPredicateFk("requestedProperty","id", criteria.getRequestedProperty()==null?null:criteria.getRequestedProperty().getId());
        addPredicateFk("requestedProperty","id", criteria.getRequestedPropertys());
        addPredicateFk("alternativeProperty","id", criteria.getAlternativeProperty()==null?null:criteria.getAlternativeProperty().getId());
        addPredicateFk("alternativeProperty","id", criteria.getAlternativePropertys());
        addPredicateFk("reviewedBy","id", criteria.getReviewedBy()==null?null:criteria.getReviewedBy().getId());
        addPredicateFk("reviewedBy","id", criteria.getReviewedBys());
        addPredicateFk("reviewedBy","email", criteria.getReviewedBy()==null?null:criteria.getReviewedBy().getEmail());
        addPredicateFk("reservationRequestStatus","id", criteria.getReservationRequestStatus()==null?null:criteria.getReservationRequestStatus().getId());
        addPredicateFk("reservationRequestStatus","id", criteria.getReservationRequestStatuss());
        addPredicateFk("reservationRequestStatus","code", criteria.getReservationRequestStatus()==null?null:criteria.getReservationRequestStatus().getCode());
        addPredicateFk("reservation","id", criteria.getReservation()==null?null:criteria.getReservation().getId());
        addPredicateFk("reservation","id", criteria.getReservations());
        addPredicateFk("reservation","reference", criteria.getReservation()==null?null:criteria.getReservation().getReference());
    }

    public ReservationRequestSpecification(ReservationRequestCriteria criteria) {
        super(criteria);
    }

    public ReservationRequestSpecification(ReservationRequestCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
