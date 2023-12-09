import React from "react";
import './styles.css';
import logoImage from '../../assets/logo.svg';
import {Link} from 'react-router-dom';
import {FiArrowLeft} from 'react-icons/fi';

export default function NewBook() {
  return(
    <div className="new-book-container">
      <div className="content">
        <section className="form">
          <img src={logoImage} alt="Erudito" />
          <h1>Add a new book</h1>
          <p>Enter the book information and click on 'Add'!</p>
          <Link className="back-link" to='/books'>
            <FiArrowLeft size={16} color="#251FC5"/>
            Home
          </Link>
        </section>

        <form>
          <input placeholder="Title"></input>
          <input placeholder="Author"></input>
          <input type="date"></input>
          <input placeholder="Price"></input>
          <button className="button" type="submit">Add</button>
        </form>
      </div>
    </div>
  );
}