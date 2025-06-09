import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

export default function AddBookView() {
    const [title, setTitle] = useState("");
    const [author, setAuthor] = useState("");
    const [isbn, setIsbn] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(""); // Reset error message
        setLoading(true); // Optionally, you can add a loading state
        try {
            await axios.post("/books", { title, author, isbn });
            navigate("/books");
        } catch (err) {
            setError(err.response?.data?.message || "Failed to add book. Please try again.");
        } finally {
            setLoading(false); // Reset loading state
        }
    };

    return (

        <div>
            <h2>Add a New Book</h2>

            {error && <p style={{ color: "red" }}>{error}</p>}

            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="title">Title</label><br/>
                    <input id="title" type="text" value={title} onChange={e => setTitle(e.target.value)}
                    required 
                    />
                </div>

                <div>
                    <label htmlFor="author">Author</label><br/>
                    <input id="author" type="text" value={author} onChange={e => setAuthor(e.target.value)}
                    required 
                    />
                </div>

                <div>
                    <label htmlFor="isbn">ISBN</label><br/>
                    <input id="isbn" type="text" value={isbn} onChange={e => setIsbn(e.target.value)}
                    required 
                    />
                </div>
                <button type="submit" disabled={loading}>Add Book</button>
                      {loading ? "Adding..." : "Add Book"}          
            </form>
        </div>
    );
}   
