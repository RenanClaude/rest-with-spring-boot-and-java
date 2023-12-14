import React from "react";
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import Login from './pages/Login';
import Books from './pages/Books';
import NewBook from './pages/NewBook';

export default function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" exact element={<Login/>}></Route>
        <Route path="/books" element={<Books/>}></Route>
        <Route path="/book/new/:bookId" element={<NewBook/>}></Route>
      </Routes>
    </BrowserRouter>
  )
}