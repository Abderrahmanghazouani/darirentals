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

import ma.zyn.app.bean.core.task.TaskType;
import ma.zyn.app.dao.criteria.core.task.TaskTypeCriteria;
import ma.zyn.app.service.facade.admin.task.TaskTypeAdminService;
import ma.zyn.app.ws.converter.task.TaskTypeConverter;
import ma.zyn.app.ws.dto.task.TaskTypeDto;
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
@RequestMapping("/api/admin/taskType/")
public class TaskTypeRestAdmin {




    @Operation(summary = "Finds a list of all taskTypes")
    @GetMapping("")
    public ResponseEntity<List<TaskTypeDto>> findAll() throws Exception {
        ResponseEntity<List<TaskTypeDto>> res = null;
        List<TaskType> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<TaskTypeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all taskTypes")
    @GetMapping("optimized")
    public ResponseEntity<List<TaskTypeDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<TaskTypeDto>> res = null;
        List<TaskType> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<TaskTypeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a taskType by id")
    @GetMapping("id/{id}")
    public ResponseEntity<TaskTypeDto> findById(@PathVariable Long id) {
        TaskType t = service.findById(id);
        if (t != null) {
            TaskTypeDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a taskType by label")
    @GetMapping("label/{label}")
    public ResponseEntity<TaskTypeDto> findByLabel(@PathVariable String label) {
	    TaskType t = service.findByReferenceEntity(new TaskType(label));
        if (t != null) {
            TaskTypeDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  taskType")
    @PostMapping("")
    public ResponseEntity<TaskTypeDto> save(@RequestBody TaskTypeDto dto) throws Exception {
        if(dto!=null){
            TaskType myT = converter.toItem(dto);
            TaskType t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                TaskTypeDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  taskType")
    @PutMapping("")
    public ResponseEntity<TaskTypeDto> update(@RequestBody TaskTypeDto dto) throws Exception {
        ResponseEntity<TaskTypeDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            TaskType t = service.findById(dto.getId());
            converter.copy(dto,t);
            TaskType updated = service.update(t);
            TaskTypeDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of taskType")
    @PostMapping("multiple")
    public ResponseEntity<List<TaskTypeDto>> delete(@RequestBody List<TaskTypeDto> dtos) throws Exception {
        ResponseEntity<List<TaskTypeDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            List<TaskType> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified taskType")
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


    @Operation(summary = "Finds a taskType and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<TaskTypeDto> findWithAssociatedLists(@PathVariable Long id) {
        TaskType loaded =  service.findWithAssociatedLists(id);
        TaskTypeDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds taskTypes by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<TaskTypeDto>> findByCriteria(@RequestBody TaskTypeCriteria criteria) throws Exception {
        ResponseEntity<List<TaskTypeDto>> res = null;
        List<TaskType> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<TaskTypeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated taskTypes by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody TaskTypeCriteria criteria) throws Exception {
        List<TaskType> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        List<TaskTypeDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets taskType data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody TaskTypeCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<TaskTypeDto> findDtos(List<TaskType> list){
        List<TaskTypeDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<TaskTypeDto> getDtoResponseEntity(TaskTypeDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public TaskTypeRestAdmin(TaskTypeAdminService service, TaskTypeConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final TaskTypeAdminService service;
    private final TaskTypeConverter converter;





}
