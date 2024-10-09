package com.calypso.binar.service;

import com.calypso.binar.model.Status;
import com.calypso.binar.repository.StatusRepository;
import com.calypso.binar.service.exception.StatusNonExistent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatusServiceTest {

    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private StatusService statusService;

    @Test
    public void testGetStatusByStatusName_whenStatusExists() throws StatusNonExistent {
        // Arrange
        String statusName = "Active";
        Status status = new Status();
        status.setStatusName(statusName);
        when(statusRepository.findByStatusName(statusName)).thenReturn(Optional.of(status));

        // Act
        Status result = statusService.getStatusByStatusName(statusName);

        // Assert
        assertNotNull(result);
        assertEquals(statusName, result.getStatusName());
        verify(statusRepository, times(1)).findByStatusName(statusName);
    }

    @Test
    public void testGetStatusByStatusName_whenStatusDoesNotExist() {
        // Arrange
        String statusName = "Inactive";
        when(statusRepository.findByStatusName(statusName)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(StatusNonExistent.class, () -> {
            statusService.getStatusByStatusName(statusName);
        });
        verify(statusRepository, times(1)).findByStatusName(statusName);
    }
}
