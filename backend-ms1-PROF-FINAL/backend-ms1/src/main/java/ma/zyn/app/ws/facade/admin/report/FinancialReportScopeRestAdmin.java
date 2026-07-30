package  ma.zyn.app.ws.facade.admin.report;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.report.FinancialReportScope;
import ma.zyn.app.dao.criteria.core.report.FinancialReportScopeCriteria;
import ma.zyn.app.service.facade.admin.report.FinancialReportScopeAdminService;
import ma.zyn.app.ws.converter.report.FinancialReportScopeConverter;
import ma.zyn.app.ws.dto.report.FinancialReportScopeDto;
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
@RequestMapping("/api/admin/financialReportScope/")
public class FinancialReportScopeRestAdmin {




    @Operation(summary = "Finds a list of all financialReportScopes")
    @GetMapping("")
    public ResponseEntity<List<FinancialReportScopeDto>> findAll() throws Exception {
        ResponseEntity<List<FinancialReportScopeDto>> res = null;
        List<FinancialReportScope> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<FinancialReportScopeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all financialReportScopes")
    @GetMapping("optimized")
    public ResponseEntity<List<FinancialReportScopeDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<FinancialReportScopeDto>> res = null;
        List<FinancialReportScope> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<FinancialReportScopeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a financialReportScope by id")
    @GetMapping("id/{id}")
    public ResponseEntity<FinancialReportScopeDto> findById(@PathVariable Long id) {
        FinancialReportScope t = service.findById(id);
        if (t != null) {
            FinancialReportScopeDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a financialReportScope by label")
    @GetMapping("label/{label}")
    public ResponseEntity<FinancialReportScopeDto> findByLabel(@PathVariable String label) {
	    FinancialReportScope t = service.findByReferenceEntity(new FinancialReportScope(label));
        if (t != null) {
            FinancialReportScopeDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  financialReportScope")
    @PostMapping("")
    public ResponseEntity<FinancialReportScopeDto> save(@RequestBody FinancialReportScopeDto dto) throws Exception {
        if(dto!=null){
            FinancialReportScope myT = converter.toItem(dto);
            FinancialReportScope t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                FinancialReportScopeDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  financialReportScope")
    @PutMapping("")
    public ResponseEntity<FinancialReportScopeDto> update(@RequestBody FinancialReportScopeDto dto) throws Exception {
        ResponseEntity<FinancialReportScopeDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            FinancialReportScope t = service.findById(dto.getId());
            converter.copy(dto,t);
            FinancialReportScope updated = service.update(t);
            FinancialReportScopeDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of financialReportScope")
    @PostMapping("multiple")
    public ResponseEntity<List<FinancialReportScopeDto>> delete(@RequestBody List<FinancialReportScopeDto> dtos) throws Exception {
        ResponseEntity<List<FinancialReportScopeDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            List<FinancialReportScope> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified financialReportScope")
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


    @Operation(summary = "Finds a financialReportScope and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<FinancialReportScopeDto> findWithAssociatedLists(@PathVariable Long id) {
        FinancialReportScope loaded =  service.findWithAssociatedLists(id);
        FinancialReportScopeDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds financialReportScopes by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<FinancialReportScopeDto>> findByCriteria(@RequestBody FinancialReportScopeCriteria criteria) throws Exception {
        ResponseEntity<List<FinancialReportScopeDto>> res = null;
        List<FinancialReportScope> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<FinancialReportScopeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated financialReportScopes by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody FinancialReportScopeCriteria criteria) throws Exception {
        List<FinancialReportScope> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        List<FinancialReportScopeDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets financialReportScope data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody FinancialReportScopeCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<FinancialReportScopeDto> findDtos(List<FinancialReportScope> list){
        List<FinancialReportScopeDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<FinancialReportScopeDto> getDtoResponseEntity(FinancialReportScopeDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public FinancialReportScopeRestAdmin(FinancialReportScopeAdminService service, FinancialReportScopeConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final FinancialReportScopeAdminService service;
    private final FinancialReportScopeConverter converter;





}
