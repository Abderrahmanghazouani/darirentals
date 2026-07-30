package  ma.zyn.app.ws.facade.admin.charge;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.charge.ChargeType;
import ma.zyn.app.dao.criteria.core.charge.ChargeTypeCriteria;
import ma.zyn.app.service.facade.admin.charge.ChargeTypeAdminService;
import ma.zyn.app.ws.converter.charge.ChargeTypeConverter;
import ma.zyn.app.ws.dto.charge.ChargeTypeDto;
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
@RequestMapping("/api/admin/chargeType/")
public class ChargeTypeRestAdmin {




    @Operation(summary = "Finds a list of all chargeTypes")
    @GetMapping("")
    public ResponseEntity<List<ChargeTypeDto>> findAll() throws Exception {
        ResponseEntity<List<ChargeTypeDto>> res = null;
        List<ChargeType> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<ChargeTypeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all chargeTypes")
    @GetMapping("optimized")
    public ResponseEntity<List<ChargeTypeDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<ChargeTypeDto>> res = null;
        List<ChargeType> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<ChargeTypeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a chargeType by id")
    @GetMapping("id/{id}")
    public ResponseEntity<ChargeTypeDto> findById(@PathVariable Long id) {
        ChargeType t = service.findById(id);
        if (t != null) {
            ChargeTypeDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a chargeType by label")
    @GetMapping("label/{label}")
    public ResponseEntity<ChargeTypeDto> findByLabel(@PathVariable String label) {
	    ChargeType t = service.findByReferenceEntity(new ChargeType(label));
        if (t != null) {
            ChargeTypeDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  chargeType")
    @PostMapping("")
    public ResponseEntity<ChargeTypeDto> save(@RequestBody ChargeTypeDto dto) throws Exception {
        if(dto!=null){
            ChargeType myT = converter.toItem(dto);
            ChargeType t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                ChargeTypeDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  chargeType")
    @PutMapping("")
    public ResponseEntity<ChargeTypeDto> update(@RequestBody ChargeTypeDto dto) throws Exception {
        ResponseEntity<ChargeTypeDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            ChargeType t = service.findById(dto.getId());
            converter.copy(dto,t);
            ChargeType updated = service.update(t);
            ChargeTypeDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of chargeType")
    @PostMapping("multiple")
    public ResponseEntity<List<ChargeTypeDto>> delete(@RequestBody List<ChargeTypeDto> dtos) throws Exception {
        ResponseEntity<List<ChargeTypeDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            List<ChargeType> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified chargeType")
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


    @Operation(summary = "Finds a chargeType and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<ChargeTypeDto> findWithAssociatedLists(@PathVariable Long id) {
        ChargeType loaded =  service.findWithAssociatedLists(id);
        ChargeTypeDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds chargeTypes by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<ChargeTypeDto>> findByCriteria(@RequestBody ChargeTypeCriteria criteria) throws Exception {
        ResponseEntity<List<ChargeTypeDto>> res = null;
        List<ChargeType> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<ChargeTypeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated chargeTypes by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody ChargeTypeCriteria criteria) throws Exception {
        List<ChargeType> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        List<ChargeTypeDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets chargeType data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody ChargeTypeCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<ChargeTypeDto> findDtos(List<ChargeType> list){
        List<ChargeTypeDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<ChargeTypeDto> getDtoResponseEntity(ChargeTypeDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public ChargeTypeRestAdmin(ChargeTypeAdminService service, ChargeTypeConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final ChargeTypeAdminService service;
    private final ChargeTypeConverter converter;





}
