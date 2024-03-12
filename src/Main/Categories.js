import React, { useState, useEffect } from 'react';
import './Categories.css';
import PropTypes from 'prop-types';
 import { message } from 'antd';

const Categories = ({ onClose }) => {
  const [activeTab, setActiveTab] = useState('영화');
  const [messageApi, contextHolder] = message.useMessage();

  const warning = (content) => {
    messageApi.open({
      type: 'warning',
      content: content,
      duration: 2,
      style: {
        marginTop: '72vh',
      },
    });
  };
  
  const openCity = (cityName) => {
    setActiveTab(cityName);
  };

  useEffect(() => {
    document.getElementById('defaultOpen').click();
  }, []);

  // 링크 클릭 시 얼럿 창을 띄우는 핸들러
  const handleLinkClick = () => {
    warning(
      <div>
        이 기능은 미구현된 기능입니다!
        <br />
        조금만 기다려 주세요 ~
      </div>
    )
  };

  return (
    <div className='ml-[-1.5rem] mt-[-1rem]'>
      <div className='flex'>
        <button onClick={onClose} className='absolute mt-[-1.3rem] mr-[-0.5rem] right-0 h-20 p-4 drop-shadow-[0_2px_1px_rgba(220,166,19,100)]'>
          <img src="img/close.png" alt='닫기' width={'30px'} height={'30px'} />
        </button>
      </div>

      <div className="tab">
        <button className={activeTab === '영화' ? 'tablinks active' : 'tablinks'} onClick={() => openCity('영화')} id="defaultOpen"> 영화</button>
        <button className={activeTab === '게임' ? 'tablinks active' : 'tablinks'} onClick={() => openCity('게임')}> 게임</button>
        <button className={activeTab === '연예인' ? 'tablinks active' : 'tablinks'} onClick={() => openCity('연예인')}>연예인</button>
        <button className={activeTab === '캐릭터' ? 'tablinks active' : 'tablinks'} onClick={() => openCity('캐릭터')}> 캐릭터</button>
        <button className={activeTab === '스포츠' ? 'tablinks active' : 'tablinks'} onClick={() => openCity('스포츠')}> 스포츠</button>
        <button className={activeTab === '만화' ? 'tablinks active' : 'tablinks'} onClick={() => openCity('만화')}> 만화</button>
      </div>

      <div id="영화" className={activeTab === '영화' ? 'tabcontent' : 'tabcontent hidden'}>
        <div className="">
          <ul>
            <li className="font-bold">영화</li>
            <li className='my-5'><a href="#" onClick={() => handleLinkClick('국내 영화')}>· 국내 영화</a></li>
            <li className='my-5'><a href="#" onClick={() => handleLinkClick('해외 영화')}>· 해외 영화</a></li>
          </ul>
        </div>
      </div>

      <div id="연예인" className={activeTab === '연예인' ? 'tabcontent' : 'tabcontent hidden'}>
        <div className="">
          <ul>
            <li className="font-bold">아이돌</li>
            <li className='my-5'><a href="#" onClick={() => handleLinkClick('남자 아이돌')}>· 남자 아이돌</a></li>
            <li className='my-5'><a href="#" onClick={() => handleLinkClick('여자 아이돌')}>· 여자 아이돌</a></li>
            <li className="my-5"><a href="#" onClick={() => handleLinkClick('남자 솔로 가수')}>· 남자 솔로 가수</a></li>
            <li className="mb-8"><a href="#" onClick={() => handleLinkClick('여자 솔로 가수')}>· 여자 솔로 가수</a></li>
          </ul>
        </div> 
        
        <div className="">
          <ul>
            <li className="font-bold">배우</li>
            <li className='my-5'><a href="#" onClick={() => handleLinkClick('남자 배우')}>· 남자 배우</a></li>
            <li className='my-5'><a href="#" onClick={() => handleLinkClick('여자 배우')}>· 여자 배우</a></li>
          </ul>
        </div>
      </div>

      <div id="게임" className={activeTab === '게임' ? 'tabcontent' : 'tabcontent hidden'}>
        <ul>
          <li className="font-bold">게임</li>
          <li className='my-5'><a href="#" onClick={() => handleLinkClick('롤플레잉 게임 RPG')}>· 롤플레잉 게임 RPG</a></li>
          <li className='my-5'><a href="#" onClick={() => handleLinkClick('시뮬레이션 게임 SLG')}>· 시뮬레이션 게임 SLG</a></li>
          <li className="my-5"><a href="#" onClick={() => handleLinkClick('액션 게임 ATG')}>· 액션 게임 ATG</a></li>
          <li className="my-5"><a href="#" onClick={() => handleLinkClick('리듬 게임')}>· 리듬 게임</a></li>
          <li className="my-5"><a href="#" onClick={() => handleLinkClick('연애 게임')}>· 연애 게임</a></li>
          <li className='mb-8'><a href="#" onClick={() => handleLinkClick('스포츠 게임')}>· 스포츠 게임</a></li>
        </ul>
      </div>

      <div id="스포츠" className={activeTab === '스포츠' ? 'tabcontent' : 'tabcontent hidden'}>
        <div className="">
          <ul>
            <li className="font-bold">스포츠</li>
            <li className='my-5'><a href="#" onClick={() => handleLinkClick('육상 경기 종목')}>· 육상 경기 종목 </a></li>
            <li className='my-5'><a href="#" onClick={() => handleLinkClick('구기 종목')}>· 구기 종목</a></li>
            <li className="my-5"><a href="#" onClick={() => handleLinkClick('라켓 스포츠')}>· 라켓 스포츠</a></li>
            <li className="my-5"><a href="#" onClick={() => handleLinkClick('수상 스포츠')}>· 수상 스포츠</a></li>
            <li className='my-5'><a href="#" onClick={() => handleLinkClick('기계 체조')}>· 기계 체조</a></li>
            <li className='mb-8'><a href="#" onClick={() => handleLinkClick('격기 종목')}>· 격기 종목</a></li>
          </ul>
        </div>
      </div>

      <div id="만화" className={activeTab === '만화' ? 'tabcontent' : 'tabcontent hidden'}>
        <div className="">
          <ul>
            <li className="font-bold">만화</li>
            <li className='my-5'><a href="#" onClick={() => handleLinkClick('웹툰')}>· 웹툰 </a></li>
            <li className='my-5'><a href="#" onClick={() => handleLinkClick('애니메이션')}>· 애니메이션</a></li>
            <li className="my-5"><a href="#" onClick={() => handleLinkClick('만화책')}>· 만화책</a></li>
          </ul>
        </div>
      </div>

      <div id="캐릭터" className={activeTab === '캐릭터' ? 'tabcontent' : 'tabcontent hidden'}>
        <ul>
          <li className="font-bold">캐릭터</li>
          <li className='my-5'><a href="#" onClick={() => handleLinkClick('카카오')}>· 카카오</a></li>
          <li className='my-5'><a href="#" onClick={() => handleLinkClick('라인')}>· 라인</a></li>
          <li className="my-5"><a href="#" onClick={() => handleLinkClick('산리오')}>· 산리오</a></li>
          <li className="my-5"><a href="#" onClick={() => handleLinkClick('기타')}>· 기타</a></li>
        </ul>
      </div>

      <div >
        {contextHolder}
      </div>
    </div>
  );
};

Categories.propTypes = {
  onClose: PropTypes.func.isRequired,
};

export default Categories;
