import PropTypes from 'prop-types';
import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const ActionBarClose = ({ actionBarName }) => {


    useEffect(() => {
        const defaultOpenElement = document.getElementById('defaultOpen');
        if (defaultOpenElement) {
            defaultOpenElement.click();
        }
    }, []);

    const navigate = useNavigate();
    const handleBack = () => {
        navigate(-1); // 이전 페이지로 이동하는 함수
    };


    return (
        <div className="w-full h-16 relative">
            <img src='img/ActionBar.png' className='absolute'></img>
            <div className='flex justify-between items-center h-full'>
                <p id="actionBar_name" className='drop-shadow-[0_2px_1px_rgba(220,166,19,100)] font-bold text-white p-6 ml-2 text-xl absolute '>{actionBarName}</p>
            </div>
            <div>
                <button className='drop-shadow-[0_2px_1px_rgba(220,166,19,100)] absolute top-0 right-4 h-full' onClick={handleBack}>
                    <img src="img/close.png" alt='닫기' width={'30px'} height={'30px'} />
                </button>
            </div>
        </div>
    );
};

ActionBarClose.propTypes = {
    actionBarName: PropTypes.string.isRequired,
};

export { ActionBarClose };
