package  ma.zyn.app.ws.facade.admin.ai;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.ai.AiUsageLog;
import ma.zyn.app.dao.criteria.core.ai.AiUsageLogCriteria;
import ma.zyn.app.service.facade.admin.ai.AiUsageLogAdminService;
import ma.zyn.app.ws.converter.ai.AiUsageLogConverter;
import ma.zyn.app.ws.dto.ai.AiUsageLogDto;
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
@RequestMapping("/api/admin/aiUsageLog/")
public class AiUsageLogRestAdmin {




    @Operation(summary = "Finds a list of all aiUsageLogs")
    @GetMapping("")
    public ResponseEntity<List<AiUsageLogDto>> findAll() throws Exception {
        ResponseEntity<List<AiUsageLogDto>> res = null;
        List<AiUsageLog> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
            converter.initObject(true);
        List<AiUsageLogDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }


    @Operation(summary = "Finds a aiUsageLog by id")
    @GetMapping("id/{id}")
    public ResponseEntity<AiUsageLogDto> findById(@PathVariable Long id) {
        AiUsageLog t = service.findById(id);
        if (t != null) {
            converter.init(true);
            AiUsageLogDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @Operation(summary = "Saves the specified  aiUsageLog")
    @PostMapping("")
    public ResponseEntity<AiUsageLogDto> save(@RequestBody AiUsageLogDto dto) throws Exception {
        if(dto!=null){
            converter.init(true);
            AiUsageLog myT = converter.toItem(dto);
            AiUsageLog t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                AiUsageLogDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  aiUsageLog")
    @PutMapping("")
    public ResponseEntity<AiUsageLogDto> update(@RequestBody AiUsageLogDto dto) throws Exception {
        ResponseEntity<AiUsageLogDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            AiUsageLog t = service.findById(dto.getId());
            converter.copy(dto,t);
            AiUsageLog updated = service.update(t);
            AiUsageLogDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of aiUsageLog")
    @PostMapping("multiple")
    public ResponseEntity<List<AiUsageLogDto>> delete(@RequestBody List<AiUsageLogDto> dtos) throws Exception {
        ResponseEntity<List<AiUsageLogDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<AiUsageLog> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified aiUsageLog")
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
    public List<AiUsageLogDto> findByEnterpriseId(@PathVariable Long id){
        return findDtos(service.findByEnterpriseId(id));
    }
    @Operation(summary = "delete by enterprise id")
    @DeleteMapping("enterprise/id/{id}")
    public int deleteByEnterpriseId(@PathVariable Long id){
        return service.deleteByEnterpriseId(id);
    }
    @Operation(summary = "find by aiUsageType code")
    @GetMapping("aiUsageType/code/{code}")
    public List<AiUsageLogDto> findByAiUsageTypeCode(@PathVariable String code){
        return findDtos(service.findByAiUsageTypeCode(code));
    }
    @Operation(summary = "delete by aiUsageType code")
    @DeleteMapping("aiUsageType/code/{code}")
    public int deleteByAiUsageTypeCode(@PathVariable String code){
        return service.deleteByAiUsageTypeCode(code);
    }
    @Operation(summary = "find by document id")
    @GetMapping("document/id/{id}")
    public List<AiUsageLogDto> findByDocumentId(@PathVariable Long id){
        return findDtos(service.findByDocumentId(id));
    }
    @Operation(summary = "delete by document id")
    @DeleteMapping("document/id/{id}")
    public int deleteByDocumentId(@PathVariable Long id){
        return service.deleteByDocumentId(id);
    }

    @Operation(summary = "Finds a aiUsageLog and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<AiUsageLogDto> findWithAssociatedLists(@PathVariable Long id) {
        AiUsageLog loaded =  service.findWithAssociatedLists(id);
        converter.init(true);
        AiUsageLogDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds aiUsageLogs by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<AiUsageLogDto>> findByCriteria(@RequestBody AiUsageLogCriteria criteria) throws Exception {
        ResponseEntity<List<AiUsageLogDto>> res = null;
        List<AiUsageLog> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initObject(true);
        List<AiUsageLogDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated aiUsageLogs by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody AiUsageLogCriteria criteria) throws Exception {
        List<AiUsageLog> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initObject(true);
        List<AiUsageLogDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets aiUsageLog data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody AiUsageLogCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<AiUsageLogDto> findDtos(List<AiUsageLog> list){
        converter.initObject(true);
        List<AiUsageLogDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<AiUsageLogDto> getDtoResponseEntity(AiUsageLogDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public AiUsageLogRestAdmin(AiUsageLogAdminService service, AiUsageLogConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final AiUsageLogAdminService service;
    private final AiUsageLogConverter converter;





}
