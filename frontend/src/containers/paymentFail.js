import React, {Component} from 'react';
import {Col, Typography, Row, Button} from 'antd';
import {Link} from 'react-router-dom';

class PaymentFail extends Component{
    render(){
        const {Title} = Typography;
        return(
            <>
            <div id="site-layout-background" style={{padding: 24, minHeight: 450,background: "#3a1c71",
  background: "-webkit-linear-gradient(to right, #3a1c71, #d76d77, #ffaf7b)",
  background: "linear-gradient(to right, #3a1c71, #d76d77, #ffaf7b)"}}>
                                <Row>
                                    <Col xs={24} sm={24} md={24}  lg={24} style={{textAlign:"center"}}>
                                        <Title style={{textAlign:"center", paddingTop:"9vw", color:"white"}} >{"결제 실패"}</Title>
                                        <Title level={4} style={{textAlign:"center", color:"white"}}>
                                            {"좀 더 좋은 기부 서비스가 되겠습니다."}
                                        </Title>
                                        <Link to="/main"><Button type="primary" style={{fontSize:"1.3vw", height:"auto"}}>확인</Button></Link>
                                    </Col>
                                </Row>
                </div>
            </>
        );
    }
}

export default PaymentFail;