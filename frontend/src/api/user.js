import {createInstance} from "./index.js"

const instance = createInstance();

function login(email, password, success, fail){
    const body = {
        email: email,
        password: password
    };
    alert(body)
    instance
    .post("/api/users/login", JSON.stringify(body))
    .then(success)
    .catch(fail);
}

export {login};