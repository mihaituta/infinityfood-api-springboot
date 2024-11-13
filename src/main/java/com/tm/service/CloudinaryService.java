package com.tm.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String folder, String customName) throws IOException {
        // Generate a custom public ID for the image
        String publicId = folder + "/" + customName;

        // Upload the image with the custom public ID
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "public_id", publicId
        ));

        // Return the public URL of the uploaded image
        return (String) uploadResult.get("public_id");
    }
}