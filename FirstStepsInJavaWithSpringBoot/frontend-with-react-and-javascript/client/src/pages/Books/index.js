import React from "react";
import './styles.css';
import logoImage from '../../assets/logo.svg'
import {Link} from 'react-router-dom';
import {FiPower, FiEdit, FiTrash2} from 'react-icons/fi';

export default function Books() {
  return(
    <div className="book-container">
      <header>
        <img src={logoImage} alt='Erudito Logo'/>
        <span>Welcome, <strong>Leandro</strong>!</span>
        <Link className="button" to='/book/new'>Add a new book</Link>
        <button type="button">
          <FiPower size={18} color="#251FC5"/>
        </button>
      </header>

      <h1>Registered books</h1>

      <ul>
        <li>
          <strong>Title:</strong>
          <p>Docker deep dive</p>

          <strong>Author:</strong>
          <p>Nigel Poulton</p>

          <strong>Price:</strong>
          <p>R$47,00</p>

          <strong>Release date:</strong>
          <p>01/01/2023</p>

          <button type="button">
            <FiEdit size={20} color="#251FC5"/>
          </button>

          <button type="button">
            <FiTrash2 size={20} color="#251FC5"/>
          </button>
        </li>

        <li>
          <strong>Title:</strong>
          <p>Docker deep dive</p>

          <strong>Author:</strong>
          <p>Nigel Poulton</p>

          <strong>Price:</strong>
          <p>R$47,00</p>

          <strong>Release date:</strong>
          <p>01/01/2023</p>

          <button type="button">
            <FiEdit size={20} color="#251FC5"/>
          </button>

          <button type="button">
            <FiTrash2 size={20} color="#251FC5"/>
          </button>
        </li>
      </ul>

    </div>
  );
}