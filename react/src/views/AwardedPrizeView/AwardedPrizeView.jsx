import { useEffect, useState, useContext } from "react";
import axios from "axios";
import { UserContext } from "../../context/UserContext";
import styles from "./AwardedPrizeView.module.css"; // optional styling

export default function AwardedPrizeView() {
    const { user } = useContext(UserContext);
    const [awards, setAwards] = useState([]);
    const [error, setError] = useState("");

    useEffect(() => {
        if (!user?.familyId) return;

        axios.get(`/prizes/family/${user.familyId}/awards`)
            .then(res => setAwards(res.data))
            .catch(err => {
                console.error(err);
                setError("Failed to load awarded prizes.");
            });
    }, [user]);

    return (
        <div className={styles.container}>
            <h2 className={styles.h2}>🏆 Awarded Prizes</h2>
            {error && <p className={styles.error}>{error}</p>}
            {awards.length > 0 ? (
                <ul className={styles.awardList}>
                {awards.map(a => (
                  <li key={a.awardId} className={styles.awardItem}>
                  <div className={styles.fireworkWrapper}>
                  <span className={styles.firework}>✨✨✨</span>
                    <div className={styles.awardContent}>
                      <strong>{a.prizeName}</strong>
                      <span>
                        {a.userId
                          ? `🏅 Awarded to ${a.username.charAt(0).toUpperCase() + a.username.slice(1)}`
                          : "👪 Awarded to the family"}
                      </span>
                      <span>📅 {new Date(a.awardedDate).toLocaleString()}</span>
                    </div>
                    <span className={styles.firework}>✨✨✨</span>
                  </div>
                </li>
                ))}
              </ul>
            ) : (
                <p>No prizes have been awarded yet.</p>
            )}
        </div>
    );
}
