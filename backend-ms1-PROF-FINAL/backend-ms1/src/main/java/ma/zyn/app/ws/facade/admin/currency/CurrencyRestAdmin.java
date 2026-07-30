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

import ma.zyn.app.bean.core.currency.Currency;
import ma.zyn.app.dao.criteria.core.currency.CurrencyCriteria;
import ma.zyn.app.service.facade.admin.currency.CurrencyAdminService;
import ma.zyn.app.ws.converter.currency.CurrencyConverter;
import ma.zyn.app.ws.dto.currency.CurrencyDto;
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
@RequestMapping("/api/admin/currency/")
public class CurrencyRestAdmin {




    @Operation(summary = "Finds a list of all currencys")
    @GetMapping("")
    public ResponseEntity<List<CurrencyDto>> findAll() throws Exception {
        ResponseEntity<List<CurrencyDto>> res = null;
        List<Currency> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        List<CurrencyDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all currencys")
    @GetMapping("optimized")
    public ResponseEntity<List<CurrencyDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<CurrencyDto>> res = null;
        List<Currency> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        List<CurrencyDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a currency by id")
    @GetMapping("id/{id}")
    public ResponseEntity<CurrencyDto> findById(@PathVariable Long id) {
        Currency t = service.findById(id);
        if (t != null) {
            converter.init(true);
            CurrencyDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a currency by label")
    @GetMapping("label/{label}")
    public ResponseEntity<CurrencyDto> findByLabel(@PathVariable String label) {
	    Currency t = service.findByReferenceEntity(new Currency(label));
        if (t != null) {
            converter.init(true);
            CurrencyDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  currency")
    @PostMapping("")
    public ResponseEntity<CurrencyDto> save(@RequestBody CurrencyDto dto) throws Exception {
        if(dto!=null){
            converter.init(true);
            Currency myT = converter.toItem(dto);
            Currency t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                CurrencyDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  currency")
    @PutMapping("")
    public ResponseEntity<CurrencyDto> update(@RequestBody CurrencyDto dto) throws Exception {
        ResponseEntity<CurrencyDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            Currency t = service.findById(dto.getId());
            converter.copy(dto,t);
            Currency updated = service.update(t);
            CurrencyDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of currency")
    @PostMapping("multiple")
    public ResponseEntity<List<CurrencyDto>> delete(@RequestBody List<CurrencyDto> dtos) throws Exception {
        ResponseEntity<List<CurrencyDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<Currency> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified currency")
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


    @Operation(summary = "Finds a currency and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<CurrencyDto> findWithAssociatedLists(@PathVariable Long id) {
        Currency loaded =  service.findWithAssociatedLists(id);
        converter.init(true);
        CurrencyDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds currencys by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<CurrencyDto>> findByCriteria(@RequestBody CurrencyCriteria criteria) throws Exception {
        ResponseEntity<List<CurrencyDto>> res = null;
        List<Currency> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        List<CurrencyDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated currencys by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody CurrencyCriteria criteria) throws Exception {
        List<Currency> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initList(false);
        List<CurrencyDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets currency data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody CurrencyCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<CurrencyDto> findDtos(List<Currency> list){
        converter.initList(false);
        List<CurrencyDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<CurrencyDto> getDtoResponseEntity(CurrencyDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public CurrencyRestAdmin(CurrencyAdminService service, CurrencyConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final CurrencyAdminService service;
    private final CurrencyConverter converter;





}
