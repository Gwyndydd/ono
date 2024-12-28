import { createSlice } from "@reduxjs/toolkit";
import { MyEntityDto } from "../../models/test.model";

import { helloWorldApi } from "../api/hello-world.api";

type HelloWorldState = {
    myEntity?: MyEntityDto;
    isPending: boolean;
};

const helloWorldInitialState: HelloWorldState = {
    myEntity: undefined,
    isPending: false
};

const helloWorldSlice = createSlice({
    name: "helloWorld",
    initialState: helloWorldInitialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addMatcher(helloWorldApi.endpoints.getHello.matchFulfilled, (state, action) => {
                console.log("getHello fulfilled", action.payload);
                state.isPending = false;
            })
            .addMatcher(helloWorldApi.endpoints.getHello.matchPending, (state, action) => {
                console.log("getHello pending : ", action);
                state.isPending = true;
            })
            .addMatcher(helloWorldApi.endpoints.getHello.matchRejected, (_state, action) => {
                console.log("getHello rejected", action.error);
            })
            .addMatcher(helloWorldApi.endpoints.registerEntity.matchFulfilled, (state, action) => {
                state.myEntity = action.payload;
                state.isPending = false;
            })

    }
});

export default helloWorldSlice;