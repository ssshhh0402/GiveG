import React, {Component} from 'react'
import {Row,Col} from 'antd';
import Inform from '../Components/Inform'

class Showing extends Component {
    state = {
        targetData : this.props.data.slice(0,4)
    }
    render(){
        const lists = this.state.targetData
        const testList = lists.map((listItem) => (
            <Col key={listItem.id} lg={6} md={12} xs={24}>
                <Inform data={listItem}/>
            </Col>
        ))
        return(
            <Row gutter={[20,20]}>
                {testList}
            </Row>
        )
    }
}


export default Showing;