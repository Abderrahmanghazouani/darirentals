package  ma.zyn.app.ws.converter.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;

import ma.zyn.app.ws.converter.auth.CollaboratorConverter;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.ws.converter.provider.ServiceProviderConverter;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.ws.converter.reservation.ReservationConverter;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.ws.converter.task.TaskPriorityConverter;
import ma.zyn.app.bean.core.task.TaskPriority;
import ma.zyn.app.ws.converter.task.TaskTypeConverter;
import ma.zyn.app.bean.core.task.TaskType;
import ma.zyn.app.ws.converter.property.PropertyConverter;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.ws.converter.task.TaskStatusConverter;
import ma.zyn.app.bean.core.task.TaskStatus;

import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.bean.core.auth.Collaborator;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.ws.dto.task.TaskDto;

@Component
public class TaskConverter {

    @Autowired
    private CollaboratorConverter collaboratorConverter ;
    @Autowired
    private ServiceProviderConverter serviceProviderConverter ;
    @Autowired
    private ReservationConverter reservationConverter ;
    @Autowired
    private TaskPriorityConverter taskPriorityConverter ;
    @Autowired
    private TaskTypeConverter taskTypeConverter ;
    @Autowired
    private PropertyConverter propertyConverter ;
    @Autowired
    private TaskStatusConverter taskStatusConverter ;
    private boolean property;
    private boolean reservation;
    private boolean serviceProvider;
    private boolean assignedTo;
    private boolean taskType;
    private boolean taskPriority;
    private boolean taskStatus;

    public  TaskConverter() {
        initObject(true);
    }

    public Task toItem(TaskDto dto) {
        if (dto == null) {
            return null;
        } else {
        Task item = new Task();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getTitle()))
                item.setTitle(dto.getTitle());
            if(StringUtil.isNotEmpty(dto.getDescription()))
                item.setDescription(dto.getDescription());
            if(dto.getProperty() != null && dto.getProperty().getId() != null){
                item.setProperty(new Property());
                item.getProperty().setId(dto.getProperty().getId());
                item.getProperty().setName(dto.getProperty().getName());
            }

            if(dto.getReservation() != null && dto.getReservation().getId() != null){
                item.setReservation(new Reservation());
                item.getReservation().setId(dto.getReservation().getId());
                item.getReservation().setReference(dto.getReservation().getReference());
            }

            if(dto.getServiceProvider() != null && dto.getServiceProvider().getId() != null){
                item.setServiceProvider(new ServiceProvider());
                item.getServiceProvider().setId(dto.getServiceProvider().getId());
                item.getServiceProvider().setName(dto.getServiceProvider().getName());
            }

            if(dto.getAssignedTo() != null && dto.getAssignedTo().getId() != null){
                item.setAssignedTo(new Collaborator());
                item.getAssignedTo().setId(dto.getAssignedTo().getId());
                item.getAssignedTo().setName(dto.getAssignedTo().getName());
            }

            if(this.taskType && dto.getTaskType()!=null)
                item.setTaskType(taskTypeConverter.toItem(dto.getTaskType())) ;

            if(this.taskPriority && dto.getTaskPriority()!=null)
                item.setTaskPriority(taskPriorityConverter.toItem(dto.getTaskPriority())) ;

            if(this.taskStatus && dto.getTaskStatus()!=null)
                item.setTaskStatus(taskStatusConverter.toItem(dto.getTaskStatus())) ;




