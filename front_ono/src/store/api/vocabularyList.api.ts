
import { CreateVocabularyListDto, VocabularyListDto } from "../../models/vocabularylist.model";
import baseApi from "./base.api";

export const vocabularyListApi = baseApi.injectEndpoints({
    endpoints: (builder) => ({
        /**
         * Endpoint pour obtenir le liste de vocabulaire par son ID
         */
        getVocabularyListByID: builder.query<VocabularyListDto, string>({
            query: (id) => ({
                method: 'GET',
                url: `/vocabulary-list/id/${id}`,
            }),
        }),

        /**
         * Endpoint pour obtenir tous les programmes d'étude d'un utilisateur
         */
        getVocabularyListsByOwner: builder.query<VocabularyListDto[], string>({
            query: (idUser) => ({
                method: 'GET',
                url: `/vocabulary-list/user/?idUser=${idUser}`,
            }),
        }),

        /**
         * Endpoint pour rechercher un liste de vocabulaire par nom ou utilisateur
         */
        searchVocabularyListByName: builder.query<VocabularyListDto, { name: string, idUser: string }>({
            query: ({ name, idUser }) => ({
                method: 'GET',
                url: `/vocabulary-list/search`,
                params: { name, idUser },
            }),
        }),

        /**
         * Endpoint pour mettre à jour un liste de vocabulaire
         */
        updateVocabularyList: builder.mutation<VocabularyListDto, { vocabularyListId: string, dto: VocabularyListDto }>({
            query: ({ vocabularyListId, dto }) => ({
                method: 'PUT',
                url: `/vocabulary-list/update/${vocabularyListId}`,
                data: dto,
            }),
        }),

        /**
         * Endpoint pour supprimer un liste de vocabulaire
         */
        deleteVocabularyList: builder.mutation<VocabularyListDto, string>({
            query: (vocabularyListId) => ({
                method: 'DELETE',
                url: `/vocabulary-list/delete/?vocaListId=${vocabularyListId}`,
            }),
        }),

        /**
         * Endpoint pour enregistrer un liste de vocabulaire
         */
        registerVocabularyList: builder.mutation<VocabularyListDto, CreateVocabularyListDto>({
            query: (dto) => ({
                method: 'POST',
                url: `/vocabulary-list/register`,
                body:dto,
            }),
        }),

        /**
         * ADD studyProgramm
         */
        addSPtoVocabularyList: builder.mutation<VocabularyListDto,{vocabularyListId: string, studyProgramId: string}>({
            query: ({vocabularyListId, studyProgramId}) =>({
                method: 'PUT',
                url: `/vocabulary-list/add-study-program/`,
                params: {vocabularyListId, studyProgramId}
            }),
        }),

        /**
         * Endpoint pour obtenir tous les listes de vocabulaire
         */
        getAllVocabularyLists: builder.query<VocabularyListDto[], void>({
            query: () => ({
                method: 'GET',
                url: `/vocabulary-list/all`,
            }),
        }),

        /**
         * Endpoint pour obtenir tous les listes de vocabulaire publics
         */
        getVocabularyListsPublic: builder.query<VocabularyListDto[], void>({
            query: () => ({
                method: 'GET',
                url: `/vocabulary-list/public`,
            }),
        }),
    }),
    overrideExisting: false, // Eviter de remplacer les endpoints existants
});

export const {
    useGetVocabularyListByIDQuery,
    useGetVocabularyListsByOwnerQuery,
    useSearchVocabularyListByNameQuery,
    useUpdateVocabularyListMutation,
    useDeleteVocabularyListMutation,
    useRegisterVocabularyListMutation,
    useGetAllVocabularyListsQuery,
    useGetVocabularyListsPublicQuery,
    useAddSPtoVocabularyListMutation
} = vocabularyListApi;
