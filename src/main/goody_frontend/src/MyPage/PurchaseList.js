import React from 'react';
import { ActionBarClose } from '../Component/ActionBarClose';

const actionBarName = "구매 & 참여 목록";
const PurchaseList = () => {
  return (
    <>
    <ActionBarClose actionBarName={actionBarName} />
    </>
  );
}

export default PurchaseList;