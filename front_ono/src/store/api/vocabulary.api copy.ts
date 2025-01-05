
import { CreateVocabularyDto, VocabularyDto } from "../../models/vocabulary.model";
import baseApi from "./base.api";

export const vocabularyApi = baseApi.injectEndpoints({
    endpoints: (builder) => ({
        /**
         * Endpoint pour obtenir le liste de vocabulaire par son ID
         */
        getVocabularyByID: builder.query<VocabularyDto, string>({
            query: (id) => ({
                method: 'GET',
                url: `/vocabulary/id/${id}`,
            }),
        }),

        /**
         * Endpoint pour obtenir tous les vocabulaires d'une liste
         */
        getAllVocabularyInList: builder.query<VocabularyDto[], string>({
            query: (idVocaList) => ({
                method: 'GET',
                url: `/vocabulary/vocabulary-list/?idVocaList=${idVocaList}`,
            }),
        }),

        /**
         * Endpoint pour rechercher un liste de vocabulaire par type dans une liste
         */
        searchVocabularyInListBytype: builder.query<VocabularyDto, { idVocaList: string, type: string }>({
            query: ({ idVocaList, type }) => ({
                method: 'GET',
                url: `/vocabulary/vocabulary-list/type/`,
                params: { idVocaList, type },
            }),
        }),

        /**
         * Endpoint pour mettre à jour un vocabulaire
         */
        updateVocabulary: builder.mutation<VocabularyDto, { vocaId: string, dto: CreateVocabularyDto }>({
            query: ({ vocaId, dto }) => ({
                method: 'PUT',
                url: `/vocabulary/update/${vocaId}`,
                params: { vocaId },
                data: dto,
            }),
        }),

        /**
         * Endpoint pour supprimer un liste de vocabulaire
         */
        deleteVocabulary: builder.mutation<VocabularyDto, string>({
            query: (vocaId) => ({
                method: 'DELETE',
                url: `/vocabulary/delete/?vocaListId=${vocaId}`,
            }),
        }),

        /**
         * Endpoint pour enregistrer un liste de vocabulaire
         */
        registerVocabulary: builder.mutation<VocabularyDto, CreateVocabularyDto>({
            query: (dto) => ({
                method: 'POST',
                url: `/vocabulary/register`,
                body:dto,
            }),
        }),
    }),
    overrideExisting: false, // Eviter de remplacer les endpoints existants
});

export const {
    useGetVocabularyByIDQuery,
    useGetAllVocabularyInListQuery,
    useDeleteVocabularyMutation,
    useRegisterVocabularyMutation,
    useSearchVocabularyInListBytypeQuery,
    useUpdateVocabularyMutation
} = vocabularyApi;
