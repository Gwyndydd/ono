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
import com.fullstack.backend.ono.models.constants.Langues;
import com.fullstack.backend.ono.models.converters.UserConverter;
import com.fullstack.backend.ono.models.converters.VocaListConverter;
import com.fullstack.backend.ono.models.dtos.UserRegistrationDto;
import com.fullstack.backend.ono.models.dtos.VocaListDto;
import com.fullstack.backend.ono.models.entities.VocabularyList;
import com.fullstack.backend.ono.models.entities.StudyProgram;
import com.fullstack.backend.ono.models.entities.User;
import com.fullstack.backend.ono.repositories.StudyProgramRepository;
import com.fullstack.backend.ono.repositories.UserRepository;
import com.fullstack.backend.ono.repositories.VocaListRepository;

@ExtendWith(MockitoExtension.class)
public class VocabularyListServiceTest {

    @InjectMocks
    private VocabularyListService vocabularyListService;
    @Mock
    private VocaListRepository vocabularyListRepository;
    @Mock
    private VocaListConverter vocabularyListConverter;

    @Mock
    private StudyProgramRepository studyProgramRepository;

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

    static VocaListDto defaultDto;
    static VocabularyList defaultEntity;

    static User defaultUser;

    static StudyProgram defaultSP;

    static UUID defaultIdVocaList = UUID.randomUUID();
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

