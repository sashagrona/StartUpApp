package net.bigmir.repositories;

import net.bigmir.model.Document;
import net.bigmir.model.StartUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("SELECT d FROM Document d WHERE d.startUp=:startup")
    List<Document> getDocumentsFromStartUp(@Param("startup")StartUp startUp);

    @Modifying
    @Query("DELETE FROM Document d WHERE d.startUp=:startUp")
    void deleteDocumentByStartUp(@Param("startUp") StartUp startUp);

    @Modifying
    @Query("DELETE FROM Document d where d.path=:path")
    void deleteDocument(@Param("path") String path);

}
