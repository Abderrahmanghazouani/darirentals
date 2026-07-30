package  ma.zyn.app.ws.dto.task;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;



import ma.zyn.app.ws.dto.auth.CollaboratorDto;
import ma.zyn.app.ws.dto.provider.ServiceProviderDto;
import ma.zyn.app.ws.dto.reservation.ReservationDto;
import ma.zyn.app.ws.dto.property.PropertyDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDto  extends AuditBaseDto {

    private String title  ;
    private String description  ;

    private PropertyDto property ;
    private ReservationDto reservation ;
    private ServiceProviderDto serviceProvider ;
    private CollaboratorDto assignedTo ;
    private TaskTypeDto taskType ;
    private TaskPriorityDto taskPriority ;
    private TaskStatusDto taskStatus ;



    public TaskDto(){
        super();
    }



    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }


    public PropertyDto getProperty(){
        return this.property;
    }

    public void setProperty(PropertyDto property){
        this.property = property;
    }
    public ReservationDto getReservation(){
        return this.reservation;
    }

    public void setReservation(ReservationDto reservation){
        this.reservation = reservation;
    }
    public ServiceProviderDto getServiceProvider(){
        return this.serviceProvider;
    }

    public void setServiceProvider(ServiceProviderDto serviceProvider){
        this.serviceProvider = serviceProvider;
    }
    public CollaboratorDto getAssignedTo(){
        return this.assignedTo;
    }

    public void setAssignedTo(CollaboratorDto assignedTo){
        this.assignedTo = assignedTo;
    }
    public TaskTypeDto getTaskType(){
        return this.taskType;
    }

    public void setTaskType(TaskTypeDto taskType){
        this.taskType = taskType;
    }
    public TaskPriorityDto getTaskPriority(){
        return this.taskPriority;
    }

    public void setTaskPriority(TaskPriorityDto taskPriority){
        this.taskPriority = taskPriority;
    }
    public TaskStatusDto getTaskStatus(){
        return this.taskStatus;
    }

    public void setTaskStatus(TaskStatusDto taskStatus){
        this.taskStatus = taskStatus;
    }






}
