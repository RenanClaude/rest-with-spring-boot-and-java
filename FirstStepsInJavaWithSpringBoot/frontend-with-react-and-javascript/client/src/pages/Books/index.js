import React, { useEffect, useState } from "react";
import { FiEdit, FiPower, FiTrash2 } from 'react-icons/fi';
import { Link, useNavigate } from 'react-router-dom';
import logoImage from '../../assets/logo.svg';
import api from '../../services/api';
import './styles.css';

export default function Books() {

  const username = localStorage.getItem('username');
  const accessToken = localStorage.getItem('accessToken');

  const [books, setBooks] = useState([]);
  const [page, setPage] = useState(0);

  const navigate = useNavigate();

  const headers = {
    headers: { Authorization: `Bearer ${accessToken}` },
    params: { page: 0, size: 4, direction: 'asc' }
  }

  async function logout() {
    localStorage.clear();
    navigate('/');
  }

  async function editBook(id) {
    try {
      navigate(`/book/new/${id}`)
    } catch (error) {
      alert('Edit book failed! Try again!')
    }
  }

  async function deleteBook(id) {
    try {
      await api.delete(`api/book/v1/${id}`, headers);

      setBooks(books.filter((book) => book.id !== id))

    } catch (error) {
      alert('Delete failed! Try again!')
    }
  }

  async function fetchMoreBooks() {
    const response = await api.get('api/book/v1',
      {
        headers: { Authorization: `Bearer ${accessToken}` },
        params: { page: page, size: 4, direction: 'asc' }
      })

    console.log(response)

    setBooks([...books, ...response.data._embedded.bookVOList]);

    const buttonLoadMore = document.getElementById("buttonLoadMore");
    page < response.data.page.totalPages - 1 ? setPage(page + 1) : buttonLoadMore.style.display = 'none';

  }

  useEffect(() => {
    fetchMoreBooks();
  }, []);


  return (
    <div className="book-container">
      <header>
        <img src={logoImage} alt='Erudito Logo' />
        <span>Welcome, <strong>{username.toUpperCase()}</strong>!</span>
        <Link className="button" to='/book/new/0'>Add a new book</Link>
        <button type="button" onClick={() => logout()}>
          <FiPower size={18} color="#251FC5" />
        </button>
      </header>

      <h1>Registered books</h1>

      <ul>
        {books.map(book => (
          <li key={book.id}>
            <strong>Title:</strong>
            <p>{book.title}</p>

            <strong>Author:</strong>
            <p>{book.author}</p>

            <strong>Price:</strong>
            <p>{Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(book.price)}</p>

            <strong>Release date:</strong>
            <p>{Intl.DateTimeFormat('pt-BR', {timeZone: 'UTC'}).format(new Date(book.launchDate))}</p>


            <button type="button" onClick={() => editBook(book.id)}>
              <FiEdit size={20} color="#251FC5" />
            </button>

            <button type="button" onClick={() => deleteBook(book.id)}>
              <FiTrash2 size={20} color="#251FC5" />
            </button>
          </li>
        ))}
      </ul>

      <button
        className="button"
        id="buttonLoadMore"
        onClick={() => fetchMoreBooks()}
        type="button"
      >
        Load more books
      </button>
    </div>
  );
}