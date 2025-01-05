package com.fullstack.backend.ono.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fullstack.backend.ono.models.dtos.VocabularyDto;
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
@RequestMapping(path = "/vocabulary", produces = "application/json")
@RequiredArgsConstructor
public class VocabularyController {

    private final VocabularyService vocabularyService;
    

    @Operation(summary = "Get vocabulary by its id")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "Vocabulary found",
        content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = VocabularyDto.class)) }),
        @ApiResponse(responseCode = "404", description = "Vocabulary not found")
    })
    @GetMapping(path = "/id/{vocaId}")
    public ResponseEntity<VocabularyDto> getVoca(@PathVariable UUID vocaId){
        return ResponseEntity.ok(vocabularyService.getVocabularybyId(vocaId));
    }



    @Operation(summary = "Get all vocabulary in the vocabulary list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vocabulary found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VocabularyDto.class)) })
    })
    @GetMapping("/vocabulary-list/")
    public ResponseEntity<List<VocabularyDto>> getVocaAllinList(@RequestParam UUID idVocaList){
        return ResponseEntity.ok(vocabularyService.getAllVocabularyinList(idVocaList));  
    }
        
    

    @Operation(summary = "Get all vocabulary in the vocabulary list by type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vocabulary found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VocabularyDto.class)) })
    })
    @GetMapping("/vocabulary-list/type/")
    public ResponseEntity<List<VocabularyDto>> getVocainListByType(
        @RequestParam UUID idVocaList,
        @RequestParam String type
        ) {
        return ResponseEntity.ok(vocabularyService.getAllVocabularyinListByType(idVocaList,type));  
    }


    /**
     * REGISTER
     * @param 
     * @return
     */
    @Operation(summary = "Register a vocabulary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vocabulary registered",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VocabularyDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping(path = "/register")
    public ResponseEntity<VocabularyDto> registerVoca(
        @RequestBody @Valid VocabularyDto vocadto
        ) {
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vocabularyService.registerVocabulary(vocadto));
    }




    @Operation(summary = "Update a vocabulary by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vocabulary updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VocabularyDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Vocabulary not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PutMapping(path = "/update/{vocaId}")
    public ResponseEntity<VocabularyDto> updateVocaList(
            @RequestBody @Valid VocabularyDto dto,
            @PathVariable @Valid UUID vocaId
            ) {
        return ResponseEntity.ok(vocabularyService.uptadeVocabulary(vocaId, dto));
    }

    /**
    * DELETE
    * @param vocaId
    * @return
    */
    @Operation(summary = "delete a vocabulary by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vocabulary deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VocabularyDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Vocabulary not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @DeleteMapping(path = "/delete/")
    public ResponseEntity<VocabularyDto> deleteVocabulary(
            @RequestParam @Valid UUID vocaId
            ) {
        return ResponseEntity.ok(vocabularyService.deleteVocabulary(vocaId));
    }
  
}
