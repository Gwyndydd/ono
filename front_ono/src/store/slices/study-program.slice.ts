import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { studyProgramApi } from '../api/studyprogram.api'; // Importez l'API de study program
import { StudyProgramDto } from "../../models/studyprogram.model"; // Assurez-vous que le DTO est bien importé

interface StudyProgramState {
    studyPrograms: StudyProgramDto[];
    currentStudyProgram?: StudyProgramDto | null;
    loading: boolean;
    error: string | null;
}

const initialState: StudyProgramState = {
    studyPrograms: [],
    currentStudyProgram: undefined,
    loading: false,
    error: null,
};

// Création du slice
const studyProgramSlice = createSlice({
    name: 'studyProgram',
    initialState,
    reducers: {
        setStudyPrograms: (state, action: PayloadAction<StudyProgramDto[]>) => {
            state.studyPrograms = action.payload;
        },
        setCurrentStudyProgram: (state, action: PayloadAction<StudyProgramDto>) => {
            state.currentStudyProgram = action.payload;
        },
        setError: (state, action: PayloadAction<string>) => {
            state.error = action.payload;
        },
    },
    extraReducers: (builder) => {
        // Intégration des actions de l'API générée par `baseApi`

        // `getStudyProgramByID`
        builder.addMatcher(
            studyProgramApi.endpoints.getStudyProgramByID.matchFulfilled,
            (state, { payload }) => {
                state.currentStudyProgram = payload;
                state.loading = false;
            }
        );
        builder.addMatcher(
            studyProgramApi.endpoints.getStudyProgramByID.matchPending,
            (state) => {
                state.loading = true;
                state.error = null;
            }
        );
        builder.addMatcher(
            studyProgramApi.endpoints.getStudyProgramByID.matchRejected,
            (state, { error }) => {
                state.loading = false;
                state.error = error.message || 'Error can not get study program';
            }
        );

        // `getAllStudyPrograms`
        builder.addMatcher(
            studyProgramApi.endpoints.getAllStudyPrograms.matchFulfilled,
            (state, { payload }) => {
                state.studyPrograms = payload;
                state.loading = false;
            }
        );
        builder.addMatcher(
            studyProgramApi.endpoints.getAllStudyPrograms.matchPending,
            (state) => {
                state.loading = true;
                state.error = null;
            }
        );
        builder.addMatcher(
            studyProgramApi.endpoints.getAllStudyPrograms.matchRejected,
            (state, { error }) => {
                state.loading = false;
                state.error = error.message || 'Error can not get all study programs';
            }
        );

        // Vous pouvez ajouter les autres endpoints de la même manière.
    },
});

// Exportez les actions
export const { setStudyPrograms, setCurrentStudyProgram, setError } = studyProgramSlice.actions;

// Exportez le reducer pour l'intégrer dans votre store Redux
export default studyProgramSlice;
