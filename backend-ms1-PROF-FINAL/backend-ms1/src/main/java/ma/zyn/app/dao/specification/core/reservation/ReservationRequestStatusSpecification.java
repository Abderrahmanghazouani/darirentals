package  ma.zyn.app.dao.specification.core.reservation;

import ma.zyn.app.dao.criteria.core.reservation.ReservationRequestStatusCriteria;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class ReservationRequestStatusSpecification extends  AbstractSpecification<ReservationRequestStatusCriteria, ReservationRequestStatus>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
        addPredicateBool("isDefault", criteria.getIsDefault());
        addPredicateInt("sortOrder", criteria.getSortOrder(), criteria.getSortOrderMin(), criteria.getSortOrderMax());
    }

    public ReservationRequestStatusSpecification(ReservationRequestStatusCriteria criteria) {
        super(criteria);
    }

    public ReservationRequestStatusSpecification(ReservationRequestStatusCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
