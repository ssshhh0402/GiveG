import React, {Component} from 'react'
import {Typography} from 'antd'

const {Title} = Typography
class Nothing extends Component{
    render(){
        return(
            <div style={{width:'100%', height: '600px',margin:'auto',textAlign:'center'}}>
                <img src='/images/loding.gif' style={{paddingTop:"18vw"}}/>
                <Title > 잠시만 기다려 주세요!</Title>
            </div>
        )
    }
}

export default Nothing