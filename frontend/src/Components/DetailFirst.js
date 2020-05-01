import React, {Component} from 'react';
import {Layout,Space, Typography} from 'antd';
import '../css/detailFirst.css';
import TargetList from './TargetList';
const {Content} = Layout;
const {Title, Paragraph} = Typography;
class DetailFirst extends Component{
    state={
        imgUrl : this.props.data,
        content: this.props.content
    }
    render(){
        // const imageList = this.state.informations['images'].map( (listItem,idx) => (
        //     <img src={listItem} style={{width:'100%'}} key={idx}/>
            
        // ))
        return(
            <Content>
                <div id="detailContent" style={{ width:"85%", padding:'10px',}}>
                    <Space size="large" direction="vertical">
                    <img src={this.state.imgUrl} style={{width:'100%'}}/> 
                    <Paragraph style={{textAlign:'left'}}>{this.state.content}</Paragraph>
                    <img src={this.state.imgUrl} style={{width:'100%'}}/>
                    </Space>
                  </div>
              </Content>
        )
    }
}

export default DetailFirst;