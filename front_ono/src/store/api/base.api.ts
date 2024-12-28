import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

import config from "../../config";
import { RootState } from "../redux.store";

const baseApi = createApi({
  reducerPath: "baseApi",
  baseQuery: fetchBaseQuery({
    baseUrl: config.backendBaseUrl,
    prepareHeaders: (headers, { getState }) => {
        const authToken = (getState() as RootState).authentication.token;
        if (authToken) {
            headers.set('Authorization', `Bearer ${authToken}`);
        }
        return headers;
    },
  }),
  endpoints: () => ({}),
});

export default baseApi;