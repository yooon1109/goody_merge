import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { BottomNavigation, BottomNavigationAction, Paper } from '@mui/material';

const iconStyle = { width: 25 }; // Adjust the size of your icons

const Nav = () => {
  const location = useLocation();
  const [selected, setSelected] = useState(location.pathname);

  const handleNavigationChange = (path) => {
    setSelected(path);
  };

  return (
    <Paper elevation={3} style={{ position: 'fixed', bottom: 0, width: '100%' }}>
      <BottomNavigation>
        <BottomNavigationAction
          label="Home"
          icon={
            <img
              src={selected === '/home' ? '../img/home_yellow.png' : '../img/home_gray.png'}
              alt="Home"
              style={iconStyle}
            />
          }
          component={Link}
          to="/home"
          onClick={() => handleNavigationChange('/home')}
          selected={selected === '/home'}
        />
        <BottomNavigationAction
          label="Collection"
          icon={
            <img
              src={selected === '/collection' ? '../img/collection_yellow.png' : '../img/collection_gray.png'}
              alt="Collection"
              style={iconStyle}
            />
          }
          component={Link}
          to="/collection"
          onClick={() => handleNavigationChange('/collection')}
          selected={selected === '/collection'}
        />
        <BottomNavigationAction
          label="Add Write"
          icon={
            <img
              src={selected === '/addWrite' ? '../img/write_yellow.png' : '../img/write_gray.png'}
              alt="Add Write"
              style={iconStyle}
            />
          }
          component={Link}
          to="/addWrite"
          onClick={() => handleNavigationChange('/addWrite')}
          selected={selected === '/addWrite'}
        />
        <BottomNavigationAction
          label="Chatting"
          icon={
            <img
              src={selected === '/chatting' ? '../img/chat_yellow.png' : '../img/chat_gray.png'}
              alt="Chatting"
              style={iconStyle}
            />
          }
          component={Link}
          to="/chatting"
          onClick={() => handleNavigationChange('/chatting')}
          selected={selected === '/chatting'}
        />
        <BottomNavigationAction
          label="My Page"
          icon={
            <img
              src={selected === '/mypage' ? '../img/my_yellow.png' : '../img/my_gray.png'}
              alt="My Page"
              style={iconStyle}
            />
          }
          component={Link}
          to="/mypage"
          onClick={() => handleNavigationChange('/mypage')}
          selected={selected === '/mypage'}
        />
      </BottomNavigation>
    </Paper>
  );
};

export { Nav };
