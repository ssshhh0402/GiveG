import React, { Component } from 'react';
import { Layout, Menu, Breadcrumb, Typography } from 'antd';
import ExplainService from '../Components/ExplainService';


class MainPage extends Component {

    state = {
        collapsed: true,
    };

    onCollapse = collapsed => {
        console.log(collapsed);
        this.setState({ collapsed });
    };

    render() {
        const { Header, Content, Footer } = Layout;
        const { Title } = Typography;

        return (


            <Layout style={{ minHeight: '100vh' }}>
                <Layout className="site-layout">
                    <Header style={{ background: 'white' }} className="header">
                        <div className="logo" />
                        <Menu theme="ligth" mode="horizontal" defaultSelectedKeys={['2']}>
                            <Menu.Item key="1">nav 1</Menu.Item>
                            <Menu.Item key="2">nav 2</Menu.Item>
                            <Menu.Item key="3">nav 3</Menu.Item>
                        </Menu>
                    </Header>

                    <Content style={{ margin: '0 0' }}>
                        <div className="site-layout-background" style={{
                            padding: 24, 
                            minHeight: 450,
                            background: '#00F260',
                            background: '-webkit-linear-gradient(to right, #0575E6, #00F260)',
                            background: 'linear-gradient(to right, #0575E6, #00F260)',
                        }}>
                            <div style={{ paddingTop: 160, textAlign: 'center' }}>
                                <Title level={2}>{"기부를 하고 싶은 모든 사람들에게"}<Title >{" -GIVE기부- "}</Title></Title>

                            </div>
                        </div>
                    </Content>

                    <ExplainService />

                    <Content className="site-layout-background" style={{ margin: '0 16px' }}>
                        <Breadcrumb style={{ margin: '0 0', paddingTop: 30, textAlign: 'center', minHeight: 100 }}>
                            <Title level={3} style={{ color: '#00b09b' }} >뭘 넣을까?</Title>
                        </Breadcrumb>
                        <div style={{ padding: 24, minHeight: 360 }}>
                            생각중.........
                  </div>
                    </Content>
                    <Footer style={{ textAlign: 'center' }}>Ant Design ©2018 Created by Ant UED</Footer>
                </Layout>
            </Layout>
        );
    }

}


export default MainPage;