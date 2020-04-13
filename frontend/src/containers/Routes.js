import React from 'react';
import {BrowserRouter as Router, Route} from 'react-router-dom';
import loginForm from './login';
import signup from '../Components/signup';
import main from './main';
export default () => (
    <div>
        <Router>
            <Route exact path='/' component={loginForm}/>
            <Route path='/signup' component={signup}/>
            <Route path='/main' component= {main}/>
        </Router>
    </div>
)
