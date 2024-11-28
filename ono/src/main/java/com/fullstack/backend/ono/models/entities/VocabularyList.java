package com.fullstack.backend.ono.models.entities;

import java.util.UUID;

import com.fullstack.backend.ono.models.constants.Langues;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "listes_vocabulaire")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VocabularyList extends AuditDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "name", columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

    @Column(name = "langue_etudie", columnDefinition = "VARCHAR(255)", nullable = false)
    @Enumerated(EnumType.STRING)
    private Langues langueEtudie;

    @Column(name = "langue_definition", columnDefinition = "VARCHAR(255)", nullable = false)
    @Enumerated(EnumType.STRING)
    private Langues langueDefinition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programme_etude", columnDefinition = "UUID")
    private StudyProgram studyProgram;

    //Column pour définir le créateur et le propriétaire de la liste
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner", columnDefinition = "UUID", nullable = false)
    private User owner;

    //Column pour définir la visibilité des listes aux autres users
    @Column(name = "visibility", columnDefinition = "BOOLEAN", nullable = false)
    private Boolean prive;






    
}
