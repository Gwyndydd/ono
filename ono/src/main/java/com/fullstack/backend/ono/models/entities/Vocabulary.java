package com.fullstack.backend.ono.models.entities;

import java.util.UUID;

import com.fullstack.backend.ono.models.constants.TypeVocabulary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = " vocabulaire")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Vocabulary extends AuditDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idVoca;

    @Column(name = "word", columnDefinition = "VARCHAR(255)", nullable = false)
    private String word;

    @Column(name = "definition", columnDefinition = "VARCHAR(255)")
    private String definition;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "liste_vocabulaire", columnDefinition = "UUID", nullable = false)
    private UUID idListeVoca;

    @Column(name = "type", columnDefinition = "VARCHAR(255)", nullable = false)
    private TypeVocabulary type;
    
}
