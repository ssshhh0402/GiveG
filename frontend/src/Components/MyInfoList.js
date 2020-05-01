import React, {Component} from 'react';
import {Row, Col,Pagination} from 'antd'
import MyInfoItem from './MyInfoItem'
class MyInfoList extends Component {
    state = {
        lists : this.props.data,
        current : 0,
    }
    onChange = values => {
        this.setState({
            current: values
        })
    }
    render(){
        const list = this.state.lists
        const InfoList = list.map((listItem,idx) => (
            <Col style={{width:'100%'}} key={idx} lg={24} md={6} lg={6}>
                <MyInfoItem data={listItem}/>
            </Col>
        ))
        return(
            <Row style={{width:'100%'}} gutter={[20, 20]}>
                {InfoList}
            </Row>

            
        )
    }
}


export default MyInfoList;