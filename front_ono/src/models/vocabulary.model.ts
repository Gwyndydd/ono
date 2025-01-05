export type VocabularyDto = {
    id: string;
    word: string;
    definition: string;
    idList: string;
    type:string;
}

export type CreateVocabularyDto = {
    word: string;
    definition?: string;
    idList: string;
    type:string;
}