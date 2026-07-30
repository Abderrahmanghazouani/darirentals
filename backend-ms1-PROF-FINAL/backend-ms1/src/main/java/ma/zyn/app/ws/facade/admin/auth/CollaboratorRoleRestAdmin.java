package  ma.zyn.app.ws.facade.admin.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.auth.CollaboratorRole;
import ma.zyn.app.dao.criteria.core.auth.CollaboratorRoleCriteria;
import ma.zyn.app.service.facade.admin.auth.CollaboratorRoleAdminService;
import ma.zyn.app.ws.converter.auth.CollaboratorRoleConverter;
import ma.zyn.app.ws.dto.auth.CollaboratorRoleDto;
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
@RequestMapping("/api/admin/collaboratorRole/")
public class CollaboratorRoleRestAdmin {




    @Operation(summary = "Finds a list of all collaboratorRoles")
    @GetMapping("")
    public ResponseEntity<List<CollaboratorRoleDto>> findAll() throws Exception {
        ResponseEntity<List<CollaboratorRoleDto>> res = null;
        List<CollaboratorRole> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<CollaboratorRoleDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all collaboratorRoles")
    @GetMapping("optimized")
    public ResponseEntity<List<CollaboratorRoleDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<CollaboratorRoleDto>> res = null;
        List<CollaboratorRole> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<CollaboratorRoleDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a collaboratorRole by id")
    @GetMapping("id/{id}")
    public ResponseEntity<CollaboratorRoleDto> findById(@PathVariable Long id) {
        CollaboratorRole t = service.findById(id);
        if (t != null) {
            CollaboratorRoleDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a collaboratorRole by label")
    @GetMapping("label/{label}")
    public ResponseEntity<CollaboratorRoleDto> findByLabel(@PathVariable String label) {
	    CollaboratorRole t = service.findByReferenceEntity(new CollaboratorRole(label));
        if (t != null) {
            CollaboratorRoleDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  collaboratorRole")
    @PostMapping("")
    public ResponseEntity<CollaboratorRoleDto> save(@RequestBody CollaboratorRoleDto dto) throws Exception {
        if(dto!=null){
            CollaboratorRole myT = converter.toItem(dto);
            CollaboratorRole t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                CollaboratorRoleDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  collaboratorRole")
    @PutMapping("")
    public ResponseEntity<CollaboratorRoleDto> update(@RequestBody CollaboratorRoleDto dto) throws Exception {
        ResponseEntity<CollaboratorRoleDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            CollaboratorRole t = service.findById(dto.getId());
            converter.copy(dto,t);
            CollaboratorRole updated = service.update(t);
            CollaboratorRoleDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of collaboratorRole")
    @PostMapping("multiple")
    public ResponseEntity<List<CollaboratorRoleDto>> delete(@RequestBody List<CollaboratorRoleDto> dtos) throws Exception {
        ResponseEntity<List<CollaboratorRoleDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            List<CollaboratorRole> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified collaboratorRole")
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


    @Operation(summary = "Finds a collaboratorRole and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<CollaboratorRoleDto> findWithAssociatedLists(@PathVariable Long id) {
        CollaboratorRole loaded =  service.findWithAssociatedLists(id);
        CollaboratorRoleDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds collaboratorRoles by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<CollaboratorRoleDto>> findByCriteria(@RequestBody CollaboratorRoleCriteria criteria) throws Exception {
        ResponseEntity<List<CollaboratorRoleDto>> res = null;
        List<CollaboratorRole> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<CollaboratorRoleDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated collaboratorRoles by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody CollaboratorRoleCriteria criteria) throws Exception {
        List<CollaboratorRole> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        List<CollaboratorRoleDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets collaboratorRole data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody CollaboratorRoleCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<CollaboratorRoleDto> findDtos(List<CollaboratorRole> list){
        List<CollaboratorRoleDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<CollaboratorRoleDto> getDtoResponseEntity(CollaboratorRoleDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public CollaboratorRoleRestAdmin(CollaboratorRoleAdminService service, CollaboratorRoleConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final CollaboratorRoleAdminService service;
    private final CollaboratorRoleConverter converter;





}
