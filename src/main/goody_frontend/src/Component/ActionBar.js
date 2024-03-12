import React from 'react';
import PropTypes from 'prop-types';
import AppBar from '@mui/material/AppBar';

const ActionBar = ({ actionBarName }) => {
  return (
    <AppBar component="nav" >
        <img src='img/ActionBar.png' className='absolute'></img>
        <p id="actionBar_name" className='drop-shadow-[0_2px_1px_rgba(220,166,19,100)] font-bold text-white p-5 ml-2 text-xl absolute '>{actionBarName}</p>
    </AppBar>
  );
};

ActionBar.propTypes = {
  actionBarName: PropTypes.string.isRequired,
};

export { ActionBar };