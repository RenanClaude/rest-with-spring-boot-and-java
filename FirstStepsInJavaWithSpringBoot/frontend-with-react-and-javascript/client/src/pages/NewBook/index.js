import React, { useState } from 'react';
import { FiArrowLeft } from 'react-icons/fi';
import { Link, useNavigate } from 'react-router-dom';
import logoImage from '../../assets/logo.svg';
import api from '../../services/api';
import './styles.css';

export default function NewBook() {

  const [id, setId] = useState(null);
  const [title, setTitle] = useState('');
  const [author, SetAuthor] = useState('');
  const [launchDate, setLauchDate] = useState('');
  const [price, setPrice] = useState('');

  const username = localStorage.getItem('username');
  const accessToken = localStorage.getItem('accessToken');

  const navigate = useNavigate();

  async function createNewBook(e) {
    e.preventDefault();

    const data = {
      title,
      author,
      launchDate,
      price,
    }

    const headers = {
      headers: { Authorization: `Bearer ${accessToken}` }
    }

    try {
      await api.post('api/book/v1', data, headers);
      navigate('/books');

    } catch (error) {
      alert('Error while recording book! Try Again!')
    }
  }

  return (
    <div className="new-book-container">
      <div className="content">
        <section className="form">
          <img src={logoImage} alt="Erudito" />
          <h1>Add a new book</h1>
          <p>Enter the book information and click on 'Add'!</p>
          <Link className="back-link" to='/books'>
            <FiArrowLeft size={16} color="#251FC5" />
            Home
          </Link>
        </section>

        <form onSubmit={createNewBook}>

          <input
            placeholder="Title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />

          <input
            placeholder="Author"
            value={author}
            onChange={(e) => SetAuthor(e.target.value)}
          />

          <input
            type="date"
            value={launchDate}
            onChange={(e) => setLauchDate(e.target.value)}
          />

          <input
            placeholder="Price"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
          />

          <button className="button" type="submit">Add</button>
        </form>
      </div>
    </div>
  );
}