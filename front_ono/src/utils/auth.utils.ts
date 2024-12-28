import { AuthLocalStorage } from "../models/authentication.model";

export const getAuthFromLocalStorage = (): AuthLocalStorage => {
    const result =  {
        id: localStorage.getItem('id') ?? undefined,
        token: localStorage.getItem('token') ?? undefined,
        expiresAt: localStorage.getItem('expiresAt') ?? undefined
    }
    console.log('getAuthFromLocalStorage result', result);
    return result;
}

export const resetAuthLocalStorage = (): void => {
    localStorage.removeItem('id');
    localStorage.removeItem('token');
    localStorage.removeItem('expiresAt');
}