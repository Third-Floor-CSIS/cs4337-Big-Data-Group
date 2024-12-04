package cs4337.group_8.ProfileService.services;

import cs4337.group_8.ProfileService.DTO.ProfileDTO;
import cs4337.group_8.ProfileService.entities.ProfileEntity;
import cs4337.group_8.ProfileService.exceptions.SampleCustomException;
import cs4337.group_8.ProfileService.repositories.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TestProfileService {

    @InjectMocks
    private ProfileService profileService;

    @Mock
    private ProfileRepository profileRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUserExistanceById_shouldReturnProfileEntity_whenUserExists() {
        ProfileEntity mockEntity = new ProfileEntity();
        mockEntity.setUser_id("123");

        when(profileRepository.findByIdEquals(anyString())).thenReturn(Optional.of(mockEntity));

        ProfileEntity result = profileService.getUserExistanceById("123");

        assert result.getUser_id().equals("123");
        verify(profileRepository, times(1)).findByIdEquals("123");
    }
    @Test
    public void getUserExistanceById_shouldThrowException_whenUserDoesNotExist() {
        when(profileRepository.findByIdEquals(anyString())).thenReturn(Optional.empty());

        assertThrows(SampleCustomException.class, () -> profileService.getUserExistanceById("123"));
        verify(profileRepository, times(1)).findByIdEquals("123");
}

}
