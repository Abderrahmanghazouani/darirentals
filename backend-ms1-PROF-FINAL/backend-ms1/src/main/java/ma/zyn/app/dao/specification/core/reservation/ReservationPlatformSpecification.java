package  ma.zyn.app.dao.specification.core.reservation;

import ma.zyn.app.dao.criteria.core.reservation.ReservationPlatformCriteria;
import ma.zyn.app.bean.core.reservation.ReservationPlatform;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class ReservationPlatformSpecification extends  AbstractSpecification<ReservationPlatformCriteria, ReservationPlatform>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
        addPredicateBool("isDefault", criteria.getIsDefault());
        addPredicateInt("sortOrder", criteria.getSortOrder(), criteria.getSortOrderMin(), criteria.getSortOrderMax());
    }

    public ReservationPlatformSpecification(ReservationPlatformCriteria criteria) {
        super(criteria);
    }

    public ReservationPlatformSpecification(ReservationPlatformCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
