import React, { Component} from 'react';
import {Layout,Button, Typography,Breadcrumb,Tabs} from 'antd';
import {connect} from 'react-redux';
import MyProgress from '../Components/MyProgress'
import CommentList from './CommentList';
import DetailFirst from '../Components/DetailFirst';
import DetailSecond from '../Components/DetailSecond';
import TargetList from '../Components/TargetList';
import {createInstance} from '../api/index';
import axios from 'axios';
import Nothing from '../Components/Nothing'
const instance = createInstance();
const {TabPane} = Tabs;
class DetailPage extends Component{
  state = {
    sider : window.innerWidth * 0.35,
    data : [],
    percentage: 0,
    posible : true,
    email : window.sessionStorage.getItem('email')
  };
  repay(){
    console.log(this.state.data)
  }
  kakaoPaying(){    
    const money = prompt('후원하실 금액을 입력해주세요', 0);
    if (money !== null){
      this.props.kakaoPay(this.props.email,this.state.data['postsId'],money)
    }
  }
  refunding(){
    const requestDto = {
      email : this.state.email,
      postsId : this.state.data['postsId']
    }
    instance.put('/api/v1/donation/refund',requestDto)
    .then(response=>{
      if (response['status'] === 200){
        alert('성공적으로 환불 되셨습니다.')
        window.location.href=(`/detail/${this.props.match.params.id}`)
      }
    })
  }
  numberWithComma(money){
    return money.toFixed(3).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  }
  resizeHandler(){
    this.setState({
      sider: window.innerWidth * 0.35
    })
  }
  
  nonLogin(){
    if (window.confirm('본 기능은 카카오 회원 전용입니다. 로그인하러 가시겠습니까?')){
      window.location.href('/')
    }
  }
  onStart(){
    console.log(this.state.data['postsId'])
    try{
    instance.put(`/api/v1/posts/start/${this.state.data['postsId']}`)
    .then(response => {
      if (response['status'] === 200){
        window.location.href=`/detail/${response['data']}`
      }
      console.log(response)
    })
  }catch(e){
    alert(e)
  }
  }
  onStop(){
    instance.put(`/api/v1/posts/stop/${this.state.data['postsId']}`)
    .then(response=>{
      console.log(response)
    })
  }

  setting(){
    if (!this.state.email){
      instance.get(`/api/v1/posts/${this.props.match.params.id}`)
      .then(response => {
        console.log(response)
        if(response['status'] === 200){
          
          this.setState({
            data:response['data'],
            percentage : ((response['data']['donationNow'] / response['data']['donationGoal']) * 100).toFixed(1),
          })
          
        }
      })
    }else{
      instance.get(`/api/v1/posts/${this.props.match.params.id}/${this.props.email}`)
      .then(response => {
        if(response['status'] === 200){
          this.setState({
            data : response['data'],
            percentage : ((response['data']['donationNow'] / response['data']['donationGoal']) * 100).toFixed(1)
          })
        }
        console.log(this.state.data)
      })
    }
  }
  componentDidMount(){
    window.addEventListener('resize', this.resizeHandler.bind(this));
    this.setting();
  };
  componentWillUnmount(){
    window.removeEventListener('resize', this.resizeHandler.bind(this));
  }

