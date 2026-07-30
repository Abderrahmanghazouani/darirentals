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

import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.dao.criteria.core.enterprise.EnterpriseCriteria;
import ma.zyn.app.service.facade.admin.enterprise.EnterpriseAdminService;
import ma.zyn.app.ws.converter.enterprise.EnterpriseConverter;
import ma.zyn.app.ws.dto.enterprise.EnterpriseDto;
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
@RequestMapping("/api/admin/enterprise/")
public class EnterpriseRestAdmin {




    @Operation(summary = "Finds a list of all enterprises")
    @GetMapping("")
    public ResponseEntity<List<EnterpriseDto>> findAll() throws Exception {
        ResponseEntity<List<EnterpriseDto>> res = null;
        List<Enterprise> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
            converter.initObject(true);
        List<EnterpriseDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all enterprises")
    @GetMapping("optimized")
    public ResponseEntity<List<EnterpriseDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<EnterpriseDto>> res = null;
        List<Enterprise> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        converter.initObject(true);
        List<EnterpriseDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a enterprise by id")
    @GetMapping("id/{id}")
    public ResponseEntity<EnterpriseDto> findById(@PathVariable Long id) {
        Enterprise t = service.findById(id);
        if (t != null) {
            converter.init(true);
            EnterpriseDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a enterprise by name")
    @GetMapping("name/{name}")
    public ResponseEntity<EnterpriseDto> findByName(@PathVariable String name) {
	    Enterprise t = service.findByReferenceEntity(new Enterprise(name));
        if (t != null) {
            converter.init(true);
            EnterpriseDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  enterprise")
    @PostMapping("")
    public ResponseEntity<EnterpriseDto> save(@RequestBody EnterpriseDto dto) throws Exception {
        if(dto!=null){
            converter.init(true);
            Enterprise myT = converter.toItem(dto);
            Enterprise t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                EnterpriseDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  enterprise")
    @PutMapping("")
    public ResponseEntity<EnterpriseDto> update(@RequestBody EnterpriseDto dto) throws Exception {
        ResponseEntity<EnterpriseDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            Enterprise t = service.findById(dto.getId());
            converter.copy(dto,t);
            Enterprise updated = service.update(t);
            EnterpriseDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of enterprise")
    @PostMapping("multiple")
    public ResponseEntity<List<EnterpriseDto>> delete(@RequestBody List<EnterpriseDto> dtos) throws Exception {
        ResponseEntity<List<EnterpriseDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<Enterprise> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified enterprise")
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


    @Operation(summary = "Finds a enterprise and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<EnterpriseDto> findWithAssociatedLists(@PathVariable Long id) {
        Enterprise loaded =  service.findWithAssociatedLists(id);
        converter.init(true);
        EnterpriseDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds enterprises by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<EnterpriseDto>> findByCriteria(@RequestBody EnterpriseCriteria criteria) throws Exception {
        ResponseEntity<List<EnterpriseDto>> res = null;
        List<Enterprise> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        converter.initObject(true);
        List<EnterpriseDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated enterprises by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody EnterpriseCriteria criteria) throws Exception {
        List<Enterprise> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initList(false);
        converter.initObject(true);
        List<EnterpriseDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets enterprise data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody EnterpriseCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<EnterpriseDto> findDtos(List<Enterprise> list){
        converter.initList(false);
        converter.initObject(true);
        List<EnterpriseDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<EnterpriseDto> getDtoResponseEntity(EnterpriseDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public EnterpriseRestAdmin(EnterpriseAdminService service, EnterpriseConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final EnterpriseAdminService service;
    private final EnterpriseConverter converter;





}
