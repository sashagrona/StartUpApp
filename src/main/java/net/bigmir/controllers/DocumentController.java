package net.bigmir.controllers;

import com.google.gson.Gson;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import net.bigmir.GenericResponse;
import net.bigmir.Uploader;
import net.bigmir.dto.result.BadRequestResult;
import net.bigmir.dto.result.ResultDTO;
import net.bigmir.dto.result.SuccessResult;
import net.bigmir.exceptions.FileOverMaximumException;
import net.bigmir.services.DocumentService;
import net.bigmir.services.StartUpService;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.SizeLimitExceededException;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    private StartUpService startUpService;

    @Autowired
    private DocumentService documentService;

    @RequestMapping(value = "/add")
    public String addDocument(@RequestParam("documents") MultipartFile[] files, @RequestParam("sName") String startUpName) throws FileUploadBase.SizeLimitExceededException {

        String path = "Documents/" + startUpName;
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Map<String, byte[]> map = new HashMap<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()){
                return "redirect:/startup/" + startUpName + "/document";
            }
            if (file.getSize()>50*1024*1024){
                throw new FileOverMaximumException("You can upload file with the size less than 100 MB only");
            }
            try {
                byte[] bytes = file.getBytes();
                map.put(file.getOriginalFilename(), bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Uploader uploader = new Uploader(files, directory, startUpService.findByName(startUpName), map, documentService);
        Thread thread = new Thread(uploader);
        thread.start();

        return "redirect:/startup/" + startUpName + "/document";
    }

    @RequestMapping(value = "/download")
    public ResponseEntity<byte[]> getDocument(Model model, @RequestParam("sName") String startUpName, @RequestParam("name") String fileName) {
        File doc = new File("Documents/" + startUpName + "/" + fileName);
        try (OutputStream os = new ByteOutputStream()) {
//            for cyrillic needs to replace '+' by ' '
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            byte[] buff = Files.readAllBytes(doc.toPath());
            os.write(buff);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Content-disposition", "attachment; filename=" + fileName);
            return new ResponseEntity<>(((ByteOutputStream) os).getBytes(), httpHeaders, HttpStatus.OK);
        } catch (IOException o) {
            o.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/delete")
    public ResponseEntity<ResultDTO> deleteDocument(@RequestParam("json") String json){

        Gson gson = new Gson();
        String path = gson.fromJson(json, String.class);
        System.out.println(path);
        File file = new File(path);
        System.out.println(file.delete());
        documentService.deleteDocument(path);
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }

    @ExceptionHandler({FileOverMaximumException.class})
    public String handleException(FileOverMaximumException exception, Model model){
        model.addAttribute("fileUpload", exception.getMessage());
        return "error";
    }

}
