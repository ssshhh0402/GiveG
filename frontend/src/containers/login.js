import React, { Component,useCallback } from 'react';
import {Link, Router, Route} from 'react-router-dom';
import {Form,Input,Button,Space,Card,Row,Col} from 'antd';
import {UserOutlined, LockOutlined} from '@ant-design/icons';
import {connect, useDispatch} from 'react-redux'; 
import KakaoLogin from 'react-kakao-login';
import styled from 'styled-components'
import '../css/login.css'
class LoginForm extends Component{
    
    responseFail = (err) => {
        console.log('------로그인 실패')
        console.log(err);
    }
    render(){
    return (
        <div className="container">
        <Space direction="vertical" style={{ paddingTop: "1vw"}}>
            <div id="Blank">
                <h1> 로그인 페이지</h1>
            </div >
            <Card style={{ width: "100%", height: "36vw", backgroundSize:"contain", backgroundImage:"url(/images/KAKAO_FRIENDs.jpg)"}}>
                <Row>
                    <Col span={12} style={{textAlign:"right"}}>
                    </Col>
                    <Col span={12} style={{paddingTop:"15vw", paddingLeft:"9vw"}}>
                        <img src="/images/explainKakao.png" width="90%"></img>
                    </Col>
                </Row>
                <div style={{ paddingLeft: '15vw', paddingTop: "5vw", margin: 'auto' }}>
                    <KakaoButton
                        jsKey={process.env.REACT_APP_KAKAO_LOGIN_API_KEY}
                        buttonText='카카오 로그인'
                        onSuccess={this.props.kakao}
                        onFailure={this.responseFail}
                        getProfile="True"
                    />
                </div>
            </Card>
        </Space>
    </div>
            );
        }
            
    };
const KakaoButton = styled(KakaoLogin)`
    padding: 0;
    width: 70%;
    height: 3vw;
    line-height: 1vw;
    color: #783c00;
    background-color: #FFEB00;
    border: 1px solid transparent;
    border-radius: 3px;
    font-size: 1.5vw;
    font-weight: bold;
    text-align: center;
    `
const mapStateToProps = state => {
    return {
        email: state.email,
        access_token : state.access_token,
    }
}
const mapDispachToProps = dispatch => {
    return {
        onFinish : (values) =>
        dispatch({
            type: 'LOG_IN_REQUEST', 
            payload: {
                email: values['email'],
                password : values['password']}
            }),
        kakao : (res) =>
        dispatch({
            type: 'LOG_IN_KAKAO',
            payload: {
                id : res['profile']['id'],
                access_token: res['response']['access_token'],
                refresh_token : res['response']['refresh_token'],     
                email : res['profile']['kakao_account']['email'],
                nickname : res['profile']['properties']['nickname'],
                profile_image : res['profile']['properties']['profile_image'],
                thumbnail : res['profile']['properties']['thumbnail_image']
            }
        })
    
    }
}
export default connect(
    mapStateToProps,
    mapDispachToProps,
    )(LoginForm);