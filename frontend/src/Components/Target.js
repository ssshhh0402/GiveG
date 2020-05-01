import React,{Component} from 'react';
import {connect} from 'react-redux';
import {Card,Typography,Tooltip,Modal,Row,Col,Button,Form,Input} from 'antd';
import {Link} from 'react-router-dom'
const {Title, Paragraph} = Typography;
class Target extends Component {
    state = {
        visible : false,
        datas : this.props.data, //Target 각각의 정보-goals 안에 하나하나
        owner : this.props.owner,           //개설자
        content : this.props.content,           //아이템ID
        money: this.props.moeny             //총 금액
    }
    handleOk = () => {
        this.setState({
            visible: false
        })
    }
    handleCancel = () => {
        this.setState({
            visible: false
        })
    }
    showModal = () => {
        this.setState({
            visible: true
        })
        console.log(this.state.visible)
    }
    numberWithComma(money){
        
                return money.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
      }
    render(){
        const {Meta} = Card;
        return(    
            <div>  
                {this.props.data['complete']
                ? 
               
                <Card
                size="small"
                onClick={this.showModal}
                type="inner">
                    <Card.Grid hoverable={false} style={{width: '100%', textAlign:'left',WebkitBoxShadow:'none', boxshadow:'none'}}>
                        <Meta title="목표"></Meta>
                    </Card.Grid>
                    <Card.Grid hoverable={false} style={{textAlign:'left',height:'120px',width: '100%',WebkitBoxShadow:'none', boxshadow:'none',wordBreak:'break-all'}}>
                        <Paragraph ellipsis={{rows:4}}>{this.state.datas['content']}</Paragraph>
                    </Card.Grid>
                    <Card.Grid hoverable={false} style={{width:'100%',WebkitBoxShadow:'none', boxshadow:'none',}}>
                        <div style={{width:'100%'}}>
                            <Paragraph  strong style={{fontSize:'1rem',}}>목표 금액</Paragraph>
                            <Paragraph strong style={{fontSize:'1.5rem'}}>{this.numberWithComma((this.state.datas['paymentPercent'] * this.state.datas['posts']['donationGoal']) * 0.01)}<span style={{fontSize:'1.0rem'}}>원</span></Paragraph>
                        </div>
                    </Card.Grid>
                 </Card> 
                 : this.state.owner !== this.props.email
                 ? 
                 <Tooltip title="아직 목표가 완료 되지 않았습니다!" placement="top">
                   
                 <Card
                     size="small"
                     type="inner">
                     <Card.Grid hoverable={false} style={{width: '100%', textAlign:'left',WebkitBoxShadow:'none', boxshadow:'none'}}>
                         <Meta title="목표"></Meta>
                     </Card.Grid>
                     <Card.Grid hoverable={false} style={{textAlign:'left',height:'120px',width: '100%',WebkitBoxShadow:'none', boxshadow:'none',wordBreak:'break-all'}}>
                         <Paragraph ellipsis={{rows:4}}>{this.props.data['content']}</Paragraph>
                     </Card.Grid>
                     <Card.Grid hoverable={false} style={{width:'100%',WebkitBoxShadow:'none', boxshadow:'none',}}>
                         <div style={{width:'100%'}}>
                             <Paragraph  strong style={{fontSize:'1rem',}}>목표 금액</Paragraph>
                             <Paragraph strong style={{fontSize:'1.5rem'}}>{this.numberWithComma((this.state.datas['paymentPercent'] * this.state.datas['posts']['donationGoal']) * 0.01)}<span style={{fontSize:'1.0rem'}}>원</span></Paragraph>
                         </div>
                     </Card.Grid>
                 </Card>
                </Tooltip>
                : this.state.datas['posts']['state'] == 'ProjectStarted'
                ? 
                <Link to={`/detail/${this.props.content}/${this.props.data['goalId']}`}>
                    <Card
                    size="small"
                    type="inner">
                    <Card.Grid hoverable={false} style={{width: '100%', textAlign:'left',WebkitBoxShadow:'none', boxshadow:'none'}}>
                        <Meta title="목표"></Meta>
                    </Card.Grid>
                    <Card.Grid hoverable={false} style={{textAlign:'left',height:'120px',width: '100%',WebkitBoxShadow:'none', boxshadow:'none',wordBreak:'break-all'}}>
                        <Paragraph ellipsis={{rows:4}}>{this.props.data['content']}</Paragraph>
                    </Card.Grid>
                    <Card.Grid hoverable={false} style={{width:'100%',WebkitBoxShadow:'none', boxshadow:'none',}}>
                        <div style={{width:'100%'}}>
                            <Paragraph  strong style={{fontSize:'1rem',}}>목표 금액</Paragraph>
                            <Paragraph strong style={{fontSize:'1.5rem'}}>{this.numberWithComma((this.state.datas['paymentPercent'] * this.state.datas['posts']['donationGoal']) * 0.01)}<span style={{fontSize:'1.0rem'}}>원</span></Paragraph>
                        </div>
                    </Card.Grid>
                    </Card>
                </Link>
                : 
                <Tooltip title="목표 결과를 등록하실수 없습니다." placement="top">
                    <Card
                    size="small"
                    type="inner">
                    <Card.Grid hoverable={false} style={{width: '100%', textAlign:'left',WebkitBoxShadow:'none', boxshadow:'none'}}>
                        <Meta title="목표"></Meta>
                    </Card.Grid>
                    <Card.Grid hoverable={false} style={{textAlign:'left',height:'120px',width: '100%',WebkitBoxShadow:'none', boxshadow:'none',wordBreak:'break-all'}}>
                        <Paragraph ellipsis={{rows:4}}>{this.props.data['content']}</Paragraph>
                    </Card.Grid>
                    <Card.Grid hoverable={false} style={{width:'100%',WebkitBoxShadow:'none', boxshadow:'none',}}>
                        <div style={{width:'100%'}}>
                            <Paragraph  strong style={{fontSize:'1rem',}}>목표 금액</Paragraph>
                            <Paragraph strong style={{fontSize:'1.5rem'}}>{this.numberWithComma((this.state.datas['paymentPercent'] * this.state.datas['posts']['donationGoal']) * 0.01)}<span style={{fontSize:'1.0rem'}}>원</span></Paragraph>
                        </div>
                    </Card.Grid>
                </Card>
                </Tooltip>
            }
            <Modal 
            centered
            width="750px"
            visible={this.state.visible}
            onOk={this.handleOk}
            onCancel={this.handleCancel}
            >
                <Row>
                <Col style={{textAlign:'center', minHeight: 100}} span={24}>
        <Title level={3} style={{color:'#00b09b'}}> {this.state.datas['resultTitle']}</Title>
                </Col>
                <Col style={{textAlign:'center', minHeight: 100}} span={24}>
                    <Title level={4}>{this.state.datas['resultContent']}</Title>
                </Col>
                <Col span={24} style={{textAlign:'center'}}>
                    <img src={this.state.datas['resultImage']} style={{width:'100%'}}/>
                </Col>
                </Row>
            </Modal>
                   
            </div>       
        )
    }
}
const mapStateToProps = state => {
    return{
        email: state.email,
    }
};
export default connect(
    mapStateToProps,
    null
)(Target);