import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { VocabularyListDto } from '../../models/vocabularylist.model';
import { vocabularyListApi } from '../api/vocabularyList.api';

interface VocabularyListState {
    vocabularyLists: VocabularyListDto[];
    currentVocabularyList?: VocabularyListDto | null;
    loading: boolean;
    error: string | null;
}

const initialState: VocabularyListState = {
    vocabularyLists: [],
    currentVocabularyList: undefined,
    loading: false,
    error: null,
};

// Création du slice
const vocabularyListSlice = createSlice({
    name: 'vocabularyList',
    initialState,
    reducers: {
        setVocabularyLists: (state, action: PayloadAction<VocabularyListDto[]>) => {
            state.vocabularyLists = action.payload;
        },
        setCurrentVocabularyList: (state, action: PayloadAction<VocabularyListDto>) => {
            state.currentVocabularyList = action.payload;
        },
        setError: (state, action: PayloadAction<string>) => {
            state.error = action.payload;
        },
    },
    extraReducers: (builder) => {
        // Intégration des actions de l'API générée par `baseApi`

        // `getVocabularyListByID`
        builder.addMatcher(
            vocabularyListApi.endpoints.getVocabularyListByID.matchFulfilled,
            (state, { payload }) => {
                state.currentVocabularyList = payload;
                state.loading = false;
            }
        );
        builder.addMatcher(
            vocabularyListApi.endpoints.getVocabularyListByID.matchPending,
            (state) => {
                state.loading = true;
                state.error = null;
            }
        );
        builder.addMatcher(
            vocabularyListApi.endpoints.getVocabularyListByID.matchRejected,
            (state, { error }) => {
                state.loading = false;
                state.error = error.message || 'Error can not get vocabulary list';
            }
        );

        // `getAllVocabularyLists`
        builder.addMatcher(
            vocabularyListApi.endpoints.getAllVocabularyLists.matchFulfilled,
            (state, { payload }) => {
                state.vocabularyLists = payload;
                state.loading = false;
            }
        );
        builder.addMatcher(
            vocabularyListApi.endpoints.getAllVocabularyLists.matchPending,
            (state) => {
                state.loading = true;
                state.error = null;
            }
        );
        builder.addMatcher(
            vocabularyListApi.endpoints.getAllVocabularyLists.matchRejected,
            (state, { error }) => {
                state.loading = false;
                state.error = error.message || 'Error can not get all vacabulary list';
            }
        );

        // Vous pouvez ajouter les autres endpoints de la même manière.
    },
});

// Exportez les actions
export const { setVocabularyLists, setCurrentVocabularyList, setError } = vocabularyListSlice.actions;

// Exportez le reducer pour l'intégrer dans votre store Redux
export default vocabularyListSlice;
