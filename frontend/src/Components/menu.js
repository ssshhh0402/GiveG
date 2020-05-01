import { Tabs, Breadcrumb, Typography } from 'antd';
import React, { Component } from 'react';
import '../css/menu.css'

const contents = [{
  tab: '목표 완성형 이란?',
  title: "목표 완성형 이란?",
  content: ["GIVE기부는 모금액이 정확하고 정당하게 사용하되도록 하며, 기부자들이 안심하고 기부할 수 있는 서비스입니다.",
    "이를 위하여 기부 프로젝트들은 세부 목표를 갖으며, 해당 목표가 달성이 되고 인증으 하여만 금액이 지불됩니다.",
    "따라서 기부자들은 목표 달성에 대한 세부 내역을 확인하며, 보다 정확히 기부 진행 상황 및 현황에 대한 확인이 가능합니다."]
}, {
  tab: '블록체인의 활용',
  title: "블록체인의 활용",
  content: ["GIVE기부는 블록체인을 통해 기부 프로젝트에 대한 모든 것을 장부로 기록을 합니다.",
    "등록, 목표, 목표 진행, 참여자, 결제, 환불 등의 데이터를 저장하여, 만일에 있을 보안 위협으로 부터 데이터를 지키며,",
    "프로젝트가 정상적으로 진행되고 있음을 기부자들이 확인할 수 있도록 합니다. 또한,",
    "프로젝트에 대한 모든 내역이 저장됨에 따라, 진행자의 기부 프로젝트에 대한 수정, 삭제, 추가 등의 변경이 모두 사용자들에게 공개됩니다."],
}, {
  tab: '결제 및 환불',
  title: '결제 및 환불',
  content: ["사용자의 편의를 위해 카카오 페이를 통한 간편 결제 및 환불을 할 수 있습니다.",
    "결제와 환불은 기본적으로 프로젝트가 진행되기 이전에 가능하며, 개인정보 이외의 금액 및 날짜로 된 이력이 블록체인에 기록됩니다.",
    "추가적으로 기부 프로젝트 중단 요청에 의해 환불이 진행될 수 있습니다."],
}, {
  tab: '기부 프로젝트 중단 요청',
  title: '기부 프로젝트 중단 요청',
  content: ["좋지 못한 방향으로 진행되는 프로젝트에 대해 기부자들은 중단 요청을 할 수 있습니다.",
    "중단 요청의해 프로젝트가 중단될 경우, 프로젝트의 진행된 목표로 인해 지급된 금액 이외의 금액이 기부금의 비율에 따라 환불됩니다.",
    "프로젝트가 중단 될 수 있는 조건은 2가지이며 모두 충족될 경우 중단됩니다.",
  ],
  condition: [
    "1. 프로젝트의 기부금의 50%이상의 비율의 기부자들이 중단을 요청한다.",
    "2. 프로젝트의 기부금의 50%이상의 기부자들이 중단을 요청한다."]
}
];

const { TabPane } = Tabs;
class Menu extends Component {
  constructor(props) {
    super(props);
    this.state = {
      mode: 'top',
    };
  }

  handleModeChange = e => {
    const mode = e.target.value;
    this.setState({ mode });
  };
  render() {
    const { mode } = this.state;
    const { Title } = Typography;

    return (
      <div id="menuContainer">
        <Breadcrumb style={{ margin: '0 0', paddingTop: 30, textAlign: 'center', minHeight: 100 }}>
          <Title level={3} style={{ color: '#00b09b' }} >목표 완성형 기부 서비스 플랫폼</Title>
        </Breadcrumb>
        <Tabs
          defaultActiveKey="0"
          tabPosition="left"
          keyboard
          size="large" >
          {contents.map((value, i) => (
            <TabPane tab={`${value.tab}`} key={i}>
              <Title level={4} style={{ paddingLeft: '6vw', textAlign: 'left', color: '#00b09b' }} >{value.title}</Title>
              <div style={{ margin: 'auto', paddingLeft: '8vw', textAlign: 'left', fontSize: '1.2vw' }}>
                {value.content.map((v, idx) => (
                  <div key={idx}>{v}</div>
                ))}
              </div>
              {
                value.condition?
                <div style={{ margin: 'auto', paddingLeft: '10vw', textAlign: 'left', fontSize: '1vw' }}>
                  {value.condition.map((v, idx) => (
                    <div key={idx}>{v}</div>
                  ))}
                </div>
                :<div></div>
              }
            </TabPane>
          ))}
        </Tabs>
      </div>
    );
  }
}

export default Menu;