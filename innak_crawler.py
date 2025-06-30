import os
import requests
from bs4 import BeautifulSoup
from urllib.parse import urljoin

BASE_URL = "https://monak.kr"
SAVE_DIR = "monak_images"
os.makedirs(SAVE_DIR, exist_ok=True)

headers = {
    "User-Agent": "Mozilla/5.0"
}

def get_post_content_and_images(post_url):
    res = requests.get(post_url, headers=headers)
    soup = BeautifulSoup(res.text, "html.parser")
    # 제목
    title = soup.select_one(".post-title, .view_subject, .bo_v_title, .view_title, h1, h2")
    title = title.get_text(strip=True) if title else "제목없음"
    # 본문 내용
    content = soup.select_one(".post-content, .view_content, .bo_v_con, .gall_con")
    content = content.get_text(strip=True) if content else ""
    # 이미지들
    img_urls = []
    for img in soup.select("img"):
        src = img.get("src")
        if src and ("jpg" in src or "png" in src):
            img_url = urljoin(BASE_URL, str(src))
            img_urls.append(img_url)
    return title, content, img_urls

def download_images(img_urls, prefix=""):
    saved_files = []
    for idx, img_url in enumerate(img_urls):
        try:
            img_data = requests.get(img_url, headers=headers).content
            filename = os.path.join(SAVE_DIR, f"{prefix}_{idx}_{os.path.basename(img_url).split('?')[0]}")
            with open(filename, "wb") as f:
                f.write(img_data)
            saved_files.append(filename)
        except Exception as e:
            print(f"✘ 이미지 다운로드 실패: {img_url} - {e}")
    return saved_files

def crawl_monak_post_detail(post_url):
    title, content, img_urls = get_post_content_and_images(post_url)
    saved_imgs = download_images(img_urls, prefix=title[:10].replace(' ', '_'))
    result = {
        "url": post_url,
        "title": title,
        "content": content,
        "images": saved_imgs
    }
    return result

if __name__ == "__main__":
    # p_idx 2000~3000 범위 순회
    for p_idx in range(2000, 3001):
        detail_url = f"https://monak.kr/post/view.php?from=list&p_idx={p_idx}&g=j&sch=1&st=5"
        print(f"\n[크롤링] {detail_url}")
        try:
            post = crawl_monak_post_detail(detail_url)
            if post['content'] or post['images']:
                print(f"[제목] {post['title']}\n[URL] {post['url']}\n[본문] {post['content'][:100]}...\n[이미지] {post['images']}")
            else:
                print("- 내용/이미지 없음")
        except Exception as e:
            print(f"- 크롤링 실패: {e}") 