import React, {Component} from 'react';
import {Col, Typography, Row, Button} from 'antd';
import {Link} from 'react-router-dom';
import {createInstance} from '../api/index'
const instance = createInstance();
class PaymentSuccess extends Component{
    
    state = {
        queryId : this.props.location.search.split('=')[1],
        donationId : window.sessionStorage.getItem('donationId')
    }
    clickHandler(){
        var queryId = this.state.queryId
        const requestDto = {
            donationId : this.state.donationId,
            pgToken : queryId
        }
        instance.put('/api/v1/donation', requestDto)
        .then(response => {
            console.log(response)
            if (response.status === 200){
                window.location.href="/"
            }
        })
    }
    render(){
        const {Title} = Typography;
        const queryID = this.props.location.search.split("=")[1];
        console.log(queryID)
        return(
            <>
            <div id="site-layout-background" style={{padding: 24, minHeight: 450,background: '#00F260',
                            background: '-webkit-linear-gradient(to right, #0575E6, #00F260)',
                            background: 'linear-gradient(to right, #0575E6, #00F260)',}}>
                                <Row>
                                    <Col xs={24} sm={24} md={24}  lg={24} style={{textAlign:"center"}}>
                                        <Title style={{textAlign:"center", paddingTop:"9vw", color:"white"}} >{"결제 완료"}</Title>
                                        <Title level={4} style={{textAlign:"center", color:"white"}}>
                                            {"더욱 세상을 이롭게하는 GIVE 기부가 되겠습니다."}
                                        </Title>
                                        <Button type="primary" style={{fontSize:"1.3vw", height:"auto"}} onClick={this.clickHandler.bind(this)}>확인</Button>
                                    </Col>
                                </Row>
                </div>
            </>
        );
    }
}

export default PaymentSuccess;