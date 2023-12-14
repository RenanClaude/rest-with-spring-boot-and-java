import React, { useEffect, useState } from 'react';
import { FiArrowLeft } from 'react-icons/fi';
import { Link, useNavigate, useParams } from 'react-router-dom';
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
  const { bookId } = useParams();

  const headers = {
    headers: { Authorization: `Bearer ${accessToken}` }
  }

  async function loadBookData() {
    try {
      const response = await api.get(`api/book/v1/${bookId}`, headers);

      const adjustedDate = response.data.launchDate.split('T', 10)[0];

      setId(response.data.id);
      setTitle(response.data.title);
      SetAuthor(response.data.author);
      setLauchDate(adjustedDate);
      setPrice(response.data.price);

    } catch (error) {
      navigate('/books')
      alert('Error recovering book! Try again!');
    }
  }

  useEffect(() => {
    if (bookId === '0') return;
    else {
      loadBookData();
    }
  }, [bookId]);

  async function createOrUpdateBook(e) {
    e.preventDefault();

    const data = {
      title,
      author,
      launchDate,
      price,
    }

    try {
      if (bookId === '0') {
        await api.post('api/book/v1', data, headers);
        navigate('/books');
      } else {
        data.id = bookId;
        await api.put(`api/book/v1`, data, headers);
        navigate('/books');
      }


    } catch (error) {
      alert('Error while recording book! Try Again!')
    }
  }

  return (
    <div className="new-book-container">
      <div className="content">
        <section className="form">
          <img src={logoImage} alt="Erudito" />
          <h1>{bookId === '0' ? "Add a new" : "Update the"} book</h1>
          <p>Enter the book information and click on {bookId === '0' ? "'Add'" : "'Update'"}!</p>
          <Link className="back-link" to='/books'>
            <FiArrowLeft size={16} color="#251FC5" />
            Back to books
          </Link>
        </section>

        <form onSubmit={createOrUpdateBook}>

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

          <button className="button" type="submit">{bookId === '0' ? "Add" : "Update"}</button>
        </form>
      </div>
    </div>
  );
}