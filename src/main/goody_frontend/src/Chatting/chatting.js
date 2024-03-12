import React, { useState, useEffect } from 'react';
import { Nav } from '../Component/Nav';
import { ActionBar } from '../Component/ActionBar';
import ChatListItem from './Component/ChatListItem';
import { Link } from 'react-router-dom';
import { Empty } from 'antd';

const Chatting = () => {
  const [chatData, setChatData] = useState([]);
  const actionBarName = "채팅목록";
  const apiurl = "/goody/chatroom/list";
  const token = localStorage.getItem('token');

  const fetchData = async () => {
    try {
      const headers = {
        Authorization: `${token}`,
      };

      const response = await fetch(apiurl, {
        method: 'GET',
        headers,
      });

      if (!response.ok) {
        throw Error('네트워크 오류');
      }

      const data = await response.json();

      if (data && data.length > 0) {
        setChatData(data);
        console.log(data);
      } else {
        console.error('API에서 데이터를 가져오는 중 오류 발생: 데이터가 비어 있습니다.');
      }
    } catch (error) {
      console.error('오류 발생:', error);
      throw error;
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <>
      <ActionBar actionBarName={actionBarName} />
  
      <div className='mt-20'>
        {chatData.length > 0 ? (
          chatData.map((chatItem, index) => (
            <Link key={chatItem.roomId} 
              to={`/chatdetails/${chatItem.roomId}?contentsId=${chatItem.contentsId}`}
              state={{ chattingEnteruser: chatItem.enterUsers }}
            >
              <ChatListItem
                chat_img={chatItem.roomImg}
                chat_id={chatItem.roomName}
                chat_explain={chatItem.enterUsers.join(', ')}
              />
              {index === chatData.length - 1 && <div style={{ marginBottom: '6rem' }}></div>}
            </Link>
          ))
        ) : (
          <div className='flex justify-center items-center h-[50rem]'>
            <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
          </div>
        )}
      </div>
  
      <Nav />
    </>
  );
};

export default Chatting;
