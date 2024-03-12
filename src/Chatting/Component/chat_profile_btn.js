import React from 'react';
import PropTypes from 'prop-types';

const ChatProfileBtn = ( {ItemInfo}) => {
    return (
        <button
className="rounded-full border w-14 h-14 z-10 overflow-hidden "
>
<img src={ItemInfo. thumbnailImg} alt="프로필사진" className="w-full h-full" />
</button>
    );
  };

  ChatProfileBtn.propTypes = {
    ItemInfo: PropTypes.shape({
      thumbnailImg: PropTypes.string.isRequired,
    }).isRequired,
  };
  
  export default ChatProfileBtn;