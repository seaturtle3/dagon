// 이벤트 이미지 관리 JavaScript

/**
 * 이벤트 수정 시 이미지 ID 활용 예시
 */
class EventImageManager {
    constructor(eventId) {
        this.eventId = eventId;
        this.imageIds = [];
    }

    /**
     * 이벤트 수정 후 이미지 ID 리스트 받기
     */
    async updateEvent(formData) {
        try {
            const response = await fetch(`/api/admin/event/${this.eventId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: formData
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const eventData = await response.json();
            
            // 이미지 ID 리스트 저장
            this.imageIds = eventData.imageIdList || [];
            
            console.log('업데이트된 이미지 ID 리스트:', this.imageIds);
            
            // 이미지 ID를 활용한 추가 작업
            this.handleImageIds();
            
            return eventData;
            
        } catch (error) {
            console.error('이벤트 수정 실패:', error);
            throw error;
        }
    }

    /**
     * 이미지 ID를 활용한 작업들
     */
    handleImageIds() {
        // 1. 이미지 ID를 로컬 스토리지에 저장
        localStorage.setItem(`event_${this.eventId}_imageIds`, JSON.stringify(this.imageIds));
        
        // 2. 이미지 삭제 버튼에 ID 연결
        this.updateDeleteButtons();
        
        // 3. 이미지 순서 변경 기능
        this.enableImageReordering();
    }

    /**
     * 이미지 삭제 버튼 업데이트
     */
    updateDeleteButtons() {
        this.imageIds.forEach((imageId, index) => {
            const deleteBtn = document.querySelector(`[data-image-id="${imageId}"]`);
            if (deleteBtn) {
                deleteBtn.onclick = () => this.deleteSingleImage(imageId);
            }
        });
    }

    /**
     * 개별 이미지 삭제
     */
    async deleteSingleImage(imageId) {
        if (!confirm('이 이미지를 삭제하시겠습니까?')) {
            return;
        }

        try {
            const response = await fetch(`/api/images/delete/${imageId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });

            if (response.ok) {
                // 이미지 ID 리스트에서 제거
                this.imageIds = this.imageIds.filter(id => id !== imageId);
                localStorage.setItem(`event_${this.eventId}_imageIds`, JSON.stringify(this.imageIds));
                
                // UI에서 이미지 제거
                const imageElement = document.querySelector(`[data-image-id="${imageId}"]`);
                if (imageElement) {
                    imageElement.remove();
                }
                
                alert('이미지가 삭제되었습니다.');
            } else {
                throw new Error('이미지 삭제 실패');
            }
        } catch (error) {
            console.error('이미지 삭제 실패:', error);
            alert('이미지 삭제 중 오류가 발생했습니다.');
        }
    }

    /**
     * 이미지 순서 변경 기능
     */
    enableImageReordering() {
        const imageContainer = document.querySelector('.event-images-container');
        if (imageContainer) {
            // 드래그 앤 드롭으로 순서 변경
            new Sortable(imageContainer, {
                animation: 150,
                onEnd: (evt) => {
                    // 순서 변경 후 서버에 업데이트
                    this.updateImageOrder();
                }
            });
        }
    }

    /**
     * 이미지 순서 업데이트
     */
    async updateImageOrder() {
        const newOrder = Array.from(document.querySelectorAll('.event-image'))
            .map(img => img.dataset.imageId);
        
        try {
            const response = await fetch(`/api/admin/event/${this.eventId}/image-order`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify({ imageIds: newOrder })
            });

            if (response.ok) {
                console.log('이미지 순서가 업데이트되었습니다.');
            }
        } catch (error) {
            console.error('이미지 순서 업데이트 실패:', error);
        }
    }

    /**
     * 모든 이미지 삭제
     */
    async deleteAllImages() {
        if (!confirm('이 이벤트의 모든 이미지를 삭제하시겠습니까?')) {
            return;
        }

        try {
            const response = await fetch(`/api/images/delete?eventId=${this.eventId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });

            if (response.ok) {
                this.imageIds = [];
                localStorage.removeItem(`event_${this.eventId}_imageIds`);
                
                // UI에서 모든 이미지 제거
                document.querySelectorAll('.event-image').forEach(img => img.remove());
                
                alert('모든 이미지가 삭제되었습니다.');
            } else {
                throw new Error('이미지 삭제 실패');
            }
        } catch (error) {
            console.error('이미지 삭제 실패:', error);
            alert('이미지 삭제 중 오류가 발생했습니다.');
        }
    }
}

/**
 * Vue.js 컴포넌트에서 사용하는 예시
 */
/*
export default {
    data() {
        return {
            eventId: null,
            imageIds: [],
            imageManager: null
        }
    },
    async mounted() {
        this.eventId = this.$route.params.id;
        this.imageManager = new EventImageManager(this.eventId);
        
        // 기존 이미지 ID 로드
        const savedIds = localStorage.getItem(`event_${this.eventId}_imageIds`);
        if (savedIds) {
            this.imageIds = JSON.parse(savedIds);
        }
    },
    methods: {
        async updateEvent() {
            const formData = new FormData();
            // 폼 데이터 설정...
            
            try {
                const result = await this.imageManager.updateEvent(formData);
                this.imageIds = result.imageIdList;
                this.$emit('event-updated', result);
            } catch (error) {
                console.error('이벤트 수정 실패:', error);
            }
        },
        
        deleteImage(imageId) {
            this.imageManager.deleteSingleImage(imageId);
        },
        
        deleteAllImages() {
            this.imageManager.deleteAllImages();
        }
    }
}
*/

/**
 * React 컴포넌트에서 사용하는 예시
 */
/*
const EventEditForm = ({ eventId }) => {
    const [imageIds, setImageIds] = useState([]);
    const [imageManager, setImageManager] = useState(null);

    useEffect(() => {
        const manager = new EventImageManager(eventId);
        setImageManager(manager);
        
        // 기존 이미지 ID 로드
        const savedIds = localStorage.getItem(`event_${eventId}_imageIds`);
        if (savedIds) {
            setImageIds(JSON.parse(savedIds));
        }
    }, [eventId]);

    const handleUpdateEvent = async (formData) => {
        try {
            const result = await imageManager.updateEvent(formData);
            setImageIds(result.imageIdList);
        } catch (error) {
            console.error('이벤트 수정 실패:', error);
        }
    };

    const handleDeleteImage = (imageId) => {
        imageManager.deleteSingleImage(imageId);
        setImageIds(prev => prev.filter(id => id !== imageId));
    };

    return (
        <div>
            {/* 폼 컴포넌트 */
    /*        <button onClick={() => imageManager?.deleteAllImages()}>
                모든 이미지 삭제
            </button>
            
            {/* 이미지 리스트}
/*              {imageIds.map(imageId => (
               <div key={imageId} data-image-id={imageId}>
                   <img src={`/api/event/image/${imageId}`} alt="이벤트 이미지" />
                   <button onClick={() => handleDeleteImage(imageId)}>
                       삭제
                   </button>
               </div>
           ))}
       </div>
   );
};
*/