
import './font.css'
import Slider from '@mui/material/Slider';
import PropTypes from 'prop-types';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Link } from 'react-router-dom';
import { useLocation } from 'react-router-dom';


const MySlider = ({ value, handleChange }) => {
  return (
    <div>
      <p className='font' style={{ fontSize: '40px', color: 'white' }}>꿀 {value}%</p>
      <Slider
        aria-label="꿀집"
        value={value}
        onChange={handleChange}
        valueLabelDisplay="auto"
        step={10}
        marks
        min={0}
        max={100}

        sx={{
          '& .MuiSlider-thumb': {
            backgroundColor: 'white',
            border: '2px solid #5F3300',
          },
          color: '#5F3300',
          height: '15px',
          width: '16rem',
        }}
      />

    </div>
  );
};

MySlider.propTypes = {
  value: PropTypes.number.isRequired,
  handleChange: PropTypes.func.isRequired,
};

const ReviewHoneyHome = () => {
  const [value, setValue] = useState(0);
  const location = useLocation();
  const itemInfoDocumentId = location.state?.itemInfoDocumentId;
  const receiveId = location.state? location.state.receiveId : '';

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  const navigate = useNavigate();
  const handleBack = () => {
    navigate(-1); // 이전 페이지로 이동하는 함수
  };

  const handleReviewClick = async () => {


    fetch(`/goody/review/rate?reviewDocumentId=${itemInfoDocumentId}&receiveId=${receiveId}&rate=${value}`, { //수정해야댐
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${localStorage.getItem('token')}`,
      },

    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('서버에서 오류가 발생했습니다.');
        }
        console.log('데이터가 성공적으로 전송되었습니다.');
        return response.json(); // 서버에서의 응답을 JSON으로 파싱

      })
      .catch((error) => {
        if (error.response) {
          console.error('HTTP 상태 코드:', error.response.status);
        }

      });
  };

  const getHoneyImage = (value) => {
    if (value >= 100) {
      return 'img/honeybox11.png'; // 슬라이더 값이 10 이상일 때 다른 이미지
    }
    else if (value >= 90) {
      return 'img/honeybox10.png';
    }
    else if (value >= 80) {
      return 'img/honeybox9.png';
    }
    else if (value >= 70) {
      return 'img/honeybox8.png';
    }
    else if (value >= 60) {
      return 'img/honeybox7.png';
    }
    else if (value >= 50) {
      return 'img/honeybox6.png';
    }
    else if (value >= 40) {
      return 'img/honeybox5.png';
    }
    else if (value >= 30) {
      return 'img/honeybox4.png';
    }
    else if (value >= 20) {
      return 'img/honeybox3.png';
    }
    else if (value >= 10) {
      return 'img/honeybox2.png';
    }
    else {
      return 'img/honeybox1.png'; // 슬라이더 값이 10 미만일 때 기본 이미지
    }
  };

  const divStyle = {
    position: 'fixed',
    top: 0,
    left: 0,
    width: '100%',
    height: '100%',
    zIndex: -1,
    background: 'linear-gradient(90deg, #F4E299 0%, #FFCF0E 100%)'
   
  };

  return (
    <>
      <div className='pl-2'>
        <div>
          <button
            className='absolute pt-4 top-0 right-4 drop-shadow-[0_2px_1px_rgba(220,166,19,100)]'
            onClick={handleBack}
          >
            <img src="img/close.png" alt='닫기' width={'30px'} height={'30px'} />
          </button>
        </div>
      </div>
      <div style={divStyle} className='relative'>


        <div className='fixed inset-0 flex items-center justify-center flex-col'>
          <p className='font font-extrabold m-5' style={{ fontSize: '40px', color: 'white' }}>
            꿀단지를 채워주세요
          </p>

          <img src={getHoneyImage(value)} className='w-72 drop-shadow-[0_3px_2px_rgba(220,166,20,100)]' alt='Honey' />

          <MySlider value={value} handleChange={handleChange} />

          <Link to={'/reviewperfect'} state={{ value }}>
            <button
              onClick={handleReviewClick}
              className='fontsmall font-bold mt-2'
              style={{
                width: '18rem',
                height: '3rem',
                borderRadius: '10px 0 10px 10px',
                backgroundColor: '#5F3300',
                color: '#FFFFFF',
              }}
            >
              다음
            </button>
          </Link>
        </div>
      </div></>
  );

}
export default ReviewHoneyHome;