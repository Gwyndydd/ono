import { StudyProgramDto, CreateStudyProgramDto } from "../../models/studyprogram.model";
import baseApi from "./base.api";

export const studyProgramApi = baseApi.injectEndpoints({
    endpoints: (builder) => ({
        /**
         * Endpoint pour obtenir le programme d'étude par son ID
         */
        getStudyProgramByID: builder.query<StudyProgramDto, string>({
            query: (id) => ({
                method: 'GET',
                url: `/study-program/id/${id}`,
            }),
        }),

        /**
         * Endpoint pour obtenir tous les programmes d'étude d'un utilisateur
         */
        getStudyProgramsByOwner: builder.query<StudyProgramDto[], string>({
            query: (idUser) => ({
                method: 'GET',
                url: `/study-program/user/?idUser=${idUser}`,
            }),
        }),

        /**
         * Endpoint pour rechercher un programme d'étude par nom ou utilisateur
         */
        searchStudyProgramByName: builder.query<StudyProgramDto, { name: string, idUser: string }>({
            query: ({ name, idUser }) => ({
                method: 'GET',
                url: `/study-program/search`,
                params: { name, idUser },
            }),
        }),

        /**
         * Endpoint pour mettre à jour un programme d'étude
         */
        updateStudyProgram: builder.mutation<StudyProgramDto, { studyProgramId: string, dto: CreateStudyProgramDto }>({
            query: ({ studyProgramId, dto }) => ({
                method: 'PUT',
                url: `/study-program/update/${studyProgramId}`,
                data: dto,
            }),
        }),

        /**
         * Endpoint pour supprimer un programme d'étude
         */
        deleteStudyProgram: builder.mutation<StudyProgramDto, string>({
            query: (studyProgramId) => ({
                method: 'DELETE',
                url: `/study-program/delete/${studyProgramId}`,
            }),
        }),

        /**
         * Endpoint pour enregistrer un programme d'étude
         */
        registerStudyProgram: builder.mutation<StudyProgramDto, CreateStudyProgramDto>({
            query: (dto) => ({
                method: 'POST',
                url: `/study-program/register`,
                body:dto,
            }),
        }),

        /**
         * Endpoint pour obtenir tous les programmes d'études
         */
        getAllStudyPrograms: builder.query<StudyProgramDto[], void>({
            query: () => ({
                method: 'GET',
                url: `/study-program/all`,
            }),
        }),

        /**
         * Endpoint pour obtenir tous les programmes d'études publics
         */
        getStudyProgramsPublic: builder.query<StudyProgramDto[], void>({
            query: () => ({
                method: 'GET',
                url: `/study-program/public`,
            }),
        }),
    }),
    overrideExisting: false, // Eviter de remplacer les endpoints existants
});

export const {
    useGetStudyProgramByIDQuery,
    useGetStudyProgramsByOwnerQuery,
    useSearchStudyProgramByNameQuery,
    useUpdateStudyProgramMutation,
    useDeleteStudyProgramMutation,
    useRegisterStudyProgramMutation,
    useGetAllStudyProgramsQuery,
    useGetStudyProgramsPublicQuery,
} = studyProgramApi;
