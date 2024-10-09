package com.calypso.binar.service.validation;

import com.calypso.binar.model.dto.AttachedFilesDTO;
import com.calypso.binar.service.exception.FileTooLargeException;
import com.calypso.binar.service.validation.casevalidation.FileValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileValidatorTest {


    private FileValidator fileValidator;

    @BeforeEach
    void setUp() {
        fileValidator = new FileValidator();
    }

    @Test
    void isFileSizeValid_fileSizeWithinLimit_shouldPass() throws FileTooLargeException {
        // Create a mock AttachedFilesDTO
        AttachedFilesDTO attachedFileDTO = new AttachedFilesDTO();
        byte[] fileData = new byte[(int)(5 * 1024 * 1024)]; // 5MB file size
        attachedFileDTO.setFileData(fileData);

        // Call the method under test
        fileValidator.isFileSizeValid(attachedFileDTO);

        // Verify that no exception is thrown
        assertDoesNotThrow(() -> fileValidator.isFileSizeValid(attachedFileDTO));
    }

    @Test
    void isFileSizeValid_fileSizeExceedsLimit_shouldThrowException() {
        AttachedFilesDTO attachedFileDTO = new AttachedFilesDTO();
        byte[] fileData = new byte[(int)(5 * 1024 * 1024) + 1]; // 5MB + 1 byte
        attachedFileDTO.setFileData(fileData);

        // Expect a FileTooLargeException to be thrown
        FileTooLargeException thrown = assertThrows(FileTooLargeException.class, () -> {
            fileValidator.isFileSizeValid(attachedFileDTO);
        });

        // Assert that the exception message matches (optional)
        assertEquals("File too large", thrown.getMessage());

    }

}
