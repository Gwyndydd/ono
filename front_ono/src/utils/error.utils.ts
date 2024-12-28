import { SerializedError } from "@reduxjs/toolkit";
import { ApiError, ApiErrorCode } from "../models/api.model";

type ApiErrorResponse = {
    status: number,
    data?: ApiError
}

export function isApiErrorResponse(error: SerializedError | ApiErrorResponse): error is ApiErrorResponse {
    return (
        typeof error === "object" &&
        error != null &&
        "status" in error &&
        typeof error.status === "number"
    );
}

export function parseErrorResponse(error: SerializedError): ApiError {
    if (isApiErrorResponse(error) && error.data) {
        return {
            code: error.data?.code,
            description: error.data?.description,
            timestamp: error.data?.timestamp
        }
    } else {
        return {
            code: ApiErrorCode.UNKNOWN_ERROR,
            description: 'Unknown error',
            timestamp: new Date().toISOString(),
        };
    }
}
