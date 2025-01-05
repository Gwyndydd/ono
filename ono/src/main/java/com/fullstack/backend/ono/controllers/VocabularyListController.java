package com.fullstack.backend.ono.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fullstack.backend.ono.models.constants.Langues;
import com.fullstack.backend.ono.models.dtos.VocaListDto;
import com.fullstack.backend.ono.models.dtos.VocabularyDto;
import com.fullstack.backend.ono.services.VocabularyListService;
import com.fullstack.backend.ono.services.VocabularyService;

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
@RequestMapping(path = "/vocabulary-list", produces = "application/json")
@RequiredArgsConstructor
public class VocabularyListController {

    private final VocabularyListService vocaListService;
    private final VocabularyService vocaService;

     /**
     * 
     * @param studyProgramId
     * @return
     */
    @Operation(summary = "Get a vocabulary List by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vocabulary List found",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = VocaListDto.class)) }),
        @ApiResponse(responseCode = "404", description = "Vocabulary List not found")
    })
    @GetMapping(path = "/id/{vocaListId}")
    public ResponseEntity<VocaListDto> getVocaList(@PathVariable UUID vocaListId){
        return ResponseEntity.ok(vocaListService.getVocabularyListId(vocaListId));
    }


    /**
     * 
     * @param dto
     * @return
     */
    @Operation(summary = "Get all vocabulary list for the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vocabulary List found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VocaListDto.class)) })
    })
    @GetMapping("/user/")
    public ResponseEntity<List<VocaListDto>> getVocaListsByOwner(@RequestParam UUID idUser) {
        return ResponseEntity.ok(vocaListService.getAllVocabularyListByOwner(idUser));  
    }


    /**
     * 
     * @param name
     * @param dto
     * @return
     */
    @Operation(summary = "Search vocabulary list by name/user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vocabulary List found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VocaListDto.class)) })
    })
    @GetMapping(path = "/search")
    public ResponseEntity<VocaListDto> searchVocabularyListByName(
        @RequestParam String name,
        @RequestParam UUID idUser
        ) {
        return ResponseEntity.ok(vocaListService.getVocabularyListByOwnerName(name, idUser));
    }



    /**
     * UPDATE
     * @param dto
     * @param studyProgramId
     * @return
     */
    @Operation(summary = "Update a vocabulary list by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vocabulary List updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VocaListDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Vocabulary List not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PutMapping(path = "/update/{vocaListId}")
    public ResponseEntity<VocaListDto> updateVocaList(
            @RequestBody @Valid VocaListDto dto,
            @PathVariable @Valid UUID vocaListId
            ) {
        return ResponseEntity.ok(vocaListService.updateVocabularyList(vocaListId,dto));
    }

     /**
     * 
     * @param dto
     * @param studyProgramId
     * @return
     */
    @Operation(summary = "add studyProgram a vocabulary list by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vocabulary List updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VocaListDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Vocabulary List not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PutMapping(path = "/add-study-program/")
    public ResponseEntity<VocaListDto> addStudyProgram(
        @RequestParam @Valid UUID vocaListId,
        @RequestParam @Valid UUID studyProgramId
    ) {
        return ResponseEntity.ok(vocaListService.addStudyProgramm(studyProgramId,vocaListId));
    }

    /**
    * DELETE
    * @param studyProgramId
    * @return
    */
    @Operation(summary = "delete a vocabulary list by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vocabulary List deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VocaListDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Vocabulary List not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @DeleteMapping(path = "/delete/")
    public ResponseEntity<VocaListDto> deleteVocaList(
            @RequestParam @Valid UUID vocaListId
            ) {
        
                List<VocabularyDto> voca = vocaService.deleteAllVocabularyFromList(vocaListId);

        return ResponseEntity.ok(vocaListService.deleVocabularyList(vocaListId));
    }


    /**
     * REGISTER
     * @param dto
     * @return
     */
    @Operation(summary = "Register a vocabulary List")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vocabulary List registered",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VocaListDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping(path = "/register")
    public ResponseEntity<VocaListDto> registerVocaList(
        @RequestBody @Valid VocaListDto dto
        ) {
                log.info("langue dto : {}", dto.getLangueEtudie());
                Langues test = Langues.getByName(dto.getLangueEtudie());
                log.info("{}",test.toString());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vocaListService.registerVocabularyList(dto));
    }

     /**
     * 
     * @param name
     * @param dto
     * @return
     */
    @Operation(summary = "GET vocabulary list visible au public")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vocabulary List found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VocaListDto.class)) })
    })
    @GetMapping(path = "/public")
    public ResponseEntity<List<VocaListDto>> getVocabularyListPublic() {
        return ResponseEntity.ok(vocaListService.getAllVocabularyListByVisibility(false));
    }

    

}
