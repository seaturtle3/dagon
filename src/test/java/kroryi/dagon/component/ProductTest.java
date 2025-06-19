package kroryi.dagon.component;

import jakarta.transaction.Transactional;
import kroryi.dagon.entity.Product;
import kroryi.dagon.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@SpringBootTest
public class ProductTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void updateProductAddresses() {
        List<String> addressList = List.of(
                "서울특별시 강남구 테헤란로 101",
                "서울특별시 마포구 월드컵북로 22",
                "부산광역시 해운대구 우동 1404",
                "부산광역시 수영구 광안해변로 219",
                "대구광역시 수성구 동대구로 59",
                "대전광역시 서구 둔산로 123",
                "광주광역시 북구 용봉로 77",
                "인천광역시 연수구 센트럴로 55",
                "울산광역시 남구 삼산로 12",
                "세종특별자치시 나성로 245",
                "경기도 성남시 분당구 정자일로 10",
                "경기도 수원시 팔달구 중부대로 80",
                "경기도 고양시 일산서구 중앙로 1600",
                "경기도 안양시 동안구 시민대로 175",
                "강원도 춘천시 방송길 5",
                "강원도 원주시 시청로 1",
                "충청북도 청주시 상당구 상당로 55",
                "충청북도 충주시 번영로 145",
                "충청남도 천안시 서북구 불당대로 123",
                "충청남도 아산시 배방로 89",
                "전라북도 전주시 덕진구 기린대로 250",
                "전라북도 군산시 대학로 558",
                "전라남도 목포시 하당로 100",
                "전라남도 순천시 중앙로 17",
                "경상북도 포항시 북구 중앙로 123",
                "경상북도 경주시 황성로 77",
                "경상남도 창원시 의창구 원이대로 432",
                "경상남도 진주시 동진로 89",
                "제주특별자치도 제주시 중앙로 10",
                "제주특별자치도 서귀포시 태평로 45",
                "서울특별시 종로구 사직로 9",
                "서울특별시 동작구 흑석로 100",
                "부산광역시 중구 중앙대로 123",
                "부산광역시 사하구 하단로 58",
                "대구광역시 중구 동성로3가 77",
                "대전광역시 중구 계룡로 125",
                "광주광역시 남구 봉선중앙로 45",
                "인천광역시 남동구 논현로 300",
                "울산광역시 중구 외솔로 10",
                "세종특별자치시 어진동 세종대로 400",
                "경기도 용인시 기흥구 중부대로 5",
                "경기도 의정부시 시민로 1",
                "강원도 강릉시 경강로 2100",
                "강원도 동해시 천곡로 42",
                "충청북도 제천시 청전대로 199",
                "충청남도 공주시 백제문화로 362",
                "전라북도 익산시 무왕로 33",
                "전라남도 여수시 돌산읍 향일암로 456",
                "경상북도 김천시 혁신로 88",
                "경상남도 양산시 물금읍 화합1길 100",
                "제주특별자치도 제주시 애월읍 하귀리 301",
                "서울특별시 성동구 왕십리로 222",
                "서울특별시 은평구 진관동 은평로 300",
                "부산광역시 금정구 중앙대로 234",
                "대구광역시 달서구 성서로 56",
                "광주광역시 서구 풍암동 상무대로 333",
                "인천광역시 서구 청라대로 66",
                "경기도 평택시 평택로 88",
                "경기도 남양주시 경춘로 1501",
                "강원도 속초시 설악산로 333",
                "충청북도 보은군 보은로 101",
                "충청남도 논산시 시민로 55",
                "전라북도 정읍시 시기동 중앙로 20",
                "전라남도 광양시 광양읍 매화로 70",
                "경상북도 안동시 경동로 221",
                "경상남도 사천시 삼천포로 89",
                "제주특별자치도 성산읍 일출로 88",
                "서울특별시 강서구 화곡로 200",
                "부산광역시 기장군 기장읍 대변로 101",
                "경기도 하남시 미사대로 50",
                "경기도 김포시 김포한강9로 50",
                "강원도 태백시 태백로 120",
                "충청북도 음성군 무극로 44",
                "충청남도 서산시 호수공원길 78",
                "전라북도 남원시 광한루로 77",
                "전라남도 담양군 담양읍 중앙로 123",
                "경상북도 영주시 영주로 300",
                "경상남도 밀양시 삼문동 밀양대로 90",
                "제주특별자치도 우도면 해안도로 77"
        );

        List<Product> productList = productRepository.findTop80ByOrderByProdIdAsc(); // 80개 가져오기

        for (int i = 0; i < productList.size(); i++) {
            productList.get(i).setProdAddress(addressList.get(i));
        }
    }

}
