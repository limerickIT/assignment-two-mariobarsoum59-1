/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sd4.controller;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import com.sd4.model.Beer;
import com.sd4.model.Brewery;
import com.sd4.model.Category;
import com.sd4.model.Style;
import com.sd4.service.BeerService;
import com.sd4.service.BreweryService;
import com.sd4.service.CategoryService;
import com.sd4.service.StyleService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Mario
 */
@RestController
public class BeerController {

    @Autowired
    private BeerService beerService;
    @Autowired
    private StyleService styleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BreweryService breweryService;

    @GetMapping(value = "beers/GetAll", produces = MediaTypes.HAL_JSON_VALUE)
    public List<Beer> getAllBeers(@RequestParam(name = "size", required = false) Integer size, @RequestParam(name = "offset", required = false) Integer offset) {
        List<Beer> beersList = beerService.findAll();

        if (size == null && offset == null) {
            size = 50;
            offset = 0;
        }

        List<Beer> paginatedList = beersList.subList(offset, offset + size);

        for (Beer b : paginatedList) {
            long beerId = b.getId();
            Link selfLink = linkTo(methodOn(BeerController.class).getOne(beerId)).withSelfRel();
            b.add(selfLink);
        }

        return paginatedList;
    }

    @GetMapping(value = "beers/GetById/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Beer> getOne(@PathVariable long id) {
        Optional<Beer> o = beerService.findOne(id);
        if (!o.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            //Link selfLink = linkTo(methodOn(BeerController.class).getOne(id)).withSelfRel();
            Link selfLink = Link.of("http://localhost:8888/beers/GetAll");
            o.get().add(selfLink);

            Link DetailsLink = Link.of("http://localhost:8888/beers/GetBeerDetailsId/" + id);
            o.get().add(DetailsLink);

            return ResponseEntity.ok(o.get());
        }
    }

    @GetMapping(value = "beers/GetBeerDetailsId/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public String getBeerDetails(@PathVariable long id) {
        Optional<Beer> o = beerService.findOne(id);
        Optional<Brewery> b = breweryService.findOne(o.get().getBrewery_id());
        String Details = "";
        if (!o.isPresent()) {
            Details = "CANNOT FIND BEER DETAILS";
        } else {
            Details = "Beer Name: " + o.get().getName() + ";Description: " + o.get().getDescription() + ";Brewery Name: " + b.get().getName();

            return Details;
        }
        return Details;
    }

    @DeleteMapping(value = "beers/Delete/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity delete(@PathVariable long id) {
        beerService.deleteByID(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/beers/Add/", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity add(@RequestBody Beer b) {
        beerService.saveBeer(b);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping(value = "/beers/Put/", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity edit(@RequestBody Beer b) { //the edit method should check if the Author object is already in the DB before attempting to save it.
        beerService.saveBeer(b);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/beers/GetImage/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@PathVariable long id, @RequestParam(name = "thumbnail", required = false) Boolean thumbnail) throws IOException {

        Optional<Beer> o = beerService.findOne(id);
        Resource resource = null;

        if (thumbnail == null || thumbnail == false) {
            resource = new ClassPathResource("static/assets/images/large/" + o.get().getImage());
        } else {
            resource = new ClassPathResource("static/assets/images/thumbs/" + o.get().getImage());
        }

        System.out.println(thumbnail);
        InputStream input = resource.getInputStream();
        return IOUtils.toByteArray(input);
    }

    @GetMapping(value = "/beers/GetImagesZipFile", produces = "application/zip")
    public void zipDownload(HttpServletResponse response) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        Resource resource = new ClassPathResource("static/assets/images/");
        InputStream input = resource.getInputStream();
        File fileToZip = resource.getFile();

        FileOutputStream fos = new FileOutputStream("Compressed.zip");
        //ZipOutputStream zipOut = new ZipOutputStream(fos);

        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ImagesZip\"");
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    @GetMapping(value = "beers/GetBeerPDF/{id}", produces = {"application/json", "text/xml"})
    public ResponseEntity<?> BeersPDF(@PathVariable long id) throws FileNotFoundException, DocumentException, IOException {

        Optional<Beer> beer = beerService.findOne(id);

        Optional<Brewery> brewery = breweryService.findOne((long) beer.get().getBrewery_id());

        Optional<Category> category = categoryService.findOne((long) beer.get().getCat_id());

        Optional<Style> style = styleService.findOne((long) beer.get().getStyle_id().longValue());

        ByteArrayInputStream bis = GeneratePDFReport.Report(1, beer, brewery, category, style);

        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=citiesreport.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }
}
