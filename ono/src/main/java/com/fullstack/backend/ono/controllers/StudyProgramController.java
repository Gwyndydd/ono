package com.fullstack.backend.ono.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fullstack.backend.ono.models.dtos.StudyProgramDto;
import com.fullstack.backend.ono.services.StudyProgrammService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/study-program", produces = "application/json")
@RequiredArgsConstructor
public class StudyProgramController {

    private final StudyProgrammService studyProgrammService;

    /**
     * 
     * @param studyProgramId
     * @return
     */
    @Operation(summary = "Get a study programm by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "StudyProgramm found",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = StudyProgramDto.class)) }),
        @ApiResponse(responseCode = "404", description = "StudyProgramm not found")
    })
    @GetMapping(path = "/id/{studyProgramId}")
    public ResponseEntity<StudyProgramDto> getProgram(@PathVariable UUID studyProgramId){
        return ResponseEntity.ok(studyProgrammService.getStudyProgramById(studyProgramId));
    }


    /**
     * 
     * @param dto
     * @return
     */
    @Operation(summary = "Get all study programm for the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "StudyProgramm found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudyProgramDto.class)) })
    })
    @GetMapping("/user/")
    public ResponseEntity<List<StudyProgramDto>> getStudyProgramsByOwner(@RequestParam UUID idUser) {
        return ResponseEntity.ok(studyProgrammService.getAllStudyProgramByOwner(idUser));
        
    }


    /**
     * 
     * @param name
     * @param dto
     * @return
     */
    @Operation(summary = "Search study programm by name/user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "StudyProgramm found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudyProgramDto.class)) })
    })
    @GetMapping(path = "/search")
    public ResponseEntity<StudyProgramDto> searchStudyProgrammByName(
        @RequestParam String name,
        @RequestParam UUID idUser
        ) {
        return ResponseEntity.ok(studyProgrammService.getStudyProgramByOwnerName(name, idUser));
    }



    /**
     * 
     * @param dto
     * @param studyProgramId
     * @return
     */
    @Operation(summary = "Update a programm study by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "StudyProgramm  updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudyProgramDto.class)) }),
            @ApiResponse(responseCode = "404", description = "StudyProgramm  not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PutMapping(path = "/update/{studyProgramId}")
    public ResponseEntity<StudyProgramDto> updateStudyProgram(
            @RequestBody @Valid StudyProgramDto dto,
            @PathVariable @Valid UUID studyProgramId
            ) {
        return ResponseEntity.ok(studyProgrammService.uptadeProgram(studyProgramId,dto));
    }

        /**
         * DELETE
         * @param studyProgramId
         * @return
         */
    @Operation(summary = "delete a programm study by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "StudyProgramm deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudyProgramDto.class)) }),
            @ApiResponse(responseCode = "404", description = "StudyProgramm  not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @DeleteMapping(path = "/delete/{studyProgramId}")
    public ResponseEntity<StudyProgramDto> deleteStudyProgram(
            @PathVariable @Valid UUID studyProgramId
            ) {
        return ResponseEntity.ok(studyProgrammService.deleteStudyProgram(studyProgramId));
    }


    /**
     * REGISTER
     * @param dto
     * @return
     */
    @Operation(summary = "Register a program study")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "StudyProgram registered",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudyProgramDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping(path = "/register")
    public ResponseEntity<StudyProgramDto> registerStudyProgram(
        @RequestBody @Valid StudyProgramDto dto
        /*@RequestBody @Valid UserDto userDto*/) {
                /*String id = "e524f93a-7bf4-46f2-933f-55b032605858";
                UserDto userDto = UserDto.builder().id(UUID.fromString(id)).username("gwen").build();*/
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studyProgrammService.registerStudyProgram(dto));
    }


     /**
     * 
     * @return
     */
    @Operation(summary = "Get all study programm")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "StudyProgramm found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudyProgramDto.class)) })
    })
    @GetMapping(path = "/all")
    public ResponseEntity<List<StudyProgramDto>> getAllStudyProgramm() {
        return ResponseEntity.ok(studyProgrammService.getAllStudyProgram());
    }

     /**
     * 
     * @param name
     * @param dto
     * @return
     */
    @Operation(summary = "GET study programm visible au public")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "StudyProgramm found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudyProgramDto.class)) })
    })
    @GetMapping(path = "/public")
    public ResponseEntity<List<StudyProgramDto>> getStudyProgrammPublic() {
        return ResponseEntity.ok(studyProgrammService.getAllStudyProgramByVisibility(false));
    }

    

}
