import { configureStore } from "@reduxjs/toolkit";
import { setupListeners } from "@reduxjs/toolkit/query";
import baseApi from "./api/base.api";
import userSlice from "./slices/users.slice";
import authenticationSlice from "./slices/authentication.slice";
import { exceptionListener } from "./middlewares/error.middleware";
import { authenticationApi } from "./api/authentication.api";
import toastSlice from "./slices/toast.slice";
import studyProgramSlice from "./slices/study-program.slice";

const reduxStore = configureStore({
  reducer: {
    // apis
    [baseApi.reducerPath]: baseApi.reducer,
    [authenticationApi.reducerPath]: authenticationApi.reducer,
    // slices
    [userSlice.reducerPath]: userSlice.reducer,
    [authenticationSlice.reducerPath]: authenticationSlice.reducer,
    [toastSlice.reducerPath]: toastSlice.reducer,
    [studyProgramSlice.reducerPath] : studyProgramSlice.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware()
      .concat(exceptionListener)
      .concat(baseApi.middleware, authenticationApi.middleware),
  devTools: true,
});

export type RootState = ReturnType<typeof reduxStore.getState>;
export type AppDispatch = typeof reduxStore.dispatch;

setupListeners(reduxStore.dispatch);

export default reduxStore;