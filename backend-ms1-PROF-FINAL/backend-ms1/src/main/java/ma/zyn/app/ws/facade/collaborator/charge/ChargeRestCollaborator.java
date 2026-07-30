package  ma.zyn.app.ws.facade.collaborator.charge;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.charge.Charge;
import ma.zyn.app.dao.criteria.core.charge.ChargeCriteria;
import ma.zyn.app.service.facade.collaborator.charge.ChargeCollaboratorService;
import ma.zyn.app.ws.converter.charge.ChargeConverter;
import ma.zyn.app.ws.dto.charge.ChargeDto;
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
@RequestMapping("/api/collaborator/charge/")
public class ChargeRestCollaborator {




    @Operation(summary = "Finds a list of all charges")
    @GetMapping("")
    public ResponseEntity<List<ChargeDto>> findAll() throws Exception {
        ResponseEntity<List<ChargeDto>> res = null;
        List<Charge> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
            converter.initObject(true);
        List<ChargeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all charges")
    @GetMapping("optimized")
    public ResponseEntity<List<ChargeDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<ChargeDto>> res = null;
        List<Charge> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        converter.initObject(true);
        List<ChargeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a charge by id")
    @GetMapping("id/{id}")
    public ResponseEntity<ChargeDto> findById(@PathVariable Long id) {
        Charge t = service.findById(id);
        if (t != null) {
            converter.init(true);
            ChargeDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a charge by label")
    @GetMapping("label/{label}")
    public ResponseEntity<ChargeDto> findByLabel(@PathVariable String label) {
	    Charge t = service.findByReferenceEntity(new Charge(label));
        if (t != null) {
            converter.init(true);
            ChargeDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  charge")
    @PostMapping("")
    public ResponseEntity<ChargeDto> save(@RequestBody ChargeDto dto) throws Exception {
        if(dto!=null){
            converter.init(true);
            Charge myT = converter.toItem(dto);
            Charge t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                ChargeDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  charge")
    @PutMapping("")
    public ResponseEntity<ChargeDto> update(@RequestBody ChargeDto dto) throws Exception {
        ResponseEntity<ChargeDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            Charge t = service.findById(dto.getId());
            converter.copy(dto,t);
            Charge updated = service.update(t);
            ChargeDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of charge")
    @PostMapping("multiple")
    public ResponseEntity<List<ChargeDto>> delete(@RequestBody List<ChargeDto> dtos) throws Exception {
        ResponseEntity<List<ChargeDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<Charge> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified charge")
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

    @Operation(summary = "find by chargeType code")
    @GetMapping("chargeType/code/{code}")
    public List<ChargeDto> findByChargeTypeCode(@PathVariable String code){
        return findDtos(service.findByChargeTypeCode(code));
    }
    @Operation(summary = "delete by chargeType code")
    @DeleteMapping("chargeType/code/{code}")
    public int deleteByChargeTypeCode(@PathVariable String code){
        return service.deleteByChargeTypeCode(code);
    }
    @Operation(summary = "find by payment id")
    @GetMapping("payment/id/{id}")
    public List<ChargeDto> findByPaymentId(@PathVariable Long id){
        return findDtos(service.findByPaymentId(id));
    }
    @Operation(summary = "delete by payment id")
    @DeleteMapping("payment/id/{id}")
    public int deleteByPaymentId(@PathVariable Long id){
        return service.deleteByPaymentId(id);
    }

    @Operation(summary = "Finds a charge and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<ChargeDto> findWithAssociatedLists(@PathVariable Long id) {
        Charge loaded =  service.findWithAssociatedLists(id);
        converter.init(true);
        ChargeDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds charges by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<ChargeDto>> findByCriteria(@RequestBody ChargeCriteria criteria) throws Exception {
        ResponseEntity<List<ChargeDto>> res = null;
        List<Charge> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        converter.initObject(true);
        List<ChargeDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated charges by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody ChargeCriteria criteria) throws Exception {
        List<Charge> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initList(false);
        converter.initObject(true);
        List<ChargeDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets charge data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody ChargeCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<ChargeDto> findDtos(List<Charge> list){
        converter.initList(false);
        converter.initObject(true);
        List<ChargeDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<ChargeDto> getDtoResponseEntity(ChargeDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public ChargeRestCollaborator(ChargeCollaboratorService service, ChargeConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final ChargeCollaboratorService service;
    private final ChargeConverter converter;





}
