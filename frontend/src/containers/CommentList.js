import React,{Component} from 'react';
import {Comment, Tooltip, List,Button,Form,Input,Avatar,Layout} from 'antd';
import moment from 'moment';
import {connect} from 'react-redux';
import '../css/commentList.css';
import {createInstance} from '../api/index';

const instance = createInstance();
const {TextArea} = Input;

const Editor = ({onSubmit,onEnter,content})=>(
    <div>
        <Form.Item>
            <TextArea name="contents" autoSize={{minRows:3,maxRows:6}} onPressEnter={onSubmit} value={content} onChange={ e=> onEnter(e)} disabled={!window.sessionStorage.getItem('email')}/>
        </Form.Item>
        <Form.Item>
            <Button htmlType="submit" onClick={onSubmit} type="primary" style={{float:'right'}} disabled={! window.sessionStorage.getItem('email')}>
                댓글 남기기
            </Button>
        </Form.Item>
    </div>
)
class CommentList extends Component {
    state={
        content:'',
        id:this.props.id,
        data : []
    };
    setting(){
        instance.get(`/api/v1/comment/${this.state.id}`)
        .then(response => {
            if(response.status === 200){
                console.log('-------setting')
                console.log(response)
                this.setState({
                    data : response.data
                })
                
            }
        })
    }
    componentWillMount(){
        this.setting();
    }
    entered = event => {
        this.setState({content: event.target.value});
    }
    finished = (values) => {
        values.preventDefault();
        var inputs = document.getElementsByName('contents')[0].value;
        console.log(this.state.id)
        instance.post('api/v1/comment',{
            author:this.props.email,
            content: inputs,
            postsId: this.state.id
        }).then(response => {
            if(response.status === 200){
            this.setting()
            this.setState({
                content:''
            })
        }
        })
    }
    render(){
        const {Content} = Layout;
        return(
            <Content style={{height:'auto !important',float:'right'}}>
                <div style={{width:"80%", padding:'10px', display:'box',float:'right'}}>
                    {this.state.data.length > 0
                    ? <List
                    id="content_list"
                    header={`${this.state.data.length}개의 댓글`}
                    itemLayout="horizontal"
                    dataSource={this.state.data}
                    renderItem={item =>(
                        <li>
                            <Comment
                                author={item.author}
                                content={item.content}
                                avatar={
                                    <Avatar
                                        src={item.userPicture}
                                        alt={this.props.email}/>
                                }/>
                        </li>
                    )}
                    />
                : <h1>등록된 댓글이 존재하지 않습니다.</h1>}

                <Comment
                avatar={
                    <Avatar
                    src={this.props.thumbnail}
                    alt={this.props.email}/>
                }
                content={
                    <Editor
                    onSubmit={this.finished}
                    onEnter={this.entered}
                    content={this.state.content}
                    />
                }/>
                </div>
            </Content>
        )
    }
}

const mapStateToProps = state => {
    return{
        email: state.email,
        thumbnail: state.thumbnail
    }
};

export default connect(
    mapStateToProps,
    null
)(CommentList);