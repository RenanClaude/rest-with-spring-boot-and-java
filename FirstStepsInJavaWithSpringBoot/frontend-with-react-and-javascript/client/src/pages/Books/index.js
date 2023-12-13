import React, { useEffect, useState } from "react";
import { FiEdit, FiPower, FiTrash2 } from 'react-icons/fi';
import { Link, useNavigate } from 'react-router-dom';
import logoImage from '../../assets/logo.svg';
import api from '../../services/api';
import './styles.css';

export default function Books() {

  const username = localStorage.getItem('username');
  const accessToken = localStorage.getItem('accessToken');

  const [books, setBooks] = useState([])

  const navigate = useNavigate();

  const headers = {
    headers: { Authorization: `Bearer ${accessToken}` }
  }

  useEffect(() => {
    api.get('api/book/v1', headers).then(response => { setBooks(response.data._embedded.bookVOList) })
  }, []);


  return (
    <div className="book-container">
      <header>
        <img src={logoImage} alt='Erudito Logo' />
        <span>Welcome, <strong>{username.toUpperCase()}</strong>!</span>
        <Link className="button" to='/book/new'>Add a new book</Link>
        <button type="button">
          <FiPower size={18} color="#251FC5" />
        </button>
      </header>

      <h1>Registered books</h1>

      <ul>
        {books.map(book => (
          <li>
            <strong>Title:</strong>
            <p>{book.title}</p>

            <strong>Author:</strong>
            <p>{book.author}</p>

            <strong>Price:</strong>
            <p>{Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(book.price)}</p>

            <strong>Release date:</strong>
            <p>{Intl.DateTimeFormat('pt-BR').format(new Date(book.launchDate))}</p>

            <button type="button">
              <FiEdit size={20} color="#251FC5" />
            </button>

            <button type="button">
              <FiTrash2 size={20} color="#251FC5" />
            </button>
          </li>
        ))}
      </ul>

    </div>
  );
}