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

import ma.zyn.app.bean.core.ai.AiUsageType;
import ma.zyn.app.dao.criteria.core.ai.AiUsageTypeCriteria;
import ma.zyn.app.service.facade.collaborator.ai.AiUsageTypeCollaboratorService;
import ma.zyn.app.ws.converter.ai.AiUsageTypeConverter;
import ma.zyn.app.ws.dto.ai.AiUsageTypeDto;
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
@RequestMapping("/api/collaborator/aiUsageType/")
public class AiUsageTypeRestCollaborator {




    @Operation(summary = "Finds a list of all aiUsageTypes")
    @GetMapping("")
    public ResponseEntity<List<AiUsageTypeDto>> findAll() throws Exception {
        ResponseEntity<List<AiUsageTypeDto>> res = null;
        List<AiUsageType> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<AiUsageTypeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all aiUsageTypes")
    @GetMapping("optimized")
    public ResponseEntity<List<AiUsageTypeDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<AiUsageTypeDto>> res = null;
        List<AiUsageType> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<AiUsageTypeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a aiUsageType by id")
    @GetMapping("id/{id}")
    public ResponseEntity<AiUsageTypeDto> findById(@PathVariable Long id) {
        AiUsageType t = service.findById(id);
        if (t != null) {
            AiUsageTypeDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a aiUsageType by label")
    @GetMapping("label/{label}")
    public ResponseEntity<AiUsageTypeDto> findByLabel(@PathVariable String label) {
	    AiUsageType t = service.findByReferenceEntity(new AiUsageType(label));
        if (t != null) {
            AiUsageTypeDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  aiUsageType")
    @PostMapping("")
    public ResponseEntity<AiUsageTypeDto> save(@RequestBody AiUsageTypeDto dto) throws Exception {
        if(dto!=null){
            AiUsageType myT = converter.toItem(dto);
            AiUsageType t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                AiUsageTypeDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  aiUsageType")
    @PutMapping("")
    public ResponseEntity<AiUsageTypeDto> update(@RequestBody AiUsageTypeDto dto) throws Exception {
        ResponseEntity<AiUsageTypeDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            AiUsageType t = service.findById(dto.getId());
            converter.copy(dto,t);
            AiUsageType updated = service.update(t);
            AiUsageTypeDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of aiUsageType")
    @PostMapping("multiple")
    public ResponseEntity<List<AiUsageTypeDto>> delete(@RequestBody List<AiUsageTypeDto> dtos) throws Exception {
        ResponseEntity<List<AiUsageTypeDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            List<AiUsageType> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified aiUsageType")
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


    @Operation(summary = "Finds a aiUsageType and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<AiUsageTypeDto> findWithAssociatedLists(@PathVariable Long id) {
        AiUsageType loaded =  service.findWithAssociatedLists(id);
        AiUsageTypeDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds aiUsageTypes by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<AiUsageTypeDto>> findByCriteria(@RequestBody AiUsageTypeCriteria criteria) throws Exception {
        ResponseEntity<List<AiUsageTypeDto>> res = null;
        List<AiUsageType> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<AiUsageTypeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated aiUsageTypes by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody AiUsageTypeCriteria criteria) throws Exception {
        List<AiUsageType> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        List<AiUsageTypeDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets aiUsageType data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody AiUsageTypeCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<AiUsageTypeDto> findDtos(List<AiUsageType> list){
        List<AiUsageTypeDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<AiUsageTypeDto> getDtoResponseEntity(AiUsageTypeDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public AiUsageTypeRestCollaborator(AiUsageTypeCollaboratorService service, AiUsageTypeConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final AiUsageTypeCollaboratorService service;
    private final AiUsageTypeConverter converter;





}
