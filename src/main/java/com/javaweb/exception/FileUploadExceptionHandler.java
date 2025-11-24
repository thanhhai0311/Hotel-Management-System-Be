package com.javaweb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class FileUploadExceptionHandler {

    // 1. Bắt lỗi File quá lớn (từ config application.properties)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "File quá lớn!");
        response.put("message", "Vui lòng tải lên ảnh có dung lượng nhỏ hơn 10MB."); // Sửa theo config của bạn

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response); // Trả về code 413
    }

    // 2. Bắt lỗi logic từ Service (Sai định dạng file, file rỗng...)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleBadFileException(IllegalArgumentException exc) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Lỗi upload file");
        response.put("message", exc.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // Trả về code 400
    }
}