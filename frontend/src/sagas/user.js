import {fork,put,takeEvery,call,all} from 'redux-saga/effects';
import {createInstance} from '../api/index'
import {createBrowserHistory} from 'history'
import {
    LOG_IN_SUCCESS,
    LOG_IN_FAILURE,
    LOG_IN_REQUEST
} from '../reducers/user';
import { Router } from 'react-router-dom';
const history = createBrowserHistory();
const instance = createInstance(); 

function* logInAPI(data){
    console.log('-----loginAPI------')
    console.log(data)
    return instance.post('/api/users/login',JSON.stringify(data));
}

function* logIn(action){
    console.log(action)
    console.log('00000000000000000000000000000000')
    try{
        console.log('--------logIn--------')
        console.log(action.payload)
        const result = yield call(logInAPI, action.payload);
        console.log('-------result-------')
        console.log(result)
        yield put({
            type: LOG_IN_SUCCESS,
            payload: result.data
        });
    } catch(error){
        yield put({error, type: LOG_IN_FAILURE, reason: error.response && error.response.data.reason || '서버에러'});
    }
}

function* watchLogin(){
    console.log('watchLogin')
    console.log(LOG_IN_REQUEST)
    yield takeEvery('LOG_IN_REQUEST', logIn)
}
//로그인-----------------------------------------------
function* kakaoLoginApi(data){
    //백엔드에 저장하는 코드
//    return instance.post('/api/users/kakao', JSON.stringfy(data));
    const result = {
        status : 'Success'
    }
    return result
}

function* kakaoApi(action){
    console.log('---------kakaoApi---------')
    console.log(action)
    console.log('-------------------')
    try{
        const result = yield call(kakaoLoginApi, action.payload)
        if (result.status === 'Success'){
            console.log('로그인 성공')
            //history.push('./main')
            window.location.href='./main'
        }else{
            alert('이미 가입된 유저 입니다.')
        }
    }catch{
        alert('서버 오류 입니다')
    }
}

function* kakao(action){
    console.log('---------Sagas kakao---------');
    console.log(action);
    console.log('------------------------');
    yield call(kakaoApi, action.payload);
}

function* watchKakao(){
    console.log('watchKakao')
    yield takeEvery('LOG_IN_KAKAO', kakao)
}

//카카오로그인-------------------------------------
export default function* rootSaga() {
    yield all([(watchLogin()),watchKakao()]);
}
