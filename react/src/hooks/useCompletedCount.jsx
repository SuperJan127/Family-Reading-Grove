import { useState, useEffect } from "react";
import axios from "axios";

export default function useCompletedCount(userId) {
  const [count, setCount] = useState(null);
  const [errorMessage, setError] = useState("");

  useEffect(() => {
    if (!userId) return;
    const token = localStorage.getItem("token");
    const headers = { Authorization: `Bearer ${token}` };

    axios
      .get(`/users/${userId}/books/completed-count`, { headers })
      .then((resp) => setCount(resp.data.count))
      .catch(() => setError("Could not load completed‐books count."));
  }, [userId]);

  return { count, errorMessage };
}
