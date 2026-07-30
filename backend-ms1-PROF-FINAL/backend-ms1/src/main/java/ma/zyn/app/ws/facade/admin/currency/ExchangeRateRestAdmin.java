package  ma.zyn.app.ws.facade.admin.currency;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.currency.ExchangeRate;
import ma.zyn.app.dao.criteria.core.currency.ExchangeRateCriteria;
import ma.zyn.app.service.facade.admin.currency.ExchangeRateAdminService;
import ma.zyn.app.ws.converter.currency.ExchangeRateConverter;
import ma.zyn.app.ws.dto.currency.ExchangeRateDto;
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
@RequestMapping("/api/admin/exchangeRate/")
public class ExchangeRateRestAdmin {




    @Operation(summary = "Finds a list of all exchangeRates")
    @GetMapping("")
    public ResponseEntity<List<ExchangeRateDto>> findAll() throws Exception {
        ResponseEntity<List<ExchangeRateDto>> res = null;
        List<ExchangeRate> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
            converter.initObject(true);
        List<ExchangeRateDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }


    @Operation(summary = "Finds a exchangeRate by id")
    @GetMapping("id/{id}")
    public ResponseEntity<ExchangeRateDto> findById(@PathVariable Long id) {
        ExchangeRate t = service.findById(id);
        if (t != null) {
            converter.init(true);
            ExchangeRateDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @Operation(summary = "Saves the specified  exchangeRate")
    @PostMapping("")
    public ResponseEntity<ExchangeRateDto> save(@RequestBody ExchangeRateDto dto) throws Exception {
        if(dto!=null){
            converter.init(true);
            ExchangeRate myT = converter.toItem(dto);
            ExchangeRate t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                ExchangeRateDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  exchangeRate")
    @PutMapping("")
    public ResponseEntity<ExchangeRateDto> update(@RequestBody ExchangeRateDto dto) throws Exception {
        ResponseEntity<ExchangeRateDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            ExchangeRate t = service.findById(dto.getId());
            converter.copy(dto,t);
            ExchangeRate updated = service.update(t);
            ExchangeRateDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of exchangeRate")
    @PostMapping("multiple")
    public ResponseEntity<List<ExchangeRateDto>> delete(@RequestBody List<ExchangeRateDto> dtos) throws Exception {
        ResponseEntity<List<ExchangeRateDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<ExchangeRate> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified exchangeRate")
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


    @Operation(summary = "Finds a exchangeRate and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<ExchangeRateDto> findWithAssociatedLists(@PathVariable Long id) {
        ExchangeRate loaded =  service.findWithAssociatedLists(id);
        converter.init(true);
        ExchangeRateDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds exchangeRates by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<ExchangeRateDto>> findByCriteria(@RequestBody ExchangeRateCriteria criteria) throws Exception {
        ResponseEntity<List<ExchangeRateDto>> res = null;
        List<ExchangeRate> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initObject(true);
        List<ExchangeRateDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated exchangeRates by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody ExchangeRateCriteria criteria) throws Exception {
        List<ExchangeRate> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initObject(true);
        List<ExchangeRateDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets exchangeRate data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody ExchangeRateCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<ExchangeRateDto> findDtos(List<ExchangeRate> list){
        converter.initObject(true);
        List<ExchangeRateDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<ExchangeRateDto> getDtoResponseEntity(ExchangeRateDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public ExchangeRateRestAdmin(ExchangeRateAdminService service, ExchangeRateConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final ExchangeRateAdminService service;
    private final ExchangeRateConverter converter;





}
