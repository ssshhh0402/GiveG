import produce from 'immer';
import {createAction} from 'redux-actions';
import {Link} from 'react'
import { render } from '@testing-library/react';
// Redux-regist 추가

import {REHYDRATE} from 'redux-persist/lib/constants'
// export const LOG_IN_REQUEST = 'USER/LOG_IN_REQUEST';
// export const LOG_IN_SUCCESS = 'USER/LOG_IN_SUCCESS';
// export const LOG_IN_FAILURE = 'USER/LOG_IN_FAILURE';
const LOG_IN = 'USER/LOG_IN_REQUEST';
const LOG_SUCCESS = 'USER/LOG_IN_SUCCESS';
const LOG_FAILURE = 'USER/LOG_IN_FAILURE';
const LOG_KAKAO = 'USER/LOG_IN_KAKAO';
export const LOG_IN_REQUEST = createAction(LOG_IN);
export const LOG_IN_SUCCESS = createAction(LOG_SUCCESS);
export const LOG_IN_FAILURE = createAction(LOG_FAILURE);
export const LOG_IN_KAKAO = createAction(LOG_KAKAO);
const initialState = {
    isLogginIn: false,
    logInErrorReason : '',
    email : '',
    password: '',
    access_token:'',
    refresh_token:'',
    persistedState: {}
    }



const reducer = (state = initialState, action) =>{
   switch (action.type){
       case REHYDRATE:
           return {...state, persistedState: action.payload};
        case 'LOG_IN_REQUEST':{
            console.log('---------reducer 지나는중-------')
            return {isLoggedIn : true}
            }
        case LOG_IN_SUCCESS: {
            console.log('-----------reducer Success')
            return {isLoggedIn: true, 
                    email:action.payload['email'],
                    password:action.payload['password']}}
        case LOG_IN_FAILURE: {
            console.log('-------reducer fail')
            alert(action.reason + "때문에 로그인에 실패하셨습니다")
            return{loginErrorReason : action.reason}
            }
        case 'LOG_IN_KAKAO':{
            console.log('--------reducer kakao')
            console.log(action['payload'])
            return{
                isLoggedIn:true,
                access_token: action['payload']['access_token'],
                refresh_token: action['payload']['refresh_token'],
                email:action['payload']['email']
            }
            }
        default: {
            console.log('-------reducer default')
            return state;
        }
        }
    }
export default reducer;