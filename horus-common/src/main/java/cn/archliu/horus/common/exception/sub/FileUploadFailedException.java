package cn.archliu.horus.common.exception.sub;

import cn.archliu.horus.common.exception.BaseException;

/**
 * @Author: Arch
 * @Date: 2022-05-21 11:59:34
 * @Description: 文件上传失败
 */
public class FileUploadFailedException extends BaseException {

    public FileUploadFailedException() {
        super("file upload failed exception !");
    }

    public FileUploadFailedException(String message) {
        super(message);
    }

    public static FileUploadFailedException throwE() {
        return new FileUploadFailedException();
    }

    public static FileUploadFailedException throwE(String message) {
        return new FileUploadFailedException(message);
    }

}
