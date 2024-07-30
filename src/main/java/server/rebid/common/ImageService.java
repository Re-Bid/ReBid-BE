package server.rebid.common;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private  final AmazonS3Client amazonS3Client;
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        metadata.setContentDisposition("inline");
        fileName = getUuidFileName(fileName);

        // "/images" 경로를 추가하여 객체 키 설정
        String objectKey = "images/" + fileName;

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, objectKey, file.getInputStream(), metadata);

        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read); // 공개 읽기 권한 부여
        putObjectRequest.setAccessControlList(acl);

        amazonS3Client.putObject(putObjectRequest);
        return amazonS3.getUrl(bucket, objectKey).toString();
    }

    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }

//    public NaverResponseDTO.imageCheck checkImage(MultipartFile file) throws IOException {
//        String imageUrl = uploadImage(file);
//
//    }
}
