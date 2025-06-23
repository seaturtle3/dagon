import os
import random
import pymysql
from datetime import datetime

# DB 연결 정보
DB_HOST = 'docs.yi.or.kr'
DB_PORT = 24306
DB_USER = 'root'
DB_PASSWORD = 'edurootroot'  # 실제 비밀번호로 변경
DB_NAME = 'dagon'

# 이미지 폴더 경로
IMAGE_DIR = 'images'

# 1. DB 연결
conn = pymysql.connect(
    host=DB_HOST, port=DB_PORT, user=DB_USER, password=DB_PASSWORD, db=DB_NAME, charset='utf8mb4'
)
cur = conn.cursor()

# 2. fishing_report의 fr_id, created_at 모두 가져오기
cur.execute("SELECT fr_id, created_at FROM fishing_report ORDER BY fr_id")
reports = cur.fetchall()

# 3. images 폴더의 이미지 파일 목록
image_files = [f for f in os.listdir(IMAGE_DIR) if f.lower().endswith(('.jpg', '.jpeg', '.png'))]
random.shuffle(image_files)
img_idx = 0

for fr_id, created_at in reports:
    # 3~4개 랜덤 선택
    num_imgs = random.randint(3, 4)
    selected_imgs = image_files[img_idx:img_idx+num_imgs]
    if not selected_imgs:
        break
    for order, img_name in enumerate(selected_imgs):
        img_path = os.path.join(IMAGE_DIR, img_name)
        with open(img_path, 'rb') as f:
            img_blob = f.read()
        # 날짜 폴더 구조 생성
        if isinstance(created_at, str):
            # 문자열이면 datetime으로 변환
            created_at_dt = datetime.strptime(created_at, '%Y-%m-%d %H:%M:%S')
        else:
            created_at_dt = created_at
        date_str = created_at_dt.strftime('%Y/%m/%d')
        image_url = f'/uploads/fishing-report/{date_str}/{img_name}'
        is_thumbnail = (order == 0)
        # INSERT
        cur.execute("""
            INSERT INTO fishing_report_image (image_url, image_data, is_thumbnail, order_index, fr_id)
            VALUES (%s, %s, %s, %s, %s)
        """, (image_url, img_blob, is_thumbnail, order, fr_id))
    img_idx += num_imgs
    if img_idx >= len(image_files):
        break

conn.commit()
cur.close()
conn.close()

print('이미지 등록 완료!') 