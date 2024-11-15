package com.tm.util;

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
        try {
            String publicId = folder + "/" + customName;
            Map<?,?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("public_id", publicId));
            return (String) uploadResult.get("public_id");
        } catch (Exception e) {
            throw new RuntimeException("Error uploading image to Cloudinary: " + e.getMessage());
        }
    }

    public void deleteImage(String publicId) {
        if(publicId != null) {
            try {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            } catch (Exception e) {
                throw new RuntimeException("Error deleting image from Cloudinary: " + e.getMessage());
            }
        }
    }

    public void deleteAssetsByPrefix(String prefix) {
        try {
            cloudinary.api().deleteResourcesByPrefix(prefix, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting assets with prefix: " + prefix, e);
        }
    }

    public void deleteFolder(String folder) {
        try {
            cloudinary.api().deleteFolder(folder, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting folder: " + folder, e);
        }
    }


}