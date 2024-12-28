export type AuthRequestDto = {
    email: string,
    password: string
}

export type AuthResponseDto = {
    id: string,
    email: string,
    token: string,
    expiresIn: number
}

export type AuthLocalStorage = {
    id?: string,
    token?: string,
    expiresAt?: string
}