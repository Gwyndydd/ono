import { createSlice } from "@reduxjs/toolkit";
import { UserDto, usersApi } from "../api/users.api"

type UserState = {
    user?: UserDto;
}

const userInitialState: UserState = {
    user: undefined,
};

const userSlice = createSlice({
    name: "user",
    initialState: userInitialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addMatcher(usersApi.endpoints.register.matchFulfilled, (state, action) => {
                state.user = action.payload
            })
    }
});

export default userSlice;