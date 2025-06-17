import { useState, useEffect, useContext } from "react";
import axios from "axios";
import styles from "./PrizeView.module.css";
import { Link } from "react-router-dom";
import { UserContext } from "../../context/UserContext";

export default function PrizeView() {
    const { user } = useContext(UserContext);
    const [showForm, setShowForm] = useState(false);
    const [error, setError] = useState("");
    const [prizes, setPrizes] = useState([]);
    const [editingPrize, setEditingPrize] = useState(null); // ✅ new state
    const [progressByPrizeId, setProgressByPrizeId] = useState({});

    const [newPrize, setNewPrize] = useState({
        prizeName: '',
        description: '',
        minutesRequired: '',
        prizesAvailable: '',
        startDate: '',
        endDate: '',
        userGroup: ''
    });

    useEffect(() => {
        if (!user?.familyId) return;

        axios.get(`/prizes/family/${user.familyId}`)
            .then(res => setPrizes(res.data))
            .catch(err => {
                console.error(err);
                setError("Failed to load family prizes.");
            });
    }, [user]);

    useEffect(() => {
        if (!user?.familyId) return;

        axios.get(`/prizes/family/${user.familyId}/grouped-progress`, { withCredentials: true })
            .then(res => {
                const progressMap = {};
                res.data.forEach(block => {
                    progressMap[block.prize.prizeId] = block.userProgressList;
                });
                setProgressByPrizeId(progressMap);
            })
            .catch(err => {
                console.error("Progress fetch failed", err);
                setError("Failed to load progress.");
            });
    }, [user]);

    const handleAddPrize = (e) => {
        e.preventDefault();

        const payload = {
            ...newPrize,
            familyId: user.familyId,
            minutesRequired: Number(newPrize.minutesRequired),
            prizesAvailable: Number(newPrize.prizesAvailable)
        };
        console.log("Adding prize:", payload);
        axios.post("/prizes", payload)
            .then(res => {
                setPrizes(prev => [...prev, res.data]);
                resetForm();
            })
            .catch(err => {
                console.error(err);
                setError("Failed to add prize.");
            });
    };

    const handleUpdatePrize = (e) => {
        e.preventDefault();

        axios.put(`/prizes/${editingPrize.prizeId}`, {
            ...newPrize,
            familyId: user.familyId
        })
            .then(res => {
                setPrizes(prev => prev.map(p => p.prizeId === editingPrize.prizeId ? res.data : p));
                resetForm();
            })
            .catch(err => {
                console.error(err);
                setError("Failed to update prize.");
            });
    };

    const handleDeletePrize = (prizeId) => {
        if (!window.confirm("Are you sure you want to delete this prize?")) return;

        axios.delete(`/prizes/${prizeId}`)
            .then(() => {
                setPrizes(prev => prev.filter(p => p.prizeId !== prizeId));
            })
            .catch(err => {
                console.error(err);
                setError("Failed to delete prize.");
            });
    };

    const handleEditClick = (prize) => {
        setEditingPrize(prize);
        setNewPrize({
            prizeName: prize.prizeName,
            description: prize.description,
            minutesRequired: prize.minutesRequired,
            prizesAvailable: prize.prizesAvailable,
            startDate: prize.startDate,
            endDate: prize.endDate,
            userGroup: prize.userGroup
        });
        setShowForm(true);
    };

    const resetForm = () => {
        setNewPrize({
            prizeName: '',
            description: '',
            minutesRequired: '',
            prizesAvailable: '',
            startDate: '',
            endDate: '',
            userGroup: ''
        });
        setEditingPrize(null);
        setShowForm(false);
    };
    
    return (
        <div className={styles.container}>
            <h2 className={styles.h2}>Prizes for Your Family</h2>

            {prizes.length > 0 ? (
                <ul className={styles.bookList}>
                    {prizes.map(prize => (
                        <li key={prize.prizeId} className={styles.bookItem}>
                            <div className={styles.bookRow}><strong>{prize.prizeName}</strong></div>
                            <div className={styles.bookRow}>{prize.description}</div>
                            <div className={styles.bookRow}>🎯 {prize.minutesRequired} min</div>
                            <div className={styles.bookRow}>🎁 Available: {prize.prizesAvailable}</div>
                            <div className={styles.bookRow}>📅 {prize.startDate} → {prize.endDate}</div>
                            <div className={styles.bookRow}>👥 Group: {prize.userGroup}</div>

                            {/* 📈 User Progress Section */}
                            <div className={styles.progressRow}>
                            {progressByPrizeId[prize.prizeId]?.length > 0 && (
                                <div className={styles.bookRow}>
                                    <strong>📈 Progress:</strong>
                                    <ul>
  {progressByPrizeId[prize.prizeId].map(user => {
    const capitalizedName = user.username.charAt(0).toUpperCase() + user.username.slice(1);
    const percentage = Math.round(user.progress * 100);
    let progressColor;

    if (percentage >= 80) progressColor = styles.progressGreen;
    else if (percentage >= 50) progressColor = styles.progressYellow;
    else progressColor = styles.progressRed;

    return (
      <li key={user.userId}>
        {capitalizedName}: {percentage}%
        <div className={styles.progressBarOuter}>
          <div
            className={`${styles.progressBarInner} ${progressColor}`}
            style={{ width: `${percentage}%` }}
          ></div>
        </div>
      </li>
    );
  })}
</ul>
                                </div>
                            )}
                            </div>
                            {/* ✅ Parent-only edit/delete buttons */}
                            {user?.role === "ROLE_PARENT" && (
                                <div className={styles.buttonRow}>
                                    <button
                                        className={styles.btnSecondary}
                                        onClick={() => handleEditClick(prize)}
                                    >
                                        Edit
                                    </button>
                                    <button
                                        className={styles.btnDanger}
                                        onClick={() => handleDeletePrize(prize.prizeId)}
                                    >
                                        Remove
                                    </button>
                                </div>
                            )}
                        </li>
                    ))}
                </ul>

            ) : <p>No prizes available for your family.</p>}

            {user?.role === "ROLE_PARENT" && (
                <button className={styles.btnPrimary} onClick={() => {
                    resetForm();
                    setShowForm(prev => !prev);
                }}>
                    {showForm ? "Cancel" : "Add a Prize"}
                </button>
            )} <br />
             <Link
        to="/prizes/awarded"
        className={styles.btnPrimary}
        style={{
            marginLeft: "1rem",
            padding: "0.5rem 1rem",
            display: "inline-block",
            width: "auto",
            textDecoration: "none",
            textAlign: "center",
          }}
    >
        🏆 View Awarded Prizes
    </Link>
            <br /><br /><br />

            {showForm && (
                <form
                    onSubmit={editingPrize ? handleUpdatePrize : handleAddPrize}
                    className={styles.addBookForm}
                >
                    {[
                        { label: "Prize Name", key: "prizeName" },
                        { label: "Description", key: "description" },
                        { label: "Minutes Required", key: "minutesRequired", type: "number" },
                        { label: "Prizes Available", key: "prizesAvailable", type: "number" },
                        { label: "Start Date", key: "startDate", type: "date" },
                        { label: "End Date", key: "endDate", type: "date" },
                        { label: "User Group", key: "userGroup" }
                    ].map(({ label, key, type = "text" }) => (
                        key === "userGroup" ? (
                            <div key={key} className={styles.formControl}>
                                <label className={styles.formControlLabel}>{label}:</label>
                                <select
                                    className={styles.formControlInput}
                                    value={newPrize[key]}
                                    onChange={(e) => setNewPrize({ ...newPrize, [key]: e.target.value })}
                                    required
                                >
                                    <option value="">-- Select Group --</option>
                                    <option value="INDIVIDUAL">Individual</option>
                                    <option value="FAMILY">Family</option>
                                </select>
                            </div>
                        ) : (
                            <div key={key} className={styles.formControl}>
                                <label className={styles.formControlLabel}>{label}:</label>
                                <input
                                    type={type}
                                    className={styles.formControlInput}
                                    value={newPrize[key]}
                                    onChange={(e) => setNewPrize({ ...newPrize, [key]: e.target.value })}
                                    required
                                />
                            </div>
                        )
                    ))}
                    <button type="submit" className={styles.btnPrimary}>
                        {editingPrize ? "Update Prize" : "Add Prize"}
                    </button>
                </form>
            )}
        </div>
    );
}