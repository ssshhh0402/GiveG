import React from 'react';
import {BrowserRouter as Router, Route} from 'react-router-dom';
import loginForm from './login';
import signup from './signup';
import main from './main';
import detail from './DetailPage';
import Lists from './Lists';
import MynavBar from '../Components/MynavBar';
import Register from './Register';
import test from '../Components/test';
import TargetInput from './TargetInput';
import MyFooter from '../Components/MyFooter';
import Nothing from '../Components/Nothing'
import PaymentSuccess from './paymentSuccess';
import PaymentFail from './paymentFail';
import {Layout} from 'antd';
export default () => (
    <div>
        <Router>   
            <Layout className="layout">       
                <MynavBar/>
                <Route exact path='/' component={main}/>
                <Route path='/signup' component={signup}/>
                <Route path='/login' component= {loginForm}/>
                <Route exact path='/detail/:id' component={detail}/>
                <Route path="/lists" component={Lists}/>
                <Route path='/register' component={Register}/>
                <Route path="/myInfo" component={test}/>
                <Route path="/detail/:id/:target" component={TargetInput}/>
                <Route path='/nothing' component={Nothing}/>
                <Route path="/paymentSuccess" component={PaymentSuccess}/>
                <Route path="/paymentFail" component={PaymentFail} />
                <MyFooter></MyFooter>
            </Layout>
        </Router>
    </div>
)

