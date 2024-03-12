import React, { useState, useEffect } from 'react';
import { Nav } from '../Component/Nav';
import TabView from './TabView';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import './Home.css';
import AppBar from '@mui/material/AppBar';
import { Drawer } from 'antd';
import Categories from './Categories'; 

// 액션바
const HomeActionBar = ({ children, imageSrc }) => {

  const [visible, setVisible] = useState(false);
  useEffect(() => {
    const defaultOpenElement = document.getElementById('defaultOpen');
    if (defaultOpenElement) {
      defaultOpenElement.click();
    }
  }, []);
  const showDrawer = () => {
    setVisible(true);
  };
  const onClose = () => {
    setVisible(false);
  };

  return (
    <div className='flex'>
      <img className='relative' src={imageSrc} alt='구디' />
      <img className='absolute mt-7 left-7' src="img/SmallLogo.png" alt='구디' width={'130px'} />
      <Link to="/Search">
        <button className='absolute right-12 h-20 p-4  drop-shadow-[0_2px_1px_rgba(220,166,19,100)]'>
          <img src="img/Search.png" alt='검색' width={'25px'} height={'25px'} />
        </button></Link>
      <button onClick={showDrawer} className='absolute right-0 h-20 p-4  drop-shadow-[0_2px_1px_rgba(220,166,19,100)]'>
        <img src="img/Hamburger.png" alt='햄버거' width={'25px'} height={'25px'} />
      </button>

      <div>
        {children && (<div className=''>{children}</div>
        )}
      </div>
    
      <Drawer
        placement="right"
        closable={false}
        open={visible}
        onClose={onClose}
      >
        {/* Categories 컴포넌트를 렌더링 */}
        <Categories onClose={onClose} />
      </Drawer>
      
    </div>
  );
};

export { HomeActionBar };


// 탭뷰 테마 커스텀 테마를 생성
const theme = createTheme({
  palette: {
    secondary: {
      main: '#FFD52B', // 원하는 색상으로 변경
    },
  },
});


const Home = () => {
  const [thisIndex, setThisIndex] = useState(0);
  //const [pageNumber, setPageNumber] = useState(1); //페이지 번호 상태

  // 이미지를 자동으로 넘기는 함수
  const autoNextImage = () => {
    const newIndex = (thisIndex + 1) % 3; // 3은 이미지의 총 개수
    setThisIndex(newIndex);
  };

  // 3초마다 이미지를 자동으로 넘기기
  useEffect(() => {
    const timer = setInterval(autoNextImage, 5000); // 5초(5000ms)마다 호출
    return () => clearInterval(timer); // 컴포넌트 언마운트 시 타이머 해제
  }, [thisIndex]);

  const navigateTo = (data) => {
    const newIndex = thisIndex + data;

    // 범위를 제한하여 이동
    if (newIndex >= 0 && newIndex <= 2) {
      setThisIndex(newIndex);
    }
  };

  return (
    <div className="App w-full">
    <ThemeProvider theme={theme}> 
      {/* 액션바 */}
      <AppBar component="nav" className='fixed top-0 w-full' style={{ zIndex: '-1' }}>
      <HomeActionBar imageSrc="img\HomeActionBar.png">
        <div className='left-0 absolute w-full flex justify-center items-center mt-24'>
          <div className="all">
          <div id="con" style={{ transform: `translateX(-${thisIndex * (window.innerWidth <= 390 ? 315 : 335)}px)` }}>
              <img className="main_img" src="img/EventImg1.png" alt="event_img" />
              <Link to={`/NoticeDetails`} state={{ img : "../img/notice_grade.png"}}>
                <a><img className="main_img" src="img/EventImg2.png" alt="event_img2" /></a>
                </Link>
                <Link to={`/NoticeDetails`} state={{ img : "../img/notice_collection.png"}}>
              <a><img className="main_img" src="img/EventImg3.png" alt="event_img3"/></a>
              </Link>
            </div>
            <button
              id="prev"
              className={`w-20 ${thisIndex === 0 ? 'hidden' : ''}`}
              onClick={() => navigateTo(-1)}
            >
              <img src="img/gray_left.png" alt="Previous" width={'10px'} />
            </button>
            <button
              id="next"
              className={thisIndex === 2 ? 'hidden' : ''}
              onClick={() => navigateTo(1)}
            >
              <img src="img/gray_right.png" alt="Next" width={'10px'} />
            </button>
            <div className="indicator">
              <span className={thisIndex === 0 ? 'dot dot_active' : 'dot'}></span>
              <span className={thisIndex === 1 ? 'dot dot_active' : 'dot'}></span>
              <span className={thisIndex === 2 ? 'dot dot_active' : 'dot'}></span>
            </div>
          </div>
        </div>
      </HomeActionBar>
      </AppBar>

      
      {/* 메인 카테고리 */}
      <div className='rounded-3xl pt-5 mt-[210px] h-[700px]' style={{backgroundColor:'white',zIndex:'10',overflow:'hidden'}}> 
      <div className='flex justify-center'>
        <div className="mb-2">
          <Link to="/maincategories?category=MOV&name=영화"><button className='w-9 mx-2.5 my-5 font-bold text-gray-600 text-xs cate'> <img src='img\Movie.png' className='shadow-lg rounded-xl mb-2 w-9' alt='MOV'></img>영화</button></Link>
          <Link to="/maincategories?category=GAME&name=게임"><button className='w-9 mx-2.5 my-5 font-bold text-gray-600 text-xs cate'> <img src='img\Games.png' className='shadow-lg rounded-xl mb-2 w-9' alt='GAME'></img>게임</button></Link>
          <Link to="/maincategories?category=ENT&name=연예인"><button className='w-9 mx-2.5 my-5 font-bold text-gray-600 text-xs cate'> <img src='img\Mic.png' className='shadow-lg rounded-xl mb-2 w-9' alt='ENT'></img>연예인</button></Link>
          <Link to="/maincategories?category=CHA&name=캐릭터"><button className='w-9 mx-2.5 my-5 font-bold text-gray-600 text-xs cate'> <img src='img\Bear.png' className='shadow-lg rounded-xl mb-2 w-9' alt='CHA'></img>캐릭터</button></Link>
          <Link to="/maincategories?category=SPO&name=스포츠"> <button className='w-9 mx-2.5 my-5 font-bold text-gray-600 text-xs cate'> <img src='img\Ball.png' className='shadow-lg rounded-xl mb-2 w-9' alt='SPO'></img>스포츠</button></Link>
          <Link to="/maincategories?category=ANI&name=만화"><button className='w-9 mx-2.5 my-5 font-bold text-gray-600 text-xs cate'> <img src='img\Book.png' className='shadow-lg rounded-xl mb-2 w-9' alt='ANI'></img>만화</button></Link>
        </div>
      </div>
      <hr />

      {/* 최근 업로드 */}
      <h3 className='font-extrabold mt-7 ml-5'> 최근 업로드 </h3>

      {/* 탭 뷰 */}
      <TabView />
      <Nav />
      </div>
      
    </ThemeProvider>
    </div>
  );
};

HomeActionBar.propTypes = {
  imageSrc: PropTypes.string.isRequired,
  children: PropTypes.node,
};

export default Home;