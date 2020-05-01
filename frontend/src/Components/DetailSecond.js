import React, {Component} from 'react';
import {Layout,Typography} from 'antd';
import '../css/detailSecond.css'
import TargetList from './TargetList'
const {Content} = Layout;
const {Title} = Typography
class DetailSecond extends Component{
    state={
        newEvent: this.props.goals, //중간목표 -goals
        owner: this.props.owner,    //개설자
        contentId : this.props.content,     //아이템 iD
        money: this.props.money   //최종 금액
    }

    render(){
        return(
            <Content>
                <div id="newContent" style={{width:'65%', padding:'10px', display:'box'}}>
                <Title level={3} style={{color:'black',width:'100%'}}>목표</Title>
                <TargetList data={this.state.newEvent} owner={this.state.owner} content={this.state.contentId} money={this.state.money}></TargetList>
              </div>
                
            </Content>
        )
    }
}



export default DetailSecond;