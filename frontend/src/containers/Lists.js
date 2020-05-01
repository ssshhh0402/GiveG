import React, { Component } from 'react'
import { Row, Col, Typography, Input, Select, Pagination } from 'antd';
import Inform from '../Components/Inform'
import { createInstance } from '../api/index'
import Nothing from '../Components/Nothing'
const instance = createInstance();
const { Title } = Typography;
const { Option } = Select;
const { Search } = Input;

class Lists extends Component {
    state = {
        targetData: [],
        current: 1,
    }
    onChange = values => {
        this.setState({
            current: values
        })
    }
    searchHandler(values) {
        if (values == ''){
            this.setting()
        }
        else{
        instance.get(`/api/v1/posts/search/${values}`)
            .then(response => {
                this.setState({
                    targetData: response.data
                })
            })
        }
    }
    componentDidMount() {
        this.setting()
    }
    setting() {
        instance.get('/api/v1/posts')
        .then(response => {
            if(response.status === 200){
                this.setState({
                    targetData: response['data']
                })
            }
        })
    }

    render() {
        const lists = this.state.targetData.slice(8 * (this.state.current - 1), 8 * (this.state.current))
        const testList = lists.map((listItem) => (
            <Col key={listItem.id} lg={6} md={12} xs={24}>
                <Inform data={listItem} />
            </Col>
        ))
        return (
            <div style={{ background: 'white' }}>

                <div id="site-layout-background" style={{
                    borderRadius: "25px", margin: '0 0', paddingTop: 30, textAlign: 'center', minHeight: 200, padding: 24, background: '#00F260',
                    background: '-webkit-linear-gradient(to right, #0575E6, #00F260)',
                    background: 'linear-gradient(to right, #0575E6, #00F260)',
                }}>

                    <Title level={2} style={{ color: "white" }}> <img src="/images/donation.png" style={{ width: "2.5%" }}></img>현재 진행중인 기부 </Title>
                    <Search style={{ width: '50%',marginTop:'1rem'}} onSearch={values => this.searchHandler(values)} />
                        
                    
                </div>
                {this.state.targetData.length > 0 ?
                    <div style={{ width: '80%', marginLeft: 'auto', marginRight: 'auto' }}>
                        <div style={{ padding: '20px' }}>
                            <Row gutter={[20, 20]}>
                                {testList}
                            </Row>
                        </div>
                        <div style={{ textAlign: "center" }}>
                            <Pagination current={this.state.current} onChange={this.onChange} total={this.state.targetData.length} pageSize={8} />
                        </div>
                    </div>
                    : <Nothing />}
            </div>
        )

    }

}


export default Lists