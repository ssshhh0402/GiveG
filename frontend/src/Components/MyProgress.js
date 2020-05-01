import React, {Component} from 'react';
import {Progress} from 'antd';


class MyProgress extends Component{
    render(){
        return(
            <Progress
            status="active"
            strokeColor={{
                '0%': '#108ee9',
                '100%': '#87d068',
            }}
            percent={this.props.perc}
            />
        )
    }
}

export default MyProgress;