  buttons(){
    if(this.state.data['state'] === 'ProjectStarted'){
      if(this.state.data['author'] === this.state.email || !this.state.email){
        return <Button type="primary" style={{height:'50px'}} block disabled>진행중</Button>
      }else{
        return <Button type="primary" style={{height:'50px'}} block onClick={this.onStop.bind(this)}>취소해주세요</Button>
      }
    }else if(this.state.data['state'] === 'ProjectEnd' || this.state.data['state'] === 'ProjectStopped'){
      return <Button type="primary" style={{height:'50px'}} block disabled>종료된 프로젝트</Button>
    }else{
      if(!this.state.data['funded']){
        if (this.state.data['author'] === this.state.email){
          return <Button type="primary" style={{height:'50px'}} block onClick={this.onStart.bind(this)}>시작하기</Button>
        }else{
          return <Button type="primary" style={{height:'50px'}} block onClick={this.refunding.bind(this)}>환불하기</Button>
      } 
      }else {
        if(this.state.email){
          if(this.state.data['author'] === this.state.email){
            return <Button type="primary" style={{height:'50px'}} block onClick={this.onStart.bind(this)}>시작하기</Button>
          }else{
            return <Button type="primary" style={{height:'50px'}} block onClick={this.kakaoPaying.bind(this)}>펀딩하기</Button>
          }
        }else{
            return <Button type="primary" style={{height:'50px'}} block disabled>펀딩하기</Button>
          }
        }
      }
    
  }
  render(){
    const tabBarStyle = {
      width:'100%',
      textAlign:'center'
    }
    const { Sider } = Layout;
    const {Title} = Typography;
    return(
      <div>
      {this.state.data.length !== 0 ?
      <div>
        <div style={{margin:'0 0', paddingTop: 30, textAlign:'center', minHeight: 100}}>
          <Title level={3} style={{color:'#00b09b'}}> {this.state.data['title']}</Title>
        </div>
        <Tabs 
          defaultActiveKey="0" 
          animated={false}
          size="large"
          style={{background:'white',height:'auto',overflow:'auto'}}
          tabBarStyle={tabBarStyle}>
            <TabPane tab="정보" key="0">
              <Layout style={{background:"white"}}>
                <DetailFirst data={this.state.data['image']} content={this.state.data['content']}/>
                <Sider width={this.state.sider} style={{background:"white"}}>
                  <div style={{width:"45%", padding:'20px'}}>
                    <div style={{height:'50px'}}>
                      {this.state['state'] === 'ProjecStopped'
                      ? <h1 style={{fontWeight:'bold'}}> 이 프로젝트는 중단되었습니다</h1>
                      : this.state['state'] === 'ProjectEnded'
                      ? <h1 style={{fontWeight:'bold'}}> 이 프로젝트는 종료되었습니다.</h1>
                      : <h1 style={{fontWeight:'bold'}}>{Math.abs(this.state.data['ddays'])}일 남음</h1>}
                    </div>
                    <MyProgress perc={this.state.percentage}/>
                    <div>
                      <h1 style={{height:'50px'}}> {this.state.percentage}% 달성</h1>
                    </div>
                    <div>
                      <h1 style={{height:'50px'}}> {this.numberWithComma(this.state.data['donationNow'])} 원 펀딩 </h1>
                    </div>
                    <div>
                      <h1 style={{height:'50px'}}>{this.state.data['peopleNum']}명 현재 후원중</h1> 
                    </div>                    
                  {this.buttons()}
                  </div>
                </Sider>
              </Layout>
            </TabPane>
            <TabPane
            tab='목표'
            key="1">
              <Layout style={{background:'white'}}>
                <DetailSecond content={this.props.match.params.id} goals={this.state.data['goals']} money={this.state.data['donationGoal']} owner={this.state.data['author']}/>
                <Sider width={this.state.sider} style={{background:"white"}}>
                  <div style={{width:"45%", padding:'20px'}}>
                    <div style={{height:'50px'}}>
                      <h1 style={{fontWeight:'bold'}}>{Math.abs(this.state.data['ddays'])}일 남음</h1>
                    </div>
                    <MyProgress perc={this.state.percentage}/>
                    <div>
                      <h1 style={{height:'50px'}}> {this.state.percentage}% 달성</h1>
                    </div>
                    <div>
                      <h1 style={{height:'50px'}}> {this.numberWithComma(this.state.data['donationNow'])} 원 펀딩 </h1>
                    </div>
                    <div>
                      <h1 style={{height:'50px'}}>{this.state.data['peopleNum']}명 현재 후원중</h1> 
                    </div>
                    {this.buttons()}
                  </div>
                </Sider>
              </Layout>
            </TabPane>
            <TabPane tab="이야기" key="2">
              <Layout style={{background:'white'}}>
                <CommentList id={this.props.match.params.id}/>
                <Sider width={this.state.sider} style={{background:"white"}}>
                  <div style={{width:"45%", padding:'20px'}}>
                    <div style={{height:'50px'}}>
                      <h1 style={{fontWeight:'bold'}}>{Math.abs(this.state.data['ddays'])}일 남음</h1>
                    </div>
                    <MyProgress perc={this.state.percentage}/>
                    <div>
                      <h1 style={{height:'50px'}}> {this.state.percentage}% 달성</h1>
                    </div>
                    <div>
                      <h1 style={{height:'50px'}}> {this.numberWithComma(this.state.data['donationNow'])}원 펀딩 </h1>
                    </div>
                    <div>
                      <h1 style={{height:'50px'}}>{this.state.data['peopleNum']}명 현재 후원중</h1> 
                    </div>
                    {this.buttons()}
                  </div>
                </Sider>
              </Layout>
            </TabPane>
          </Tabs>
          </div>
          : <Nothing/>}
          </div>
        );
    }

}
const mapStateToProps = state => {
  return {
    email: state.email
  }
};

const mapDispachToProps = dispatch => {
  return {
    kakaoPay : (userEmail, postsId, userMoney) =>
    dispatch({
      type: 'KAKAO_PAYMENT',
      payload:{
        id : postsId,
        email : userEmail,
        money : userMoney
      }
    })
  }
}
export default connect(
  mapStateToProps,
  mapDispachToProps
)(DetailPage);