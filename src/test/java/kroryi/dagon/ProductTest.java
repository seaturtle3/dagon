package kroryi.dagon;

import kroryi.dagon.entity.Product;

public class ProductTest {
    public static void main(String[] args) {
        // 예제 데이터 생성
        Product product = new Product();
        product.setMainType(Product.MainType.sea);
        product.setSubType(Product.SubType.mud_flat);

        // 출력 확인
        System.out.println("메인 분류: " + product.getMainType());
        System.out.println("서브 분류: " + product.getSubType());
        System.out.println("서브 분류의 메인 타입: " + product.getSubType().getMainType());
    }
}
