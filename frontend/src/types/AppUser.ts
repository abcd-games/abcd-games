export type AppUser = {
    id: number,
    username: string,
    email: string,
    role: string,
    googleUser: GoogleUser | null,
}

export type AppUserRequest = {
    username: string,
    email: string,
    password: string,
}

type GoogleUser = {
    id: number,
    email: string,
    picture: string,
}
