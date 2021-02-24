package vn.tpsc.it4u.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.nio.file.Path;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
// import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import io.swagger.annotations.ApiOperation;

import vn.tpsc.it4u.util.ApiResponseUtils;

@RestController
@RequestMapping("${app.api.version}")
public class UploadController {


    @Autowired
    ApiResponseUtils apiResponse;

    @Value("${app.ubnt.upload.path}")
    private String path;

    @Value("${app.ubnt.upload.image-path}")
    private String imagePath;

    @Value("${app.ubnt.upload.mobile-name}")
    private String mobileName;

    @Value("${app.ubnt.upload.desktop-name}")
    private String desktopName;

    @Value("${app.ubnt.upload.index-file-path}")
    private String indexFilePath;

    @Value("${app.ubnt.upload.home-file-path}")
    private String homeFilePath;

    @ApiOperation(value = "Get images")
    @GetMapping("/images/{id}/{typePreview}/{name}")
    public void hotspotImage(@PathVariable(value = "id") String id, @PathVariable(value = "typePreview") String typePreview, HttpServletResponse response)
            throws IOException {
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        if (typePreview.equals("mobile")) {
            File file = new File(path + id + "/" + imagePath + mobileName);
            Path path = Paths.get(file.getAbsolutePath());
            response.getOutputStream().write(Files.readAllBytes(path));
        }
        else {
            File file = new File(path + id + "/" + imagePath + desktopName);
            Path path = Paths.get(file.getAbsolutePath());
            response.getOutputStream().write(Files.readAllBytes(path));
        }
    }

    @ApiOperation(value = "Get config.properties")
    @GetMapping("/properties/{id}")
    public String getConfigProperties(@PathVariable(value = "id") String id) throws Exception {
        File file = new File(path + id + "/" + "config.properties");
        Path path = Paths.get(file.getAbsolutePath());
        String content = new String(Files.readAllBytes(path));
        JSONObject result = new JSONObject();
        result.put("data", content);
        return result.toString();
    }

    @ApiOperation(value = "Get index.html file")
    @GetMapping("/hotspot/index/{id}")
    public String getHotspotIndexFile(@PathVariable(value = "id") String id) throws Exception {
        File file = new File(path + id + indexFilePath);
        Path path = Paths.get(file.getAbsolutePath());
        String content = new String(Files.readAllBytes(path));
        JSONObject result = new JSONObject();
        result.put("data", content);
        return result.toString();
    }

    @ApiOperation(value = "Upload index.html file")
    @PostMapping("/uploadHotspot/index/{id}")
    public ResponseEntity<?> uploadHotspotIndexFile(@PathVariable(value = "id") String id, @RequestParam MultipartFile file,
            Locale locale) throws Exception {
        if (file == null) {
            throw new RuntimeException("You must select the a file for uploading");
        }
        InputStream inputStream = file.getInputStream();
        Files.delete(Paths.get(path + id + indexFilePath));
        System.out.println("Product Image Deleted !!!");
        Files.copy(inputStream, Paths.get(path + id + indexFilePath),
                StandardCopyOption.REPLACE_EXISTING);
        return ResponseEntity.ok(apiResponse.success(1001, locale));
    }

    @ApiOperation(value = "Get home.html file")
    @GetMapping("/hotspot/home/{id}")
    public String getHotspotHomeFile(@PathVariable(value = "id") String id) throws Exception {
        File file = new File(path + id + homeFilePath);
        Path path = Paths.get(file.getAbsolutePath());
        String content = new String(Files.readAllBytes(path));
        JSONObject result = new JSONObject();
        result.put("data", content);
        return result.toString();
    }

    @ApiOperation(value = "Upload home.html file")
    @PostMapping("/uploadHotspot/home/{id}")
    public ResponseEntity<?> uploadHotspotHomeFile(@PathVariable(value = "id") String id,
            @RequestParam MultipartFile file, Locale locale) throws Exception {
        if (file == null) {
            throw new RuntimeException("You must select the a file for uploading");
        }
        InputStream inputStream = file.getInputStream();
        Files.delete(Paths.get(path + id + homeFilePath));
        System.out.println("Product Image Deleted !!!");
        Files.copy(inputStream, Paths.get(path + id + homeFilePath), StandardCopyOption.REPLACE_EXISTING);
        return ResponseEntity.ok(apiResponse.success(1001, locale));
    }

    @ApiOperation(value = "Upload file")
    @PostMapping("/upload/{id}")
    public ResponseEntity<?> uploadFile(@PathVariable(value = "id") String id, @RequestParam MultipartFile file, 
            Locale locale) throws Exception {
        if (file == null) {
            throw new RuntimeException("You must select the a file for uploading");
        }
        InputStream inputStream = file.getInputStream();
        String originalName = file.getOriginalFilename();
        String name = file.getName();
        String contentType = file.getContentType();
        long size = file.getSize();
        System.out.println("inputStream: " + inputStream);
        System.out.println("originalName: " + originalName);
        System.out.println("name: " + name);
        System.out.println("contentType: " + contentType);
        System.out.println("size: " + size);
        System.out.println("path: " + System.getProperty("java.io.tmpdir"));
        Files.copy(inputStream, Paths.get(path + id + "/" + originalName), StandardCopyOption.REPLACE_EXISTING);
        return ResponseEntity.ok(apiResponse.success(1001, locale));
    }

    @ApiOperation(value = "Upload mobile image")
    @PostMapping("/upload/{id}/mobile")
    public ResponseEntity<?> uploadMobileImage(@PathVariable(value = "id") String id, @RequestParam MultipartFile file,
            Locale locale) throws Exception {
        if (file == null) {
            throw new RuntimeException("You must select the a file for uploading");
        }
        InputStream inputStream = file.getInputStream();
        Files.delete(Paths.get(path + id + "/" + imagePath + mobileName));
        System.out.println("Product Image Deleted !!!");
        Files.copy(inputStream, Paths.get(path + id + "/" + imagePath + mobileName), StandardCopyOption.REPLACE_EXISTING);
        return ResponseEntity.ok(apiResponse.success(1001, locale));
    }

    @ApiOperation(value = "Upload desktop image")
    @PostMapping("/upload/{id}/desktop")
    public ResponseEntity<?> uploadDesktopImage(@PathVariable(value = "id") String id, @RequestParam MultipartFile file, 
            Locale locale) throws Exception {
        if (file == null) {
            throw new RuntimeException("You must select the a file for uploading");
        }
        InputStream inputStream = file.getInputStream();
        Files.delete(Paths.get(path + id + "/" + imagePath + desktopName));
        System.out.println("Product Image Deleted !!!");
        Files.copy(inputStream, Paths.get(path + id + "/"+ imagePath + desktopName), StandardCopyOption.REPLACE_EXISTING);
        return ResponseEntity.ok(apiResponse.success(1001, locale));
    }

    @ApiOperation(value = "Download file")
    @GetMapping("/download/{file}")
    public ResponseEntity<Resource> download(@PathVariable(value = "file") String fileName) throws IOException {
        File file = new File(path + fileName);

        HttpHeaders header = new HttpHeaders();
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        return ResponseEntity.ok().headers(header).contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
    }
}
