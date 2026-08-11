package  ma.zyn.app.dao.specification.core.task;

import ma.zyn.app.dao.criteria.core.task.TaskCriteria;
import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.zynerator.specification.AbstractSpecification;

import java.time.LocalDate;


public class TaskSpecification extends  AbstractSpecification<TaskCriteria, Task>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("title", criteria.getTitle(),criteria.getTitleLike());
        addPredicateLocalDate("dueDate", criteria.getDueDate(), criteria.getDueDateFrom(), criteria.getDueDateTo());
        addPredicateFk("property","id", criteria.getProperty()==null?null:criteria.getProperty().getId());
        addPredicateFk("property","id", criteria.getPropertys());
        addPredicateFk("reservation","id", criteria.getReservation()==null?null:criteria.getReservation().getId());
        addPredicateFk("reservation","id", criteria.getReservations());
        addPredicateFk("reservation","reference", criteria.getReservation()==null?null:criteria.getReservation().getReference());
        addPredicateFk("serviceProvider","id", criteria.getServiceProvider()==null?null:criteria.getServiceProvider().getId());
        addPredicateFk("serviceProvider","id", criteria.getServiceProviders());
        addPredicateFk("assignedTo","id", criteria.getAssignedTo()==null?null:criteria.getAssignedTo().getId());
        addPredicateFk("assignedTo","id", criteria.getAssignedTos());
        addPredicateFk("assignedTo","email", criteria.getAssignedTo()==null?null:criteria.getAssignedTo().getEmail());
        addPredicateFk("taskType","id", criteria.getTaskType()==null?null:criteria.getTaskType().getId());
        addPredicateFk("taskType","id", criteria.getTaskTypes());
        addPredicateFk("taskType","code", criteria.getTaskType()==null?null:criteria.getTaskType().getCode());
        addPredicateFk("taskPriority","id", criteria.getTaskPriority()==null?null:criteria.getTaskPriority().getId());
        addPredicateFk("taskPriority","id", criteria.getTaskPrioritys());
        addPredicateFk("taskPriority","code", criteria.getTaskPriority()==null?null:criteria.getTaskPriority().getCode());
        addPredicateFk("taskStatus","id", criteria.getTaskStatus()==null?null:criteria.getTaskStatus().getId());
        addPredicateFk("taskStatus","id", criteria.getTaskStatuss());
        addPredicateFk("taskStatus","code", criteria.getTaskStatus()==null?null:criteria.getTaskStatus().getCode());
    }

    public TaskSpecification(TaskCriteria criteria) {
        super(criteria);
    }

    public TaskSpecification(TaskCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

    // Le framework (SpecificationHelper) ne fournit d'overload addPredicate que pour LocalDateTime,
    // pas pour LocalDate : implémenté ici localement (même pattern que ReservationSpecification).
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