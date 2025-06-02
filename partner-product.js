document.addEventListener("DOMContentLoaded", () => {
    // 페이지 로드 시 상품 목록 불러오기
    loadProductList();
    loadProductRegisterForm();

    const productForm = document.getElementById("productForm");
    if (productForm) {
        productForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const token = localStorage.getItem('authToken');

            const productData = {
                name: document.getElementById("prodName").value,
                description: document.getElementById("prodDesc").value,
                price: parseFloat(document.getElementById("prodPrice").value)
            };

            const response = await fetch("/api/partner/product/create", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(productData)
            });

            if (response.ok) {
                alert("상품이 등록되었습니다.");
                document.getElementById("productForm").reset();
                loadProductList(); // 목록 새로고침
            } else {
                alert("상품 등록 실패");
            }
        });
    }

    async function loadProductList() {
        const token = localStorage.getItem('authToken');
        const container = document.getElementById("productListContainer");
        
        if (!container) {
            console.error("상품 목록을 표시할 컨테이너를 찾을 수 없습니다.");
            return;
        }

        try {
            const response = await fetch(`/api/partner/product/my-products`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (response.ok) {
                const products = await response.json();
                console.log("불러온 상품 목록:", products);
                renderProductList(products);
            } else {
                console.error("상품 목록 조회 실패:", response.status);
                alert("상품을 불러오지 못했습니다.");
            }
        } catch (error) {
            console.error("상품 목록 조회 중 오류 발생:", error);
            alert("상품 목록을 불러오는 중 오류가 발생했습니다.");
        }
    }

    // ... 나머지 코드는 그대로 유지 ...
}); 