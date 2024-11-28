package com.fullstack.backend.ono.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fullstack.backend.ono.models.dtos.StudyProgramDto;
import com.fullstack.backend.ono.models.dtos.UserDto;
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
@RequestMapping(path = "/study-programm", produces = "application/json")
@RequiredArgsConstructor
public class StudyProgramController {

    private final StudyProgrammService studyProgrammService;

    @Operation(summary = "Get a study programm by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "StudyProgramm found",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = StudyProgramDto.class)) }),
        @ApiResponse(responseCode = "404", description = "StudyProgramm not found")
    })
    @GetMapping(path = "/{studyProgramId}")
    public ResponseEntity<StudyProgramDto> getProgram(@PathVariable UUID studyProgramId){
        return ResponseEntity.ok(studyProgrammService.getStudyProgramById(studyProgramId));
    }

    @Operation(summary = "Get all study programm for the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "StudyProgramm found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudyProgramDto.class)) })
    })
    @GetMapping
    public ResponseEntity<List<StudyProgramDto>> getMovies(@RequestBody @Valid UserDto dto) {
        return ResponseEntity.ok(studyProgrammService.getAllStudyProgramByOwner(dto));
        
    }

    @Operation(summary = "Search study programm by name/user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "StudyProgramm found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudyProgramDto.class)) })
    })
    @GetMapping(path = "/search")
    public ResponseEntity<StudyProgramDto> searchStudyProgrammByName(
        @RequestParam String name,
        @RequestBody @Valid UserDto dto
        ) {
        return ResponseEntity.ok(studyProgrammService.getStudyProgramByOwnerName(name, dto));
    }

    @Operation(summary = "Update a programm study by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "StudyProgramm  updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudyProgramDto.class)) }),
            @ApiResponse(responseCode = "404", description = "StudyProgramm  not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PutMapping(path = "/{studyProgramId}")
    public ResponseEntity<StudyProgramDto> updateStudyProgram(
            @RequestBody @Valid StudyProgramDto dto,
            @PathVariable @Valid UUID studyProgramId
            ) {
        return ResponseEntity.ok(studyProgrammService.uptadeProgram(studyProgramId,dto));
    }


    @Operation(summary = "delete a programm study by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "StudyProgramm deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudyProgramDto.class)) }),
            @ApiResponse(responseCode = "404", description = "StudyProgramm  not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @DeleteMapping(path = "/{studyProgramId}")
    public ResponseEntity<StudyProgramDto> updateStudyProgram(
            @PathVariable @Valid UUID studyProgramId
            ) {
        return ResponseEntity.ok(studyProgrammService.deleteStudyProgram(studyProgramId));
    }

    @Operation(summary = "Register a program study")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "StudyProgram registered",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudyProgramDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<StudyProgramDto> registerMovie(
        @RequestBody @Valid StudyProgramDto dto,
        @RequestBody @Valid UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studyProgrammService.registerStudyProgram(dto, userDto));
    }

    

}
