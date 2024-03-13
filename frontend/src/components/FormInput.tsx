import {Validation} from "../helper/Validation-helper.ts";
import {ChangeEvent, useState} from "react";
import {FormErrors, validateErrors} from "../helper/Error-helper.ts";
import ValidationError from "./ValidationError.tsx";

export type Props = {
    name: string,
    label: string,
    type: string,
    id: string,
    placeholder: string,
    validation: Validation,
    className: string,
    setFormValue: (event: ChangeEvent<HTMLInputElement>) => void
}

export function FormInput(props: Props) {
    const [errors, setErrors] = useState<FormErrors>([])

    const onValueChange = (event: ChangeEvent<HTMLInputElement>) => {
        const {value} = event.target;
        setErrors(validateErrors(value, props.validation));
        props.setFormValue(event);
    }

    return (
        <>
            <input
                onChange={onValueChange}
                id={props.id}
                type={props.type}
                className={props.className}
                placeholder={props.placeholder}
                name={props.name}
                required={props.validation.required?.value}
                pattern={JSON.stringify(props.validation.pattern?.value)}
            />
            <label htmlFor={props.id}>{props.label}</label>
            {errors &&
                <ValidationError errorMessage={errors[0]?.message}></ValidationError>
            }
        </>
    );
}

