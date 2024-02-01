export default function RegisterPage(){
    return (
        <form>
            <h1 className="h3 mb-3 fw-normal">Please register to play games</h1>

            <div className="form-floating">
                <input type="text" className="form-control" id="floatingInput" placeholder="Username"/>
                <label htmlFor="floatingInput">Username</label>
            </div>
            <div className="form-floating">
                <input type="email" className="form-control" id="floatingInput" placeholder="name@example.com"/>
                <label htmlFor="floatingInput">Email address</label>
            </div>
            <div className="form-floating">
                <input type="password" className="form-control" id="floatingPassword" placeholder="Password"/>
                <label htmlFor="floatingPassword">Password</label>
            </div>
            <button className="btn btn-primary w-100 py-2 my-2" type="submit">Register now</button>
        </form>
    );
}