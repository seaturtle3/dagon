import os
import requests
from bs4 import BeautifulSoup
from urllib.parse import urljoin
from tqdm import tqdm

BASE_URL = "http://www.jonathanho.kr"
BOARD_URL = f"{BASE_URL}/board/johwang"
SAVE_DIR = "downloaded_images"
os.makedirs(SAVE_DIR, exist_ok=True)

headers = {
    "User-Agent": "Mozilla/5.0"
}

def get_post_links(page=1):
    url = f"{BOARD_URL}?page={page}"
    res = requests.get(url, headers=headers)
    soup = BeautifulSoup(res.text, "html.parser")
    links = []

    for a_tag in soup.select("a"):
        href = a_tag.get("href", "")
        if href.startswith("/post/"):
            full_url = urljoin(BASE_URL, href)
            links.append(full_url)

    return links


def download_images_from_post(post_url):
    res = requests.get(post_url, headers=headers)
    soup = BeautifulSoup(res.text, "html.parser")
    img_tags = soup.select("img")

    for i, img in enumerate(img_tags):
        src = img.get("src")
        if not src:
            continue

        img_url = urljoin(BASE_URL, src)
        try:
            img_data = requests.get(img_url, headers=headers).content
            filename = os.path.join(SAVE_DIR, f"{hash(img_url)}.jpg")
            with open(filename, "wb") as f:
                f.write(img_data)
            print(f"[✔] 이미지 저장: {filename}")
        except Exception as e:
            print(f"[✘] 다운로드 실패: {img_url} - {e}")

def crawl_all_images(pages=3):
    for page in range(1, pages + 1):
        print(f"\n[페이지 {page}] 게시글 링크 수집 중...")
        links = get_post_links(page)
        print(f"▶ 게시글 {len(links)}개 발견")
        for post_url in tqdm(links, desc="게시글 처리"):
            download_images_from_post(post_url)

if __name__ == "__main__":
    crawl_all_images(pages=3)  # 첫 3페이지만 수집
