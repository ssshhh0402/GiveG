import axios from 'axios';

function createInstance(){
    const instance = axios.create({
        baseURL: "http://i02b202.p.ssafy.io:8081",
        headers:{
            "Content-Type": "application/json",
        }
    });
    return instance;
}


export {createInstance}