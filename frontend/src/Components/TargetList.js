import React,{Component} from 'react';
import {Row,Col} from 'antd'
import Target from './Target'
class TargetList extends Component {
    state={
        lists : this.props.data,    //중간 목표들 -goals
        owner : this.props.owner,   //개설자
        content : this.props.content,    //아이템ID
        money : this.props.money    //도네 총 금액
    }
    render(){
        const targetlist = this.state.lists.map((listItem,idx) => (
            <Col style={{width:'100%'}} key={idx} lg={24} md={8} lg={8}>
                <Target data={listItem} owner={this.state.owner} content={this.state.content} money={this.state.money}/>
            </Col>
        ))
        return(
            <Row style={{width:'100%'}} gutter={[20,20]}>
                {targetlist}
            </Row>
        )
    }
}


export default TargetList;