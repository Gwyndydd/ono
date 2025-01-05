import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { VocabularyDto } from '../../models/vocabulary.model';
import { vocabularyApi } from '../api/vocabulary.api copy';

interface VocabularyState {
    vocabularyList: VocabularyDto[];
    currentVocabulary?: VocabularyDto | null;
    loading: boolean;
    error: string | null;
}

const initialState: VocabularyState = {
    vocabularyList: [],
    currentVocabulary: undefined,
    loading: false,
    error: null,
};

// Création du slice
const vocabularySlice = createSlice({
    name: 'vocabulary',
    initialState,
    reducers: {
        setVocabularyList: (state, action: PayloadAction<VocabularyDto[]>) => {
            state.vocabularyList = action.payload;
        },
        setCurrentVocabulary: (state, action: PayloadAction<VocabularyDto>) => {
            state.currentVocabulary = action.payload;
        },
        setError: (state, action: PayloadAction<string>) => {
            state.error = action.payload;
        },
    },
    extraReducers: (builder) => {
        // Intégration des actions de l'API générée par `baseApi`

        // `getVocabularyListByID`
        builder.addMatcher(
            vocabularyApi.endpoints.getVocabularyByID.matchFulfilled,
            (state, { payload }) => {
                state.currentVocabulary = payload;
                state.loading = false;
            }
        );
        builder.addMatcher(
            vocabularyApi.endpoints.getVocabularyByID.matchPending,
            (state) => {
                state.loading = true;
                state.error = null;
            }
        );
        builder.addMatcher(
            vocabularyApi.endpoints.getVocabularyByID.matchRejected,
            (state, { error }) => {
                state.loading = false;
                state.error = error.message || 'Error can not get vocabulary';
            }
        );

        // `getAllVocabularyLists`
        builder.addMatcher(
            vocabularyApi.endpoints.getAllVocabularyInList.matchFulfilled,
            (state, { payload }) => {
                state.vocabularyList = payload;
                state.loading = false;
            }
        );
        builder.addMatcher(
            vocabularyApi.endpoints.getAllVocabularyInList.matchPending,
            (state) => {
                state.loading = true;
                state.error = null;
            }
        );
        builder.addMatcher(
            vocabularyApi.endpoints.getAllVocabularyInList.matchRejected,
            (state, { error }) => {
                state.loading = false;
                state.error = error.message || 'Error can not get all vocabulary in list';
            }
        );

        // Vous pouvez ajouter les autres endpoints de la même manière.
    },
});

// Exportez les actions
export const { setVocabularyList, setCurrentVocabulary, setError } = vocabularySlice.actions;

// Exportez le reducer pour l'intégrer dans votre store Redux
export default vocabularySlice;
