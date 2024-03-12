import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

const ActionBarDot = ({ actionBarName }) => {
    const [showOptions, setShowOptions] = useState(false);

  const handleDdongButtonClick = () => {
    setShowOptions((prevShowOptions) => !prevShowOptions);
  };

  const handleOptionClick = (option) => {
    console.log(`Selected option: ${option}`);
    setShowOptions(false); // Close the options after selecting an option
  };

  return (
    <div className="w-full h-16 relative">
      <img src="img\ActionBar.png" className="absolute" />

      <div style={{ position: 'absolute', right:65, top:25 }}>
        <Link to="/collection">
        <button>
          <img src="img\Close.png" className='drop-shadow-[0_2px_1px_rgba(220,166,19,100)]' style={{ width: '30px', height: '30px' }} />
        </button>
        </Link>
      </div>

      <div style={{ position: 'absolute', right: 35, top: 28 }}>
        <button onClick={handleDdongButtonClick}>
          <img src="img\Ddong.png" className='drop-shadow-[0_2px_1px_rgba(220,166,19,100)]' style={{ width: '5px', height: '25px' }}  />
        </button>

          {showOptions && (
          <div style={{ position: 'absolute', right:20, top: -3, backgroundColor: '#ffffff', border: '1px solid #575757', borderRadius: '5px', padding: '5px', zIndex: 1 }}>
            <div style={{display: 'block' }} onClick={() => handleOptionClick('수정')}>
              <Link to="/collectionWrite2">
                <button style={{width:'35px'}}>수정</button>
              </Link>
            </div>
            
            <div style={{ display: 'block', marginTop: '5px' }} onClick={() => handleOptionClick('삭제')}>
              <Link to="/collection">
                <button>삭제</button>
              </Link>
            </div>
          </div>
        )}
      </div>
      <p id="actionBar_name" className='drop-shadow-[0_2px_1px_rgba(220,166,19,100)] font-bold text-white p-5 ml-2 text-xl absolute '>
        {actionBarName}
      </p>
    </div>
  );
};
  
  ActionBarDot.propTypes = {
    actionBarName: PropTypes.string.isRequired,
  };
  
  export { ActionBarDot };