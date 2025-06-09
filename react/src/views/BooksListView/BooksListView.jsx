import { useEffect, useState } from "react";
import axios from "axios";

export default function BooksListView() {
  const [books, setBooks]   = useState([]);
  const [error, setError]   = useState("");

  useEffect(() => {
    axios.get("/books")
      .then(resp => {
        setBooks(resp.data);
      })
      .catch(err => {
        console.error(err);
        setError("Failed to load books.");
      });
  }, []);

  return (
    <div>
      <h2>All Books</h2>
      {error && <p style={{ color: "red" }}>{error}</p>}
      {books.length === 0
        ? <p>No books found.</p>
        : (
          <table>
            <thead>
              <tr>
                <th>Title</th>
                <th>Author</th>
                <th>ISBN</th>
              </tr>
            </thead>
            <tbody>
              {books.map(book => (
                <tr key={book.bookId}>
                  <td>{book.title}</td>
                  <td>{book.author}</td>
                  <td>{book.isbn}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )
      }
    </div>
  );
}
