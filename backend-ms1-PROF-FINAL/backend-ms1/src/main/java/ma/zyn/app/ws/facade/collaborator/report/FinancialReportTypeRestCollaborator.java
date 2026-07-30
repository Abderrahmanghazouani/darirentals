package  ma.zyn.app.ws.facade.collaborator.report;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.report.FinancialReportType;
import ma.zyn.app.dao.criteria.core.report.FinancialReportTypeCriteria;
import ma.zyn.app.service.facade.collaborator.report.FinancialReportTypeCollaboratorService;
import ma.zyn.app.ws.converter.report.FinancialReportTypeConverter;
import ma.zyn.app.ws.dto.report.FinancialReportTypeDto;
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
@RequestMapping("/api/collaborator/financialReportType/")
public class FinancialReportTypeRestCollaborator {




    @Operation(summary = "Finds a list of all financialReportTypes")
    @GetMapping("")
    public ResponseEntity<List<FinancialReportTypeDto>> findAll() throws Exception {
        ResponseEntity<List<FinancialReportTypeDto>> res = null;
        List<FinancialReportType> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<FinancialReportTypeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all financialReportTypes")
    @GetMapping("optimized")
    public ResponseEntity<List<FinancialReportTypeDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<FinancialReportTypeDto>> res = null;
        List<FinancialReportType> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<FinancialReportTypeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a financialReportType by id")
    @GetMapping("id/{id}")
    public ResponseEntity<FinancialReportTypeDto> findById(@PathVariable Long id) {
        FinancialReportType t = service.findById(id);
        if (t != null) {
            FinancialReportTypeDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a financialReportType by label")
    @GetMapping("label/{label}")
    public ResponseEntity<FinancialReportTypeDto> findByLabel(@PathVariable String label) {
	    FinancialReportType t = service.findByReferenceEntity(new FinancialReportType(label));
        if (t != null) {
            FinancialReportTypeDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  financialReportType")
    @PostMapping("")
    public ResponseEntity<FinancialReportTypeDto> save(@RequestBody FinancialReportTypeDto dto) throws Exception {
        if(dto!=null){
            FinancialReportType myT = converter.toItem(dto);
            FinancialReportType t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                FinancialReportTypeDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  financialReportType")
    @PutMapping("")
    public ResponseEntity<FinancialReportTypeDto> update(@RequestBody FinancialReportTypeDto dto) throws Exception {
        ResponseEntity<FinancialReportTypeDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            FinancialReportType t = service.findById(dto.getId());
            converter.copy(dto,t);
            FinancialReportType updated = service.update(t);
            FinancialReportTypeDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of financialReportType")
    @PostMapping("multiple")
    public ResponseEntity<List<FinancialReportTypeDto>> delete(@RequestBody List<FinancialReportTypeDto> dtos) throws Exception {
        ResponseEntity<List<FinancialReportTypeDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            List<FinancialReportType> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified financialReportType")
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


    @Operation(summary = "Finds a financialReportType and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<FinancialReportTypeDto> findWithAssociatedLists(@PathVariable Long id) {
        FinancialReportType loaded =  service.findWithAssociatedLists(id);
        FinancialReportTypeDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds financialReportTypes by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<FinancialReportTypeDto>> findByCriteria(@RequestBody FinancialReportTypeCriteria criteria) throws Exception {
        ResponseEntity<List<FinancialReportTypeDto>> res = null;
        List<FinancialReportType> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<FinancialReportTypeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated financialReportTypes by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody FinancialReportTypeCriteria criteria) throws Exception {
        List<FinancialReportType> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        List<FinancialReportTypeDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets financialReportType data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody FinancialReportTypeCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<FinancialReportTypeDto> findDtos(List<FinancialReportType> list){
        List<FinancialReportTypeDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<FinancialReportTypeDto> getDtoResponseEntity(FinancialReportTypeDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public FinancialReportTypeRestCollaborator(FinancialReportTypeCollaboratorService service, FinancialReportTypeConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final FinancialReportTypeCollaboratorService service;
    private final FinancialReportTypeConverter converter;





}
