import os
import time
import requests
import base64
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from PIL import Image
from io import BytesIO
from tqdm import tqdm
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.options import Options

def create_folder(folder_path):
    if not os.path.exists(folder_path):
        os.makedirs(folder_path)

def init_driver():
    options = Options()
    options.add_argument("--no-sandbox")
    options.add_argument("--disable-dev-shm-usage")
    options.add_argument("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
    return webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=options)

def scroll_down(driver, scroll_pause_time=1.0, max_scrolls=10):
    last_height = driver.execute_script("return document.body.scrollHeight")
    for _ in range(max_scrolls):
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        time.sleep(scroll_pause_time)
        new_height = driver.execute_script("return document.body.scrollHeight")
        if new_height == last_height:
            break
        last_height = new_height

def fetch_image_urls(driver, keyword, max_links=500):
    search_url = f"https://www.google.com/search?hl=ko&tbm=isch&q={keyword}"
    driver.get(search_url)
    image_urls = set()
    scroll_down(driver, max_scrolls=20)

    # 여러 셀렉터를 동시에 시도
    selector = "img.Q4LuWd, img.YQ4gaf, img.rg_i"
    try:
        WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, selector))
        )
    except Exception as e:
        print("썸네일 요소를 찾지 못했습니다:", e)
        print("페이지 일부 소스:", driver.page_source[:1000])
        driver.save_screenshot("debug.png")
        driver.quit()
        exit(1)
    thumbnails = driver.find_elements(By.CSS_SELECTOR, selector)
    print("thumbnails 개수:", len(thumbnails))

    for thumb in thumbnails:
        src = thumb.get_attribute("src")
        if src and (src.startswith("http") or src.startswith("data:image")):
            print("썸네일 src:", src[:60])
            image_urls.add(src)
        # 클릭 생략!
        driver.execute_script("arguments[0].removeAttribute('target');", thumb)

    print(f"[INFO] 최종 수집된 이미지 URL: {len(image_urls)}")
    return image_urls

def download_images(folder_path, image_urls):
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
    }
    for i, url in enumerate(tqdm(image_urls)):
        print(f"다운로드 시도: {url[:60]}...")
        try:
            if url.startswith("http"):
                response = requests.get(url, headers=headers, timeout=5)
                img = Image.open(BytesIO(response.content)).convert("RGB")
                img.save(os.path.join(folder_path, f"fishing_{i+1:03}.jpg"))
            elif url.startswith("data:image"):
                header, encoded = url.split(",", 1)
                img_data = base64.b64decode(encoded)
                img = Image.open(BytesIO(img_data)).convert("RGB")
                img.save(os.path.join(folder_path, f"fishing_{i+1:03}.jpg"))
            else:
                print(f"지원하지 않는 URL 형식: {url}")
        except Exception as e:
            print(f"다운로드 실패: {e}")
            continue

if __name__ == "__main__":
    keyword = "조황정보"
    save_dir = "images"
    create_folder(save_dir)

    driver = init_driver()
    urls = fetch_image_urls(driver, keyword, max_links=500)
    print(urls)
    driver.quit()

    print(f"[INFO] 총 수집된 이미지 URL: {len(urls)}")
    download_images(save_dir, urls)
    print("[INFO] 이미지 다운로드 완료")

    main_window = driver.current_window_handle
    for handle in driver.window_handles:
        if handle != main_window:
            driver.switch_to.window(handle)
            driver.close()
    driver.switch_to.window(main_window)
