import {createStore, applyMiddleware} from 'redux';
import {createLogger} from 'redux-logger';
import createSagaMiddleware from 'redux-saga'
import reducer from './reducers/user';
import rootSaga from './sagas/user';

//redux-persist
import {persistStore,persistReducer} from 'redux-persist'
import storage from 'redux-persist/lib/storage';
// const logger = createLogger();
// const sagaMiddleware = createSagaMiddleware();
// const store = createStore(reducer, applyMiddleware(logger, sagaMiddleware));
// sagaMiddleware.run(rootSaga);
// persist 적용 안한거
const logger = createLogger();
const persistConfig = {
    key: 'root',
    storage
 };
const persistedReducer = persistReducer(persistConfig, reducer);
const sagaMiddleware = createSagaMiddleware();
const store = createStore(persistedReducer,applyMiddleware(logger,sagaMiddleware));
const persistor = persistStore(store);
sagaMiddleware.run(rootSaga);
//export default store;
export {store, persistor};