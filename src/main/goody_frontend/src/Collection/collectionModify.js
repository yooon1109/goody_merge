import React, { useState, useEffect } from 'react';
import { Link} from 'react-router-dom';
import { ActionBarClose } from '../Component/ActionBarClose';
import { CollectionTag } from '../Collection/CollectionTag';
import { useLocation } from 'react-router-dom';
import PropTypes from 'prop-types';

const actionBarName = "컬렉션 수정";

const CollectionModify = () => {
  const location = useLocation();
  const { datatitle, dataexplain, datahashtag, datadocumentid } = location.state || {};

  const [title, setTitle] = useState(datatitle || '');
  const [story, setStory] = useState(dataexplain || '');
  const [loggedIn, setLoggedIn] = useState(false);
  const [collectionTags, setCollectionTags] = useState(datahashtag || []); // 필드 데이터를 관리
 
  useEffect(() => {
    // 사용자 로그인 상태를 확인하는 로직을 구현
    const token = localStorage.getItem('token'); // 또는 세션에서 토큰을 가져올 수 있음
    if (token) {
      setLoggedIn(true); // 토큰이 있다면 로그인 상태로 설정
    }
  }, []);



  const handleTitleChange = (newTitle) => {
    setTitle(newTitle);
  };

  const handleStoryChange = (newStory) => {
    setStory(newStory);
  };


  const handleTagsChange = (newTags) => {
    setCollectionTags(newTags); // 필드 데이터 업데이트
  };

      
      const updateCollection = async () => {
        
        const url = `/goody/collection/update?documentId=${datadocumentid}`;
      
        try {

            if (!loggedIn) {
                // 로그인되지 않은 경우 처리
                alert('로그인 후에 글을 작성할 수 있습니다.');
                return;
              }
          const response = await fetch(url, {
            method: 'PATCH',
            headers: {
                
                Authorization: `${localStorage.getItem('token')}`,
                'Content-Type': 'application/json'
              },
            body: JSON.stringify({
              title: title,
              explain:  story,
              hashTags: collectionTags
            }),
          });
      
          if (response.ok) {
            console.log('컬렉션 업데이트 성공!');
          } else {
            console.error('컬렉션 업데이트 실패:', response.statusText);
          }
        } catch (error) {
          console.error('오류 발생:', error);
        }
      };
      
   
    return (
        <div>
            {!loggedIn && <link to="/login" />} {/* 로그인되지 않은 경우 로그인 페이지로 리디렉션 */}
            <ActionBarClose actionBarName={actionBarName} />
            <div>
                <p className="text-3xl text-[#FFD52B] font-serif flex justify-center mt-10 font-bold">Title</p>
                <div className='flex-col px-4 '>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => handleTitleChange(e.target.value)}
                        placeholder='제목'
                        maxLength={30}
                        className='shadow-[0_0_4px_0_rgba(174,174,174,0.7)] rounded-lg w-[380px] h-12 pl-4'
                    />
                </div>
            </div>
            <div>
                <p className="text-3xl text-[#FFD52B] font-serif flex justify-center mt-5 font-bold">STORY</p>
                <div className='flex-col px-4 '>
                    <textarea
                        type="text"
                        value={story}
                        onChange={(e) => handleStoryChange(e.target.value)}
                        placeholder=' 내용'
                        maxLength={400}
                        rows={story.split('\n').length}
                        className='py-2 pl-2 shadow-[0_0_4px_0_rgba(174,174,174,0.7)] rounded-lg w-[380px] h-[300px]'
                        style={{ resize: 'none' }}
                    />
                </div>

            
            <div className=''>
            <p className="text-3xl text-[#FFD52B] font-serif flex justify-center my-5 font-bold">HASHTAGE</p>
            <CollectionTag onTagsChange={handleTagsChange} defaultTags={datahashtag} />

            </div>


            </div>
           
            <div style={{ position: 'fixed', bottom: 30, left: 0, right: 0, display: 'flex', justifyContent: 'center', background: 'white' }}>

                <button onClick={updateCollection}>
                    <Link to="/collection">
                    <div>
                        <button className='w-48 h-11 border rounded-2xl text-bold' style={{ backgroundColor: '#FFD52B' }}>수정</button>
                    </div>
                    </Link>
                </button>
            </div>
            <div className='pb-20'/>
        </div>
    );
}


CollectionModify.propTypes = {
    route: PropTypes.shape({
      params: PropTypes.shape({
        state: PropTypes.shape({
          datatitle: PropTypes.string,
          dataexplain: PropTypes.string,
          datahashtag: PropTypes.arrayOf(PropTypes.string),
        }),
      }),
    }),
  };
export default CollectionModify;