        defaultSP = createStudyProgram(username, description, false, defaultUser);
        defaultUser = createUser(username, email, password);
        defaultEntity = createVocabularyList(name, description, false, defaultUser);
        defaultDto = createVocaListDto(name, description, false);
        
    }
    
    @Test
    void testRegisterSP_ok_returnDto(){

        Mockito.when(vocabularyListRepository.findByOwnerIdAndName(Mockito.any(),Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(studyProgramRepository.findById(defaultIdSP)).thenReturn(Optional.of(defaultSP));
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(defaultUser));
        Mockito.when(vocabularyListRepository.save(Mockito.any())).thenReturn(defaultEntity);
        Mockito.when(vocabularyListConverter.convert(Mockito.any())).thenReturn(defaultDto);

        VocaListDto result = vocabularyListService.registerVocabularyList(defaultDto);

        Mockito.verify(vocabularyListRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(vocabularyListConverter, Mockito.times(1)).convert(Mockito.any());

        assertEquals(defaultDto, result);

    }

    @Test
    void testRegisterSP_alreadyExist_throwError(){

        Mockito.when(vocabularyListRepository.findByOwnerIdAndName(Mockito.any(),Mockito.any())).thenReturn(Optional.of(defaultEntity));
        
        Assertions.assertThrows(NotFoundException.class, () -> {
            vocabularyListService.registerVocabularyList(defaultDto);
        });

        Mockito.verify(vocabularyListRepository, Mockito.times(1)).findByOwnerIdAndName(Mockito.any(),Mockito.any());

    }

    @Test
    void testAddStudyProgramm_ok_returnDto(){
        defaultEntity.setStudyProgram(null);

        Mockito.when(vocabularyListRepository.findById(Mockito.any())).thenReturn(Optional.of(defaultEntity));
        Mockito.when(studyProgramRepository.findById(defaultIdSP)).thenReturn(Optional.of(defaultSP));
        Mockito.when(vocabularyListConverter.convert(Mockito.any())).thenReturn(defaultDto);

        VocaListDto result = vocabularyListService.addStudyProgramm(defaultIdSP, defaultIdVocaList);

        Mockito.verify(vocabularyListRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(studyProgramRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(vocabularyListRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(vocabularyListConverter, Mockito.times(1)).convert(Mockito.any());

        assertEquals(defaultDto, result);
    }

    @Test
    void testAddStudyProgramm_ListNotFound_throwError(){
        defaultEntity.setStudyProgram(null);

        Mockito.when(vocabularyListRepository.findById(Mockito.any())).thenReturn(Optional.empty());;

        Assertions.assertThrows(NotFoundException.class, () -> {
            vocabularyListService.addStudyProgramm(defaultIdSP, defaultIdVocaList);
        });

        Mockito.verify(vocabularyListRepository, Mockito.times(1)).findById(Mockito.any());
    }

    @Test
    void testAddStudyProgramm_SPNotFound_throwError(){
        defaultEntity.setStudyProgram(null);

        Mockito.when(vocabularyListRepository.findById(Mockito.any())).thenReturn(Optional.of(defaultEntity));
        Mockito.when(studyProgramRepository.findById(defaultIdSP)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> {
            vocabularyListService.addStudyProgramm(defaultIdSP, defaultIdVocaList);
        });

        Mockito.verify(vocabularyListRepository, Mockito.times(1)).findById(Mockito.any());
    }

    @Test
    void testUpdate_ok_returnDto(){

        Mockito.when(vocabularyListRepository.findById(Mockito.any())).thenReturn(Optional.of(defaultEntity));
        Mockito.when(vocabularyListRepository.findByOwnerIdAndName(Mockito.any(),Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(vocabularyListRepository.save(Mockito.any())).thenReturn(defaultEntity);
        Mockito.when(vocabularyListConverter.convert(Mockito.any())).thenReturn(defaultDto);

        VocaListDto result = vocabularyListService.updateVocabularyList(defaultIdSP,defaultDto);

        Mockito.verify(vocabularyListRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(vocabularyListRepository, Mockito.times(1)).findByOwnerIdAndName(Mockito.any(),Mockito.any());
        Mockito.verify(vocabularyListRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(vocabularyListConverter, Mockito.times(1)).convert(Mockito.any());

        assertEquals(defaultDto, result);

    }

    @Test
    void testUpdateSP_alreadyExist_throwError(){

        Mockito.when(vocabularyListRepository.findById(Mockito.any())).thenReturn(Optional.of(defaultEntity));
        Mockito.when(vocabularyListRepository.findByOwnerIdAndName(Mockito.any(),Mockito.any())).thenReturn(Optional.of(defaultEntity));

        Assertions.assertThrows(NotFoundException.class, () -> {
            vocabularyListService.updateVocabularyList(defaultIdSP,defaultDto);
        });

        Mockito.verify(vocabularyListRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(vocabularyListRepository, Mockito.times(1)).findByOwnerIdAndName(Mockito.any(),Mockito.any());
    }

    @Test
    void testUpdateSP_NotFound_throwError(){

        Mockito.when(vocabularyListRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        
        Assertions.assertThrows(NotFoundException.class, () -> {
            vocabularyListService.updateVocabularyList(defaultIdSP,defaultDto);
        });

        Mockito.verify(vocabularyListRepository, Mockito.times(1)).findById(Mockito.any());

    }

    @Test
    void testGetById_returnDto(){

        Mockito.when(vocabularyListRepository.findById(Mockito.any())).thenReturn(Optional.of(defaultEntity));
        Mockito.when(vocabularyListConverter.convert(Mockito.any())).thenReturn(defaultDto);

        VocaListDto result = vocabularyListService.getVocabularyListId(defaultIdSP);

        Mockito.verify(vocabularyListRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(vocabularyListConverter, Mockito.times(1)).convert(Mockito.any());

        assertEquals(defaultDto, result);
    }

    @Test
    void testGetByID_DoNotExist_throwError(){

        Assertions.assertThrows(NotFoundException.class, () -> {
            vocabularyListService.getVocabularyListId(defaultIdSP);
        });

        Mockito.verify(vocabularyListRepository, Mockito.times(1)).findById(Mockito.any());

    }

    @Test
    void testGetByOwnerAndName_returnDto(){

        Mockito.when(vocabularyListRepository.findByOwnerIdAndName(Mockito.any(),Mockito.any()))
        .thenReturn(Optional.of(defaultEntity));
        Mockito.when(vocabularyListConverter.convert(Mockito.any())).thenReturn(defaultDto);

        VocaListDto result = vocabularyListService.getVocabularyListByOwnerName(defaultDto.getName(),defaultIdUser);

        Mockito.verify(vocabularyListRepository, Mockito.times(1)).findByOwnerIdAndName(Mockito.any(),Mockito.any());
        Mockito.verify(vocabularyListConverter, Mockito.times(1)).convert(Mockito.any());

        assertEquals(defaultDto, result);
    }

    @Test
    void testGetByOwnerAndName_DoNotExist_throwError(){

        Assertions.assertThrows(NotFoundException.class, () -> {
            vocabularyListService.getVocabularyListByOwnerName(defaultDto.getName(),defaultIdUser);
        });

        Mockito.verify(vocabularyListRepository, Mockito.times(1)).findByOwnerIdAndName(Mockito.any(),Mockito.any());

    }

    @Test
    void testGetAllVocabularyListByOwner_returnDto(){

        VocabularyList sp = createVocabularyList(defaultEntity.getName(), null, false, defaultUser);
        sp.setId(UUID.randomUUID());
        
        List<VocabularyList> lSPrograms = new ArrayList<>();
        lSPrograms.add(sp);
        lSPrograms.add(defaultEntity);

        List<VocaListDto> lSProgramDtos = new ArrayList<>();
        lSProgramDtos.add(createVocaListDto(defaultDto.getName(), null, false));
        lSProgramDtos.add(defaultDto);


        Mockito.when(vocabularyListRepository.findAllByOwnerId(Mockito.any()))
        .thenReturn(lSPrograms);
        Mockito.when(vocabularyListConverter.convert(lSPrograms.get(1))).thenReturn(defaultDto);
        Mockito.when(vocabularyListConverter.convert(lSPrograms.get(0))).thenReturn(lSProgramDtos.get(0));

        List<VocaListDto> result = vocabularyListService.getAllVocabularyListByOwner(defaultIdUser);

        Mockito.verify(vocabularyListRepository, Mockito.times(1)).findAllByOwnerId(Mockito.any());
        Mockito.verify(vocabularyListConverter, Mockito.times(2)).convert(Mockito.any());

        assertEquals(lSProgramDtos, result);
    }


    @Test
    void testGetAllProgramByVisibility_returnDto(){

        VocabularyList sp = createVocabularyList(defaultEntity.getName(), null, false, defaultUser);
        sp.setId(UUID.randomUUID());
        
        List<VocabularyList> lSPrograms = new ArrayList<>();
        lSPrograms.add(sp);
        lSPrograms.add(defaultEntity);

        List<VocaListDto> lSProgramDtos = new ArrayList<>();
        lSProgramDtos.add(createVocaListDto(defaultDto.getName(), null, false));
        lSProgramDtos.add(defaultDto);


        Mockito.when(vocabularyListRepository.findAllByVisibilityOrderByUpdatedAtDesc(Mockito.anyBoolean()))
        .thenReturn(lSPrograms);
        Mockito.when(vocabularyListConverter.convert(lSPrograms.get(1))).thenReturn(defaultDto);
        Mockito.when(vocabularyListConverter.convert(lSPrograms.get(0))).thenReturn(lSProgramDtos.get(0));

        List<VocaListDto> result = vocabularyListService.getAllVocabularyListByVisibility(false);

        Mockito.verify(vocabularyListRepository, Mockito.times(1)).findAllByVisibilityOrderByUpdatedAtDesc(Mockito.anyBoolean());
        Mockito.verify(vocabularyListConverter, Mockito.times(2)).convert(Mockito.any());

        assertEquals(lSProgramDtos, result);
    }

    @Test
    void testDelete_returnDto(){

        Mockito.when(vocabularyListRepository.findById(Mockito.any())).thenReturn(Optional.of(defaultEntity));
        Mockito.when(vocabularyListConverter.convert(Mockito.any())).thenReturn(defaultDto);

        VocaListDto result = vocabularyListService.deleVocabularyList(defaultIdSP);

        Mockito.verify(vocabularyListRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(vocabularyListRepository, Mockito.times(1)).deleteById(Mockito.any());
        Mockito.verify(vocabularyListConverter, Mockito.times(1)).convert(Mockito.any());

        assertEquals(defaultDto, result);
    }

    @Test
    void testDelete_DoNotExist_throwError(){

        Assertions.assertThrows(NotFoundException.class, () -> {
            vocabularyListService.deleVocabularyList(defaultIdSP);
        });

        Mockito.verify(vocabularyListRepository, Mockito.times(1)).findById(Mockito.any());

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

    private static VocaListDto createVocaListDto(String name, String description, boolean visibility){
        return VocaListDto.builder()
            .name(name)
            .langueEtudie("Coreen")
            .langueDefinition("Francais")
            .idProgrammeEtude(defaultIdSP)
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

    private static VocabularyList createVocabularyList(String name, String description, boolean visibility, User defaultUser){
        VocabularyList entity = VocabularyList.builder()
            .id(defaultIdVocaList)
            .name(name)
            .langueDefinition(Langues.FRANCAIS)
            .langueEtudie(Langues.COREEN)
            .studyProgram(defaultSP)
            .owner(defaultUser)
            .visibility(visibility)
            .build();

        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        
        return entity;
    }
    
}
