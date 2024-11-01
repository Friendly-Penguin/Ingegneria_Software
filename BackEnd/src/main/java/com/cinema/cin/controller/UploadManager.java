package com.cinema.cin.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB

@WebServlet("/upload")

public class UploadManager extends HttpServlet {

    public UploadManager() {
        super();
    }

    private static final String UPLOAD_DIR = "C:/Users/marce/IdeaProjects/Cinema/src/main/webapp/images/locandine/";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fileName = null;
        String fileExtension = null;

        // Crea il percorso della directory di upload
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Trova il numero più grande dei file esistenti
        Integer maxNumber = findMaxNumber(uploadDir);

        // Processa il file upload
        for (Part part : request.getParts()) {
            fileName = "locandina" + (++maxNumber);
            fileExtension = getFileExtension(part);
            part.write(UPLOAD_DIR + File.separator + fileName + fileExtension);
        }
        String NomeFile = fileName + fileExtension;
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(NomeFile);

    }

    private int findMaxNumber(File folder) {
        int maxNumber = 0;
        File[] files = folder.listFiles();
        if (files != null) {
            Pattern pattern = Pattern.compile("locandina(\\d+)");
            for (File file : files) {
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.find()) {
                    int number = Integer.parseInt(matcher.group(1));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                }
            }
        }
        return maxNumber;
    }

    private String getFileExtension(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                String fileName = token.substring(token.indexOf('=') + 2, token.length() - 1);
                return fileName.substring(fileName.lastIndexOf('.'));
            }
        }
        return "";
    }

}
