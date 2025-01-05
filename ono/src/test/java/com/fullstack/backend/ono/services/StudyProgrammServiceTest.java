package com.fullstack.backend.ono.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fullstack.backend.ono.exceptions.NotFoundException;
import com.fullstack.backend.ono.exceptions.errors.StudyProgramErrorCode;
import com.fullstack.backend.ono.models.converters.StudyProgrammConverter;
import com.fullstack.backend.ono.models.converters.UserConverter;
import com.fullstack.backend.ono.models.dtos.StudyProgramDto;
import com.fullstack.backend.ono.models.dtos.UserDto;
import com.fullstack.backend.ono.models.dtos.UserRegistrationDto;
import com.fullstack.backend.ono.models.entities.StudyProgram;
import com.fullstack.backend.ono.models.entities.User;
import com.fullstack.backend.ono.repositories.StudyProgramRepository;
import com.fullstack.backend.ono.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class StudyProgrammServiceTest {

    @InjectMocks
    private StudyProgrammService studyProgrammService;
    @Mock
    private StudyProgramRepository studyProgramRepository;
    @Mock
    private StudyProgrammConverter studyProgrammConverter;

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserConverter userConverter;
    

    @Mock
    static Clock clock;

    @Mock
    private BCryptPasswordEncoder encoder;

    static StudyProgramDto defaultDto;
    static StudyProgram defaultEntity;

    static User defaultUser;
    static UserRegistrationDto defaultUserRegistrationDto;

    static UUID defaultIdUser = UUID.randomUUID();

    static UUID defaultIdSP = UUID.randomUUID();

    @BeforeAll
    public static void setUp(){
        clock = Clock.fixed(Instant.now(), Clock.systemDefaultZone().getZone());

        String name = "test";
        String description = "pour test";

        String username = "UserTest";
        String email = "test@test.fr";
        String password = "password";

        defaultUserRegistrationDto = createUserDto(username, email, password);
        defaultUser = createUser(username, email, password);
        defaultEntity = createStudyProgram(name, description, false, defaultUser);
        defaultDto = createStudyProgramDto(name, description, false);
        
    }
    
    @Test
    void testRegisterSP_ok_returnDto(){

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(defaultUser));
        Mockito.when(studyProgramRepository.save(Mockito.any())).thenReturn(defaultEntity);
        Mockito.when(studyProgrammConverter.convert(Mockito.any())).thenReturn(defaultDto);

        StudyProgramDto result = studyProgrammService.registerStudyProgram(defaultDto);

        Mockito.verify(studyProgramRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(studyProgrammConverter, Mockito.times(1)).convert(Mockito.any());

        assertEquals(defaultDto, result);

    }

    @Test
    void testRegisterSP_alreadyExist_throwError(){

        Mockito.when(studyProgramRepository.findByOwnerIdAndName(Mockito.any(),Mockito.any())).thenReturn(Optional.of(defaultEntity));
        
        Assertions.assertThrows(NotFoundException.class, () -> {
            studyProgrammService.registerStudyProgram(defaultDto);
        });

        Mockito.verify(studyProgramRepository, Mockito.times(1)).findByOwnerIdAndName(Mockito.any(),Mockito.any());

    }

    @Test
    void testUpdateSP_returnDto(){

        Mockito.when(studyProgramRepository.findById(Mockito.any())).thenReturn(Optional.of(defaultEntity));
        Mockito.when(studyProgramRepository.save(Mockito.any())).thenReturn(defaultEntity);
        Mockito.when(studyProgrammConverter.convert(Mockito.any())).thenReturn(defaultDto);

        StudyProgramDto result = studyProgrammService.uptadeProgram(defaultIdSP,defaultDto);

        Mockito.verify(studyProgramRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(studyProgrammConverter, Mockito.times(1)).convert(Mockito.any());

        assertEquals(defaultDto, result);

    }

    @Test
    void testUpdateSP_alreadyExist_throwError(){

        Assertions.assertThrows(NotFoundException.class, () -> {
            studyProgrammService.uptadeProgram(defaultIdSP,defaultDto);
        });

        Mockito.verify(studyProgramRepository, Mockito.times(1)).findById(Mockito.any());

    }

    @Test
    void testGetById_returnDto(){

        Mockito.when(studyProgramRepository.findById(Mockito.any())).thenReturn(Optional.of(defaultEntity));
        Mockito.when(studyProgrammConverter.convert(Mockito.any())).thenReturn(defaultDto);

        StudyProgramDto result = studyProgrammService.getStudyProgramById(defaultIdSP);

        Mockito.verify(studyProgramRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(studyProgrammConverter, Mockito.times(1)).convert(Mockito.any());

        assertEquals(defaultDto, result);
    }

    @Test
    void testGetByID_DoNotExist_throwError(){

        Assertions.assertThrows(NotFoundException.class, () -> {
            studyProgrammService.getStudyProgramById(defaultIdSP);
        });

        Mockito.verify(studyProgramRepository, Mockito.times(1)).findById(Mockito.any());

    }

    @Test
    void testGetByOwnerAndName_returnDto(){

        Mockito.when(studyProgramRepository.findByOwnerIdAndName(Mockito.any(),Mockito.any()))
        .thenReturn(Optional.of(defaultEntity));
        Mockito.when(studyProgrammConverter.convert(Mockito.any())).thenReturn(defaultDto);

        StudyProgramDto result = studyProgrammService.getStudyProgramByOwnerName(defaultDto.getName(),defaultIdUser);

        Mockito.verify(studyProgramRepository, Mockito.times(1)).findByOwnerIdAndName(Mockito.any(),Mockito.any());
        Mockito.verify(studyProgrammConverter, Mockito.times(1)).convert(Mockito.any());

        assertEquals(defaultDto, result);
    }

    @Test
    void testGetByOwnerAndName_DoNotExist_throwError(){

        Assertions.assertThrows(NotFoundException.class, () -> {
            studyProgrammService.getStudyProgramByOwnerName(defaultDto.getName(),defaultIdUser);
        });

        Mockito.verify(studyProgramRepository, Mockito.times(1)).findByOwnerIdAndName(Mockito.any(),Mockito.any());

    }

    @Test
    void testSearchStudyProgram_returnDto(){

        StudyProgram sp = createStudyProgram(defaultEntity.getName(), null, false, defaultUser);
        sp.setId(UUID.randomUUID());
        
        List<StudyProgram> lSPrograms = new ArrayList<>();
        lSPrograms.add(sp);
        lSPrograms.add(defaultEntity);

        List<StudyProgramDto> lSProgramDtos = new ArrayList<>();
        lSProgramDtos.add(createStudyProgramDto(defaultDto.getName(), null, false));
        lSProgramDtos.add(defaultDto);


        Mockito.when(studyProgramRepository.searchByVocaAndUserNameWithVisibility(Mockito.any(),Mockito.any()))
        .thenReturn(lSPrograms);
        Mockito.when(studyProgrammConverter.convert(lSPrograms.get(1))).thenReturn(defaultDto);
        Mockito.when(studyProgrammConverter.convert(lSPrograms.get(0))).thenReturn(lSProgramDtos.get(0));

        List<StudyProgramDto> result = studyProgrammService.searchStudyProgram(defaultDto.getName(),defaultIdUser);

        Mockito.verify(studyProgramRepository, Mockito.times(1)).searchByVocaAndUserNameWithVisibility(Mockito.any(),Mockito.any());
        Mockito.verify(studyProgrammConverter, Mockito.times(2)).convert(Mockito.any());

        assertEquals(lSProgramDtos, result);
    }

    @Test
    void testGetAllStudyProgramByOwner_returnDto(){

        StudyProgram sp = createStudyProgram(defaultEntity.getName(), null, false, defaultUser);
        sp.setId(UUID.randomUUID());
        
        List<StudyProgram> lSPrograms = new ArrayList<>();
        lSPrograms.add(sp);
        lSPrograms.add(defaultEntity);

        List<StudyProgramDto> lSProgramDtos = new ArrayList<>();
        lSProgramDtos.add(createStudyProgramDto(defaultDto.getName(), null, false));
        lSProgramDtos.add(defaultDto);


        Mockito.when(studyProgramRepository.findAllByOwnerId(Mockito.any()))
        .thenReturn(lSPrograms);
        Mockito.when(studyProgrammConverter.convert(lSPrograms.get(1))).thenReturn(defaultDto);
        Mockito.when(studyProgrammConverter.convert(lSPrograms.get(0))).thenReturn(lSProgramDtos.get(0));

        List<StudyProgramDto> result = studyProgrammService.getAllStudyProgramByOwner(defaultIdUser);

        Mockito.verify(studyProgramRepository, Mockito.times(1)).findAllByOwnerId(Mockito.any());
        Mockito.verify(studyProgrammConverter, Mockito.times(2)).convert(Mockito.any());

        assertEquals(lSProgramDtos, result);
    }

    @Test
    void testGetAllStudyProgram_returnDto(){

        StudyProgram sp = createStudyProgram(defaultEntity.getName(), null, false, defaultUser);
        sp.setId(UUID.randomUUID());
        
        List<StudyProgram> lSPrograms = new ArrayList<>();
        lSPrograms.add(sp);
        lSPrograms.add(defaultEntity);

        List<StudyProgramDto> lSProgramDtos = new ArrayList<>();
        lSProgramDtos.add(createStudyProgramDto(defaultDto.getName(), null, false));
        lSProgramDtos.add(defaultDto);


        Mockito.when(studyProgramRepository.findAll())
        .thenReturn(lSPrograms);
        Mockito.when(studyProgrammConverter.convert(lSPrograms.get(1))).thenReturn(defaultDto);
        Mockito.when(studyProgrammConverter.convert(lSPrograms.get(0))).thenReturn(lSProgramDtos.get(0));

        List<StudyProgramDto> result = studyProgrammService.getAllStudyProgram();

        Mockito.verify(studyProgramRepository, Mockito.times(1)).findAll();
        Mockito.verify(studyProgrammConverter, Mockito.times(2)).convert(Mockito.any());

        assertEquals(lSProgramDtos, result);
    }

    @Test
    void testGetAllProgramByVisibility_returnDto(){

        StudyProgram sp = createStudyProgram(defaultEntity.getName(), null, false, defaultUser);
        sp.setId(UUID.randomUUID());
        
        List<StudyProgram> lSPrograms = new ArrayList<>();
        lSPrograms.add(sp);
        lSPrograms.add(defaultEntity);

        List<StudyProgramDto> lSProgramDtos = new ArrayList<>();
        lSProgramDtos.add(createStudyProgramDto(defaultDto.getName(), null, false));
        lSProgramDtos.add(defaultDto);


        Mockito.when(studyProgramRepository.findAllByVisibilityOrderByUpdatedAtDesc(Mockito.anyBoolean()))
        .thenReturn(lSPrograms);
        Mockito.when(studyProgrammConverter.convert(lSPrograms.get(1))).thenReturn(defaultDto);
        Mockito.when(studyProgrammConverter.convert(lSPrograms.get(0))).thenReturn(lSProgramDtos.get(0));

        List<StudyProgramDto> result = studyProgrammService.getAllStudyProgramByVisibility(false);

        Mockito.verify(studyProgramRepository, Mockito.times(1)).findAllByVisibilityOrderByUpdatedAtDesc(Mockito.anyBoolean());
        Mockito.verify(studyProgrammConverter, Mockito.times(2)).convert(Mockito.any());

        assertEquals(lSProgramDtos, result);
    }

    @Test
    void testDelete_returnDto(){

        Mockito.when(studyProgramRepository.findById(Mockito.any())).thenReturn(Optional.of(defaultEntity));
        Mockito.when(studyProgrammConverter.convert(Mockito.any())).thenReturn(defaultDto);

        StudyProgramDto result = studyProgrammService.deleteStudyProgram(defaultIdSP);

        Mockito.verify(studyProgramRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(studyProgramRepository, Mockito.times(1)).deleteById(Mockito.any());
        Mockito.verify(studyProgrammConverter, Mockito.times(1)).convert(Mockito.any());

        assertEquals(defaultDto, result);
    }

    @Test
    void testDelete_DoNotExist_throwError(){

        Assertions.assertThrows(NotFoundException.class, () -> {
            studyProgrammService.deleteStudyProgram(defaultIdSP);
        });

        Mockito.verify(studyProgramRepository, Mockito.times(1)).findById(Mockito.any());

    }
    


    //
    private static User createUser(String username, String email, String password) {
        User entity = User.builder()
            .id(defaultIdUser)
            .username(username)
            .email(email)
            .password(password)
            .build();
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
    
        return entity;
    }

    private static UserRegistrationDto createUserDto(String username, String email, String password) {
        UserRegistrationDto entity = UserRegistrationDto.builder()
            .username(username)
            .email(email)
            .password(password)
            .build();    
        return entity;
    }

    private static StudyProgramDto createStudyProgramDto(String name, String description, boolean visibility){
        return StudyProgramDto.builder()
            .name(name)
            .description(description)
            .idOwner(defaultIdUser)
            .prive(visibility)
            .build();   
    }

    private static StudyProgram createStudyProgram(String name, String description, boolean visibility, User defaultUser){
        StudyProgram entity = StudyProgram.builder()
            .id(defaultIdSP)
            .name(name)
            .description(description)
            .owner(defaultUser)
            .visibility(visibility)
            .build();

        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        
        return entity;
    }
    
}
