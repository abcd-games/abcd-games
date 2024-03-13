export type Props={
    errorMessage: string
}
function ValidationError(props: Props) {
    return (
        <>
            <p>{props.errorMessage}</p>
        </>
    );
}

export default ValidationError;