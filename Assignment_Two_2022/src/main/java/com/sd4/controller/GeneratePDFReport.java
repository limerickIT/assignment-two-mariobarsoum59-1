/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sd4.controller;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.log.LoggerFactory;
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
import java.util.Optional;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.IOException;
import java.net.MalformedURLException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Mario
 */
public class GeneratePDFReport {

    private static final com.itextpdf.text.log.Logger logger = LoggerFactory.getLogger(GeneratePDFReport.class);

    @Autowired
    private BeerService beerService;
    @Autowired
    private StyleService styleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BreweryService breweryService;

    public static ByteArrayInputStream Report(long id, Optional<Beer> beer, Optional<Brewery> brewery, Optional<Category> category, Optional<Style> style)
            throws MalformedURLException, IOException {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 24, Font.BOLD);
            Font HeadingFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            Font NormalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12);

            Paragraph title = new Paragraph(beer.get().getName(), boldFont);

            LineSeparator ls = new LineSeparator();

            Paragraph Abv = new Paragraph("ABV ", HeadingFont);
            Chunk ChunkABV = new Chunk(beer.get().getAbv() + "", NormalFont);

            Paragraph Description = new Paragraph("\nDescription:", HeadingFont);
            Chunk ChunkDesc = new Chunk(beer.get().getDescription(), NormalFont);
            
            double sellingPrice = Math.round(beer.get().getSell_price().doubleValue() * 100.0) / 100.0;;

            Paragraph SellPrice = new Paragraph("\nSell Price:", HeadingFont);
            Chunk ChunkSellPrice = new Chunk(sellingPrice + "", NormalFont);

            Paragraph BreweryName = new Paragraph("\nBreweryName:", HeadingFont);
            Chunk ChunkBrewName = new Chunk(brewery.get().getName(), NormalFont);

            Paragraph BreweryWebsite = new Paragraph("\nBrewery Website:", HeadingFont);
            Chunk ChunkWebsite = new Chunk(brewery.get().getWebsite(), NormalFont);

            Paragraph BeerCategory = new Paragraph("\nBeer Category:", HeadingFont);
            Chunk ChunkCat = new Chunk(category.get().getCat_name(), NormalFont);

            Paragraph StyleName = new Paragraph("\nStyle:", HeadingFont);
            Chunk ChunkStyle = new Chunk(style.get().getStyle_name(), NormalFont);

            String filename = "src\\main\\resources\\static\\assets\\images\\large\\" + beer.get().getImage();
            Image image = Image.getInstance(filename);
            image.scaleAbsolute(200, 300);
            image.setAlignment(Element.ALIGN_RIGHT);

            PdfWriter.getInstance(document, out);
            document.open();

            document.add(title);
            document.add(new Chunk(ls));

            document.add(Abv);
            document.add(ChunkABV);

            document.add(Description);
            document.add(ChunkDesc);

            document.add(SellPrice);
            document.add(ChunkSellPrice);

            document.add(BreweryName);
            document.add(ChunkBrewName);

            document.add(BreweryWebsite);
            document.add(ChunkWebsite);

            document.add(BeerCategory);
            document.add(ChunkCat);

            document.add(StyleName);
            document.add(ChunkStyle);
            document.add(image);
            document.close();

        } catch (DocumentException ex) {

            logger.error("Error occurred: {0}", ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
