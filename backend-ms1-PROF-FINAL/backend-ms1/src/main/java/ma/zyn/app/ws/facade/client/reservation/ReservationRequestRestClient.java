package  ma.zyn.app.ws.facade.client.reservation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.dao.criteria.core.reservation.ReservationRequestCriteria;
import ma.zyn.app.service.facade.client.reservation.ReservationRequestClientService;
import ma.zyn.app.ws.converter.reservation.ReservationRequestConverter;
import ma.zyn.app.ws.dto.reservation.ReservationRequestDto;
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
@RequestMapping("/api/client/reservationRequest/")
public class ReservationRequestRestClient {




    @Operation(summary = "Finds a list of all reservationRequests")
    @GetMapping("")
    public ResponseEntity<List<ReservationRequestDto>> findAll() throws Exception {
        ResponseEntity<List<ReservationRequestDto>> res = null;
        List<ReservationRequest> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
            converter.initObject(true);
        List<ReservationRequestDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }


    @Operation(summary = "Finds a reservationRequest by id")
    @GetMapping("id/{id}")
    public ResponseEntity<ReservationRequestDto> findById(@PathVariable Long id) {
        ReservationRequest t = service.findById(id);
        if (t != null) {
            converter.init(true);
            ReservationRequestDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @Operation(summary = "Saves the specified  reservationRequest")
    @PostMapping("")
    public ResponseEntity<ReservationRequestDto> save(@RequestBody ReservationRequestDto dto) throws Exception {
        if(dto!=null){
            converter.init(true);
            ReservationRequest myT = converter.toItem(dto);
            ReservationRequest t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                ReservationRequestDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  reservationRequest")
    @PutMapping("")
    public ResponseEntity<ReservationRequestDto> update(@RequestBody ReservationRequestDto dto) throws Exception {
        ResponseEntity<ReservationRequestDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            ReservationRequest t = service.findById(dto.getId());
            converter.copy(dto,t);
            ReservationRequest updated = service.update(t);
            ReservationRequestDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of reservationRequest")
    @PostMapping("multiple")
    public ResponseEntity<List<ReservationRequestDto>> delete(@RequestBody List<ReservationRequestDto> dtos) throws Exception {
        ResponseEntity<List<ReservationRequestDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<ReservationRequest> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified reservationRequest")
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

    @Operation(summary = "find by client id")
    @GetMapping("client/id/{id}")
    public List<ReservationRequestDto> findByClientId(@PathVariable Long id){
        return findDtos(service.findByClientId(id));
    }
    @Operation(summary = "delete by client id")
    @DeleteMapping("client/id/{id}")
    public int deleteByClientId(@PathVariable Long id){
        return service.deleteByClientId(id);
    }
    @Operation(summary = "find by reservationRequestStatus code")
    @GetMapping("reservationRequestStatus/code/{code}")
    public List<ReservationRequestDto> findByReservationRequestStatusCode(@PathVariable String code){
        return findDtos(service.findByReservationRequestStatusCode(code));
    }
    @Operation(summary = "delete by reservationRequestStatus code")
    @DeleteMapping("reservationRequestStatus/code/{code}")
    public int deleteByReservationRequestStatusCode(@PathVariable String code){
        return service.deleteByReservationRequestStatusCode(code);
    }
    @Operation(summary = "find by reservation id")
    @GetMapping("reservation/id/{id}")
    public List<ReservationRequestDto> findByReservationId(@PathVariable Long id){
        return findDtos(service.findByReservationId(id));
    }
    @Operation(summary = "delete by reservation id")
    @DeleteMapping("reservation/id/{id}")
    public int deleteByReservationId(@PathVariable Long id){
        return service.deleteByReservationId(id);
    }

    @Operation(summary = "Finds a reservationRequest and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<ReservationRequestDto> findWithAssociatedLists(@PathVariable Long id) {
        ReservationRequest loaded =  service.findWithAssociatedLists(id);
        converter.init(true);
        ReservationRequestDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds reservationRequests by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<ReservationRequestDto>> findByCriteria(@RequestBody ReservationRequestCriteria criteria) throws Exception {
        ResponseEntity<List<ReservationRequestDto>> res = null;
        List<ReservationRequest> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initObject(true);
        List<ReservationRequestDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated reservationRequests by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody ReservationRequestCriteria criteria) throws Exception {
        List<ReservationRequest> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initObject(true);
        List<ReservationRequestDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets reservationRequest data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody ReservationRequestCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<ReservationRequestDto> findDtos(List<ReservationRequest> list){
        converter.initObject(true);
        List<ReservationRequestDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<ReservationRequestDto> getDtoResponseEntity(ReservationRequestDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public ReservationRequestRestClient(ReservationRequestClientService service, ReservationRequestConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final ReservationRequestClientService service;
    private final ReservationRequestConverter converter;





}
