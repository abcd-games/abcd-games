import {ValidationMessageColors, ValidationMessageSize} from "../helper/Validation-helper.ts";

export type Props={
    validationMessage: string
    validationMessageColor: ValidationMessageColors
    validationMessageSize : ValidationMessageSize
}

function ValidationMessage(props: Props) {
    return (
        <>
            <p
                className={props.validationMessageColor}
                style={props.validationMessageSize}>{props.validationMessage}
            </p>
        </>
    );
}

export default ValidationMessage;
