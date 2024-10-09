package com.calypso.binar.service.validation.casevalidation;

import com.calypso.binar.model.dto.AttachedFilesDTO;
import com.calypso.binar.service.exception.FileTooLargeException;
import org.springframework.stereotype.Component;

@Component
public class FileValidator {
    private static final long MAX_FILE_SIZE = 5 * 1024L * 1024; // 5MB in bytes

    public void isFileSizeValid(AttachedFilesDTO attachedFileDTO) throws FileTooLargeException {
        // Check if fileData is less than or equal to the maximum size
        if(attachedFileDTO.getFileData().length > MAX_FILE_SIZE){
            throw new FileTooLargeException();
        }
    }
}
