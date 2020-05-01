import React, { Component } from 'react';
import { Layout, Menu, Breadcrumb, Typography } from 'antd';




class ExplainService extends Component {

    state = {
        collapsed: true,
    };

    onCollapse = collapsed => {
        console.log(collapsed);
        this.setState({ collapsed });
    };

    render() {
        
        const { Content, Sider } = Layout;
        const { Title } = Typography;

        return (


            <Content style={{ padding: '0 0' }}>
                <Breadcrumb style={{ margin: '0 0', paddingTop:30 , textAlign:'center', minHeight:100}}>
                    <Title level={3} style={{color: '#00b09b'}} >서비스 설명</Title>
                </Breadcrumb>
                <Layout style={{ padding: '0 0' }}>
                    <Sider style={{background:'rgb(240, 242, 245)'}} width={350}>
                            <div style={{textAlign:'center', paddingTop:70,  color: '#00b09b'}} ><p>서비스 1</p></div>
                            <div style={{textAlign:'center', paddingTop:20, color: '#00b09b'}} ><p>서비스 1</p></div>
                            <div style={{textAlign:'center', paddingTop:20, color: '#00b09b'}} ><p>서비스 1</p></div>
                            <div style={{textAlign:'center', paddingTop:20, color: '#00b09b'}} ><p>서비스 1</p></div>
                            <div style={{textAlign:'center', paddingTop:20, color: '#00b09b'}} ><p>서비스 1</p></div>
                    </Sider>
                    <Content style={{ padding: '0 24px', minHeight: 380 }}>
                        {"그때 말했던 우리 서비스 설명 넣을 부분"}
                    </Content>
                </Layout>
            </Content>
        );
    }

}


export default ExplainService;