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

import ma.zyn.app.bean.core.report.FinancialReportProperty;
import ma.zyn.app.dao.criteria.core.report.FinancialReportPropertyCriteria;
import ma.zyn.app.service.facade.admin.report.FinancialReportPropertyAdminService;
import ma.zyn.app.ws.converter.report.FinancialReportPropertyConverter;
import ma.zyn.app.ws.dto.report.FinancialReportPropertyDto;
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
@RequestMapping("/api/admin/financialReportProperty/")
public class FinancialReportPropertyRestAdmin {




    @Operation(summary = "Finds a list of all financialReportPropertys")
    @GetMapping("")
    public ResponseEntity<List<FinancialReportPropertyDto>> findAll() throws Exception {
        ResponseEntity<List<FinancialReportPropertyDto>> res = null;
        List<FinancialReportProperty> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
            converter.initObject(true);
        List<FinancialReportPropertyDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }


    @Operation(summary = "Finds a financialReportProperty by id")
    @GetMapping("id/{id}")
    public ResponseEntity<FinancialReportPropertyDto> findById(@PathVariable Long id) {
        FinancialReportProperty t = service.findById(id);
        if (t != null) {
            converter.init(true);
            FinancialReportPropertyDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @Operation(summary = "Saves the specified  financialReportProperty")
    @PostMapping("")
    public ResponseEntity<FinancialReportPropertyDto> save(@RequestBody FinancialReportPropertyDto dto) throws Exception {
        if(dto!=null){
            converter.init(true);
            FinancialReportProperty myT = converter.toItem(dto);
            FinancialReportProperty t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                FinancialReportPropertyDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  financialReportProperty")
    @PutMapping("")
    public ResponseEntity<FinancialReportPropertyDto> update(@RequestBody FinancialReportPropertyDto dto) throws Exception {
        ResponseEntity<FinancialReportPropertyDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            FinancialReportProperty t = service.findById(dto.getId());
            converter.copy(dto,t);
            FinancialReportProperty updated = service.update(t);
            FinancialReportPropertyDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of financialReportProperty")
    @PostMapping("multiple")
    public ResponseEntity<List<FinancialReportPropertyDto>> delete(@RequestBody List<FinancialReportPropertyDto> dtos) throws Exception {
        ResponseEntity<List<FinancialReportPropertyDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<FinancialReportProperty> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified financialReportProperty")
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

    @Operation(summary = "find by financialReport id")
    @GetMapping("financialReport/id/{id}")
    public List<FinancialReportPropertyDto> findByFinancialReportId(@PathVariable Long id){
        return findDtos(service.findByFinancialReportId(id));
    }
    @Operation(summary = "delete by financialReport id")
    @DeleteMapping("financialReport/id/{id}")
    public int deleteByFinancialReportId(@PathVariable Long id){
        return service.deleteByFinancialReportId(id);
    }

    @Operation(summary = "Finds a financialReportProperty and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<FinancialReportPropertyDto> findWithAssociatedLists(@PathVariable Long id) {
        FinancialReportProperty loaded =  service.findWithAssociatedLists(id);
        converter.init(true);
        FinancialReportPropertyDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds financialReportPropertys by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<FinancialReportPropertyDto>> findByCriteria(@RequestBody FinancialReportPropertyCriteria criteria) throws Exception {
        ResponseEntity<List<FinancialReportPropertyDto>> res = null;
        List<FinancialReportProperty> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initObject(true);
        List<FinancialReportPropertyDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated financialReportPropertys by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody FinancialReportPropertyCriteria criteria) throws Exception {
        List<FinancialReportProperty> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initObject(true);
        List<FinancialReportPropertyDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets financialReportProperty data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody FinancialReportPropertyCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<FinancialReportPropertyDto> findDtos(List<FinancialReportProperty> list){
        converter.initObject(true);
        List<FinancialReportPropertyDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<FinancialReportPropertyDto> getDtoResponseEntity(FinancialReportPropertyDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public FinancialReportPropertyRestAdmin(FinancialReportPropertyAdminService service, FinancialReportPropertyConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final FinancialReportPropertyAdminService service;
    private final FinancialReportPropertyConverter converter;





}
