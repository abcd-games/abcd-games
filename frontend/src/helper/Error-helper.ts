import {Validation} from "./Validation-helper.ts";

export type FormErrors = FormError[]
export type FormError = {
    message: string
}


export const validateErrors = (value: string| number, validation: Validation) : FormErrors=>{
    if (!validation){
        return []
    }
    const errors : FormErrors = [];
    if (typeof value === "string") {
        if (validation.required) {
            if (value.length === 0) {
                errors.push({message: validation.required.message})
            }
        }
        if (validation.maxLength) {
            if (value.length > validation.maxLength.value) {
                errors.push({message: validation.maxLength.message})
            }
        }
        if (validation.minLength) {
            if (value.length < validation.minLength.value) {
                errors.push({message: validation.minLength.message})
            }
        }
        if (validation.pattern) {
            if (!value.match(validation.pattern.value)) {
                errors.push({message: validation.pattern.message})
            }
        }
    }
    if (typeof value === "number"){

    }
    return errors;
}