package net.bigmir.services;

import net.bigmir.model.Document;
import net.bigmir.model.StartUp;
import net.bigmir.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Transactional
    public void saveDoc(Document document){
        documentRepository.save(document);
    }

    @Transactional
    public List<Document> getDocumentsFromStartUp(StartUp startUp){
        return documentRepository.getDocumentsFromStartUp(startUp);
    }

    @Transactional
    public void deleteDocument(String path){
        documentRepository.deleteDocument(path);
    }
}
