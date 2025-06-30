import os
import random
import pymysql
from datetime import datetime
import shutil
from PIL import Image
import io

# DB 연결 정보
DB_HOST = 'docs.yi.or.kr'
DB_PORT = 24306
DB_USER = 'root'
DB_PASSWORD = 'edurootroot'  # 실제 비밀번호로 변경
DB_NAME = 'dagon'

# 이미지 폴더 경로
IMAGE_DIR = 'monak_images'
UPLOADS_ROOT = os.path.join(os.getcwd(), 'uploads', 'fishing-diary')

# 1. DB 연결
conn = pymysql.connect(
    host=DB_HOST, port=DB_PORT, user=DB_USER, password=DB_PASSWORD, db=DB_NAME, charset='utf8mb4'
)
cur = conn.cursor()

# 2. fishing_diary의 fd_id, created_at 모두 가져오기
cur.execute("SELECT fd_id, created_at FROM fishing_diary ORDER BY fd_id")
diaries = cur.fetchall()
print(diaries)

# 3. images 폴더의 이미지 파일 목록
image_files = [f for f in os.listdir(IMAGE_DIR) if f.lower().endswith(('.jpg', '.jpeg', '.png'))]
random.shuffle(image_files)
img_idx = 0

for fd_id, created_at in diaries:
    # 4~10개 랜덤 선택
    num_imgs = random.randint(4, 10)
    selected_imgs = image_files[img_idx:img_idx+num_imgs]
    if not selected_imgs:
        break
    for order, img_name in enumerate(selected_imgs):
        img_path = os.path.join(IMAGE_DIR, img_name)
        with open(img_path, 'rb') as f:
            img_blob = f.read()
        # 썸네일 생성 (첫 번째 이미지일 때만)
        thumbnail_blob = None
        if order == 0:
            try:
                with Image.open(img_path) as im:
                    im = im.convert('RGB')
                    im.thumbnail((400, 300))
                    thumb_io = io.BytesIO()
                    im.save(thumb_io, format='JPEG')
                    thumbnail_blob = thumb_io.getvalue()
            except Exception as e:
                print(f"썸네일 생성 실패: {img_name} - {e}")
        # 날짜 폴더 구조 생성
        if isinstance(created_at, str):
            created_at_dt = datetime.strptime(created_at, '%Y-%m-%d %H:%M:%S')
        else:
            created_at_dt = created_at
        date_str = created_at_dt.strftime('%Y/%m/%d')
        image_url = f'/uploads/fishing-diary/{date_str}/{img_name}'
        is_thumbnail = (order == 0)
        # INSERT
        cur.execute("""
            INSERT INTO fishing_diary_image (image_url, image_data, is_thumbnail, order_index, fd_id, thumbnail_data)
            VALUES (%s, %s, %s, %s, %s, %s)
        """, (image_url, img_blob, is_thumbnail, order, fd_id, thumbnail_blob))
        # 파일 이동
        target_dir = os.path.join(UPLOADS_ROOT, *date_str.split('/'))
        os.makedirs(target_dir, exist_ok=True)
        target_path = os.path.join(target_dir, img_name)
        shutil.move(img_path, target_path)
    img_idx += num_imgs
    if img_idx >= len(image_files):
        break

conn.commit()
cur.close()
conn.close()

print('fishing_diary_image 이미지 등록 및 파일 이동 완료!') 