package  ma.zyn.app.ws.facade.admin.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.dao.criteria.core.task.TaskCriteria;
import ma.zyn.app.service.facade.admin.task.TaskAdminService;
import ma.zyn.app.ws.converter.task.TaskConverter;
import ma.zyn.app.ws.dto.task.TaskDto;
import ma.zyn.app.zynerator.controller.AbstractController;
import ma.zyn.app.zynerator.dto.AuditEntityDto;
import ma.zyn.app.zynerator.util.PaginatedList;


import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ma.zyn.app.zynerator.process.Result;


import org.springframework.web.multipart.MultipartFile;
import ma.zyn.app.zynerator.dto.FileTempDto;

@RestController
@RequestMapping("/api/admin/task/")
public class TaskRestAdmin {




    @Operation(summary = "Finds a list of all tasks")
    @GetMapping("")
    public ResponseEntity<List<TaskDto>> findAll() throws Exception {
        ResponseEntity<List<TaskDto>> res = null;
        List<Task> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
            converter.initObject(true);
        List<TaskDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all tasks")
    @GetMapping("optimized")
    public ResponseEntity<List<TaskDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<TaskDto>> res = null;
        List<Task> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initObject(true);
        List<TaskDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a task by id")
    @GetMapping("id/{id}")
    public ResponseEntity<TaskDto> findById(@PathVariable Long id) {
        Task t = service.findById(id);
        if (t != null) {
            converter.init(true);
            TaskDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a task by title")
    @GetMapping("title/{title}")
    public ResponseEntity<TaskDto> findByTitle(@PathVariable String title) {
	    Task t = service.findByReferenceEntity(new Task(title));
        if (t != null) {
            converter.init(true);
            TaskDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  task")
    @PostMapping("")
    public ResponseEntity<TaskDto> save(@RequestBody TaskDto dto) throws Exception {
        if(dto!=null){
            converter.init(true);
            Task myT = converter.toItem(dto);
            Task t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                TaskDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  task")
    @PutMapping("")
    public ResponseEntity<TaskDto> update(@RequestBody TaskDto dto) throws Exception {
        ResponseEntity<TaskDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            Task t = service.findById(dto.getId());
            converter.copy(dto,t);
            Task updated = service.update(t);
            TaskDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of task")
    @PostMapping("multiple")
    public ResponseEntity<List<TaskDto>> delete(@RequestBody List<TaskDto> dtos) throws Exception {
        ResponseEntity<List<TaskDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<Task> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified task")
    @DeleteMapping("id/{id}")
    public ResponseEntity<Long> deleteById(@PathVariable Long id) throws Exception {
        ResponseEntity<Long> res;
        HttpStatus status = HttpStatus.PRECONDITION_FAILED;
        if (id != null) {
            boolean resultDelete = service.deleteById(id);
            if (resultDelete) {
                status = HttpStatus.OK;
            }
        }
        res = new ResponseEntity<>(id, status);
        return res;
    }

    @Operation(summary = "find by reservation id")
    @GetMapping("reservation/id/{id}")
    public List<TaskDto> findByReservationId(@PathVariable Long id){
        return findDtos(service.findByReservationId(id));
    }
    @Operation(summary = "delete by reservation id")
    @DeleteMapping("reservation/id/{id}")
    public int deleteByReservationId(@PathVariable Long id){
        return service.deleteByReservationId(id);
    }
    @Operation(summary = "find by serviceProvider id")
    @GetMapping("serviceProvider/id/{id}")
    public List<TaskDto> findByServiceProviderId(@PathVariable Long id){
        return findDtos(service.findByServiceProviderId(id));
    }
    @Operation(summary = "delete by serviceProvider id")
    @DeleteMapping("serviceProvider/id/{id}")
    public int deleteByServiceProviderId(@PathVariable Long id){
        return service.deleteByServiceProviderId(id);
    }
    @Operation(summary = "find by assignedTo id")
    @GetMapping("assignedTo/id/{id}")
    public List<TaskDto> findByAssignedToId(@PathVariable Long id){
        return findDtos(service.findByAssignedToId(id));
    }
    @Operation(summary = "delete by assignedTo id")
    @DeleteMapping("assignedTo/id/{id}")
    public int deleteByAssignedToId(@PathVariable Long id){
        return service.deleteByAssignedToId(id);
    }
    @Operation(summary = "find by taskType code")
    @GetMapping("taskType/code/{code}")
    public List<TaskDto> findByTaskTypeCode(@PathVariable String code){
        return findDtos(service.findByTaskTypeCode(code));
    }
    @Operation(summary = "delete by taskType code")
    @DeleteMapping("taskType/code/{code}")
    public int deleteByTaskTypeCode(@PathVariable String code){
        return service.deleteByTaskTypeCode(code);
    }
    @Operation(summary = "find by taskPriority code")
    @GetMapping("taskPriority/code/{code}")
    public List<TaskDto> findByTaskPriorityCode(@PathVariable String code){
        return findDtos(service.findByTaskPriorityCode(code));
    }
    @Operation(summary = "delete by taskPriority code")
    @DeleteMapping("taskPriority/code/{code}")
    public int deleteByTaskPriorityCode(@PathVariable String code){
        return service.deleteByTaskPriorityCode(code);
    }
    @Operation(summary = "find by taskStatus code")
    @GetMapping("taskStatus/code/{code}")
    public List<TaskDto> findByTaskStatusCode(@PathVariable String code){
        return findDtos(service.findByTaskStatusCode(code));
    }
    @Operation(summary = "delete by taskStatus code")
    @DeleteMapping("taskStatus/code/{code}")
    public int deleteByTaskStatusCode(@PathVariable String code){
        return service.deleteByTaskStatusCode(code);
    }

    @Operation(summary = "Finds a task and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<TaskDto> findWithAssociatedLists(@PathVariable Long id) {
        Task loaded =  service.findWithAssociatedLists(id);
        converter.init(true);
        TaskDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds tasks by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<TaskDto>> findByCriteria(@RequestBody TaskCriteria criteria) throws Exception {
        ResponseEntity<List<TaskDto>> res = null;
        List<Task> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initObject(true);
        List<TaskDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated tasks by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody TaskCriteria criteria) throws Exception {
        List<Task> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initObject(true);
        List<TaskDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets task data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody TaskCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<TaskDto> findDtos(List<Task> list){
        converter.initObject(true);
        List<TaskDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<TaskDto> getDtoResponseEntity(TaskDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public TaskRestAdmin(TaskAdminService service, TaskConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final TaskAdminService service;
    private final TaskConverter converter;





}
