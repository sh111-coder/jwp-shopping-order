package cart.acceptance;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import cart.global.exception.ErrorCode;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class CartItemAcceptanceTest extends AcceptanceTest {

    @Nested
    @DisplayName("장바구니 상품 담기 시")
    class addCartItems {

        @Test
        @DisplayName("마음에 드는 상품을 사용자의 장바구니에 담을 수 있다.")
        void success() {
            // given

            // when

            // then
        }

        @Nested
        @DisplayName("장바구니 상품 담기가 실패하는 경우")
        class fail {

            @Test
            @DisplayName("사용자를 선택하지 않은 경우 실패한다.")
            void when_not_select_member() {
                // when
                ExtractableResponse<Response> 장바구니_상품_담기_요청 = 사용자를_선택하지_않은_장바구니_상품_담기_요청();
                JsonPath jsonPath = 장바구니_상품_담기_요청.body().jsonPath();

                // then
                assertSoftly(softly -> {
                    softly.assertThat(장바구니_상품_담기_요청.statusCode()).isEqualTo(ErrorCode.NOT_EXISTS_AUTHORIZATION_HEADER.getStatus().value());
                    softly.assertThat(jsonPath.getString("code")).isEqualTo(ErrorCode.NOT_EXISTS_AUTHORIZATION_HEADER.getCode());
                    softly.assertThat(jsonPath.getString("message")).isEqualTo(ErrorCode.NOT_EXISTS_AUTHORIZATION_HEADER.getMessage());
                });
            }

            @Test
            @DisplayName("올바른 인증 방식이 아닌 경우 예외가 발생한다.")
            void when_incorrect_auth() {
                // when
                ExtractableResponse<Response> 장바구니_상품_담기_요청 = 올바르지_않은_인증_방식_장바구니_상품_담기_요청();
                JsonPath jsonPath = 장바구니_상품_담기_요청.body().jsonPath();

                // then
                assertSoftly(softly -> {
                    softly.assertThat(장바구니_상품_담기_요청.statusCode()).isEqualTo(ErrorCode.NOT_EXISTS_AUTHORIZATION_PREFIX.getStatus().value());
                    softly.assertThat(jsonPath.getString("code")).isEqualTo(ErrorCode.NOT_EXISTS_AUTHORIZATION_PREFIX.getCode());
                    softly.assertThat(jsonPath.getString("message")).isEqualTo(ErrorCode.NOT_EXISTS_AUTHORIZATION_PREFIX.getMessage());
                });
            }

            @Test
            @DisplayName("없는 사용자를 선택한 경우 예외가 발생한다.")
            void when_not_exists_member_select() {
                // when
                ExtractableResponse<Response> 장바구니_상품_담기_요청 = 없는_사용자_장바구니_상품_담기_요청();
                JsonPath jsonPath = 장바구니_상품_담기_요청.body().jsonPath();

                // then
                assertSoftly(softly -> {
                    softly.assertThat(장바구니_상품_담기_요청.statusCode()).isEqualTo(ErrorCode.AUTH_MEMBER_FAIL.getStatus().value());
                    softly.assertThat(jsonPath.getString("code")).isEqualTo(ErrorCode.AUTH_MEMBER_FAIL.getCode());
                    softly.assertThat(jsonPath.getString("message")).isEqualTo(ErrorCode.AUTH_MEMBER_FAIL.getMessage());
                });
            }
        }

        private ExtractableResponse<Response> 사용자를_선택하지_않은_장바구니_상품_담기_요청() {
            return RestAssured.given().log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/cart-items")
                    .then().log().all()
                    .extract();
        }

        private ExtractableResponse<Response> 올바르지_않은_인증_방식_장바구니_상품_담기_요청() {
            return RestAssured.given().log().all()
                    .header("Authorization", "abcd")
                    .when()
                    .get("/cart-items")
                    .then().log().all()
                    .extract();
        }

        private ExtractableResponse<Response> 없는_사용자_장바구니_상품_담기_요청() {
            return RestAssured.given().log().all()
                    .auth().preemptive().basic("notExist", "notExist")
                    .when()
                    .get("/cart-items")
                    .then().log().all()
                    .extract();
        }

    }

}
