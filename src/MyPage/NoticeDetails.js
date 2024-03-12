import React from "react";
import { useNavigate } from "react-router-dom";
import { useLocation } from "react-router-dom";

const NoticeDetails =( ) => {
    const navigate = useNavigate();
    const location = useLocation();

    const img = location.state?.img;
      const handleBack = () => {
        navigate(-1); 
    };
    
    console.log();
    return(
        <>
        <div>
                <button className='drop-shadow-[0_2px_1px_rgba(220,166,19,100)] absolute top-3 right-1' onClick={handleBack}>
                    <img src="img/close.png" alt='닫기' width={'30px'} height={'30px'} />
                </button>

                <img src={img} alt="이미지" className="w-full h-full"/>

        </div>
        
        </>

    );
}

export default NoticeDetails;