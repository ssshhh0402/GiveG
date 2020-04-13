import React, { Component,useCallback } from 'react';
import {Link, Router, Route} from 'react-router-dom';
import {Form,Input,Button} from 'antd';
import {UserOutlined, LockOutlined} from '@ant-design/icons';
import {connect, useDispatch} from 'react-redux'; 
import KakaoLogin from 'react-kakao-login';
import styled from 'styled-components'
class LoginForm extends Component{
    responseFail = (err) => {
        console.log('------로그인 실패')
        console.log(err);
    }
    render(){
    return (
        <div>
        <Form
        name="normal_login"
        className="login-form"
        onFinish={this.props.onFinish}
        >
            <Form.Item
            name="email"
            className="login-form"
            rules={[
                {
                    required:true,
                }
            ]}>
                <input placeholder="Email"/>
            </Form.Item>
            <Form.Item
                className="login-form"
                name="password"
                rules={[
                    {
                        required:true,
                    },
                ]}
                >
                <Input                            
                prefix={<LockOutlined className="site-form-item-icon" />}
                type="password"
                placeholder="비밀번호"
                />
            </Form.Item>

                <Form.Item>
                    <Button type="primary" htmlType="submit" className="login-form-button">
                        로그인
                    </Button>
                </Form.Item>
                <KakaoButton
                jsKey='694327e75b013df6c9b6cd2e3ac819ff'
                buttonText='카카오 로그인'
                onSuccess={this.props.kakao}
                onFailure={this.responseFail}
                getProfile="True"
                />
        </Form>
            <h1>{this.props.email}</h1>
            <h1>{this.props.access_token}</h1>
            <Link to='./main'>메인으로</Link>
        </div>
        
            );
        }
            
    };
const KakaoButton = styled(KakaoLogin)`
    padding: 0;
    width: 100%;
    height: 50px;
    line-height: 44px;
    color: #783c00;
    background-color: #FFEB00;
    border: 1px solid transparent;
    border-radius: 3px;
    font-size: 14px;
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
            password : values['password']   
        }}),
        kakao : (res) =>
        dispatch({
            type: 'LOG_IN_KAKAO',
            payload: {
                access_token: res['response']['access_token'],
                refresh_token : res['response']['refresh_token'],     
                email : res['profile']['kakao_account']['email']
            }
        })
    
    }
}
export default connect(
    mapStateToProps,
    mapDispachToProps,
    )(LoginForm);