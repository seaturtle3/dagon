import os
import requests
from bs4 import BeautifulSoup
from urllib.parse import urljoin
from tqdm import tqdm

BASE_URL = "https://thefishing.kr"
LIST_URL = f"{BASE_URL}/reservation/list.php"
SAVE_DIR = "thefishing_images"
os.makedirs(SAVE_DIR, exist_ok=True)

headers = {
    "User-Agent": "Mozilla/5.0"
}

def get_boat_image_links(page=1):
    url = f"{LIST_URL}?page={page}"
    res = requests.get(url, headers=headers)
    soup = BeautifulSoup(res.text, "html.parser")
    img_links = []

    # 배 썸네일 이미지 추출 (예상: .thumb, .img, img 태그 등)
    for img_tag in soup.select("img"):  # 필요시 더 구체적 selector로 변경
        src = img_tag.get("src")
        if isinstance(src, list):
            src = src[0]
        if src and ("boat" in src or "ship" in src or "jpg" in src or "png" in src):
            img_url = urljoin(BASE_URL, src)
            img_links.append(img_url)
    return img_links

def download_images(img_urls):
    for img_url in tqdm(img_urls, desc="이미지 다운로드"):
        try:
            img_data = requests.get(img_url, headers=headers).content
            filename = os.path.join(SAVE_DIR, f"{hash(img_url)}.jpg")
            with open(filename, "wb") as f:
                f.write(img_data)
            print(f"✔ 이미지 저장: {filename}")
        except Exception as e:
            print(f"✘ 이미지 다운로드 실패: {img_url} - {e}")

def crawl_all_boat_images(pages=3):
    all_img_links = set()
    for page in range(1, pages + 1):
        print(f"\n[페이지 {page}] 배 이미지 링크 수집 중...")
        img_links = get_boat_image_links(page)
        print(f"▶ 이미지 {len(img_links)}개 발견")
        all_img_links.update(img_links)
    download_images(list(all_img_links))

if __name__ == "__main__":
    crawl_all_boat_images(pages=3)  # 첫 3페이지만 테스트 