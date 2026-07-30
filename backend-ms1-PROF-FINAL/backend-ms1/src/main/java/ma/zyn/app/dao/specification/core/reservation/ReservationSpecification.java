package  ma.zyn.app.dao.specification.core.reservation;

import ma.zyn.app.dao.criteria.core.reservation.ReservationCriteria;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class ReservationSpecification extends  AbstractSpecification<ReservationCriteria, Reservation>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("reference", criteria.getReference(),criteria.getReferenceLike());
        addPredicateBigDecimal("amount", criteria.getAmount(), criteria.getAmountMin(), criteria.getAmountMax());
        addPredicateBigDecimal("pricePerNight", criteria.getPricePerNight(), criteria.getPricePerNightMin(), criteria.getPricePerNightMax());
        addPredicateFk("client","id", criteria.getClient()==null?null:criteria.getClient().getId());
        addPredicateFk("client","id", criteria.getClients());
        addPredicateFk("client","email", criteria.getClient()==null?null:criteria.getClient().getEmail());
        addPredicateFk("property","id", criteria.getProperty()==null?null:criteria.getProperty().getId());
        addPredicateFk("property","id", criteria.getPropertys());
        addPredicateFk("reservationPlatform","id", criteria.getReservationPlatform()==null?null:criteria.getReservationPlatform().getId());
        addPredicateFk("reservationPlatform","id", criteria.getReservationPlatforms());
        addPredicateFk("reservationPlatform","code", criteria.getReservationPlatform()==null?null:criteria.getReservationPlatform().getCode());
        addPredicateFk("reservationStatus","id", criteria.getReservationStatus()==null?null:criteria.getReservationStatus().getId());
        addPredicateFk("reservationStatus","id", criteria.getReservationStatuss());
        addPredicateFk("reservationStatus","code", criteria.getReservationStatus()==null?null:criteria.getReservationStatus().getCode());
    }

    public ReservationSpecification(ReservationCriteria criteria) {
        super(criteria);
    }

    public ReservationSpecification(ReservationCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
