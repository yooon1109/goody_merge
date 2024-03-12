import React from "react";
import { ActionBarClose } from '../Component/ActionBarClose';
import { Link } from 'react-router-dom';

const NoticeList = () => {
    const actionBarName = "공지사항";


    return(
        <>
        <ActionBarClose actionBarName={actionBarName} />
        <div className="flex flex-col items-center justify-center mt-5 w-full h-full">
        

            <Link to={`/NoticeDetails`} state={{ img : "../img/notice_grade.png"}}>
            <div className="w-96 h-72 border  m-10 ">

                
                <div className="w-full h-3/4 border">
                    <img src="../img/notice_grade_thum.png" alt="구디 등급제도" className="p-1 w-full h-full"/>
                </div>
                
                <div className="w-full h-1/4 border flex flex-col items-center justify-center text-center">
                    <p className="text-lg">  구디 등급제도 </p>
                </div>
            </div>
            </Link>


            <Link to={`/NoticeDetails`} state={{ img : "../img/notice_collection.png"}}>
            <div className="w-96 h-72 border  m-10 ">

                
                    <div className="w-full h-3/4 border">
                        <img src="../img/notice_collection_thum.png" alt="구디 등급제도" className="p-1 w-full h-full"/>
                    </div>

                    <div className="w-full h-1/4 border flex flex-col items-center justify-center text-center">
                        <p className="text-lg">  구디 컬렉션 안내</p>
                    </div>
            </div>
            </Link>
            
        
        </div>
        
        </>

    );
}

export default NoticeList;