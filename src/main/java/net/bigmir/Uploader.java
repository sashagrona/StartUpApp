package net.bigmir;

import net.bigmir.model.Document;
import net.bigmir.model.StartUp;
import net.bigmir.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Map;

public class Uploader implements Runnable {

    @Autowired
    private DocumentService docService;

    private MultipartFile[] documents;
    private File directory;
    private StartUp startUp;
    private Map<String, byte[]> map;


    public Uploader(MultipartFile[] documents, File directory, StartUp startUp, Map<String, byte[]> map, DocumentService docService) {
        this.documents = documents;
        this.directory = directory;
        this.startUp = startUp;
        this.map = map;
        this.docService = docService;
    }

    @Override
    public void run() {

        for (MultipartFile file : documents) {
            String docName = file.getOriginalFilename();
            byte[] bytes = map.get(docName);
            String fullPath = directory.getPath() + "/" + docName;
            File doc = new File(fullPath);
            System.out.println(docName);
            System.out.println(startUp.getName());
            System.out.println(fullPath);
            try (OutputStream os = new FileOutputStream(doc)) {
                os.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            converting bytes into MB
            BigDecimal s = BigDecimal.valueOf(bytes.length).divide(BigDecimal.valueOf(1024 * 1024)).setScale(1, BigDecimal.ROUND_HALF_UP);
            Document document = new Document(docName, startUp, fullPath, s.doubleValue());
            System.out.println(document.getName());
            docService.saveDoc(document);

        }
    }
}
