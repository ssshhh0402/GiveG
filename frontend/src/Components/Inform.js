import React, {Component} from 'react';
import {Card} from 'antd';
import MyProgress from './MyProgress'
import {Link} from 'react-router-dom';


class Inform extends Component {
    state = {
        data : this.props.data,
    }
    numberWithComma(money){
        return money.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
      }
    render(){
        const {Meta} = Card;
        const url = './detail/' + this.state.data['id']
        
        return(
            <Link to={url}>
                <Card
                    size="small"
                    type="inner"
                >
                <Card.Grid hoverable={false} style={{width:'100%',height:'200px', padding:'0'}}>
                    <img alt="이미지 못불러옴!" src={this.state.data['image']} style={{height:'200px',width:'100%'}}></img>
                </Card.Grid>
                <Card.Grid hoverable={false} style={{width: '100%', textAlign: 'center', WebkitBoxShadow:'none', boxshadow:'none'}}>
                    <Meta title={this.state.data['title']} description={this.state.data['author']}></Meta>
                </Card.Grid>
                <Card.Grid hoverable={false} style={{width: '100%', textAlign: 'center', WebkitBoxShadow:'none', boxshadow:'none', paddingBottom:'0'}}>
                    <MyProgress perc={((this.state.data['donationNow']/this.state.data['donationGoal']) * 100).toFixed(1)}/>
                </Card.Grid>
                <Card.Grid hoverable={false} style={{display:"flex",width:'100%',justifyContent:'space-between', fontWeight:'bold',color:'#90949C',WebkitBoxShadow:'none',boxshadow:'none',paddingTop:'0',paddingBottom:'0'}}>
                    <span>{this.numberWithComma(this.state.data['donationGoal'])}원</span>
                    <span> {Math.abs(this.state.data['ddays'])}일 남음</span>
                </Card.Grid>
                </Card>
            </Link>
            )
        }
    }




export default Inform;