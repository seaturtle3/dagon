const express = require('express');
const mysql = require('mysql');
const app = express();

app.use(express.json());

// MySQL 연결 설정
const db = mysql.createConnection({
    host: 'localhost',
    user: 'admin',
    password: 'edurootroot',
    database: 'mysql'
});

// 아이디 중복 확인 API
app.post('/api/users/check-uid', (req, res) => {
    const { uid } = req.body;
    const query = 'SELECT COUNT(*) AS count FROM users WHERE uid = ?';
    db.query(query, [uid], (err, result) => {
        if (err) return res.status(500).json({ error: '서버 오류' });
        res.json({ exists: result[0].count > 0 });
    });
});

// 이메일 중복 확인 API
app.post('/api/users/check-email', (req, res) => {
    const { email } = req.body;
    const query = 'SELECT COUNT(*) AS count FROM users WHERE uemail = ?';
    db.query(query, [email], (err, result) => {
        if (err) return res.status(500).json({ error: '서버 오류' });
        res.json({ exists: result[0].count > 0 });
    });
});

// 서버 실행
app.listen(3000, () => {
    console.log('서버가 http://localhost:3000 에서 실행 중입니다.');
});