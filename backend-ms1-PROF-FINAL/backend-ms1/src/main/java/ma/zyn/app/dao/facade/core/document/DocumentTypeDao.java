package ma.zyn.app.dao.facade.core.document;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.document.DocumentType;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.document.DocumentType;
import java.util.List;


@Repository
public interface DocumentTypeDao extends AbstractRepository<DocumentType,Long>  {
    DocumentType findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW DocumentType(item.id,item.label) FROM DocumentType item")
    List<DocumentType> findAllOptimized();

}
