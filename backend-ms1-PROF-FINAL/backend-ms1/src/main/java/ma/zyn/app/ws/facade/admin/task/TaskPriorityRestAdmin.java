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

import ma.zyn.app.bean.core.task.TaskPriority;
import ma.zyn.app.dao.criteria.core.task.TaskPriorityCriteria;
import ma.zyn.app.service.facade.admin.task.TaskPriorityAdminService;
import ma.zyn.app.ws.converter.task.TaskPriorityConverter;
import ma.zyn.app.ws.dto.task.TaskPriorityDto;
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
@RequestMapping("/api/admin/taskPriority/")
public class TaskPriorityRestAdmin {




    @Operation(summary = "Finds a list of all taskPrioritys")
    @GetMapping("")
    public ResponseEntity<List<TaskPriorityDto>> findAll() throws Exception {
        ResponseEntity<List<TaskPriorityDto>> res = null;
        List<TaskPriority> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<TaskPriorityDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all taskPrioritys")
    @GetMapping("optimized")
    public ResponseEntity<List<TaskPriorityDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<TaskPriorityDto>> res = null;
        List<TaskPriority> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<TaskPriorityDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a taskPriority by id")
    @GetMapping("id/{id}")
    public ResponseEntity<TaskPriorityDto> findById(@PathVariable Long id) {
        TaskPriority t = service.findById(id);
        if (t != null) {
            TaskPriorityDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a taskPriority by label")
    @GetMapping("label/{label}")
    public ResponseEntity<TaskPriorityDto> findByLabel(@PathVariable String label) {
	    TaskPriority t = service.findByReferenceEntity(new TaskPriority(label));
        if (t != null) {
            TaskPriorityDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  taskPriority")
    @PostMapping("")
    public ResponseEntity<TaskPriorityDto> save(@RequestBody TaskPriorityDto dto) throws Exception {
        if(dto!=null){
            TaskPriority myT = converter.toItem(dto);
            TaskPriority t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                TaskPriorityDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  taskPriority")
    @PutMapping("")
    public ResponseEntity<TaskPriorityDto> update(@RequestBody TaskPriorityDto dto) throws Exception {
        ResponseEntity<TaskPriorityDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            TaskPriority t = service.findById(dto.getId());
            converter.copy(dto,t);
            TaskPriority updated = service.update(t);
            TaskPriorityDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of taskPriority")
    @PostMapping("multiple")
    public ResponseEntity<List<TaskPriorityDto>> delete(@RequestBody List<TaskPriorityDto> dtos) throws Exception {
        ResponseEntity<List<TaskPriorityDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            List<TaskPriority> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified taskPriority")
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


    @Operation(summary = "Finds a taskPriority and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<TaskPriorityDto> findWithAssociatedLists(@PathVariable Long id) {
        TaskPriority loaded =  service.findWithAssociatedLists(id);
        TaskPriorityDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds taskPrioritys by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<TaskPriorityDto>> findByCriteria(@RequestBody TaskPriorityCriteria criteria) throws Exception {
        ResponseEntity<List<TaskPriorityDto>> res = null;
        List<TaskPriority> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<TaskPriorityDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated taskPrioritys by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody TaskPriorityCriteria criteria) throws Exception {
        List<TaskPriority> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        List<TaskPriorityDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets taskPriority data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody TaskPriorityCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<TaskPriorityDto> findDtos(List<TaskPriority> list){
        List<TaskPriorityDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<TaskPriorityDto> getDtoResponseEntity(TaskPriorityDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public TaskPriorityRestAdmin(TaskPriorityAdminService service, TaskPriorityConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final TaskPriorityAdminService service;
    private final TaskPriorityConverter converter;





}
