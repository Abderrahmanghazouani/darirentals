package  ma.zyn.app.ws.facade.collaborator.ai;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.ai.AiQuota;
import ma.zyn.app.dao.criteria.core.ai.AiQuotaCriteria;
import ma.zyn.app.service.facade.collaborator.ai.AiQuotaCollaboratorService;
import ma.zyn.app.ws.converter.ai.AiQuotaConverter;
import ma.zyn.app.ws.dto.ai.AiQuotaDto;
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
@RequestMapping("/api/collaborator/aiQuota/")
public class AiQuotaRestCollaborator {




    @Operation(summary = "Finds a list of all aiQuotas")
    @GetMapping("")
    public ResponseEntity<List<AiQuotaDto>> findAll() throws Exception {
        ResponseEntity<List<AiQuotaDto>> res = null;
        List<AiQuota> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
            converter.initObject(true);
        List<AiQuotaDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }


    @Operation(summary = "Finds a aiQuota by id")
    @GetMapping("id/{id}")
    public ResponseEntity<AiQuotaDto> findById(@PathVariable Long id) {
        AiQuota t = service.findById(id);
        if (t != null) {
            converter.init(true);
            AiQuotaDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @Operation(summary = "Saves the specified  aiQuota")
    @PostMapping("")
    public ResponseEntity<AiQuotaDto> save(@RequestBody AiQuotaDto dto) throws Exception {
        if(dto!=null){
            converter.init(true);
            AiQuota myT = converter.toItem(dto);
            AiQuota t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                AiQuotaDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  aiQuota")
    @PutMapping("")
    public ResponseEntity<AiQuotaDto> update(@RequestBody AiQuotaDto dto) throws Exception {
        ResponseEntity<AiQuotaDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            AiQuota t = service.findById(dto.getId());
            converter.copy(dto,t);
            AiQuota updated = service.update(t);
            AiQuotaDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of aiQuota")
    @PostMapping("multiple")
    public ResponseEntity<List<AiQuotaDto>> delete(@RequestBody List<AiQuotaDto> dtos) throws Exception {
        ResponseEntity<List<AiQuotaDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<AiQuota> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified aiQuota")
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
    public List<AiQuotaDto> findByEnterpriseId(@PathVariable Long id){
        return findDtos(service.findByEnterpriseId(id));
    }
    @Operation(summary = "delete by enterprise id")
    @DeleteMapping("enterprise/id/{id}")
    public int deleteByEnterpriseId(@PathVariable Long id){
        return service.deleteByEnterpriseId(id);
    }

    @Operation(summary = "Finds a aiQuota and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<AiQuotaDto> findWithAssociatedLists(@PathVariable Long id) {
        AiQuota loaded =  service.findWithAssociatedLists(id);
        converter.init(true);
        AiQuotaDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds aiQuotas by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<AiQuotaDto>> findByCriteria(@RequestBody AiQuotaCriteria criteria) throws Exception {
        ResponseEntity<List<AiQuotaDto>> res = null;
        List<AiQuota> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initObject(true);
        List<AiQuotaDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated aiQuotas by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody AiQuotaCriteria criteria) throws Exception {
        List<AiQuota> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initObject(true);
        List<AiQuotaDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets aiQuota data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody AiQuotaCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<AiQuotaDto> findDtos(List<AiQuota> list){
        converter.initObject(true);
        List<AiQuotaDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<AiQuotaDto> getDtoResponseEntity(AiQuotaDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public AiQuotaRestCollaborator(AiQuotaCollaboratorService service, AiQuotaConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final AiQuotaCollaboratorService service;
    private final AiQuotaConverter converter;





}
