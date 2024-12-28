import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { AuthRequestDto, AuthResponseDto } from "../../models/authentication.model";
import config from "../../config";


export const authenticationApi = createApi({
    reducerPath: 'authenticationApi',
    baseQuery: fetchBaseQuery({ baseUrl: config.backendBaseUrl }),
    endpoints: (builder) => ({
        login: builder.mutation<AuthResponseDto, AuthRequestDto>({
            query: ({email, password}) => ({
                method: 'POST',
                url: `/auth/login`,
                body: {
                    email,
                    password
                }
            })
        })
    })
});

export const {
   useLoginMutation
} = authenticationApi;