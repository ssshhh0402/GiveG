import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Avatar,Row,Col,Typography,Tabs} from 'antd';
import Lists from '../containers/Lists';
import {createInstance} from '../api/index';
import MyInfoList from '../Components/MyInfoList';
const instance = createInstance();
const {TabPane} = Tabs;
const {Title,Paragraph} = Typography;
class test extends Component {
    state={
        funded : [],
        hosted: [],
        refunded : []
    }
    componentDidMount(){
        this.setting()
    }
    sorting(datas){
        const i_funded = []
        const i_refunded = []
        datas.forEach((listItem, idx) => {
            if (listItem['state'] === 'Funded'){
                i_funded.push(listItem)
            }else if(listItem['state'] === 'Refunded'){
                i_refunded.push(listItem)
            }
        })
        this.setState({
            funded:i_funded,
            refunded:i_refunded
        })
    }
    setting(){
        instance.get(`/api/v1/user/donation/${this.props.email}`)
                .then(response => {
                    
                    var lists = (response.data).sort((a,b) => (a['createDate'] > b['createDate']) ? 1: -1)
                    this.sorting(lists)
                    instance.get(`/api/v1/user/posts/${this.props.email}`)
                    .then(response => {
                        this.setState({
                            hosted:(response.data).sort((a,b) => (a['createDate'] > b['createDate']) ? 1: -1)
                        })
                    });
                })
    }
    render(){
        const tabBarStyle={
            width:'100%',
            textAlign:'start',
            background:'white'
        }
        return(
            <div style={{padding:'30px', background:'white'}}>
                <Row style={{width:'70%',marginLeft:'auto',marginRight:'auto'}}>
                    <Col span={20} style={{height:'300px'}}>
                        <Title level={2}>{this.props.email}</Title>
                        <Title level={4}> {this.props.nickname}</Title>
                        <Paragraph level={4}>회원 등급</Paragraph>
                    </Col>
                    <Col span={4} >
                        <Avatar
                        src={this.props.thumbnail}
                        alt={this.props.email}
                        style={{width:'auto',height:'150px'}}
                        />
                    </Col>
                </Row>
                <Row style={{marginTop:'15px',textAlign:'center', width:'70%',marginLeft:'auto',marginRight:'auto'}} gutter={[16,16]}>
                    <Col span={3}>
                        <h1>{this.state.hosted.length}</h1>
                        <h3>Hosted</h3>
                    </Col>
                    <Col span={3}>
                        <h1>{this.state.funded.length}</h1>
                        <h3>Funded</h3>
                    </Col>
                    <Col span={3}>
                        <h1>{this.state.refunded.length}</h1>
                        <h3>Refunded</h3>
                    </Col>
                   
                </Row>
                <Row style={{ width:'70%',marginLeft:'auto',marginTop:'50px',marginRight:'auto'}}>
                    <Tabs
                    size="large"
                    style={{background:'#F5F7FA',height:'auto',overflowY:'auto',width:'100%', overflowX:'hidden'}}
                    tabBarStyle={tabBarStyle} 
                    defaultActivaKey="0">
                        <TabPane tab="Funding" key="0">
                            {this.state.funded.length > 0
                            ?
                            <div>
                            <h3>{this.state.funded.length}개의 제품을 펀딩 중입니다.</h3>
                            <MyInfoList data={this.state.funded}/>
                            </div>
                            :   
                            <div style={{textAlign:'center'}}>
                                <h1>현재 펀딩중인 활동이 없습니다.</h1>
                            </div> 
                            }   
                        </TabPane>
                        <TabPane tab="Hosting" key="1">
                            {this.state.hosted.length > 0
                            ? 
                            <div>
                            <h3>{this.state.hosted.length}개의 제품을 좋아합니다.</h3>
                            <MyInfoList data={this.state.hosted}/>
                            </div>
                            :
                            <div style={{textAlign:'center'}}>
                            <h1>현재 좋아요 누른 제품이 존재하지 않습니다.</h1>
                            </div>}
                        </TabPane>
                        <TabPane tab="Refunded" key="2">
                            {this.state.refunded.length > 0
                            ?
                            <div>
                                <h3>{this.state.refunded.length}개의 제품을 Refunding하셨습니다.</h3>
                                <MyInfoList data={this.state.refunded}/>
                            </div> 
                            :
                            <div style={{textAlign:'center'}}>
                                <h1>현재 Refunding 받은 제품이 존재하지 않습니다.</h1>
                            </div>
                        }
                        </TabPane>
                    </Tabs>
                </Row> 
            </div>
            
        )
    }
}

const mapStateToProps = state => {
    return{
        email: state.email,
        nickname: state.nickname,
        thumbnail: state.thumbnail
    }
}
export default connect(
    mapStateToProps,
    null
)(test);