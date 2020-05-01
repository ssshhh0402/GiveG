import React,{Component} from 'react';
import {Form,Input,Button,Upload,Row,Col,Typography} from 'antd';
import {createInstance} from '../api/index'

const instance = createInstance()

class TargetInput extends Component {
    constructor(props){
        super(props);
        this.goBack = this.goBack.bind(this);
    }
    state = {
        selectedFile : [],
        content: this.props.match.params.target,
        id : this.props.match.params.id
    }
    onFinish = values => {
        const form = new FormData()
        form.append('complete', 1);
        form.append('image', this.state.selectedFile[0]);
        form.append('resultContent', values['content']);
        form.append('resultTitle', values['title']);
        try{
            instance.put(`/api/v1/goal/${this.state.content}`,form,{
                headers:{'Content-Type' : 'multipart/form-data'}
            }).then(response => {
                console.log(response)
                if(response.status === 200){
                    alert('중간목표 등록이 완료되었습니다!')
                    this.goBack();
                }
            })
        }catch(e){
            alert(e)
        }
        

    }
    goBack = () => {
        this.props.history.goBack();
    }
    render(){
        const {Title} = Typography;
        const {selectedFile} = this.state.selectedFile;
        const properties = {
            listType: 'picture',
            onRemove: file=> {
                this.setState(state => {
                    const index = state.selectedFile.indexOf(file);
                    const newFileList = state.selectedFile.slice();
                    newFileList.splice(index, 1);
                    return {
                        selectedFile: newFileList
                    }
                });
            },
            beforeUpload: file => {
                this.setState(state => ({
                    selectedFile:[...state.selectedFile, file],
                }));
                return false;
            },
            selectedFile,
        };
    return(
        <div>
          
            <div style={{margin:'0 0', paddingTop: 30, textAlign:'center', minHeight: 100}}>
                <Title level={3}> 목표 달성 입력</Title>
            </div>
            <div style={{width:'75%', textAlign:'start', display:'box',marginLeft:'auto',marginRight:'auto'}}>
                <Form
                name="register"
                onFinish={this.onFinish}>
                    <Row>
                        <Col span={3}>
                            <Title level={4}>제목</Title>
                        </Col>
                        <Col span={21}>
                            <Form.Item
                            name="title"
                            style={{width:'100%'}}
                            rules={[{
                                required:true,
                                message: '제목을 입력해주세요!'
                            }]}>
                                <Input />
                            </Form.Item>
                        </Col>
                        <Col span={3}>
                            <Title level={4}> 첨부파일</Title>
                        </Col>
                        <Col span={21}>
                            <Form.Item
                            name="images"
                            style={{width:'100%'}}
                            >
                                <Upload {...properties}>
                                    <Button >업로드</Button>
                                </Upload>
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row>
                        <Form.Item
                        name="content"
                        style={{width:'100%'}}>
                            <Input.TextArea allowClear autoSize={{minRows: 4}}>
                            </Input.TextArea>
                        </Form.Item>
                    </Row>
                    <Row>
                        <Col span={11} offset={1}>
                            <Button htmlType="submit" type="primary" style={{width:'30%',float:'right'}}>저장</Button>
                        </Col>
                        <Col span={11} offset={1}>
                            <Button type="primary" style={{width:'30%'}} onClick={this.goBack}>취소</Button>
                            
                        </Col>
                    </Row>
                </Form>
            </div>
        </div>
    
    )
}
}


export default TargetInput;