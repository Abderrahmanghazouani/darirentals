package  ma.zyn.app.dao.criteria.core.task;


import ma.zyn.app.dao.criteria.core.auth.CollaboratorCriteria;
import ma.zyn.app.dao.criteria.core.provider.ServiceProviderCriteria;
import ma.zyn.app.dao.criteria.core.reservation.ReservationCriteria;
import ma.zyn.app.dao.criteria.core.property.PropertyCriteria;

import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class TaskCriteria extends  BaseCriteria  {

    private String title;
    private String titleLike;
    private String description;
    private String descriptionLike;

    private PropertyCriteria property ;
    private List<PropertyCriteria> propertys ;
    private ReservationCriteria reservation ;
    private List<ReservationCriteria> reservations ;
    private ServiceProviderCriteria serviceProvider ;
    private List<ServiceProviderCriteria> serviceProviders ;
    private CollaboratorCriteria assignedTo ;
    private List<CollaboratorCriteria> assignedTos ;
    private TaskTypeCriteria taskType ;
    private List<TaskTypeCriteria> taskTypes ;
    private TaskPriorityCriteria taskPriority ;
    private List<TaskPriorityCriteria> taskPrioritys ;
    private TaskStatusCriteria taskStatus ;
    private List<TaskStatusCriteria> taskStatuss ;


    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitleLike(){
        return this.titleLike;
    }
    public void setTitleLike(String titleLike){
        this.titleLike = titleLike;
    }

    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescriptionLike(){
        return this.descriptionLike;
    }
    public void setDescriptionLike(String descriptionLike){
        this.descriptionLike = descriptionLike;
    }


    public PropertyCriteria getProperty(){
        return this.property;
    }

    public void setProperty(PropertyCriteria property){
        this.property = property;
    }
    public List<PropertyCriteria> getPropertys(){
        return this.propertys;
    }

    public void setPropertys(List<PropertyCriteria> propertys){
        this.propertys = propertys;
    }
    public ReservationCriteria getReservation(){
        return this.reservation;
    }

    public void setReservation(ReservationCriteria reservation){
        this.reservation = reservation;
    }
    public List<ReservationCriteria> getReservations(){
        return this.reservations;
    }

    public void setReservations(List<ReservationCriteria> reservations){
        this.reservations = reservations;
    }
    public ServiceProviderCriteria getServiceProvider(){
        return this.serviceProvider;
    }

    public void setServiceProvider(ServiceProviderCriteria serviceProvider){
        this.serviceProvider = serviceProvider;
    }
    public List<ServiceProviderCriteria> getServiceProviders(){
        return this.serviceProviders;
    }

    public void setServiceProviders(List<ServiceProviderCriteria> serviceProviders){
        this.serviceProviders = serviceProviders;
    }
    public CollaboratorCriteria getAssignedTo(){
        return this.assignedTo;
    }

    public void setAssignedTo(CollaboratorCriteria assignedTo){
        this.assignedTo = assignedTo;
    }
    public List<CollaboratorCriteria> getAssignedTos(){
        return this.assignedTos;
    }

    public void setAssignedTos(List<CollaboratorCriteria> assignedTos){
        this.assignedTos = assignedTos;
    }
    public TaskTypeCriteria getTaskType(){
        return this.taskType;
    }

    public void setTaskType(TaskTypeCriteria taskType){
        this.taskType = taskType;
    }
    public List<TaskTypeCriteria> getTaskTypes(){
        return this.taskTypes;
    }

    public void setTaskTypes(List<TaskTypeCriteria> taskTypes){
        this.taskTypes = taskTypes;
    }
    public TaskPriorityCriteria getTaskPriority(){
        return this.taskPriority;
    }

    public void setTaskPriority(TaskPriorityCriteria taskPriority){
        this.taskPriority = taskPriority;
    }
    public List<TaskPriorityCriteria> getTaskPrioritys(){
        return this.taskPrioritys;
    }

    public void setTaskPrioritys(List<TaskPriorityCriteria> taskPrioritys){
        this.taskPrioritys = taskPrioritys;
    }
    public TaskStatusCriteria getTaskStatus(){
        return this.taskStatus;
    }

    public void setTaskStatus(TaskStatusCriteria taskStatus){
        this.taskStatus = taskStatus;
    }
    public List<TaskStatusCriteria> getTaskStatuss(){
        return this.taskStatuss;
    }

    public void setTaskStatuss(List<TaskStatusCriteria> taskStatuss){
        this.taskStatuss = taskStatuss;
    }
}
