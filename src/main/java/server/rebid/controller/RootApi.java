package server.rebid.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import server.rebid.common.CommonResponse;
import server.rebid.common.ImageService;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class RootApi {

    private final ImageService imageService;

    @GetMapping("/health")
    public String healthCheck(){
        return "I'm healthy!";
    }

    @GetMapping("/hello")
    public CommonResponse hello() {
        return CommonResponse.onSuccess("Hello, world!");
    }

    @PostMapping("/imageTest")
    public CommonResponse imageTest(
            @RequestPart MultipartFile image
            ) throws IOException {
        return CommonResponse.onSuccess(imageService.uploadImage(image));
    }

}
