import {fork,put,takeEvery,call,all} from 'redux-saga/effects';
import {createInstance} from '../api/index';
import {createBrowserHistory} from 'history';
import axios from 'axios';
import {
    LOG_IN_SUCCESS,
    LOG_IN_FAILURE,
    LOG_IN_REQUEST
} from '../reducers/user';
const instance = createInstance(); 

function* logInAPI(data){
    console.log('-----loginAPI------')
    console.log(data)
    return instance.post('/api/users/login',JSON.stringify(data));
}

function* logIn(action){
    
    try{
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
    yield takeEvery('LOG_IN_REQUEST', logIn)
}
//로그인-----------------------------------------------
function* kakaoLoginApi(data){
    const datas = {
        id : data['id'],
        email: data['email'],
        name : data['nickname'],
        picture: data['thumbnail'],
        role: 'admin'
    }
    instance.post('/api/v1/user',JSON.stringify(datas))
    .then((result) =>{
        if(result['status'] === 200){
            console.log('로그인 성공!')
            window.sessionStorage.setItem('email',data['email'])
            window.location.href="/"
        }
    })

}

function* kakaoApi(action){

    try{
        yield call(kakaoLoginApi, action)
    }catch(e){
        console.log(e)
        alert('서버 오류 입니다')
    }
}

function* kakao(action){

    yield call(kakaoApi, action['payload']);
}

function* watchKakao(){
    yield takeEvery('LOG_IN_KAKAO', kakao)
}
//카카오로그인-------------------------------------

function* paymentApi(action){
    instance.post('/api/v1/donation',{
        amount: action['money'],
        postsId: action['id'],
        userId : action['email']
    }).then(response => {
        if(response['status'] === 200){
            console.log(response.data.donationId)
            console.log(response.data.redirectUrl)
            window.sessionStorage.setItem('donationId', response.data.donationId)            
            window.location.href= response.data.redirectUrl
        }})
    }

function* payment(action){
    console.log('----------카카오 페이먼트 컴컴')
    console.log(action)
    console.log('-----------------------')
    yield call(paymentApi, action.payload)
}

function* watchPay(){
    console.log('watchPayment')
    yield takeEvery('KAKAO_PAYMENT', payment)
}
//카카오페이-------------------------------------
function* registerApi(action){
    console.log('RegisterApi 도착')
    console.log(action)
    console.log('-------------------')
    // const datas = {
    //     author:action['author'],
    //     content:action['content'],
    //     donationGoal:action['donationGoal'],
    //     endDate:action['endDate'],
    //     goalContent1:action['target1']['desc'],
    //     goalContent2:action['target2']['desc'],
    //     goalContent3:action['target3']['desc'],
    //     goalPercent1:action['target1']['percent'],
    //     goalPercent2:action['target2']['percent'],
    //     goalPercent3:action['target3']['percent'],
    //     image:action['image'],
    //     title:action['title']
    // }
    //Try catch로 등록하는거 해놓으면 될듯
    try{
        instance.post('/api/v1/posts',{
            params:{
               action
            }
        },{
            headers:{'Content-Type' : 'multipart/form-data'}
        }).then(response => {
            console.log(response)
        })
    }catch(e){
        console.log(e)}
}

function* register(action){
    console.log('-----------등록 컴컴')
    console.log(action)
    console.log('-------------------')
    yield call(registerApi, action.payload)
}

function* watchRegister(){
    console.log('watchRegister')
    yield takeEvery('Contents_Register', register)
}
//Contents 등록----------------------------------
export default function* rootSaga() {
    yield all([(watchLogin()),watchKakao(),watchPay(),watchRegister()]);
}
