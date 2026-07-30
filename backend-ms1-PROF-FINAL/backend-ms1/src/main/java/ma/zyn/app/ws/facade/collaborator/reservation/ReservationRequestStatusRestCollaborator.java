package  ma.zyn.app.ws.facade.collaborator.reservation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.reservation.ReservationRequestStatus;
import ma.zyn.app.dao.criteria.core.reservation.ReservationRequestStatusCriteria;
import ma.zyn.app.service.facade.collaborator.reservation.ReservationRequestStatusCollaboratorService;
import ma.zyn.app.ws.converter.reservation.ReservationRequestStatusConverter;
import ma.zyn.app.ws.dto.reservation.ReservationRequestStatusDto;
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
@RequestMapping("/api/collaborator/reservationRequestStatus/")
public class ReservationRequestStatusRestCollaborator {




    @Operation(summary = "Finds a list of all reservationRequestStatuss")
    @GetMapping("")
    public ResponseEntity<List<ReservationRequestStatusDto>> findAll() throws Exception {
        ResponseEntity<List<ReservationRequestStatusDto>> res = null;
        List<ReservationRequestStatus> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<ReservationRequestStatusDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all reservationRequestStatuss")
    @GetMapping("optimized")
    public ResponseEntity<List<ReservationRequestStatusDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<ReservationRequestStatusDto>> res = null;
        List<ReservationRequestStatus> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<ReservationRequestStatusDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a reservationRequestStatus by id")
    @GetMapping("id/{id}")
    public ResponseEntity<ReservationRequestStatusDto> findById(@PathVariable Long id) {
        ReservationRequestStatus t = service.findById(id);
        if (t != null) {
            ReservationRequestStatusDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a reservationRequestStatus by label")
    @GetMapping("label/{label}")
    public ResponseEntity<ReservationRequestStatusDto> findByLabel(@PathVariable String label) {
	    ReservationRequestStatus t = service.findByReferenceEntity(new ReservationRequestStatus(label));
        if (t != null) {
            ReservationRequestStatusDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  reservationRequestStatus")
    @PostMapping("")
    public ResponseEntity<ReservationRequestStatusDto> save(@RequestBody ReservationRequestStatusDto dto) throws Exception {
        if(dto!=null){
            ReservationRequestStatus myT = converter.toItem(dto);
            ReservationRequestStatus t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                ReservationRequestStatusDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  reservationRequestStatus")
    @PutMapping("")
    public ResponseEntity<ReservationRequestStatusDto> update(@RequestBody ReservationRequestStatusDto dto) throws Exception {
        ResponseEntity<ReservationRequestStatusDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            ReservationRequestStatus t = service.findById(dto.getId());
            converter.copy(dto,t);
            ReservationRequestStatus updated = service.update(t);
            ReservationRequestStatusDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of reservationRequestStatus")
    @PostMapping("multiple")
    public ResponseEntity<List<ReservationRequestStatusDto>> delete(@RequestBody List<ReservationRequestStatusDto> dtos) throws Exception {
        ResponseEntity<List<ReservationRequestStatusDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            List<ReservationRequestStatus> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified reservationRequestStatus")
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


    @Operation(summary = "Finds a reservationRequestStatus and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<ReservationRequestStatusDto> findWithAssociatedLists(@PathVariable Long id) {
        ReservationRequestStatus loaded =  service.findWithAssociatedLists(id);
        ReservationRequestStatusDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds reservationRequestStatuss by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<ReservationRequestStatusDto>> findByCriteria(@RequestBody ReservationRequestStatusCriteria criteria) throws Exception {
        ResponseEntity<List<ReservationRequestStatusDto>> res = null;
        List<ReservationRequestStatus> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<ReservationRequestStatusDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated reservationRequestStatuss by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody ReservationRequestStatusCriteria criteria) throws Exception {
        List<ReservationRequestStatus> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        List<ReservationRequestStatusDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets reservationRequestStatus data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody ReservationRequestStatusCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<ReservationRequestStatusDto> findDtos(List<ReservationRequestStatus> list){
        List<ReservationRequestStatusDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<ReservationRequestStatusDto> getDtoResponseEntity(ReservationRequestStatusDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public ReservationRequestStatusRestCollaborator(ReservationRequestStatusCollaboratorService service, ReservationRequestStatusConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final ReservationRequestStatusCollaboratorService service;
    private final ReservationRequestStatusConverter converter;





}
