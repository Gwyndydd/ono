export type StudyProgramDto = {
    id: string;
    name: string;
    description: string;
    idOwner: string;
    prive: boolean;
}

export type CreateStudyProgramDto = {
    name: string;
    description?: string;
    idOwner: string;
    prive: boolean;
}
