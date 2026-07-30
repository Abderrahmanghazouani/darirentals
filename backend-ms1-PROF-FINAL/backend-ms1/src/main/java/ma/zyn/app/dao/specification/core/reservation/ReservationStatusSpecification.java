package  ma.zyn.app.dao.specification.core.reservation;

import ma.zyn.app.dao.criteria.core.reservation.ReservationStatusCriteria;
import ma.zyn.app.bean.core.reservation.ReservationStatus;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class ReservationStatusSpecification extends  AbstractSpecification<ReservationStatusCriteria, ReservationStatus>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
        addPredicateBool("isDefault", criteria.getIsDefault());
        addPredicateInt("sortOrder", criteria.getSortOrder(), criteria.getSortOrderMin(), criteria.getSortOrderMax());
    }

    public ReservationStatusSpecification(ReservationStatusCriteria criteria) {
        super(criteria);
    }

    public ReservationStatusSpecification(ReservationStatusCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
