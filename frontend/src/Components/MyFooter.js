import React, {Component} from 'react';
import {Layout, Row, Col} from 'antd';

const {Footer} = Layout;
class MyFooter extends Component{
    
    render(){
        return(
            <>
                <Footer>
                    <Row>
                        <Col span={24} style={{textAlign:"center"}}>
                            <div><h2>어쩌삼(어쩌다 보니 3명)</h2></div>
                            <div><img src="/images/backEnd.png" width="3%" /> <span style={{paddingRight:"0.3vw"}}>안우진</span> 
                            <img src="/images/frontEnd.png" width="3%" /> <span style={{paddingRight:"0.3vw"}}>홍정우</span>
                            <img src="/images/blockChain.png" width="3%" /> <span style={{paddingRight:"0.3vw"}}>정대윤</span> </div>
                        </Col>
                        <Col span={24} style={{paddingTop:"2vw",textAlign:"center"}}>Design in SSAFY</Col>
                    </Row>
                </Footer>
            </>
        );
    }
}

export default MyFooter;