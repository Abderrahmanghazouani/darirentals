package  ma.zyn.app.dao.specification.core.task;

import ma.zyn.app.dao.criteria.core.task.TaskPriorityCriteria;
import ma.zyn.app.bean.core.task.TaskPriority;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class TaskPrioritySpecification extends  AbstractSpecification<TaskPriorityCriteria, TaskPriority>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
        addPredicateBool("isDefault", criteria.getIsDefault());
        addPredicateInt("sortOrder", criteria.getSortOrder(), criteria.getSortOrderMin(), criteria.getSortOrderMax());
    }

    public TaskPrioritySpecification(TaskPriorityCriteria criteria) {
        super(criteria);
    }

    public TaskPrioritySpecification(TaskPriorityCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
