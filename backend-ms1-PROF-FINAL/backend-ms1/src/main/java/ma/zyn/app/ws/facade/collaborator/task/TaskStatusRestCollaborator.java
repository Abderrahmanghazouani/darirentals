package  ma.zyn.app.ws.facade.collaborator.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.task.TaskStatus;
import ma.zyn.app.dao.criteria.core.task.TaskStatusCriteria;
import ma.zyn.app.service.facade.collaborator.task.TaskStatusCollaboratorService;
import ma.zyn.app.ws.converter.task.TaskStatusConverter;
import ma.zyn.app.ws.dto.task.TaskStatusDto;
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
@RequestMapping("/api/collaborator/taskStatus/")
public class TaskStatusRestCollaborator {




    @Operation(summary = "Finds a list of all taskStatuss")
    @GetMapping("")
    public ResponseEntity<List<TaskStatusDto>> findAll() throws Exception {
        ResponseEntity<List<TaskStatusDto>> res = null;
        List<TaskStatus> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<TaskStatusDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all taskStatuss")
    @GetMapping("optimized")
    public ResponseEntity<List<TaskStatusDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<TaskStatusDto>> res = null;
        List<TaskStatus> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<TaskStatusDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a taskStatus by id")
    @GetMapping("id/{id}")
    public ResponseEntity<TaskStatusDto> findById(@PathVariable Long id) {
        TaskStatus t = service.findById(id);
        if (t != null) {
            TaskStatusDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a taskStatus by label")
    @GetMapping("label/{label}")
    public ResponseEntity<TaskStatusDto> findByLabel(@PathVariable String label) {
	    TaskStatus t = service.findByReferenceEntity(new TaskStatus(label));
        if (t != null) {
            TaskStatusDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  taskStatus")
    @PostMapping("")
    public ResponseEntity<TaskStatusDto> save(@RequestBody TaskStatusDto dto) throws Exception {
        if(dto!=null){
            TaskStatus myT = converter.toItem(dto);
            TaskStatus t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                TaskStatusDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  taskStatus")
    @PutMapping("")
    public ResponseEntity<TaskStatusDto> update(@RequestBody TaskStatusDto dto) throws Exception {
        ResponseEntity<TaskStatusDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            TaskStatus t = service.findById(dto.getId());
            converter.copy(dto,t);
            TaskStatus updated = service.update(t);
            TaskStatusDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of taskStatus")
    @PostMapping("multiple")
    public ResponseEntity<List<TaskStatusDto>> delete(@RequestBody List<TaskStatusDto> dtos) throws Exception {
        ResponseEntity<List<TaskStatusDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            List<TaskStatus> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified taskStatus")
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


    @Operation(summary = "Finds a taskStatus and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<TaskStatusDto> findWithAssociatedLists(@PathVariable Long id) {
        TaskStatus loaded =  service.findWithAssociatedLists(id);
        TaskStatusDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds taskStatuss by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<TaskStatusDto>> findByCriteria(@RequestBody TaskStatusCriteria criteria) throws Exception {
        ResponseEntity<List<TaskStatusDto>> res = null;
        List<TaskStatus> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<TaskStatusDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated taskStatuss by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody TaskStatusCriteria criteria) throws Exception {
        List<TaskStatus> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        List<TaskStatusDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets taskStatus data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody TaskStatusCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<TaskStatusDto> findDtos(List<TaskStatus> list){
        List<TaskStatusDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<TaskStatusDto> getDtoResponseEntity(TaskStatusDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public TaskStatusRestCollaborator(TaskStatusCollaboratorService service, TaskStatusConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final TaskStatusCollaboratorService service;
    private final TaskStatusConverter converter;





}
