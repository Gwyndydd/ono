import { isRejectedWithValue, Middleware, MiddlewareAPI } from "@reduxjs/toolkit";
import { ApiErrorCode } from "../../models/api.model";
import { isApiErrorResponse } from "../../utils/error.utils";
import { disconnect } from "../slices/authentication.slice";
import { resetAuthLocalStorage } from "../../utils/auth.utils";
import { addToast } from "../slices/toast.slice";
import { uid } from "radash";

const DISONNECT_ON_ERRORS = [ApiErrorCode.AUTH_002, ApiErrorCode.AUTH_003, ApiErrorCode.AUTH_004];

export const exceptionListener: Middleware = (listenerApi: MiddlewareAPI) => (next) => (action) => {

    if (isRejectedWithValue(action) && action.payload) {
        if (isApiErrorResponse(action.payload)
            && action.payload.data?.code
            && DISONNECT_ON_ERRORS.includes(action.payload.data?.code)
        ) {
            console.warn('Disconnecting user due to error', action.payload.data?.code);
            
            resetAuthLocalStorage();
            listenerApi.dispatch(disconnect());
            listenerApi.dispatch(addToast({ id: uid(7), message: 'You have been disconnected', type: 'error' }));
            return;
        }
    }

    return next(action);
};