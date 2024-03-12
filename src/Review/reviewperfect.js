import React from 'react';
import './font.css';
import Button_honey from './Reviewhoneybtn';
import Button_honey_2 from './Reviewhoneybtn2';
import { Link } from 'react-router-dom';
import { useLocation } from 'react-router-dom';

const getHoneyImage = (value) => {
  if (value >= 100) {
    return 'img/honeybox11.png'; // 슬라이더 값이 10 이상일 때 다른 이미지
  }
  else if(value >= 90){
    return 'img/honeybox10.png';
  } 
  else if(value >= 80){
    return 'img/honeybox9.png';
  } 
  else if(value >= 70){
    return 'img/honeybox8.png';
  } 
  else if(value >= 60){
    return 'img/honeybox7.png';
  } 
  else if(value >= 50){
    return 'img/honeybox6.png';
  } 
  else if(value >= 40){
    return 'img/honeybox5.png';
  } 
  else if(value >= 30){
    return 'img/honeybox4.png';
  } 
  else if(value >= 20){
    return 'img/honeybox3.png';
  } 
  else if(value >= 10){
    return 'img/honeybox2.png';
  } 
  else {
    return 'img/honeybox1.png'; // 슬라이더 값이 10 미만일 때 기본 이미지
  }
};

const reviewperfect = () => {
  const location = useLocation();
  const value = location.state ? location.state.value : 0;

  const divStyle = {
    zIndex: -1,
    width: '100%',
    height: '100vh',
    background: 'linear-gradient(90deg, #F4E299 0%, #FFCF0E 100%)'
    /* 그 외 다른 스타일 설정 가능 */
  };
  
  return (
    <>
    <div style={divStyle}>
      <div className='fixed inset-0 flex items-center justify-center flex-col'>
        <p className='text-white text-xl p-4 drop-shadow-[0_2px_1px_rgba(220,166,19,100)] '> { value } / 100 꿀을 드렸어요 ! </p>
        <img src={getHoneyImage(value)} className='w-72 drop-shadow-[0_3px_2px_rgba(220,166,20,100)]' alt='Honey' />
        <div className='pt-10 pb-5 text-center'>
          <p className='text-xl p-3 font-bold ' style={{ color:'#5F3300' }}>리뷰 작성 완료!</p>
          <p className='text-lg font-bold text-white drop-shadow-[0_2px_1px_rgba(220,166,19,100)] '>작성하신 리뷰는 <br/> 다른 사용자들에게 많은 도움이 될거예요</p>
        </div>

        <Link to='/reviewlist'>
        <Button_honey >내가 작성한 리뷰 보러가기</Button_honey>
        </Link>

        <Link to='/home'>
        <Button_honey_2 className='bg-white' style={{color:'#5F3300'}}>홈</Button_honey_2>
        </Link>
      </div>        
    </div>
    </>
  );
};

export default reviewperfect;