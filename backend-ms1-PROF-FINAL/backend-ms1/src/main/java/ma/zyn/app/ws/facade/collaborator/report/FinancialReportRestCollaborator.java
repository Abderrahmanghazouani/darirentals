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

import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.dao.criteria.core.report.FinancialReportCriteria;
import ma.zyn.app.service.facade.collaborator.report.FinancialReportCollaboratorService;
import ma.zyn.app.ws.converter.report.FinancialReportConverter;
import ma.zyn.app.ws.dto.report.FinancialReportDto;
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
@RequestMapping("/api/collaborator/financialReport/")
public class FinancialReportRestCollaborator {




    @Operation(summary = "Finds a list of all financialReports")
    @GetMapping("")
    public ResponseEntity<List<FinancialReportDto>> findAll() throws Exception {
        ResponseEntity<List<FinancialReportDto>> res = null;
        List<FinancialReport> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
            converter.initObject(true);
        List<FinancialReportDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }


    @Operation(summary = "Finds a financialReport by id")
    @GetMapping("id/{id}")
    public ResponseEntity<FinancialReportDto> findById(@PathVariable Long id) {
        FinancialReport t = service.findById(id);
        if (t != null) {
            converter.init(true);
            FinancialReportDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @Operation(summary = "Saves the specified  financialReport")
    @PostMapping("")
    public ResponseEntity<FinancialReportDto> save(@RequestBody FinancialReportDto dto) throws Exception {
        if(dto!=null){
            converter.init(true);
            FinancialReport myT = converter.toItem(dto);
            FinancialReport t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                FinancialReportDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  financialReport")
    @PutMapping("")
    public ResponseEntity<FinancialReportDto> update(@RequestBody FinancialReportDto dto) throws Exception {
        ResponseEntity<FinancialReportDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            FinancialReport t = service.findById(dto.getId());
            converter.copy(dto,t);
            FinancialReport updated = service.update(t);
            FinancialReportDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of financialReport")
    @PostMapping("multiple")
    public ResponseEntity<List<FinancialReportDto>> delete(@RequestBody List<FinancialReportDto> dtos) throws Exception {
        ResponseEntity<List<FinancialReportDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<FinancialReport> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified financialReport")
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


    @Operation(summary = "Finds a financialReport and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<FinancialReportDto> findWithAssociatedLists(@PathVariable Long id) {
        FinancialReport loaded =  service.findWithAssociatedLists(id);
        converter.init(true);
        FinancialReportDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds financialReports by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<FinancialReportDto>> findByCriteria(@RequestBody FinancialReportCriteria criteria) throws Exception {
        ResponseEntity<List<FinancialReportDto>> res = null;
        List<FinancialReport> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        converter.initObject(true);
        List<FinancialReportDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated financialReports by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody FinancialReportCriteria criteria) throws Exception {
        List<FinancialReport> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initList(false);
        converter.initObject(true);
        List<FinancialReportDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets financialReport data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody FinancialReportCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<FinancialReportDto> findDtos(List<FinancialReport> list){
        converter.initList(false);
        converter.initObject(true);
        List<FinancialReportDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<FinancialReportDto> getDtoResponseEntity(FinancialReportDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public FinancialReportRestCollaborator(FinancialReportCollaboratorService service, FinancialReportConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final FinancialReportCollaboratorService service;
    private final FinancialReportConverter converter;





}