        return item;
        }
    }


    public TaskDto toDto(Task item) {
        if (item == null) {
            return null;
        } else {
            TaskDto dto = new TaskDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getTitle()))
                dto.setTitle(item.getTitle());
            if(StringUtil.isNotEmpty(item.getDescription()))
                dto.setDescription(item.getDescription());
            if(this.property && item.getProperty()!=null) {
                dto.setProperty(propertyConverter.toDto(item.getProperty())) ;

            }
            if(this.reservation && item.getReservation()!=null) {
                dto.setReservation(reservationConverter.toDto(item.getReservation())) ;

            }
            if(this.serviceProvider && item.getServiceProvider()!=null) {
                dto.setServiceProvider(serviceProviderConverter.toDto(item.getServiceProvider())) ;

            }
            if(this.assignedTo && item.getAssignedTo()!=null) {
                dto.setAssignedTo(collaboratorConverter.toDto(item.getAssignedTo())) ;

            }
            if(this.taskType && item.getTaskType()!=null) {
                dto.setTaskType(taskTypeConverter.toDto(item.getTaskType())) ;

            }
            if(this.taskPriority && item.getTaskPriority()!=null) {
                dto.setTaskPriority(taskPriorityConverter.toDto(item.getTaskPriority())) ;

            }
            if(this.taskStatus && item.getTaskStatus()!=null) {
                dto.setTaskStatus(taskStatusConverter.toDto(item.getTaskStatus())) ;

            }


        return dto;
        }
    }

    public void init(boolean value) {
        initObject(value);
    }

    public void initObject(boolean value) {
        this.property = value;
        this.reservation = value;
        this.serviceProvider = value;
        this.assignedTo = value;
        this.taskType = value;
        this.taskPriority = value;
        this.taskStatus = value;
    }
	
    public List<Task> toItem(List<TaskDto> dtos) {
        List<Task> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (TaskDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<TaskDto> toDto(List<Task> items) {
        List<TaskDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (Task item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(TaskDto dto, Task t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getProperty() == null  && dto.getProperty() != null){
            t.setProperty(new Property());
        }else if (t.getProperty() != null  && dto.getProperty() != null){
            t.setProperty(null);
            t.setProperty(new Property());
        }
        if(t.getReservation() == null  && dto.getReservation() != null){
            t.setReservation(new Reservation());
        }else if (t.getReservation() != null  && dto.getReservation() != null){
            t.setReservation(null);
            t.setReservation(new Reservation());
        }
        if(t.getServiceProvider() == null  && dto.getServiceProvider() != null){
            t.setServiceProvider(new ServiceProvider());
        }else if (t.getServiceProvider() != null  && dto.getServiceProvider() != null){
            t.setServiceProvider(null);
            t.setServiceProvider(new ServiceProvider());
        }
        if(t.getAssignedTo() == null  && dto.getAssignedTo() != null){
            t.setAssignedTo(new Collaborator());
        }else if (t.getAssignedTo() != null  && dto.getAssignedTo() != null){
            t.setAssignedTo(null);
            t.setAssignedTo(new Collaborator());
        }
        if(t.getTaskType() == null  && dto.getTaskType() != null){
            t.setTaskType(new TaskType());
        }else if (t.getTaskType() != null  && dto.getTaskType() != null){
            t.setTaskType(null);
            t.setTaskType(new TaskType());
        }
        if(t.getTaskPriority() == null  && dto.getTaskPriority() != null){
            t.setTaskPriority(new TaskPriority());
        }else if (t.getTaskPriority() != null  && dto.getTaskPriority() != null){
            t.setTaskPriority(null);
            t.setTaskPriority(new TaskPriority());
        }
        if(t.getTaskStatus() == null  && dto.getTaskStatus() != null){
            t.setTaskStatus(new TaskStatus());
        }else if (t.getTaskStatus() != null  && dto.getTaskStatus() != null){
            t.setTaskStatus(null);
            t.setTaskStatus(new TaskStatus());
        }
        if (dto.getProperty() != null)
        propertyConverter.copy(dto.getProperty(), t.getProperty());
        if (dto.getReservation() != null)
        reservationConverter.copy(dto.getReservation(), t.getReservation());
        if (dto.getServiceProvider() != null)
        serviceProviderConverter.copy(dto.getServiceProvider(), t.getServiceProvider());
        if (dto.getAssignedTo() != null)
        collaboratorConverter.copy(dto.getAssignedTo(), t.getAssignedTo());
        if (dto.getTaskType() != null)
        taskTypeConverter.copy(dto.getTaskType(), t.getTaskType());
        if (dto.getTaskPriority() != null)
        taskPriorityConverter.copy(dto.getTaskPriority(), t.getTaskPriority());
        if (dto.getTaskStatus() != null)
        taskStatusConverter.copy(dto.getTaskStatus(), t.getTaskStatus());
    }

    public List<Task> copy(List<TaskDto> dtos) {
        List<Task> result = new ArrayList<>();
        if (dtos != null) {
            for (TaskDto dto : dtos) {
                Task instance = new Task();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


    public CollaboratorConverter getCollaboratorConverter(){
        return this.collaboratorConverter;
    }
    public void setCollaboratorConverter(CollaboratorConverter collaboratorConverter ){
        this.collaboratorConverter = collaboratorConverter;
    }
    public ServiceProviderConverter getServiceProviderConverter(){
        return this.serviceProviderConverter;
    }
    public void setServiceProviderConverter(ServiceProviderConverter serviceProviderConverter ){
        this.serviceProviderConverter = serviceProviderConverter;
    }
    public ReservationConverter getReservationConverter(){
        return this.reservationConverter;
    }
    public void setReservationConverter(ReservationConverter reservationConverter ){
        this.reservationConverter = reservationConverter;
    }
    public TaskPriorityConverter getTaskPriorityConverter(){
        return this.taskPriorityConverter;
    }
    public void setTaskPriorityConverter(TaskPriorityConverter taskPriorityConverter ){
        this.taskPriorityConverter = taskPriorityConverter;
    }
    public TaskTypeConverter getTaskTypeConverter(){
        return this.taskTypeConverter;
    }
    public void setTaskTypeConverter(TaskTypeConverter taskTypeConverter ){
        this.taskTypeConverter = taskTypeConverter;
    }
    public PropertyConverter getPropertyConverter(){
        return this.propertyConverter;
    }
    public void setPropertyConverter(PropertyConverter propertyConverter ){
        this.propertyConverter = propertyConverter;
    }
    public TaskStatusConverter getTaskStatusConverter(){
        return this.taskStatusConverter;
    }
    public void setTaskStatusConverter(TaskStatusConverter taskStatusConverter ){
        this.taskStatusConverter = taskStatusConverter;
    }
    public boolean  isProperty(){
        return this.property;
    }
    public void  setProperty(boolean property){
        this.property = property;
    }
    public boolean  isReservation(){
        return this.reservation;
    }
    public void  setReservation(boolean reservation){
        this.reservation = reservation;
    }
    public boolean  isServiceProvider(){
        return this.serviceProvider;
    }
    public void  setServiceProvider(boolean serviceProvider){
        this.serviceProvider = serviceProvider;
    }
    public boolean  isAssignedTo(){
        return this.assignedTo;
    }
    public void  setAssignedTo(boolean assignedTo){
        this.assignedTo = assignedTo;
    }
    public boolean  isTaskType(){
        return this.taskType;
    }
    public void  setTaskType(boolean taskType){
        this.taskType = taskType;
    }
    public boolean  isTaskPriority(){
        return this.taskPriority;
    }
    public void  setTaskPriority(boolean taskPriority){
        this.taskPriority = taskPriority;
    }
    public boolean  isTaskStatus(){
        return this.taskStatus;
    }
    public void  setTaskStatus(boolean taskStatus){
        this.taskStatus = taskStatus;
    }
}
