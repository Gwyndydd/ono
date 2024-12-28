import { createSlice } from "@reduxjs/toolkit";
import { authenticationApi } from "../api/authentication.api";
import { DateTime } from "luxon";
import { getAuthFromLocalStorage, resetAuthLocalStorage } from "../../utils/auth.utils";

type AuthState = {
    isAuthenticated: boolean;
    token?: string;
    expiresAt?: string;
}

const getInitialAuthState = (): AuthState => {
    let auth = getAuthFromLocalStorage();
    if (auth.expiresAt && DateTime.fromISO(auth.expiresAt) < DateTime.now()) {
        auth = {
            token: undefined,
            expiresAt: undefined
        }
        resetAuthLocalStorage();
    }
    return {
        isAuthenticated: auth.token !== undefined && auth.expiresAt !== undefined,
        token: auth.token,
        expiresAt: auth.expiresAt
    }
}

const authenticationSlice = createSlice({
    name: "authentication",
    initialState: getInitialAuthState(),
    reducers: {
        disconnect: (state) => {
            state.isAuthenticated = false;
            state.token = undefined;
            state.expiresAt = undefined;
        }
    },
    extraReducers: (builder) => {
        builder
            .addMatcher(authenticationApi.endpoints.login.matchFulfilled, (state, action) => {
                state.isAuthenticated = true;
                state.token = action.payload.token;
                state.expiresAt = DateTime.now().plus({ millisecond: action.payload.expiresIn}).toISO();
            })
    }
});

export const { disconnect } = authenticationSlice.actions;

export default authenticationSlice;