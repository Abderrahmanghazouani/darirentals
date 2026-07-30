package  ma.zyn.app.ws.facade.admin.enterprise;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;
import ma.zyn.app.dao.criteria.core.enterprise.EnterpriseMembershipCriteria;
import ma.zyn.app.service.facade.admin.enterprise.EnterpriseMembershipAdminService;
import ma.zyn.app.ws.converter.enterprise.EnterpriseMembershipConverter;
import ma.zyn.app.ws.dto.enterprise.EnterpriseMembershipDto;
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
@RequestMapping("/api/admin/enterpriseMembership/")
public class EnterpriseMembershipRestAdmin {




    @Operation(summary = "Finds a list of all enterpriseMemberships")
    @GetMapping("")
    public ResponseEntity<List<EnterpriseMembershipDto>> findAll() throws Exception {
        ResponseEntity<List<EnterpriseMembershipDto>> res = null;
        List<EnterpriseMembership> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
            converter.initObject(true);
        List<EnterpriseMembershipDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }


    @Operation(summary = "Finds a enterpriseMembership by id")
    @GetMapping("id/{id}")
    public ResponseEntity<EnterpriseMembershipDto> findById(@PathVariable Long id) {
        EnterpriseMembership t = service.findById(id);
        if (t != null) {
            converter.init(true);
            EnterpriseMembershipDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @Operation(summary = "Saves the specified  enterpriseMembership")
    @PostMapping("")
    public ResponseEntity<EnterpriseMembershipDto> save(@RequestBody EnterpriseMembershipDto dto) throws Exception {
        if(dto!=null){
            converter.init(true);
            EnterpriseMembership myT = converter.toItem(dto);
            EnterpriseMembership t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                EnterpriseMembershipDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  enterpriseMembership")
    @PutMapping("")
    public ResponseEntity<EnterpriseMembershipDto> update(@RequestBody EnterpriseMembershipDto dto) throws Exception {
        ResponseEntity<EnterpriseMembershipDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            EnterpriseMembership t = service.findById(dto.getId());
            converter.copy(dto,t);
            EnterpriseMembership updated = service.update(t);
            EnterpriseMembershipDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of enterpriseMembership")
    @PostMapping("multiple")
    public ResponseEntity<List<EnterpriseMembershipDto>> delete(@RequestBody List<EnterpriseMembershipDto> dtos) throws Exception {
        ResponseEntity<List<EnterpriseMembershipDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<EnterpriseMembership> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified enterpriseMembership")
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

    @Operation(summary = "find by enterprise id")
    @GetMapping("enterprise/id/{id}")
    public List<EnterpriseMembershipDto> findByEnterpriseId(@PathVariable Long id){
        return findDtos(service.findByEnterpriseId(id));
    }
    @Operation(summary = "delete by enterprise id")
    @DeleteMapping("enterprise/id/{id}")
    public int deleteByEnterpriseId(@PathVariable Long id){
        return service.deleteByEnterpriseId(id);
    }
    @Operation(summary = "find by collaboratorRole code")
    @GetMapping("collaboratorRole/code/{code}")
    public List<EnterpriseMembershipDto> findByCollaboratorRoleCode(@PathVariable String code){
        return findDtos(service.findByCollaboratorRoleCode(code));
    }
    @Operation(summary = "delete by collaboratorRole code")
    @DeleteMapping("collaboratorRole/code/{code}")
    public int deleteByCollaboratorRoleCode(@PathVariable String code){
        return service.deleteByCollaboratorRoleCode(code);
    }

    @Operation(summary = "Finds a enterpriseMembership and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<EnterpriseMembershipDto> findWithAssociatedLists(@PathVariable Long id) {
        EnterpriseMembership loaded =  service.findWithAssociatedLists(id);
        converter.init(true);
        EnterpriseMembershipDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds enterpriseMemberships by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<EnterpriseMembershipDto>> findByCriteria(@RequestBody EnterpriseMembershipCriteria criteria) throws Exception {
        ResponseEntity<List<EnterpriseMembershipDto>> res = null;
        List<EnterpriseMembership> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        converter.initObject(true);
        List<EnterpriseMembershipDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated enterpriseMemberships by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody EnterpriseMembershipCriteria criteria) throws Exception {
        List<EnterpriseMembership> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initList(false);
        converter.initObject(true);
        List<EnterpriseMembershipDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets enterpriseMembership data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody EnterpriseMembershipCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<EnterpriseMembershipDto> findDtos(List<EnterpriseMembership> list){
        converter.initList(false);
        converter.initObject(true);
        List<EnterpriseMembershipDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<EnterpriseMembershipDto> getDtoResponseEntity(EnterpriseMembershipDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public EnterpriseMembershipRestAdmin(EnterpriseMembershipAdminService service, EnterpriseMembershipConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final EnterpriseMembershipAdminService service;
    private final EnterpriseMembershipConverter converter;





}
