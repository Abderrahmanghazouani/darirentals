package ma.zyn.app.bean.core.task;






import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.bean.core.property.Property;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;
import java.time.LocalDate;

@Entity
@Table(name = "task")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="task_seq",sequenceName="task_seq",allocationSize=1, initialValue = 1)
public class Task  extends BaseEntity     {




    @Column(length = 500)
    private String title;

    private String description;

    private LocalDate dueDate;

    private Property property ;
    private Reservation reservation ;
    private ServiceProvider serviceProvider ;
    private Collaborator assignedTo ;
    private TaskType taskType ;
    private TaskPriority taskPriority ;
    private TaskStatus taskStatus ;


    public Task(){
        super();
    }

    public Task(Long id){
        this.id = id;
    }

    public Task(Long id,String title){
        this.id = id;
        this.title = title ;
    }
    public Task(String title){
        this.title = title ;
    }




    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="task_seq")
    @Override
    public Long getId(){
        return this.id;
    }
    @Override
    public void setId(Long id){
        this.id = id;
    }
    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    @Column(columnDefinition="TEXT")
    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public LocalDate getDueDate(){
        return this.dueDate;
    }
    public void setDueDate(LocalDate dueDate){
        this.dueDate = dueDate;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property")
    public Property getProperty(){
        return this.property;
    }
    public void setProperty(Property property){
        this.property = property;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation")
    public Reservation getReservation(){
        return this.reservation;
    }
    public void setReservation(Reservation reservation){
        this.reservation = reservation;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_provider")
    public ServiceProvider getServiceProvider(){
        return this.serviceProvider;
    }
    public void setServiceProvider(ServiceProvider serviceProvider){
        this.serviceProvider = serviceProvider;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    public Collaborator getAssignedTo(){
        return this.assignedTo;
    }
    public void setAssignedTo(Collaborator assignedTo){
        this.assignedTo = assignedTo;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_type")
    public TaskType getTaskType(){
        return this.taskType;
    }
    public void setTaskType(TaskType taskType){
        this.taskType = taskType;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_priority")
    public TaskPriority getTaskPriority(){
        return this.taskPriority;
    }
    public void setTaskPriority(TaskPriority taskPriority){
        this.taskPriority = taskPriority;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_status")
    public TaskStatus getTaskStatus(){
        return this.taskStatus;
    }
    public void setTaskStatus(TaskStatus taskStatus){
        this.taskStatus = taskStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id != null && id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}