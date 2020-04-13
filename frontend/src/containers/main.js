import React, { Component,useCallback } from 'react';
import { connect } from 'react-redux';

class main extends Component {
    render(){
        return(
            <div>
                <h1> 이거시 우리의 메인페이지인거시다</h1>
                <h3>{this.props.email}</h3>
                <h3>{this.props.access_token}</h3>
                <h3>{this.props.id}</h3>
            </div>
        )
    }
}
const mapStateToProps = state => {
    return {
        id: 0,
        email: state.email,
        access_token : state.access_token
    }
}
const mapDispachToProps = dispatch => {
    return {
        onFinish : (values) => 
        dispatch({
            type: '',
            payload: {}
        })
    }
}
export default connect(
    mapStateToProps,
    mapDispachToProps
)(main);