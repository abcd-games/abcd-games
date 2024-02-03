export type AppUser = {
    id: number,
    username: string,
    email: string,
    role: string,
}

export type AppUserRequest = {
    username: string,
    email: string,
    password: string,
}
