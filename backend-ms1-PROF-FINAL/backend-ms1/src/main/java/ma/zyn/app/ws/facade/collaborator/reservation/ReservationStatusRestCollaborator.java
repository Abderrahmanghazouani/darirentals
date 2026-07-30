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

import ma.zyn.app.bean.core.reservation.ReservationStatus;
import ma.zyn.app.dao.criteria.core.reservation.ReservationStatusCriteria;
import ma.zyn.app.service.facade.collaborator.reservation.ReservationStatusCollaboratorService;
import ma.zyn.app.ws.converter.reservation.ReservationStatusConverter;
import ma.zyn.app.ws.dto.reservation.ReservationStatusDto;
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
@RequestMapping("/api/collaborator/reservationStatus/")
public class ReservationStatusRestCollaborator {




    @Operation(summary = "Finds a list of all reservationStatuss")
    @GetMapping("")
    public ResponseEntity<List<ReservationStatusDto>> findAll() throws Exception {
        ResponseEntity<List<ReservationStatusDto>> res = null;
        List<ReservationStatus> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<ReservationStatusDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all reservationStatuss")
    @GetMapping("optimized")
    public ResponseEntity<List<ReservationStatusDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<ReservationStatusDto>> res = null;
        List<ReservationStatus> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<ReservationStatusDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a reservationStatus by id")
    @GetMapping("id/{id}")
    public ResponseEntity<ReservationStatusDto> findById(@PathVariable Long id) {
        ReservationStatus t = service.findById(id);
        if (t != null) {
            ReservationStatusDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a reservationStatus by label")
    @GetMapping("label/{label}")
    public ResponseEntity<ReservationStatusDto> findByLabel(@PathVariable String label) {
	    ReservationStatus t = service.findByReferenceEntity(new ReservationStatus(label));
        if (t != null) {
            ReservationStatusDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  reservationStatus")
    @PostMapping("")
    public ResponseEntity<ReservationStatusDto> save(@RequestBody ReservationStatusDto dto) throws Exception {
        if(dto!=null){
            ReservationStatus myT = converter.toItem(dto);
            ReservationStatus t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                ReservationStatusDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  reservationStatus")
    @PutMapping("")
    public ResponseEntity<ReservationStatusDto> update(@RequestBody ReservationStatusDto dto) throws Exception {
        ResponseEntity<ReservationStatusDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            ReservationStatus t = service.findById(dto.getId());
            converter.copy(dto,t);
            ReservationStatus updated = service.update(t);
            ReservationStatusDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of reservationStatus")
    @PostMapping("multiple")
    public ResponseEntity<List<ReservationStatusDto>> delete(@RequestBody List<ReservationStatusDto> dtos) throws Exception {
        ResponseEntity<List<ReservationStatusDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            List<ReservationStatus> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified reservationStatus")
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


    @Operation(summary = "Finds a reservationStatus and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<ReservationStatusDto> findWithAssociatedLists(@PathVariable Long id) {
        ReservationStatus loaded =  service.findWithAssociatedLists(id);
        ReservationStatusDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds reservationStatuss by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<ReservationStatusDto>> findByCriteria(@RequestBody ReservationStatusCriteria criteria) throws Exception {
        ResponseEntity<List<ReservationStatusDto>> res = null;
        List<ReservationStatus> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        List<ReservationStatusDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated reservationStatuss by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody ReservationStatusCriteria criteria) throws Exception {
        List<ReservationStatus> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        List<ReservationStatusDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets reservationStatus data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody ReservationStatusCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<ReservationStatusDto> findDtos(List<ReservationStatus> list){
        List<ReservationStatusDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<ReservationStatusDto> getDtoResponseEntity(ReservationStatusDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }






   public ReservationStatusRestCollaborator(ReservationStatusCollaboratorService service, ReservationStatusConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final ReservationStatusCollaboratorService service;
    private final ReservationStatusConverter converter;





}
