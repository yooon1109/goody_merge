import React, { useState, useEffect } from 'react';
import {  useNavigate } from 'react-router-dom';
import { ActionBarClose } from '../Component/ActionBarClose';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import  { CollectionTag }  from '../Collection/CollectionTag';
import { message } from 'antd';

const actionBarName = "컬렉션 작성";
  



const CollectionWrite = () => {
    const [title, setTitle] = useState('');
    const [story, setStory] = useState('');
    const [images, setImages] = useState([]);
    const [loggedIn, setLoggedIn] = useState(false);
    const [collectionTags, setCollectionTags] = useState([]); // 필드 데이터를 관리
    const navigate = useNavigate(); 


    const [messageApi, contextHolder] = message.useMessage();

const warning = (content) => {
  messageApi.open({
    type: 'warning',
    content: content,
    duration: 2,
    style: {
      marginTop: '72vh',
    },
  });
};

    useEffect(() => {
        // 사용자 로그인 상태를 확인하는 로직을 구현
        const token = localStorage.getItem('token'); // 또는 세션에서 토큰을 가져올 수 있음
        if (token) {
            setLoggedIn(true); // 토큰이 있다면 로그인 상태로 설정
        }
    }, []);

    const handleDeleteImage = (index) => {
        // 이미지 목록에서 이미지를 삭제합니다.
        const updatedImages = [...images];
        updatedImages.splice(index, 1);
        setImages(updatedImages);
      };

    const handleTitleChange = (newTitle) => {
        setTitle(newTitle);
    };

    const handleStoryChange = (newStory) => {
        setStory(newStory);
    };

    const handleFileChange = (event) => {
        const files = event.target.files;
        const newImages = Array.from(files);
        setImages([...images, ...newImages]);
    };
    const handleTagsChange = (newTags) => {
        setCollectionTags(newTags); // 필드 데이터 업데이트
      };

    const handleMiniRegisterClick = async () => {
        try {
            // 사용자 인증 여부 확인
            if (!loggedIn) {
                // 로그인되지 않은 경우 처리
                alert('로그인 후에 글을 작성할 수 있습니다.');
                return;
            }
    
            // 필수 필드 값 체크
            if (!title) {
                warning('제목을 입력해주세요');
                return;
            }
    
            if (!story) {
                warning('내용을 입력해주세요');
                return;
            }
    
            if (collectionTags.length === 0) {
                warning('해시태그를 입력해주세요');
                return;
            }
    
            if (images.length === 0) {
                warning('사진을 등록해주세요');
                return;
            }

            // 나머지 글 작성 로직
            const formData = new FormData();
        
        
           
            formData.append('title', title); // 제목을 추가
            
            formData.append('explain', story); // 내용을 추가
            formData.append('hashTags', collectionTags);
            
            if (images && images.length > 0) {
                images.forEach((image, index) => {
                    formData.append(`filePath[${index}]`, image);
                });
            }

            console.log([...formData.entries()]);

            const response = await fetch('/goody/collection/create', {
                method: 'POST',
                body: formData, // 멀티파트(form-data) 형식으로 데이터를 보냅니다.
                headers: {
                    // 토큰을 사용하여 사용자 인증
                    Authorization: `${localStorage.getItem('token')}`,
                },
            });

            if (response.ok) {
                // 데이터가 성공적으로 API로 전송되었습니다.
                console.log('데이터가 성공적으로 전송되었습니다.');
                navigate('/collection');
            } else {
                
            
                console.error('오류가 발생했습니다:');
            }
                       
            
        } catch (error) {
            console.error('오류가 발생했습니다:', error);
            
                
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
            <CollectionTag onTagsChange={handleTagsChange} /> 
            </div>


            </div>
            <div>
                <img src='img/PhotoText.png' className='w-24 ml-5 mt-2'></img>
        <div className='relative inline-block'>
          {images.map((image, index) => (
            <div key={index} style={{ position: 'relative', display: 'inline-block'}}>
              <img src={URL.createObjectURL(image)} className='m-4 rounded-xl object-cover w-[100px] h-[100px]' alt="SelectedImage" />
              <button
              onClick={() => handleDeleteImage(index)} style={{ position: 'absolute',top: '0',right: '0',padding: '20px', zIndex: '1',cursor: 'pointer',}}>
                <HighlightOffIcon />
              </button>
            </div>
          ))}
        </div>
      </div>
            <div style={{ position: 'fixed', bottom: 30, left: 0, right: 0, display: 'flex', justifyContent: 'center', background: 'white' }}>
                <button>
                    <input type="file" multiple accept="image/*" className="hidden" onChange={handleFileChange} />
                    <label htmlFor="fileInput"> <img src='img\CollectionCamera.PNG' className=' w-15 h-14 mx-5' id="selectedImage" alt="Selected"></img></label>            </button>

                <button>
                    <input type="file" id="fileInput" className="hidden" onChange={handleFileChange} />
                    <label htmlFor="fileInput"> <img src='img\Gallery.PNG' className=' w-15 h-14 mr-5'></img></label>
                </button>

                <button onClick={handleMiniRegisterClick}>
                    
                        <img src='img\AddBtn.png' className='w-48 h-11' alt="Mini Register"></img>
                   
                </button>

          
                <div>
        {contextHolder}
            </div>
            </div>
            <div className='pb-20'/>
           
        </div>
        
        
    );
}

export default CollectionWrite;
