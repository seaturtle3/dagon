document.addEventListener('DOMContentLoaded', () => {
    fetch('/api/admin/stats', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('authToken') // 필요시
        }
    })
        .then(response => response.json())
        .then(data => {
            document.getElementById('today-user-count').innerText = data.todayUserCount || 0;
            document.getElementById('recent-login-user-count').innerText = data.recentLoginUserCount || 0;
            document.getElementById('inactive-user-count').innerText = data.inactiveUserCount || 0;
            document.getElementById('reported-user-count').innerText = data.reportedUserCount || 0;
        })
        .catch(error => {
            console.error('대시보드 통계 불러오기 실패:', error);
        });
});
document.addEventListener("DOMContentLoaded", () => {
    // API 기본 경로
    const baseUrl = "/api/admin";

    // 총 예약 수 가져와서 표시
    fetch(`${baseUrl}/reservation/total`)
        .then(res => res.json())
        .then(totalCount => {
            document.getElementById("total-reservation-count").textContent = totalCount;
        })
        .catch(console.error);

    // 일별 예약 수 (최근 7일) 가져와서 표시
    fetch(`${baseUrl}/reservation/daily`)
        .then(res => res.json())
        .then(dailyData => {
            const dailyList = document.getElementById("daily-reservation-list");
            dailyList.innerHTML = ""; // 초기화
            dailyData.forEach(item => {
                // item은 { date: "2025-05-14", count: 15 } 형태라고 가정
                const li = document.createElement("li");
                li.textContent = `${item.date}: ${item.count}건`;
                dailyList.appendChild(li);
            });
        })
        .catch(console.error);

    // 가장 많이 예약된 파트너 TOP3 가져와서 표시
    fetch(`${baseUrl}/partner/top3`)
        .then(res => res.json())
        .then(top3Partners => {
            console.log("Top3 Partners 데이터 확인:", top3Partners);  // 여기에 추가!
            const top3List = document.getElementById("top3-partners-list");
            top3List.innerHTML = ""; // 초기화
            top3Partners.forEach(partner => {
                const li = document.createElement("li");
                li.textContent = `${partner.partnerName} (예약 ${partner.reservationCount}건)`;
                top3List.appendChild(li);
            });
        })
        .catch(console.error);
});


async function fetchPartnerStats() {
    console.log("📌 fetchPartnerStats 실행됨"); // ✅ 실행 확인용 로그

    try {
        const response = await fetch('/api/admin/pending/count', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("authToken")}`
            }
        });

        if (!response.ok) {
            const errorText = await response.text();
            console.error('❌ API 호출 실패:', errorText);
            throw new Error('API 호출 실패');
        }

        const data = await response.json();
        console.log("✅ 승인 대기 파트너 수:", data); // 👈 데이터 값 확인

        document.getElementById('pending-partner-count').textContent = data;
    } catch (error) {
        console.error('❌ 유저/파트너 통계 조회 실패:', error);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    fetch('/api/admin/count', {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem("authToken")}`
        }
    })
        .then(res => {
            if (!res.ok) throw new Error("응답 실패");
            return res.json();
        })
        .then(data => {
            document.getElementById("user-count").innerText = data.userCount;
            document.getElementById("partner-count").innerText = data.partnerCount;
        })
        .catch(err => console.error("유저 통계 조회 실패", err));

    fetchPartnerStats(); // 만약 fetchPartnerStats 함수가 있다면 여기에 호출
});

document.addEventListener('DOMContentLoaded', () => {
    fetch('/api/admin/reservations/counts', {
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('authToken'),
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) throw new Error("예약 통계 응답 실패");
            return response.json();
        })
        .then(data => {
            console.log('예약 통계 데이터:', data);
            console.log('todayCount:', data.todayCount, 'futureCount:', data.futureCount);

            document.getElementById('today-reservation-count').textContent = data.todayReservationCount || 0;
            document.getElementById('future-reservation-count').textContent = data.futureReservationCount || 0;




            if(todayEl) todayEl.textContent = data.todayCount || 0;
            if(futureEl) futureEl.textContent = data.futureCount || 0;
        })
        .catch(error => {

        });
});

