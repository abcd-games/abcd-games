import {validateErrors} from "./Error-helper.ts";
import {CSSProperties} from "react";

export const validationDanger = "text-danger"
export const validationWarning = "text-warning"
export const validationSuccess ="text-success"

export type ValidationMessageColors = typeof validationDanger | typeof validationWarning | typeof validationSuccess

export const validationTextSmall : CSSProperties = {fontSize: 14}
export const validationTextBig : CSSProperties = {fontSize:16}

export type ValidationMessageSize = typeof validationTextSmall | typeof validationTextBig

export type Validation={
    required?: {
        value:boolean,
        message: string,
    },
    minLength?: {
        value:number
        message: string
    },
    maxLength?: {
        value:number,
        message:string
    },
    pattern?: {
        value: RegExp,
        message: string,
    }
}

export const name_validation : Validation = {
        required: {
            value: true,
            message: 'required',
        },
        minLength: {
            value:3,
            message:"3 characters min"
        },
        maxLength: {
            value: 20,
            message: '20 characters max',
        },
}

export const password_validation = {
        required: {
            value: true,
            message: 'required',
        },
        minLength: {
            value: 6,
            message: 'min 6 characters',
        },
}

export const num_validation = {
        required: {
            value: true,
            message: 'required',
        },
}

export const email_validation = {
        required: {
            value: true,
            message: 'required',
        },
        pattern: {
            value:
                /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
            message: 'not valid',
        },
}

export const checkFieldValid = (value: string, validation: Validation) =>{
    return validateErrors(value,validation).length === 0;
}
