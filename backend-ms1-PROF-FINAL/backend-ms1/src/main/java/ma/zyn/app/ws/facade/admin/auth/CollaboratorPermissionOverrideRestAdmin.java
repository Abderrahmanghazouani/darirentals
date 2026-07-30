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

import ma.zyn.app.bean.core.auth.CollaboratorPermissionOverride;
import ma.zyn.app.dao.criteria.core.auth.CollaboratorPermissionOverrideCriteria;
import ma.zyn.app.service.facade.admin.auth.CollaboratorPermissionOverrideAdminService;
import ma.zyn.app.ws.converter.auth.CollaboratorPermissionOverrideConverter;
import ma.zyn.app.ws.dto.auth.CollaboratorPermissionOverrideDto;
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
@RequestMapping("/api/admin/collaboratorPermissionOverride/")
public class CollaboratorPermissionOverrideRestAdmin {




    @Operation(summary = "Finds a list of all collaboratorPermissionOverrides")
    @GetMapping("")
    public ResponseEntity<List<CollaboratorPermissionOverrideDto>> findAll() throws Exception {
        ResponseEntity<List<CollaboratorPermissionOverrideDto>> res = null;
        List<CollaboratorPermissionOverride> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
            converter.initObject(true);
        List<CollaboratorPermissionOverrideDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }


    @Operation(summary = "Finds a collaboratorPermissionOverride by id")
    @GetMapping("id/{id}")
    public ResponseEntity<CollaboratorPermissionOverrideDto> findById(@PathVariable Long id) {
        CollaboratorPermissionOverride t = service.findById(id);
        if (t != null) {
            converter.init(true);
            CollaboratorPermissionOverrideDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @Operation(summary = "Saves the specified  collaboratorPermissionOverride")
    @PostMapping("")
    public ResponseEntity<CollaboratorPermissionOverrideDto> save(@RequestBody CollaboratorPermissionOverrideDto dto) throws Exception {
        if(dto!=null){
            converter.init(true);
            CollaboratorPermissionOverride myT = converter.toItem(dto);
            CollaboratorPermissionOverride t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                CollaboratorPermissionOverrideDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  collaboratorPermissionOverride")
    @PutMapping("")
    public ResponseEntity<CollaboratorPermissionOverrideDto> update(@RequestBody CollaboratorPermissionOverrideDto dto) throws Exception {
        ResponseEntity<CollaboratorPermissionOverrideDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            CollaboratorPermissionOverride t = service.findById(dto.getId());
            converter.copy(dto,t);
            CollaboratorPermissionOverride updated = service.update(t);
            CollaboratorPermissionOverrideDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of collaboratorPermissionOverride")
    @PostMapping("multiple")
    public ResponseEntity<List<CollaboratorPermissionOverrideDto>> delete(@RequestBody List<CollaboratorPermissionOverrideDto> dtos) throws Exception {
        ResponseEntity<List<CollaboratorPermissionOverrideDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<CollaboratorPermissionOverride> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified collaboratorPermissionOverride")
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

    @Operation(summary = "find by enterpriseMembership id")
    @GetMapping("enterpriseMembership/id/{id}")
    public List<CollaboratorPermissionOverrideDto> findByEnterpriseMembershipId(@PathVariable Long id){
        return findDtos(service.findByEnterpriseMembershipId(id));
    }
    @Operation(summary = "delete by enterpriseMembership id")
    @DeleteMapping("enterpriseMembership/id/{id}")
    public int deleteByEnterpriseMembershipId(@PathVariable Long id){
        return service.deleteByEnterpriseMembershipId(id);
    }

    @Operation(summary = "Finds a collaboratorPermissionOverride and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<CollaboratorPermissionOverrideDto> findWithAssociatedLists(@PathVariable Long id) {
        CollaboratorPermissionOverride loaded =  service.findWithAssociatedLists(id);
        converter.init(true);
        CollaboratorPermissionOverrideDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds collaboratorPermissionOverrides by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<CollaboratorPermissionOverrideDto>> findByCriteria(@RequestBody CollaboratorPermissionOverrideCriteria criteria) throws Exception {
        ResponseEntity<List<CollaboratorPermissionOverrideDto>> res = null;
        List<CollaboratorPermissionOverride> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initObject(true);
        List<CollaboratorPermissionOverrideDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated collaboratorPermissionOverrides by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody CollaboratorPermissionOverrideCriteria criteria) throws Exception {
        List<CollaboratorPermissionOverride> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initObject(true);
        List<CollaboratorPermissionOverrideDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets collaboratorPermissionOverride data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody CollaboratorPermissionOverrideCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<CollaboratorPermissionOverrideDto> findDtos(List<CollaboratorPermissionOverride> list){
        converter.initObject(true);
        List<CollaboratorPermissionOverrideDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<CollaboratorPermissionOverrideDto> getDtoResponseEntity(CollaboratorPermissionOverrideDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public CollaboratorPermissionOverrideRestAdmin(CollaboratorPermissionOverrideAdminService service, CollaboratorPermissionOverrideConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final CollaboratorPermissionOverrideAdminService service;
    private final CollaboratorPermissionOverrideConverter converter;





}
