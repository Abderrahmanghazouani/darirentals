package  ma.zyn.app.dao.specification.core.task;

import ma.zyn.app.dao.criteria.core.task.TaskTypeCriteria;
import ma.zyn.app.bean.core.task.TaskType;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class TaskTypeSpecification extends  AbstractSpecification<TaskTypeCriteria, TaskType>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
        addPredicateBool("isDefault", criteria.getIsDefault());
        addPredicateInt("sortOrder", criteria.getSortOrder(), criteria.getSortOrderMin(), criteria.getSortOrderMax());
    }

    public TaskTypeSpecification(TaskTypeCriteria criteria) {
        super(criteria);
    }

    public TaskTypeSpecification(TaskTypeCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
