import React from 'react';
import PropTypes from 'prop-types';

const ChatListItem = ({ chat_img, chat_id, chat_explain }) => {
  return (
    <div className="block w-full h-15">
      <div className="p-5 flex w-full">
        <img src={chat_img} alt="프로필사진" className="rounded-full border" style={{ width: '2.5rem', height: '2.5rem' }}></img>
        <div className="pl-3">
          <div style={{ fontSize: '1.1rem' }} className="font-extrabold flex justify-start">
            {chat_id}
          </div>
          <div style={{ fontSize: '0.9rem' }} className="flex justify-start">
            {chat_explain}
          </div>
        </div>
      </div>
      <hr />
    </div>
  );
};

ChatListItem.propTypes = {
  chat_img: PropTypes.string.isRequired,
  chat_id: PropTypes.string,
  chat_explain: PropTypes.string.isRequired,
};

export default ChatListItem;
