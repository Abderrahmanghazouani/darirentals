package  ma.zyn.app.ws.facade.admin.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.payment.PaymentStatus;
import ma.zyn.app.dao.criteria.core.payment.PaymentStatusCriteria;
import ma.zyn.app.service.facade.admin.payment.PaymentStatusAdminService;
import ma.zyn.app.ws.converter.payment.PaymentStatusConverter;
import ma.zyn.app.ws.dto.payment.PaymentStatusDto;
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
@RequestMapping("/api/admin/paymentStatus/")
public class PaymentStatusRestAdmin {




    @Operation(summary = "Finds a list of all paymentStatuss")
    @GetMapping("")
    public ResponseEntity<List<PaymentStatusDto>> findAll() throws Exception {
        ResponseEntity<List<PaymentStatusDto>> res = null;
        List<PaymentStatus> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<PaymentStatusDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all paymentStatuss")
    @GetMapping("optimized")
    public ResponseEntity<List<PaymentStatusDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<PaymentStatusDto>> res = null;
        List<PaymentStatus> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<PaymentStatusDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a paymentStatus by id")
    @GetMapping("id/{id}")
    public ResponseEntity<PaymentStatusDto> findById(@PathVariable Long id) {
        PaymentStatus t = service.findById(id);
        if (t != null) {
            PaymentStatusDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a paymentStatus by label")
    @GetMapping("label/{label}")
    public ResponseEntity<PaymentStatusDto> findByLabel(@PathVariable String label) {
	    PaymentStatus t = service.findByReferenceEntity(new PaymentStatus(label));
        if (t != null) {
            PaymentStatusDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  paymentStatus")
    @PostMapping("")
    public ResponseEntity<PaymentStatusDto> save(@RequestBody PaymentStatusDto dto) throws Exception {
        if(dto!=null){
            PaymentStatus myT = converter.toItem(dto);
            PaymentStatus t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                PaymentStatusDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  paymentStatus")
    @PutMapping("")
    public ResponseEntity<PaymentStatusDto> update(@RequestBody PaymentStatusDto dto) throws Exception {
        ResponseEntity<PaymentStatusDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            PaymentStatus t = service.findById(dto.getId());
            converter.copy(dto,t);
            PaymentStatus updated = service.update(t);
            PaymentStatusDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of paymentStatus")
    @PostMapping("multiple")
    public ResponseEntity<List<PaymentStatusDto>> delete(@RequestBody List<PaymentStatusDto> dtos) throws Exception {
        ResponseEntity<List<PaymentStatusDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            List<PaymentStatus> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified paymentStatus")
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


    @Operation(summary = "Finds a paymentStatus and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<PaymentStatusDto> findWithAssociatedLists(@PathVariable Long id) {
        PaymentStatus loaded =  service.findWithAssociatedLists(id);
        PaymentStatusDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds paymentStatuss by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<PaymentStatusDto>> findByCriteria(@RequestBody PaymentStatusCriteria criteria) throws Exception {
        ResponseEntity<List<PaymentStatusDto>> res = null;
        List<PaymentStatus> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<PaymentStatusDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated paymentStatuss by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody PaymentStatusCriteria criteria) throws Exception {
        List<PaymentStatus> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        List<PaymentStatusDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets paymentStatus data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody PaymentStatusCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<PaymentStatusDto> findDtos(List<PaymentStatus> list){
        List<PaymentStatusDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<PaymentStatusDto> getDtoResponseEntity(PaymentStatusDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public PaymentStatusRestAdmin(PaymentStatusAdminService service, PaymentStatusConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final PaymentStatusAdminService service;
    private final PaymentStatusConverter converter;





}
