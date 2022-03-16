/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sd4.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sd4.model.Brewery;
import com.sd4.service.BreweryService;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mario
 */
@RestController
public class QRCodeRestController {

    @Autowired
    BreweryService service;

    @GetMapping(value = "brewery/QRCode/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getCode(@PathVariable("id") long id) throws WriterException, IOException {

        Optional<Brewery> brewery = service.findOne(id);

        String Vcard = "BEGIN:VCARD"
                + "\nVERSION:3.0"
                + "\nFN:" + brewery.get().getName()
                + "\nADR:" + brewery.get().getAddress1() + " " + brewery.get().getAddress2() + " " + brewery.get().getCity() + " " + brewery.get().getCountry()
                + "\nTEL:" + brewery.get().getPhone()
                + "\nEMAIL;" + brewery.get().getEmail()
                + "\nURL:" + brewery.get().getWebsite()
                + "\nEND:VCARD";

        System.out.println(Vcard);

        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(Vcard, BarcodeFormat.QR_CODE, 500, 500);

        BufferedImage code = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(code, "jpeg", baos);
        baos.flush();
        byte[] codeInBytes = baos.toByteArray();
        baos.close();

        return codeInBytes;
    }

}
