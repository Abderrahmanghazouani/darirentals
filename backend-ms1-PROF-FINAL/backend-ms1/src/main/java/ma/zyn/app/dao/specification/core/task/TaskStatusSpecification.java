package  ma.zyn.app.dao.specification.core.task;

import ma.zyn.app.dao.criteria.core.task.TaskStatusCriteria;
import ma.zyn.app.bean.core.task.TaskStatus;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class TaskStatusSpecification extends  AbstractSpecification<TaskStatusCriteria, TaskStatus>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
        addPredicateBool("isDefault", criteria.getIsDefault());
        addPredicateInt("sortOrder", criteria.getSortOrder(), criteria.getSortOrderMin(), criteria.getSortOrderMax());
    }

    public TaskStatusSpecification(TaskStatusCriteria criteria) {
        super(criteria);
    }

    public TaskStatusSpecification(TaskStatusCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
