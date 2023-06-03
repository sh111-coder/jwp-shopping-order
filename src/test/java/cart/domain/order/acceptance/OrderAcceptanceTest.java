package cart.domain.order.acceptance;

import static cart.fixtures.CartItemFixtures.*;
import static cart.fixtures.MemberFixtures.Dooly;
import static cart.fixtures.OrderFixtures.Dooly_Order1;
import static cart.fixtures.OrderFixtures.Dooly_Order2;
import static cart.fixtures.OrderItemFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import cart.AcceptanceTest;
import cart.domain.cartitem.domain.CartItem;
import cart.domain.member.application.dto.MemberCashChargeRequest;
import cart.domain.order.application.dto.OrderRequest;
import cart.domain.order.domain.dto.OrderCartItemDto;
import cart.domain.product.domain.Product;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderAcceptanceTest extends AcceptanceTest {

    @Nested
    @DisplayName("장바구니에 담긴 상품을 주문할 때")
    class orderCartItem {

        @Test
        @DisplayName("인증 정보가 없으면 예외가 발생한다.")
        void throws_when_not_found_authentication() {
            // given
            OrderRequest request = Dooly_Order1.REQUEST();

            // when
            Response response = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .post("/orders")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("인증 정보가 존재하지 않습니다.")
            );
        }

        @Test
        @DisplayName("Authorization 헤더의 값이 Basic으로 시작하지 않으면 예외가 발생한다.")
        void throws_when_authorization_not_start_basic() {
            // given
            OrderRequest request = Dooly_Order1.REQUEST();

            // when
            Response response = RestAssured.given().log().all()
                    .header(HttpHeaders.AUTHORIZATION, "NO BASIC")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .post("/orders")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("BASIC 인증 정보가 존재하지 않습니다. PREFIX로 BASIC을 넣어주세요.")
            );
        }

        @Test
        @DisplayName("인증된 사용자가 아니면 예외가 발생한다.")
        void throws_when_not_authentication_user() {
            // given
            String email = "notExist@email.com";
            String password = "notExistPassword";
            OrderRequest request = Dooly_Order1.REQUEST();

            // when
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(email, password)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .post("/orders")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("인증된 사용자가 아닙니다.")
            );
        }

        @Nested
        @DisplayName("인증된 사용자와 사용자의 장바구니 아이디 리스트를 담아서 요청할 시")
        class requestWithAuthMemberAndCartItemIds {

            @Test
            @DisplayName("요청 받은 장바구니 상품 아이디 중 하나라도 해당 사용자의 장바구니 상품이 아니면 예외가 발생한다.")
            void throws_when_request_cartItemIds_not_member_cartItem() {
                // given
                CartItem cartItem1 = Dooly_CartItem1.ENTITY();
                Product product1 = Dooly_CartItem1.PRODUCT;
                CartItem cartItem2 = Ber_CartItem1.ENTITY();
                Product product2 = Ber_CartItem1.PRODUCT;

                List<OrderCartItemDto> orderCartItemDtos = List.of(
                        new OrderCartItemDto(cartItem1.getId(), product1.getName(), product1.getPrice(), product1.getImageUrl()),
                        new OrderCartItemDto(cartItem2.getId(), product2.getName(), product2.getPrice(), product2.getImageUrl())
                );
                OrderRequest request = new OrderRequest(orderCartItemDtos);

                // when
                Response response = RestAssured.given().log().all()
                        .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(request)
                        .post("/orders")
                        .then().log().all()
                        .extract().response();

                // then
                assertAll(
                        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                        () -> assertThat(response.getBody().asString()).isEqualTo("장바구니 상품에 없는 상품입니다.")
                );
            }

            @Test
            @DisplayName("총 주문 금액보다 사용자의 현재 잔액이 적다면 예외가 발생한다.")
            void throws_when_current_member_cash_less() {
                // given
                OrderRequest request =  Dooly_Order1.REQUEST();

                // when
                Response response = RestAssured.given().log().all()
                        .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(request)
                        .post("/orders")
                        .then().log().all()
                        .extract().response();

                // then
                assertAll(
                        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                        () -> assertThat(response.getBody().asString()).isEqualTo("사용자의 현재 금액이 주문 금액보다 적습니다.")
                );
            }

            @Nested
            @DisplayName("주문 시 상품 정보가 변경될 때")
            class updateProductInfo {

                @Test
                @DisplayName("상품 이름이 변경되면 예외가 발생한다.")
                void throws_when_update_productName() {
                    // given
                    OrderRequest orderRequest = Dooly_Order1.UPDATE_NAME_REQUEST();

                    // when
                    Response response = RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(orderRequest)
                            .post("/orders")
                            .then().log().all()
                            .extract().response();

                    // then
                    assertAll(
                            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                            () -> assertThat(response.getBody().asString()).isEqualTo("주문 상품 정보와 기존 상품 정보가 일치하지 않습니다.")
                    );
                }

                @Test
                @DisplayName("상품 가격이 변경되면 예외가 발생한다.")
                void throws_when_update_productPrice() {
                    // given
                    OrderRequest orderRequest = Dooly_Order1.UPDATE_PRICE_REQUEST();

                    // when
                    Response response = RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(orderRequest)
                            .post("/orders")
                            .then().log().all()
                            .extract().response();

                    // then
                    assertAll(
                            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                            () -> assertThat(response.getBody().asString()).isEqualTo("주문 상품 정보와 기존 상품 정보가 일치하지 않습니다.")
                    );
                }

                @Test
                @DisplayName("상품 이미지 URL이 변경되면 예외가 발생한다.")
                void throws_when_update_productImageUrl() {
                    // given
                    OrderRequest orderRequest = Dooly_Order1.UPDATE_IMAGE_URL_REQUEST();

                    // when
                    Response response = RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(orderRequest)
                            .post("/orders")
                            .then().log().all()
                            .extract().response();

                    // then
                    assertAll(
                            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                            () -> assertThat(response.getBody().asString()).isEqualTo("주문 상품 정보와 기존 상품 정보가 일치하지 않습니다.")
                    );
                }
            }

            @Nested
            @DisplayName("정상적으로 주문이 될 때")
            class successOrder {

                @Test
                @DisplayName("주문이 성공한다.")
                void success() {
                    // given
                    int cashToCharge = 150000;
                    MemberCashChargeRequest chargeRequest = new MemberCashChargeRequest(cashToCharge);
                    OrderRequest orderRequest = Dooly_Order1.REQUEST();
                    String emptyBody = "";

                    // when
                    RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(chargeRequest)
                            .post("/members/cash")
                            .then().log().all();

                    Response response = RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(orderRequest)
                            .post("/orders")
                            .then().log().all()
                            .extract().response();

                    // then
                    assertAll(
                            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED.value()),
                            () -> assertThat(response.getHeader("Location")).contains("/orders/"),
                            () -> assertThat(response.getBody().asString()).isEqualTo(emptyBody)
                    );
                }

                @Test
                @DisplayName("사용자의 현재 금액에서 총 주문 금액을 차감한다.")
                void payCash() {
                    // given
                    int cashToCharge = 150000;
                    MemberCashChargeRequest chargeRequest = new MemberCashChargeRequest(cashToCharge);
                    OrderRequest orderRequest =  Dooly_Order1.REQUEST();
                    int chargedCash = Dooly.CASH + cashToCharge;
                    int totalPrice = Dooly_CartItem1.PRICE + Dooly_CartItem2.PRICE;
                    int cashAfterOrder = chargedCash - totalPrice;

                    // when
                    RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(chargeRequest)
                            .post("/members/cash")
                            .then().log().all();

                    RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(orderRequest)
                            .post("/orders")
                            .then().log().all();

                    Response response = RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .get("/members/cash")
                            .then().log().all()
                            .extract().response();

                    // then
                    assertAll(
                            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(response.jsonPath().getInt("totalCash")).isEqualTo(cashAfterOrder)
                    );
                }

                @Test
                @DisplayName("주문이 되면 사용자의 장바구니 상품에서 주문한 장바구니 상품을 삭제한다.")
                void deleteMemberCartItem() {
                    // given
                    int cashToCharge = 150000;
                    MemberCashChargeRequest chargeRequest = new MemberCashChargeRequest(cashToCharge);
                    OrderRequest orderRequest =  Dooly_Order1.REQUEST();

                    RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(chargeRequest)
                            .post("/members/cash")
                            .then().log().all();

                    RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(orderRequest)
                            .post("/orders")
                            .then().log().all();

                    Response response = RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .get("/cart-items")
                            .then().log().all()
                            .extract().response();
                    JsonPath jsonPath = response.jsonPath();

                    assertThat(jsonPath.getString("[0].product.name")).isNotEqualTo(Dooly_CartItem2.PRODUCT.getName());
                }
            }
        }
    }

    @Nested
    @DisplayName("주문 목록 조회 시")
    class showOrders {

        @Test
        @DisplayName("인증 정보가 없으면 예외가 발생한다.")
        void throws_when_not_found_authentication() {
            // when
            Response response = RestAssured.given().log().all()
                    .get("/orders")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("인증 정보가 존재하지 않습니다.")
            );
        }

        @Test
        @DisplayName("Authorization 헤더의 값이 Basic으로 시작하지 않으면 예외가 발생한다.")
        void throws_when_authorization_not_start_basic() {
            // when
            Response response = RestAssured.given().log().all()
                    .header(HttpHeaders.AUTHORIZATION, "NO BASIC")
                    .get("/orders")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("BASIC 인증 정보가 존재하지 않습니다. PREFIX로 BASIC을 넣어주세요.")
            );
        }

        @Test
        @DisplayName("인증된 사용자가 아니면 예외가 발생한다.")
        void throws_when_not_authentication_user() {
            // given
            String email = "notExist@email.com";
            String password = "notExistPassword";

            // when
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(email, password)
                    .get("/orders")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("인증된 사용자가 아닙니다.")
            );
        }

        @Test
        @DisplayName("해당 사용자가 주문한 상품 목록이 조회된다.")
        void success() {
            // when
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                    .get("/orders")
                    .then().log().all()
                    .extract().response();
            JsonPath jsonPath = response.jsonPath();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value()),

                    () -> assertThat(jsonPath.getLong("orders[0].orderId")).isEqualTo(Dooly_Order2.ID),
                    () -> assertThat(jsonPath.getString("orders[0].orderedDateTime")).matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"),
                    () -> assertThat(jsonPath.getInt("orders[0].totalPrice")).isEqualTo(Dooly_Order2.TOTAL_PRICE),

                    () -> assertThat(jsonPath.getLong("orders[0].products[0].product.id")).isEqualTo(Dooly_Order_Item3.PRODUCT_ID),
                    () -> assertThat(jsonPath.getString("orders[0].products[0].product.name")).isEqualTo(Dooly_Order_Item3.NAME),
                    () -> assertThat(jsonPath.getInt("orders[0].products[0].product.price")).isEqualTo(Dooly_Order_Item3.PRICE),
                    () -> assertThat(jsonPath.getString("orders[0].products[0].product.imageUrl")).isEqualTo(Dooly_Order_Item3.IMAGE_URL),
                    () -> assertThat(jsonPath.getInt("orders[0].products[0].quantity")).isEqualTo(Dooly_Order_Item3.QUANTITY),

                    () -> assertThat(jsonPath.getLong("orders[1].orderId")).isEqualTo(Dooly_Order1.ID),
                    () -> assertThat(jsonPath.getString("orders[1].orderedDateTime")).matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"),
                    () -> assertThat(jsonPath.getInt("orders[1].totalPrice")).isEqualTo(Dooly_Order1.TOTAL_PRICE),

                    () -> assertThat(jsonPath.getLong("orders[1].products[0].product.id")).isEqualTo(Dooly_Order_Item2.PRODUCT_ID),
                    () -> assertThat(jsonPath.getString("orders[1].products[0].product.name")).isEqualTo(Dooly_Order_Item2.NAME),
                    () -> assertThat(jsonPath.getInt("orders[1].products[0].product.price")).isEqualTo(Dooly_Order_Item2.PRICE),
                    () -> assertThat(jsonPath.getString("orders[1].products[0].product.imageUrl")).isEqualTo(Dooly_Order_Item2.IMAGE_URL),
                    () -> assertThat(jsonPath.getInt("orders[1].products[0].quantity")).isEqualTo(Dooly_Order_Item2.QUANTITY),

                    () -> assertThat(jsonPath.getLong("orders[1].products[1].product.id")).isEqualTo(Dooly_Order_Item1.PRODUCT_ID),
                    () -> assertThat(jsonPath.getString("orders[1].products[1].product.name")).isEqualTo(Dooly_Order_Item1.NAME),
                    () -> assertThat(jsonPath.getInt("orders[1].products[1].product.price")).isEqualTo(Dooly_Order_Item1.PRICE),
                    () -> assertThat(jsonPath.getString("orders[1].products[1].product.imageUrl")).isEqualTo(Dooly_Order_Item1.IMAGE_URL),
                    () -> assertThat(jsonPath.getInt("orders[1].products[1].quantity")).isEqualTo(Dooly_Order_Item1.QUANTITY)
            );
        }
    }

    @Nested
    @DisplayName("주문 상세 조회 시")
    class showOrder {

        @Test
        @DisplayName("인증 정보가 없으면 예외가 발생한다.")
        void throws_when_not_found_authentication() {
            // when
            Response response = RestAssured.given().log().all()
                    .get("/orders/1")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("인증 정보가 존재하지 않습니다.")
            );
        }

        @Test
        @DisplayName("Authorization 헤더의 값이 Basic으로 시작하지 않으면 예외가 발생한다.")
        void throws_when_authorization_not_start_basic() {
            // when
            Response response = RestAssured.given().log().all()
                    .header(HttpHeaders.AUTHORIZATION, "NO BASIC")
                    .get("/orders/1")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("BASIC 인증 정보가 존재하지 않습니다. PREFIX로 BASIC을 넣어주세요.")
            );
        }

        @Test
        @DisplayName("인증된 사용자가 아니면 예외가 발생한다.")
        void throws_when_not_authentication_user() {
            // given
            String email = "notExist@email.com";
            String password = "notExistPassword";

            // when
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(email, password)
                    .get("/orders/1")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("인증된 사용자가 아닙니다.")
            );
        }

        @Test
        @DisplayName("주문 ID에 해당하는 주문이 없으면 예외가 발생한다.")
        void throws_when_order_not_found() {
            // given
            Long notExistId = -1L;

            // when
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                    .get("/orders/" + notExistId)
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("주문 ID에 해당하는 주문을 찾을 수 없습니다.")
            );
        }

        @Test
        @DisplayName("정상적으로 해당 주문이 조회된다.")
        void success() {
            // given
            Long orderId = Dooly_Order1.ID;

            // when
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                    .get("/orders/" + orderId)
                    .then().log().all()
                    .extract().response();
            JsonPath jsonPath = response.jsonPath();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value()),

                    () -> assertThat(jsonPath.getLong("orderId")).isEqualTo(Dooly_Order1.ID),
                    () -> assertThat(jsonPath.getString("orderedDateTime")).matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"),
                    () -> assertThat(jsonPath.getInt("totalPrice")).isEqualTo(Dooly_Order1.TOTAL_PRICE),

                    () -> assertThat(jsonPath.getLong("products[0].product.id")).isEqualTo(Dooly_Order_Item2.PRODUCT_ID),
                    () -> assertThat(jsonPath.getString("products[0].product.name")).isEqualTo(Dooly_Order_Item2.NAME),
                    () -> assertThat(jsonPath.getInt("products[0].product.price")).isEqualTo(Dooly_Order_Item2.PRICE),
                    () -> assertThat(jsonPath.getString("products[0].product.imageUrl")).isEqualTo(Dooly_Order_Item2.IMAGE_URL),

                    () -> assertThat(jsonPath.getLong("products[1].product.id")).isEqualTo(Dooly_Order_Item1.PRODUCT_ID),
                    () -> assertThat(jsonPath.getString("products[1].product.name")).isEqualTo(Dooly_Order_Item1.NAME),
                    () -> assertThat(jsonPath.getInt("products[1].product.price")).isEqualTo(Dooly_Order_Item1.PRICE),
                    () -> assertThat(jsonPath.getString("products[1].product.imageUrl")).isEqualTo(Dooly_Order_Item1.IMAGE_URL)
            );
        }
    }
}
