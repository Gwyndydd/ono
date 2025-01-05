export type VocabularyListDto = {
    id: string;
    name: string;
    langeEtudie: string;
    langueDefinition: string;
    idProgrammeEtude: string;
    idOwner: string;
    prive:boolean

}

export type CreateVocabularyListDto = {
    name: string;
    langeEtudie: string;
    langueDefinition: string;
    idProgrammeEtude?: string;
    idOwner: string;
    prive:boolean

}