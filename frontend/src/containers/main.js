import React, { Component,useCallback } from 'react';
import {Link} from 'react-router-dom';
import { connect } from 'react-redux';
import {Carousel,Typography,Breadcrumb, Layout, Row,Col} from 'antd';
import '../css/main.css';
import Mynavbar from '../Components/MynavBar';
import Menu from '../Components/menu';
import Lists from './Lists';
import Showing from '../Components/Showing'
import  {createInstance} from '../api/index'
import Nothing from '../Components/Nothing'
const instance = createInstance();
const {Title} = Typography;
const {Content} = {Layout}
class main extends Component {
    state = {
        data : []
        
    }
    componentDidMount(){
        this.setting()
    }
    setting(){
        instance.get('/api/v1/posts')
          .then(response => {
              if(response['status'] === 200){
                console.log(response['data'])  
                this.setState({
                      data: response['data']
                  })
               
              }
          })
        
        }
    render(){
        const {Title} = Typography;
        
        return(
            <div>
            {this.state.data.length > 0 ?            
            <div>
                <div id="site-layout-background" style={{padding: 24, minHeight: 450,background: '#00F260',
                            background: '-webkit-linear-gradient(to right, #0575E6, #00F260)',
                            background: 'linear-gradient(to right, #0575E6, #00F260)',}}>
                                <Row>
                                    <Col xs={24} sm={24} md={24}  lg={12}>
                                        <Title style={{textAlign:"center", paddingTop:"9vw", color:"white"}} >GIVE 기부</Title>
                                        <Title level={4} style={{textAlign:"left", paddingLeft:"10vw", color:"white"}}>
                                            {"GIVE기부는 목표 완성형 기부 플랫폼으로 기부를 등록, 참여할 수 있으며,"}
                                            <br></br>
                                            {"기부한 프로젝트의 목표가 완료됨에 따라 기부금의 사용을 명확히 확인할 수 있습니다."}
                                        </Title>
                                    </Col>
                                    <Col xs={24} sm={24} md={24}  lg={12}><img src="/images/main2.gif" height="100%" width="100%"/></Col>
                                </Row>
                </div>
                <Menu/>
                <Title level={3} style={{color:"#00b09b", textAlign:"center"}}> {'마감 임박 프로젝트'}</Title>
                <Carousel
                    autoplay
                    effect="fade"
                    dotPosition="bottom"
                    dots="false"
                    style={{paddingTop:"2vw"}}    
                >
                    <div>
                        <div style={{textAlign:"left", backgroundImage:`url(${this.state.data[0].image})`, width:"100%", height:"30vw", backgroundSize:'cover',backgroundPosition:'center',backgroundRepeat:'no-repeat'}}>
                            <p style={{height:"5vw" , color: 'black', paddingLeft:"7vw",paddingTop:"20vw", fontSize:"3vw", }}>마감임박</p>
                            <p style={{color: 'black',fontSize:"1.5vw",paddingLeft:"7vw", paddingTop:"1vw"}}>{this.state.data[0].title}</p>
                        </div>
                    </div>
                    <div >
                        <div style={{textAlign:"left",backgroundImage:`url(${this.state.data[1].image})`, width:"100%", height:"30vw", backgroundSize:'cover',backgroundPosition:'center',backgroundRepeat:'no-repeat'}}>
                            <p style={{height:"5vw" , color: 'black', paddingLeft:"7vw",paddingTop:"20vw", fontSize:"3vw", }}>마감임박</p>
                            <p style={{color: 'black',fontSize:"1.5vw",paddingLeft:"7vw", paddingTop:"1vw"}}>{this.state.data[1].title}</p>
                        </div>
                    </div>
                    <div >
                        <div style={{textAlign:"left",backgroundImage:`url(${this.state.data[2].image})`, width:"100%", height:"30vw", backgroundSize:'cover',backgroundPosition:'center',backgroundRepeat:'no-repeat'}}>
                            <p style={{height:"5vw" , color: 'black', paddingLeft:"7vw",paddingTop:"20vw", fontSize:"3vw", }}>마감임박</p>
                            <p style={{color: 'black',fontSize:"1.5vw",paddingLeft:"7vw", paddingTop:"1vw"}}>{this.state.data[2].title}</p>
                        </div>
                    </div>
                    <div >
                        <div style={{textAlign:"left",backgroundImage:`url(${this.state.data[3].image})`, width:"100%", height:"30vw", backgroundSize:'cover',backgroundPosition:'center',backgroundRepeat:'no-repeat'}}>
                            <p style={{height:"5vw" , color: 'black', paddingLeft:"7vw",paddingTop:"20vw", fontSize:"3vw", }}>마감임박</p>
                            <p style={{color: 'black',fontSize:"1.5vw",paddingLeft:"7vw", paddingTop:"1vw"}}>{this.state.data[3].title}</p>
                        </div>
                    </div>

                </Carousel>
                <div id="menuContainer" style={{width:"90%",marginLeft:"auto",marginRight:'auto',background:'white'}}>
                    <Breadcrumb style={{margin:'0 0', paddingTop: 30, textAlign:'center', minHeight: 100}}>
                        <Title level={3} style={{color:'#00b09b'}}> 현재 진행중 Funding</Title>
                    </Breadcrumb>
                    <Showing data={this.state.data}></Showing>
                </div>
                
            </div>
    : <Nothing />}
    </div>
        )
    }
}
const mapStateToProps = state => {
    return {
        id: 0,
        email: state.email,
        access_token : state.access_token
    }
}
const mapDispachToProps = dispatch => {
    return {
        onFinish : (values) => 
        dispatch({
            type: '',
            payload: {}
        })
    }
}
export default connect(
    mapStateToProps,
    mapDispachToProps
)(main);