document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('notification-form-container').innerHTML = `
      <div class="p-4 rounded-lg shadow bg-white w-full max-w-2xl mx-auto">
        <h2 class="text-xl font-semibold mb-4">관리자 알림 등록</h2>

        <form id="adminNotificationForm" class="space-y-4">
          <div>
            <label for="receiverUid" class="block font-medium mb-1">수신자 유저 아이디 (비워두면 전체 발송)</label>
            <input type="text" id="receiverUid" name="receiverUid" class="w-full border rounded p-2" placeholder="유저 아이디 입력 or 비워두세요">
          </div>

          <div>
            <label for="type" class="block font-medium mb-1">알림 타입</label>
            <select id="type" name="type" class="w-full border rounded p-2">
              <option value="NOTICE">공지사항</option>
              <option value="REPLY">답변</option>
              <option value="RESERVATION">예약 알림</option>
            </select>
          </div>

          <div>
            <label for="title" class="block font-medium mb-1">제목</label>
            <input type="text" id="title" name="title" class="w-full border rounded p-2" required>
          </div>

          <div>
            <label for="content" class="block font-medium mb-1">내용</label>
            <textarea id="content" name="content" rows="4" class="w-full border rounded p-2" required></textarea>
          </div>

          <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded shadow">
            알림 보내기
          </button>
        </form>

        <div id="resultMessage" class="mt-4 text-green-600 font-semibold hidden"></div>
      </div>
    `;

    document.getElementById('adminNotificationForm').addEventListener('submit', async function (e) {
        e.preventDefault();

        const receiverUid = document.getElementById('receiverUid').value || null;
        const type = document.getElementById('type').value;
        const title = document.getElementById('title').value;
        const content = document.getElementById('content').value;

        const payload = {
            receiverUid: receiverUid || null,
            senderType: "ADMIN",
            type,
            title,
            content
        };

        try {
            const res = await fetch('/api/notifications/admin-broadcast', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`
                },
                body: JSON.stringify(payload)
            });

            if (res.ok) {
                document.getElementById('resultMessage').textContent = "알림이 성공적으로 전송되었습니다.";
                document.getElementById('resultMessage').classList.remove('hidden');
                document.getElementById('adminNotificationForm').reset();
            } else {
                alert("알림 전송 실패: " + res.statusText);
            }
        } catch (err) {
            alert("오류 발생: " + err.message);
        }
    });
});
