import sys
import pymysql
from PIL import Image
import io

# === DB 접속 정보 ===
DB_HOST = 'docs.yi.or.kr'
DB_PORT = 43306
DB_USER = 'root'      # <-- 여기에 DB 사용자명 입력
DB_PASS = 'edurootroot'      # <-- 여기에 DB 비밀번호 입력
DB_NAME = 'dagon'

# === 썸네일 크기 ===
THUMBNAIL_SIZE = (200, 200)

# === 처리할 테이블 목록 ===
ALL_TABLES = ['fishing_diary_image', 'fishing_report_image', 'prod_image']

# === 썸네일 생성 및 저장 함수 ===
def process_table(table_name):
    print(f'\n[{table_name}] 테이블 처리 시작...')
    with conn.cursor() as cursor:
        cursor.execute(f"SELECT id, image_data FROM {table_name} WHERE image_data IS NOT NULL")
        rows = cursor.fetchall()
        for row in rows:
            id, image_bytes = row
            try:
                image = Image.open(io.BytesIO(image_bytes))
                if image.mode != 'RGB':
                    image = image.convert('RGB')
                image.thumbnail(THUMBNAIL_SIZE)
                thumb_io = io.BytesIO()
                image.save(thumb_io, format='JPEG')
                thumb_bytes = thumb_io.getvalue()
                cursor.execute(
                    f"UPDATE {table_name} SET thumbnail_data=%s WHERE id=%s",
                    (thumb_bytes, id)
                )
                print(f"썸네일 저장 완료: id={id}")
            except Exception as e:
                print(f"에러: id={id}, {e}")
        conn.commit()
    print(f'[{table_name}] 테이블 처리 완료!')

# === 명령행 인자 파싱 ===
def print_help():
    print("사용법: python make_thumbnails.py [테이블명1 테이블명2 ...]")
    print("예시: python make_thumbnails.py fishing_diary_image")
    print("테이블 목록:")
    for t in ALL_TABLES:
        print(f"  - {t}")
    print("인자를 생략하면 모든 테이블을 처리합니다.")

if __name__ == "__main__":
    if len(sys.argv) > 1 and sys.argv[1] in ('--help', '-h'):
        print_help()
        sys.exit(0)
    if len(sys.argv) > 1:
        tables = [t for t in sys.argv[1:] if t in ALL_TABLES]
        if not tables:
            print("잘못된 테이블명입니다. --help로 사용법을 확인하세요.")
            sys.exit(1)
    else:
        tables = ALL_TABLES

    # === DB 연결 ===
    conn = pymysql.connect(
        host=DB_HOST,
        port=DB_PORT,
        user=DB_USER,
        password=DB_PASS,
        db=DB_NAME,
        charset='utf8mb4'
    )

    for table in tables:
        process_table(table)

    conn.close()
    print('\n선택한 테이블 썸네일 변환 및 저장 완료!') 