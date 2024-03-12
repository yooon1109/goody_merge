// App.js 파일
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Chatdetails  from '../Chatting/chatdetails';
import Address  from '../Chatting/address';
import Review from '../Review/review';
import Honeyhome from '../Review/ReviewHoneyHome';
import Reviewperfect from '../Review/reviewperfect';
import Collection from '../Collection/collection';
import AddWrite from '../Write/AddWrite';
import Mypage from '../MyPage/mypage';
import NoticeList from '../MyPage/NoticeList';
import NoticeDetails from '../MyPage/NoticeDetails';
import ReviewList from '../MyPage/ReviewList';
import FavoriteList from '../MyPage/FavoriteList';
import PurchaseList from '../MyPage/PurchaseList';
import MyContentsList from '../MyPage/MyContentsList';
import CollectionList from '../MyPage/CollectionList';
import Categories from './Categories';
import CollectionDetail from '../Collection/collectionDetail';
import SearchDetail from '../Search/SearchDetail';
import CollectionWrtie from '../Collection/collectionWrite';
import CollectionSearch from '../Collection/collectionSearch';
import CollectionModify from '../Collection/collectionModify';
import Search from '../Search/Search';
import WriteDetail from '../Write/WriteDetail';
import Home from './Home';
import Login from '../Login/Login';
import Join from '../Login/Join';
import Chatting from '../Chatting/chatting';
import TabView from './TabView';
import MainCategories from './MainCategories';

const App = () => {
    return (
      <Router>
        <Routes>
        <Route path="/chatting" element={<Chatting />} />
          <Route path="/address/:roomId" element={<Address />} />
          <Route path="/chatdetails/:roomId" element={<Chatdetails />} />
          <Route path="/review" element={<Review />} />
          <Route path="/honeyhome" element={<Honeyhome />} />
          <Route path="/reviewperfect" element={<Reviewperfect />} />
          <Route path="/addWrite" element={<AddWrite />} />
          <Route path="/mypage" element={<Mypage />} />
          <Route path="/noticelist" element={<NoticeList />} />
          <Route path="/noticeDetails" element={<NoticeDetails />} />
          <Route path="/reviewlist" element={<ReviewList />} />
          <Route path="/favoritelist" element={<FavoriteList />} />
          <Route path="/purchaselist" element={<PurchaseList />} />
          <Route path="/myContentsList" element={<MyContentsList />} />
          <Route path="/collectionlist" element={<CollectionList />} />
          <Route path="/categories" element={<Categories />} />
          <Route path="/collection" element={<Collection />} />
          <Route path="/collectionDetail/:collectionId" element={<CollectionDetail />} />
          <Route  path="/collectionWrite" element={<CollectionWrtie/>} />
          <Route path="/collectionmodify" element={<CollectionModify />} />
          <Route exact path="/Search" element={<Search />} />
          <Route exact path="/WriteDetail/:documentId"  element={<WriteDetail />} />
          <Route path="/SearchDetail/" element={<SearchDetail />} />
          <Route path="/SearchDetail/:Searchtext" element={<SearchDetail />} />
          <Route path="/collectionSearch" element={<CollectionSearch />} />
          <Route path="/home" element={<Home />} />
          <Route path="/join" element={<Join />} />
          <Route path="/tabview" element={<TabView />} />
          <Route path="/maincategories" element={<MainCategories />} />
          <Route path="/" element={<Login />} />

        </Routes>
      </Router>
    );
  };
  
  export default App;