import { Menu,Layout } from 'antd';
import React,{Component} from 'react';
import {Link} from 'react-router-dom';
import {
  MailOutlined,
  AppstoreOutlined,
  SettingOutlined,
} from '@ant-design/icons';
import '../css/navbar.css'

class MynavBar extends Component {
  state = {
    current: 'mail',
  };
  logout(){
    window.sessionStorage.clear();
    window.location.href="/";
  }
  handleClick = e => {
    console.log('clickEvent ', e);
    this.setState({
      current: e.key,
    });
  };

  render() {
    const {Header} = Layout;
    return (
      <Header className="header"> 
        <Link to='/'>
          <div className="logo"/>
        </Link>
        <Menu mode="horizontal" style={{float:'right'}}defaultSelectedKeys={['2']}>
          <Menu.Item key="mail"> 
              {window.sessionStorage.getItem('email')
              ? 
              <span onClick={this.logout.bind(this)}>로그아웃</span>
              :
                <Link to="/login"> 
                  <span>로그인</span>
                </Link>
              }
          </Menu.Item>
          <Menu.Item key="app">
            {window.sessionStorage.getItem('email')
            ?
            <Link to='/myInfo'>
            {/* <AppstoreOutlined/> */}
              <span>내정보</span>
            </Link>
            :
            <Link to='/login'>
              <span>내정보</span>
            </Link>
            }
          </Menu.Item>
          <Menu.Item key="alipay">
            {window.sessionStorage.getItem('email')
            ?
            <Link to='/register'>
              <span>등록</span>
            </Link>
            :
            <Link to="./login">
              <span>등록</span>
            </Link>
            }
          </Menu.Item>
          <Menu.Item key="lists">
            
            <Link to='/lists'>
              <span>기부하기</span>
            </Link>          
          </Menu.Item>
          
        </Menu>
    </Header>
    );
  }
}

export default MynavBar;