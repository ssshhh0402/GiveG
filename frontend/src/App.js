import React from 'react';
import RegistForm from './RegistForm'
import './App.css';
import {Provider, connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import Routes from './containers/Routes';


import 'antd/dist/antd.css'
//import store from './store'
import * as userActions from './reducers/user'
// persist 적용 전
import {store, persistor} from './store'
import {PersistGate} from 'redux-persist/integration/react';

const App = () => {
  return (
      <Provider store={store}>
        <PersistGate loading={null} persistor={persistor}>
          <Routes>
          </Routes>
        </PersistGate>
      </Provider>
  );
};
export default App;
