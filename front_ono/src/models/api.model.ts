export enum ApiErrorCode {
    AUTH_001 = "AUTH_001", // Authentication failed
    AUTH_002 = "AUTH_002", // Unauthorized
    AUTH_003 = "AUTH_003", // JWT invalid
    AUTH_004 = "AUTH_004", // JWT expired
    UNKNOWN_ERROR = "UNKNOWN_ERROR"
}

export type ApiError = {
    code: ApiErrorCode;
    description: string;
    timestamp: string;
}