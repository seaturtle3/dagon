// 이벤트 이미지 삭제 관련 JavaScript 함수들

/**
 * 이벤트의 모든 이미지 삭제
 * @param {number} eventId - 삭제할 이벤트의 ID
 * @returns {Promise} 삭제 결과
 */
async function deleteEventImages(eventId) {
    try {
        const response = await fetch(`/api/images/delete?eventId=${eventId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.text();
        console.log('이미지 삭제 결과:', result);
        
        // 성공 메시지 표시
        if (response.status === 200) {
            alert('이벤트 이미지가 성공적으로 삭제되었습니다.');
            // 필요시 페이지 새로고침 또는 UI 업데이트
            location.reload();
        }
        
        return result;
    } catch (error) {
        console.error('이미지 삭제 실패:', error);
        alert('이미지 삭제 중 오류가 발생했습니다: ' + error.message);
        throw error;
    }
}

/**
 * 개별 이미지 삭제
 * @param {number} imageId - 삭제할 이미지의 ID
 * @returns {Promise} 삭제 결과
 */
async function deleteSingleImage(imageId) {
    try {
        const response = await fetch(`/api/images/delete/${imageId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.text();
        console.log('개별 이미지 삭제 결과:', result);
        
        if (response.status === 200) {
            alert('이미지가 성공적으로 삭제되었습니다.');
            // 필요시 해당 이미지 요소만 UI에서 제거
            const imageElement = document.querySelector(`[data-image-id="${imageId}"]`);
            if (imageElement) {
                imageElement.remove();
            }
        }
        
        return result;
    } catch (error) {
        console.error('개별 이미지 삭제 실패:', error);
        alert('이미지 삭제 중 오류가 발생했습니다: ' + error.message);
        throw error;
    }
}

/**
 * 이벤트 수정 시 기존 이미지 삭제 후 새 이미지 업로드
 * @param {number} eventId - 이벤트 ID
 * @param {FileList} newImages - 새로 업로드할 이미지들
 */
async function updateEventImages(eventId, newImages) {
    try {
        // 1. 기존 이미지 삭제
        await deleteEventImages(eventId);
        
        // 2. 새 이미지 업로드 (이벤트 수정 API 호출)
        const formData = new FormData();
        formData.append('eventId', eventId);
        
        for (let i = 0; i < newImages.length; i++) {
            formData.append('images', newImages[i]);
        }
        
        // 이벤트 수정 API 호출 (실제 구현에 맞게 수정 필요)
        const response = await fetch(`/api/admin/event/${eventId}`, {
            method: 'POST',
            body: formData
        });
        
        if (response.ok) {
            alert('이벤트 이미지가 성공적으로 업데이트되었습니다.');
            location.reload();
        } else {
            throw new Error('이벤트 수정 실패');
        }
        
    } catch (error) {
        console.error('이벤트 이미지 업데이트 실패:', error);
        alert('이벤트 이미지 업데이트 중 오류가 발생했습니다: ' + error.message);
    }
}

/**
 * 이미지 삭제 확인 다이얼로그
 * @param {number} eventId - 이벤트 ID
 * @param {string} imageName - 이미지 이름 (선택사항)
 */
function confirmImageDeletion(eventId, imageName = '') {
    const message = imageName 
        ? `"${imageName}" 이미지를 삭제하시겠습니까?`
        : `이 이벤트의 모든 이미지를 삭제하시겠습니까?`;
    
    if (confirm(message)) {
        deleteEventImages(eventId);
    }
}

// Vue.js 컴포넌트에서 사용하는 예시 (props.eventId 사용)
/*
export default {
    props: {
        eventId: {
            type: Number,
            required: true
        }
    },
    methods: {
        async deleteImages() {
            try {
                await deleteEventImages(this.eventId);
                this.$emit('images-deleted');
            } catch (error) {
                console.error('이미지 삭제 실패:', error);
            }
        }
    }
}
*/

// React 컴포넌트에서 사용하는 예시
/*
const EventImageManager = ({ eventId }) => {
    const handleDeleteImages = async () => {
        try {
            await deleteEventImages(eventId);
            // 상태 업데이트 또는 콜백 호출
        } catch (error) {
            console.error('이미지 삭제 실패:', error);
        }
    };

    return (
        <button onClick={handleDeleteImages}>
            이미지 삭제
        </button>
    );
};
*/ 