import React, {useState} from 'react';
import { useNavigate } from "react-router-dom"
import { Nav } from '../Component/Nav';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

const CollectionItem = ({ item }) => {
  // 컬렉션 아이템 정보를 받아와서 렌더링
  return (
    <div className='inline-flex'>
      <Link to={`/collectionDetail/${item.documentId}`}>
        <button>
          <div>
            <img
              src={item.thumbnailPath} // 이미지 URL 사용
              className='drop-shadow-[0_2px_1px_rgba(220,166,19,100)] Collecthin_image col_item'
            />
          </div>
        </button>
      </Link>
    </div>
  );
};

const collectionDetail = () => {
    const navigate = useNavigate();
    const [text1, setText1] = useState(''); //입력한 값
    const [text2, setText2] = useState(''); //입력한 값
    const [text3, setText3] = useState(''); //입력한 값
    const [collectionItems, setCollectionItems] = useState(null);

    let apiUrl = `/goody/collection/search`;


    if (text1) {
      apiUrl += `?hashTag1=${text1}`;
    } else if (text2) {
      apiUrl += `?hashTag1=${text2}`;
    } else if (text3) {
      apiUrl += `?hashTag1=${text3}`;
    }

    if ( text1 && text2) {
      apiUrl += `&hashTag2=${text2}`;
    }

    if (text1 && text2 && text3) {
      apiUrl += `&hashTag3=${text3}`;
    }
    
  
    const token = localStorage.getItem('token');

  {/* 뒤로가기 함수*/}
    const handleBack = () => {
      navigate(-1);
    }


  {/* 검색 및 api 호출 */}



    const handleTextChange1 = (event) => {
      setText1(event.target.value);
    };
    const handleTextChange2 = (event) => {
      setText2(event.target.value);
    };
    const handleTextChange3 = (event) => {
      setText3(event.target.value);
    };

    const handleSearch = () => {
   
      // 요청 헤더 설정
    const headers = {
        Authorization: `${token}`,
    };
    
    
    if (!text1 && !text2 && !text3) {
      window.alert('해시태그를 입력해주세요!');
    }
    
     
    else{
      // GET 요청을 수행
      fetch(apiUrl, {
        method: 'GET',
        headers: headers,
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          return response.json();
        })
        .then((responseData) => {
          // 데이터를 추출하고 상태 변수에 저장
          setCollectionItems(responseData.collections);
     
        })
        .catch((error) => {
          console.error('API 요청 중 오류 발생:', error);
       
        });
      }
    };
  
    return(
    <>
    <div className="w-full h-full rounded-b-2xl  bg-[#FFD52B] top-0 items-center flex justify-center">
      

      <div>
        <div className="flex items-center justify-center">
       
           <p className='text-2xl text-white font-serif p-0.5'>Collection Search</p>

           <button onClick={handleBack}>
            <img src='../img/blackClose.png' className='absolute w-[1.1rem] h-[1.1rem] right-3' alt="뒤로가기"/>
          </button>
      </div>



        <div className='bg-yellow justify-center items-center w-full h-36' style={{ alignItems: 'center' }}>
        
        <form >
          <div className='h-[2rem] w-[20rem] m-3 border-rounded-[0.5rem] flex items-center'>
            
            <input
              type="text"
              value={text1}
              maxLength={100}
              onChange={handleTextChange1}
              placeholder = '# 해시태그를 입력해주세요'
              className='transparent outline-none border-0 rounded-2xl focus:outline-none w-[22rem] '
              style={{ border: 'none', padding: '0 8px'}}
            />
          </div>
        </form>

        <form >
          <div className='h-[2rem] w-[20rem] m-3  border-rounded-[0.5rem] flex items-center'>
           
            <input
              type="text"
              value={text2}
              maxLength={100}
              onChange={handleTextChange2}
              placeholder = '# 해시태그를 입력해주세요'
              className='transparent outline-none border-0  rounded-2xl focus:outline-none w-[22rem] '
              style={{ border: 'none', padding: '0 8px'}}
            />
          </div>
        </form>
        
        <form >
          <div className='h-[2rem] w-[20rem] m-3  border-rounded-[0.5rem] flex items-center'>
           
            <input
              type="text"
              value={text3}
              maxLength={100}
              onChange={handleTextChange3}
              placeholder = '# 해시태그를 입력해주세요'
              className='transparent outline-none border-0 rounded-2xl focus:outline-none w-[22rem] '
              style={{ border: 'none', padding: '0 8px'}}
            />
          </div>
        </form>


    </div>
    
    <div className='flex justify-center items-center pb-3'>
    <button onClick={handleSearch}>
    <img src="../img/Search2.png" alt='검색' width={'30px'} height={'30px'} />
    </button>
    </div>
    </div>
    

    
   
    </div>
   
    
    <div className='flex flex-col items-center p-3'>
    <div className='grid grid-cols-3 gap-1'> {/* 그리드 컨테이너, 3개의 열과 간격 설정 */}
          
          {Array.isArray(collectionItems) &&
            collectionItems.map((item, index) => (
              <CollectionItem key={index} item={item} />
            ))
          }
        </div>
    </div>

    
    <Nav/>
    </>
    );
}

// CollectionItem 컴포넌트 내에서 아래와 같이 PropTypes 설정
CollectionItem.propTypes = {
  item: PropTypes.shape({
    documentId: PropTypes.string.isRequired,
    thumbnailPath: PropTypes.string.isRequired,
  }).isRequired,
};


export default collectionDetail;