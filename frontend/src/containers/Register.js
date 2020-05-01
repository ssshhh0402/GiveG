import React, {Component} from 'react';
import {Form,Input,Select,Row,Col,Button,Checkbox,DatePicker,Upload,InputNumber} from 'antd';
import {UploadOutlined, InboxOutlined} from '@ant-design/icons';
import {connect} from 'react-redux';
import {Typography,Breadcrumb} from 'antd';
import {createInstance} from '../api/index';
import moment from 'moment'

const instance = createInstance(); 
const formItemLayout = {
    labelCol: {
        xs: { span: 24},
        sm: { span: 8}
    },
    wrapperCol: {
        xs: { span: 24},
        sm: { span: 16}
    }
};
const {Option} = Select
const tailFormItemLayout = {
    wrapperCol: {
        xs: {
            span: 24,
            offset: 0
        },
        sm: {
            span: 16,
            offset: 8
        }
    }
};
class Register extends Component{
    state={
        selectedFile : [],
        totalMoney: 0,
    }
    numberWithComma(money){
        return money.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
      }
    onFinish = values => {
        if (values['target1']['percent'] + values['target2']['percent']+ values['target3']['percent'] !== 100){
            alert('세부 목표를 다시 설정해주세요!')
        }
        else{
            values['days'] = moment(values['days'],'YYYY-MM-DD').format('YYYY-MM-DD')
            let form = new FormData();
            form.append('author', this.props.email);
            form.append('content', values['description']);
            form.append('donationGoal',values['money']);
            form.append('endDate', moment(values['days'],'YYYY-MM-DD').format('YYYY-MM-DD'));
            form.append('goalContent1', values['target1']['desc']);
            form.append('goalContent2', values['target2']['desc']);
            form.append('goalContent3', values['target3']['desc']);
            form.append('goalPercent1',values['target1']['percent']);
            form.append('goalPercent2', values['target2']['percent']);
            form.append('goalPercent3',values['target3']['percent']);
            form.append('image',this.state.selectedFile[0]);
            form.append('title',values['title']);
            // this.props.onFinishing(form);
            console.log(this.state.selectedFile)
            console.log(form.get('image'))
            console.log(form.get('endDate'))
            console.log(form)
            console.log(form.get('author'))
            try{
                instance.post('/api/v1/posts', form,{
                headers:{'Content-Type' : 'multipart/form-data'}
            }).then(response => {
                console.log(response)
                if (response.status === 200){
                    alert('등록이 완료되었습니다.')
                    window.location.href='/'
                }
            })
        }catch(e){
            console.log(e)}
     }
    }
    onChange = values => {
        return this.setState({
            totalMoney: values,
        })
    }
    render(){
        const {Title} = Typography;
        const {selectedFile} = this.state;
        const props = {
            listType:'picture',
            onRemove: file => {
                this.setState(state => {
                    const index = state.selectedFile.indexOf(file);
                    const newFileList = state.selectedFile.slice();
                    newFileList.splice(index, 1);
                    return {
                        selectedFile: newFileList,
                    };
                });
            },
            
            beforeUpload: file => {
                this.setState(state =>({
                    selectedFile:[...state.selectedFile, file],
                }));
                return false;
            },
            selectedFile,
        };
        return(
            <div style={{backgroundColor:""}}>
            <div id="site-layout-background"  style={{borderRadius:"25px", margin:'0 0', paddingTop: 30, textAlign:'center', minHeight: 200,padding: 24, background: '#00F260',
                        background: '-webkit-linear-gradient(to right, #0575E6, #00F260)',
                        background: 'linear-gradient(to right, #0575E6, #00F260)'}}>
                    <Title level={2} style={{paddingTop:"2.5vw", color:"white"}}><img src="/images/frontEnd.png"  style={{width:"3%"}}/>기부 프로젝트 등록</Title>
            </div>            
            <div style={{ marginTop:"2vw", width:'75%',textAlign:'center', display:'box', paddingLeft:"3vw"}}>
                <Form
                {...formItemLayout}
                name="register"
                onFinish={this.onFinish}
                >
                    <Form.Item
                    name="title"
                    label="Title"
                    rules={[
                        {
                            required: true,
                            message: 'please input your Title'
                        }
                    ]}
                    >
                        <Input />
                    </Form.Item>
                    <Form.Item
                    name="money"
                    label="목표금액"
                    rules={[{
                        required: true,
                        message: 'please input your money'
                    }]}
                    >
                        <InputNumber
                        formatter={value=> `${value}원`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
                        initialValue={0}
                        step={10000}
                        max= {10000000}
                        style={{width:'100%'}}
                        onChange={this.onChange}
                        />
                    </Form.Item>
                    <Form.Item
                    label="세부목표 1">
                            <Input.Group compact>
                                <Form.Item
                                style={{width:'100%'}}
                                name={['target1','percent']}
                                >
                                    <Select style={{width:'100%',textAlign:'left'}} >
                                        <Option value={10}>10%({this.numberWithComma(this.state.totalMoney * 0.1)}원)</Option>
                                        <Option value={20}>20%({this.numberWithComma(this.state.totalMoney * 0.2)}원)</Option>
                                        <Option value={30}>30%({this.numberWithComma(this.state.totalMoney * 0.3)}원)</Option>
                                        <Option value={40}>40%({this.numberWithComma(this.state.totalMoney * 0.4)}원)</Option>
                                        <Option value={50}>50%({this.numberWithComma(this.state.totalMoney * 0.5)}원)</Option>
                                                                           
                                    </Select>
                                </Form.Item>
                                <Form.Item
                                name={['target1', 'desc']}
                                noStyle
                                >
                                    <Input.TextArea style={{textAlign:'start'}}></Input.TextArea>
                                </Form.Item>
                            </Input.Group>
                    </Form.Item>
                    <Form.Item
                    label="세부목표 2">
                            <Input.Group compact>
                                <Form.Item
                                style={{width:'100%'}}
                                name={['target2','percent']}
                                >
                                    <Select style={{width:'100%',textAlign:'left'}}>
                                        <Option value={10}>10%({this.numberWithComma(this.state.totalMoney * 0.1)}원)</Option>
                                        <Option value={20}>20%({this.numberWithComma(this.state.totalMoney * 0.2)}원)</Option>
                                        <Option value={30}>30%({this.numberWithComma(this.state.totalMoney * 0.3)}원)</Option>
                                        <Option value={40}>40%({this.numberWithComma(this.state.totalMoney * 0.4)}원)</Option>
                                        <Option value={50}>50%({this.numberWithComma(this.state.totalMoney * 0.5)}원)</Option>
                                    </Select>
                                </Form.Item>
                                <Form.Item
                                name={['target2', 'desc']}
                                noStyle
                                >
                                    <Input.TextArea style={{textAlign:'start'}}></Input.TextArea>
                                </Form.Item>
                            </Input.Group>
                    </Form.Item>
                    <Form.Item
                    label="세부목표 3">
                            <Input.Group compact>
                                <Form.Item
                                style={{width:'100%'}}
                                name={['target3','percent']}
                                >
                                    <Select style={{width:'100%',textAlign:'left'}}>
                                        <Option value={10}>10%({this.numberWithComma(this.state.totalMoney * 0.1)}원)</Option>
                                        <Option value={20}>20%({this.numberWithComma(this.state.totalMoney * 0.2)}원)</Option>
                                        <Option value={30}>30%({this.numberWithComma(this.state.totalMoney * 0.3)}원)</Option>
                                        <Option value={40}>40%({this.numberWithComma(this.state.totalMoney * 0.4)}원)</Option>
                                        <Option value={50}>50%({this.numberWithComma(this.state.totalMoney * 0.5)}원)</Option>
                                    </Select>
                                </Form.Item>
                                <Form.Item
                                name={['target3', 'desc']}
                                noStyle
                                >
                                    <Input.TextArea style={{textAlign:'start'}}></Input.TextArea>
                                </Form.Item>
                            </Input.Group>
                    </Form.Item>
                    <Form.Item
                    name="days"
                    label="종료날짜"
                    rules={[{
                        required:true,
                        message: 'please input your end day'
                    }]}>
                        <DatePicker style={{width:'100%'}} format='YYYY-MM-DD'/>
                    </Form.Item>
                    <Form.Item
                    name="pictures"
                    label="관련 이미지"
                    rules={[{
                        required: true,
                        message: 'please input your images'
                    }]}>
                    <Upload {...props}>
                        <Button >
                            <UploadOutlined/> Upload
                        </Button>
                    </Upload>
                    </Form.Item>
                    <Form.Item
                    name="description"
                    label="추가 설명"
                    rules={[{
                        required:'true',
                        message:'please input your description'
                    }]}>
                        <Input.TextArea/>
                    </Form.Item>
                    <Form.Item>
                        <Button type="primary" htmlType="submit" style={{width:'30%',marginLeft:'auto', marginTop:"2vw"}}>등록하기</Button>
                    </Form.Item>
                     
                    
                    
                </Form>
            </div>
            </div>
        )
    }
}
const mapStateToProps = state => {
    return {
      email: state.email
    }
  };
const mapDispachToProps = dispatch => {
    return {
        onFinishing : (values) => 
        dispatch({
            type: "Contents_Register",
            payload: {
                // title: values['title'],
                // donationGoal: values['money'],
                // endDate : values['days'],
                // image: values['pictures'],
                // goal : values['goal'],
                // author : values['owner'],
                // content:values['description'],
                // state:'string',
                // target1:values['target1'],
                // target2:values['target2'],
                // target3:values['target3']
                data : values
            }
        })
    }
}
export default connect(
    mapStateToProps,
    mapDispachToProps
)(Register);