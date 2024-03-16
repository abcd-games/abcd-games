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
    switch (typeof value) {
        case "string":
            if (validation.required && value.length === 0) {
                errors.push({ message: validation.required.message });
            }
            if (validation.maxLength && value.length > validation.maxLength.value) {
                errors.push({ message: validation.maxLength.message });
            }
            if (validation.minLength && value.length < validation.minLength.value) {
                errors.push({ message: validation.minLength.message });
            }
            if (validation.pattern && !validation.pattern.value.exec(value)) {
                errors.push({ message: validation.pattern.message });
            }
            break;
        case "number":
    }
    return errors;
}
