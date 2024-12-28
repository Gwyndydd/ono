import { UserDto, UserRegistrationDto } from "../../models/users.model";
import baseApi from "./base.api";

export const usersApi = baseApi.injectEndpoints({
    endpoints: (builder) => ({
        register: builder.mutation<UserDto, UserRegistrationDto>({
            query: (dto) => ({
                method: "POST",
                url: `/users/register`,
                body: dto
            })
        })
    }),
    overrideExisting: false
});

export const {
    useRegisterMutation
} = usersApi;
