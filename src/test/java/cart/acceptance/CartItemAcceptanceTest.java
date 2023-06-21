package cart.acceptance;

import static cart.fixture.CartItemFixtures.MEMBER_A_CART_ITEM1;
import static cart.fixture.MemberFixtures.MEMBER_A;
import static cart.fixture.ProductFixtures.PIZZA;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;

import cart.cartitem.application.dto.CartItemQuantityUpdateRequest;
import cart.cartitem.application.dto.CartItemRequest;
import cart.cartitem.application.dto.CartItemResponse;
import cart.global.exception.ErrorCode;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class CartItemAcceptanceTest extends AcceptanceTest {

    @Nested
    @DisplayName("장바구니 목록 조회 시")
    class showCartItems {

        @Test
        @DisplayName("사용자의 장바구니에 담긴 상품 목록을 조회할 수 있다.")
        void success() {
            // when
            ExtractableResponse<Response> 회원A_장바구니_상품_목록_조회_요청 = 회원A_장바구니_상품_목록_조회_요청();
            List<CartItemResponse> actualResponses = 회원A_장바구니_상품_목록_조회_요청.jsonPath().getList(".", CartItemResponse.class);

            // then
            assertSoftly(softly -> {
                softly.assertThat(회원A_장바구니_상품_목록_조회_요청.statusCode()).isEqualTo(HttpStatus.OK.value());
                softly.assertThat(actualResponses).usingRecursiveFieldByFieldElementComparator()
                        .isEqualTo(MEMBER_A.CART_ITEM_RESPONSES);
            });
        }

        @Nested
        @DisplayName("장바구니 상품 목록 조회가 실패하는 경우")
        class fail {

            @Test
            @DisplayName("사용자를 선택하지 않은 경우 실패한다.")
            void when_not_select_member() {
                // when
                ExtractableResponse<Response> 장바구니_상품_목록_조회_요청 = 사용자를_선택하지_않은_장바구니_상품_목록_조회_요청();
                JsonPath jsonPath = 장바구니_상품_목록_조회_요청.body().jsonPath();

                // then
                assertSoftly(softly -> {
                    softly.assertThat(장바구니_상품_목록_조회_요청.statusCode()).isEqualTo(ErrorCode.NOT_EXISTS_AUTHORIZATION_HEADER.getStatus().value());
                    softly.assertThat(jsonPath.getString("code")).isEqualTo(ErrorCode.NOT_EXISTS_AUTHORIZATION_HEADER.getCode());
                    softly.assertThat(jsonPath.getString("message")).isEqualTo(ErrorCode.NOT_EXISTS_AUTHORIZATION_HEADER.getMessage());
                });
            }

            @Test
            @DisplayName("올바른 인증 방식이 아닌 경우 예외가 발생한다.")
            void when_incorrect_auth() {
                // when
                ExtractableResponse<Response> 장바구니_상품_목록_조회_요청 = 올바르지_않은_인증_방식_장바구니_상품_목록_조회_요청();
                JsonPath jsonPath = 장바구니_상품_목록_조회_요청.body().jsonPath();

                // then
                assertSoftly(softly -> {
                    softly.assertThat(장바구니_상품_목록_조회_요청.statusCode()).isEqualTo(ErrorCode.NOT_EXISTS_AUTHORIZATION_PREFIX.getStatus().value());
                    softly.assertThat(jsonPath.getString("code")).isEqualTo(ErrorCode.NOT_EXISTS_AUTHORIZATION_PREFIX.getCode());
                    softly.assertThat(jsonPath.getString("message")).isEqualTo(ErrorCode.NOT_EXISTS_AUTHORIZATION_PREFIX.getMessage());
                });
            }

            @Test
            @DisplayName("없는 사용자를 선택한 경우 예외가 발생한다.")
            void when_not_exists_member_select() {
                // when
                ExtractableResponse<Response> 장바구니_상품_목록_조회_요청 = 없는_사용자_장바구니_상품_목록_조회_요청();
                JsonPath jsonPath = 장바구니_상품_목록_조회_요청.body().jsonPath();

                // then
                assertSoftly(softly -> {
                    softly.assertThat(장바구니_상품_목록_조회_요청.statusCode()).isEqualTo(ErrorCode.AUTH_MEMBER_FAIL.getStatus().value());
                    softly.assertThat(jsonPath.getString("code")).isEqualTo(ErrorCode.AUTH_MEMBER_FAIL.getCode());
                    softly.assertThat(jsonPath.getString("message")).isEqualTo(ErrorCode.AUTH_MEMBER_FAIL.getMessage());
                });
            }
        }
    }

    @Nested
    @DisplayName("장바구니 상품 담기 시")
    class addCartItems {

        @Test
        @DisplayName("마음에 드는 상품을 사용자의 장바구니에 담을 수 있다.")
        void success() {
            // when
            ExtractableResponse<Response> 회원A_장바구니_상품_담기_요청 = 회원A_장바구니_상품_담기_요청();

            // then
            assertSoftly(softly -> {
                softly.assertThat(회원A_장바구니_상품_담기_요청.statusCode()).isEqualTo(HttpStatus.CREATED.value());
                softly.assertThat(회원A_장바구니_상품_담기_요청.header("Location")).contains("/cart-items/");
            });
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
    }

    @Nested
    @DisplayName("장바구니 상품 수량 변경 시")
    class updateCartItemQuantity {

        @Test
        @DisplayName("사용자의 장바구니 상품 수량을 변경할 수 있다.")
        void success() {
            // when
            ExtractableResponse<Response> 회원A_장바구니_상품1_수량_변경_요청 = 회원A_장바구니_상품1_수량_변경_요청();

            // then
            assertSoftly(softly -> {
                softly.assertThat(회원A_장바구니_상품1_수량_변경_요청.statusCode()).isEqualTo(HttpStatus.OK.value());
            });
        }

        @Test
        @DisplayName("사용자의 장바구니 상품 수량을 0개로 변경 시 상품을 삭제한다.")
        void remove_cartItem_when_quantity_0() {
            // when
            ExtractableResponse<Response> 회원A_장바구니_상품_담기_요청 = 회원A_장바구니_상품_담기_요청();

            // then
            assertSoftly(softly -> {
                softly.assertThat(회원A_장바구니_상품_담기_요청.statusCode()).isEqualTo(HttpStatus.CREATED.value());
                softly.assertThat(회원A_장바구니_상품_담기_요청.header("Location")).contains("/cart-items/");
            });
        }

        @Nested
        @DisplayName("장바구니 상품 수량 변경이 실패하는 경우")
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

            @Test
            @DisplayName("해당 장바구니 상품이 존재하지 않는 경우 실패한다")
            void not_exist_cartItem() {
                // given

                // when

                // then
            }

            @Test
            @DisplayName("수량을 0개 이하로 변경하는 경우 실패한다")
            void update_less_than_0() {
                // given

                // when

                // then
            }
        }
    }

    private ExtractableResponse<Response> 회원A_장바구니_상품_목록_조회_요청() {
        return RestAssured.given().log().all()
                .auth().preemptive().basic(MEMBER_A.EMAIL, MEMBER_A.PASSWORD)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/cart-items")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 사용자를_선택하지_않은_장바구니_상품_목록_조회_요청() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/cart-items")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 올바르지_않은_인증_방식_장바구니_상품_목록_조회_요청() {
        return RestAssured.given().log().all()
                .header("Authorization", "abcd")
                .when()
                .get("/cart-items")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 없는_사용자_장바구니_상품_목록_조회_요청() {
        return RestAssured.given().log().all()
                .auth().preemptive().basic("notExist", "notExist")
                .when()
                .get("/cart-items")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 회원A_장바구니_상품_담기_요청() {
        CartItemRequest cartItemRequest = new CartItemRequest(PIZZA.ID);

        return RestAssured.given().log().all()
                .auth().preemptive().basic(MEMBER_A.EMAIL, MEMBER_A.PASSWORD)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(cartItemRequest)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/cart-items")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 사용자를_선택하지_않은_장바구니_상품_담기_요청() {
        CartItemRequest cartItemRequest = new CartItemRequest(PIZZA.ID);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(cartItemRequest)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/cart-items")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 올바르지_않은_인증_방식_장바구니_상품_담기_요청() {
        CartItemRequest cartItemRequest = new CartItemRequest(PIZZA.ID);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "abcd")
                .body(cartItemRequest)
                .when()
                .get("/cart-items")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 없는_사용자_장바구니_상품_담기_요청() {
        CartItemRequest cartItemRequest = new CartItemRequest(PIZZA.ID);

        return RestAssured.given().log().all()
                .auth().preemptive().basic("notExist", "notExist")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(cartItemRequest)
                .when()
                .get("/cart-items")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 회원A_장바구니_상품1_수량_변경_요청() {
        CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(10);

        return RestAssured.given().log().all()
                .auth().preemptive().basic(MEMBER_A.EMAIL, MEMBER_A.PASSWORD)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/cart-items/" + MEMBER_A_CART_ITEM1.ID)
                .then().log().all()
                .extract();
    }
}
