package  ma.zyn.app.dao.specification.core.reservation;

import ma.zyn.app.dao.criteria.core.reservation.ReservationCriteria;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.zynerator.specification.AbstractSpecification;

import java.time.LocalDate;


public class ReservationSpecification extends  AbstractSpecification<ReservationCriteria, Reservation>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("reference", criteria.getReference(),criteria.getReferenceLike());
        addPredicateBigDecimal("amount", criteria.getAmount(), criteria.getAmountMin(), criteria.getAmountMax());
        addPredicateBigDecimal("pricePerNight", criteria.getPricePerNight(), criteria.getPricePerNightMin(), criteria.getPricePerNightMax());
        addPredicateLocalDate("checkInDate", criteria.getCheckInDate(), criteria.getCheckInDateFrom(), criteria.getCheckInDateTo());
        addPredicateLocalDate("checkOutDate", criteria.getCheckOutDate(), criteria.getCheckOutDateFrom(), criteria.getCheckOutDateTo());
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

    // Le framework (SpecificationHelper) ne fournit d'overload addPredicate que pour LocalDateTime,
    // pas pour LocalDate : on l'implémente ici localement plutôt que de toucher au framework partagé.
    private void addPredicateLocalDate(String name, LocalDate value, LocalDate valueFrom, LocalDate valueTo) {
        if (value != null) {
            predicates.add(builder.equal(root.<LocalDate>get(name), value));
        }
        if (valueFrom != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(name), valueFrom));
        }
        if (valueTo != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get(name), valueTo));
        }
    }

}
