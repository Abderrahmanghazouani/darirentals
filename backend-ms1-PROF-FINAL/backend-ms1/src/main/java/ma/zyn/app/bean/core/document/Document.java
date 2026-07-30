package ma.zyn.app.bean.core.document;






import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.bean.core.charge.Charge;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;
import java.math.BigDecimal;

@Entity
@Table(name = "document")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="document_seq",sequenceName="document_seq",allocationSize=1, initialValue = 1)
public class Document  extends BaseEntity     {




    @Column(length = 500)
    private String fileName;

    @Column(length = 500)
    private String file;

    @Column(length = 500)
    private String extractedVendor;

    private BigDecimal extractedAmount = BigDecimal.ZERO;

    private DocumentType documentType ;
    private Reservation reservation ;
    private Charge charge ;


    public Document(){
        super();
    }

    public Document(Long id){
        this.id = id;
    }

    public Document(Long id,String fileName){
        this.id = id;
        this.fileName = fileName ;
    }
    public Document(String fileName){
        this.fileName = fileName ;
    }




    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="document_seq")
      @Override
    public Long getId(){
        return this.id;
    }
        @Override
    public void setId(Long id){
        this.id = id;
    }
    public String getFileName(){
        return this.fileName;
    }
    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    public String getFile(){
        return this.file;
    }
    public void setFile(String file){
        this.file = file;
    }
    public String getExtractedVendor(){
        return this.extractedVendor;
    }
    public void setExtractedVendor(String extractedVendor){
        this.extractedVendor = extractedVendor;
    }
    public BigDecimal getExtractedAmount(){
        return this.extractedAmount;
    }
    public void setExtractedAmount(BigDecimal extractedAmount){
        this.extractedAmount = extractedAmount;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_type")
    public DocumentType getDocumentType(){
        return this.documentType;
    }
    public void setDocumentType(DocumentType documentType){
        this.documentType = documentType;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation")
    public Reservation getReservation(){
        return this.reservation;
    }
    public void setReservation(Reservation reservation){
        this.reservation = reservation;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge")
    public Charge getCharge(){
        return this.charge;
    }
    public void setCharge(Charge charge){
        this.charge = charge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return id != null && id.equals(document.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

