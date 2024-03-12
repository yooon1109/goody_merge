import React, { useState } from 'react';
import { ActionBarClose } from '../Component/ActionBarClose';
import Button_y from './reviewbtn';
import Button_g from './reviewbtn_gray';
import { Link } from 'react-router-dom';
import { useLocation } from 'react-router-dom';


const review = () => {
  const actionBarName = "리뷰";
  const location = useLocation();
  const itemInfoDocumentId = location.state? location.state.itemInfoDocumentId : '';
  const chattingEnteruser =  location.state? location.state.chattingEnteruser : '';
  const uniqueChattingEnteruser = Array.from(new Set(chattingEnteruser));
  const maxSelectedButtons = 4; // 최대 선택 가능한 버튼 수
  const [selectedButtons, setSelectedButtons] = useState([]);
  const [isClicked, setIsClicked] = useState(false); // 버튼 클릭 상태를 저장하는 상태

  const receiveId = uniqueChattingEnteruser.filter(userId => userId !== localStorage.getItem('userId'));

  console.log(uniqueChattingEnteruser);
  console.log(receiveId);

  
  // 버튼 클릭 핸들러
  const handleButtonClick = (buttonName) => {
    setIsClicked(!isClicked); // 클릭 상태를 반전시킴

    if (selectedButtons.includes(buttonName)) {
      // 이미 선택된 버튼이면 해제
      setSelectedButtons(selectedButtons.filter((name) => name !== buttonName));
    } else if (selectedButtons.length < maxSelectedButtons) {
      // 최대 선택 가능한 개수에 도달하지 않았을 때만 선택
      setSelectedButtons([...selectedButtons, buttonName]);
    }
  };

  // 클릭한 버튼 정보를 서버에 보냄
  const selectedReviewData = {
    reviewerId: localStorage.getItem('userId'), 
    reviewKeywords: selectedButtons,
    contentId: itemInfoDocumentId, 
    documentId : itemInfoDocumentId
  };

  const handleReviewClick = async () => {

    fetch(`/goody/review/keywords?receiveId=${receiveId}&contentsDocumentId=${itemInfoDocumentId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${localStorage.getItem('token')}`,
      },
      body: JSON.stringify(selectedReviewData), // JSON 형식으로 데이터 전송
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

  return (
    <>
      <ActionBarClose actionBarName={actionBarName} />

      <div className=' flex justify-center items-center flex-col pt-32'>
        <div className='fontsmall'>
          이런 점이 가장 좋았어요
        </div>

        <div className='flex'>
          <Button_y
            onClick={() => handleButtonClick('good1')}
            active={selectedButtons.includes('good1')}>친절해요</Button_y>
          <Button_y
            onClick={() => handleButtonClick('good2')}
            active={selectedButtons.includes('good2')}>응답이 빨라요</Button_y>
        </div>
        <div className='flex'>
          <Button_y
            onClick={() => handleButtonClick('good3')}
            active={selectedButtons.includes('good3')}>믿어도 돼요</Button_y>
          <Button_y
            onClick={() => handleButtonClick('good4')}
            active={selectedButtons.includes('good4')}>상품상태가 좋아요</Button_y>
        </div>
        <div className='flex'>
          <Button_y
            onClick={() => handleButtonClick('good5')}
            active={selectedButtons.includes('good5')}>저렴해요</Button_y>
          <Button_y
            onClick={() => handleButtonClick('good6')}
            active={selectedButtons.includes('good6')}>시간약속을 잘지켜요</Button_y>
        </div>

        <div className='fontsmall pt-5'>
          이런 점이 조금 아쉬웠어요
        </div>

        <div className='flex'>
          <Button_g
            onClick={() => handleButtonClick('bad1')}
            active={selectedButtons.includes('bad1')}>불친절해요</Button_g>
          <Button_g
            onClick={() => handleButtonClick('bad2')}
            active={selectedButtons.includes('bad2')}>응답이 느려요</Button_g>
        </div>
        <div className='flex'>
          <Button_g
            onClick={() => handleButtonClick('bad3')}
            active={selectedButtons.includes('bad3')}>믿지 못하겠어요</Button_g>
          <Button_g
            onClick={() => handleButtonClick('bad4')}
            active={selectedButtons.includes('bad4')}>상품상태가 안좋아요</Button_g>
        </div>
        <div className='flex'>
          <Button_g
            onClick={() => handleButtonClick('bad5')}
            active={selectedButtons.includes('bad5')}>비싸요</Button_g>
          <Button_g
            onClick={() => handleButtonClick('bad6')}
            active={selectedButtons.includes('bad6')}>시간약속을 못지켜요</Button_g>
        </div>

        <div className='fixed bottom-0 p-2'>
          <img className='rounded-2xl mb-2' src="img/review.png" alt="안내문" style={{ width: '25rem', height: '4rem' }} />

          <Link 
                    to = {'/honeyhome'}
                    state = { {itemInfoDocumentId, receiveId} }
                >
            <button
              className='font-bold mt-2 text-lg'
              onClick={handleReviewClick}
              style={{
                width: '25rem', height: '3rem',
                borderRadius: '10px 0 10px 10px',
                backgroundColor: '#5F3300', color: '#FFFFFF'
              }}>
              꿀단지 채우러 가기
            </button>
          </Link>
        </div>
      </div>
    </>
  );
};

export default review;