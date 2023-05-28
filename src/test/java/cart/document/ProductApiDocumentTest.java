package cart.document;

import static cart.fixtures.CartItemFixtures.Dooly_CartItem1;
import static cart.fixtures.MemberFixtures.Dooly;
import static cart.fixtures.ProductFixtures.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import cart.application.MemberService;
import cart.application.ProductService;
import cart.config.MemberArgumentResolver;
import cart.config.WebMvcConfig;
import cart.dto.AuthMember;
import cart.ui.ProductApiController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;

@AutoConfigureRestDocs
@WebMvcTest(ProductApiController.class)
@ExtendWith(RestDocumentationExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class ProductApiDocumentTest {

    private static final String BASIC_PREFIX = "Basic ";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private WebMvcConfig webMvcConfig;

    private final String encodeAuthInfo = Base64Utils.encodeToString((Dooly.EMAIL + ":" + Dooly.PASSWORD).getBytes());


    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProductApiController(productService))
                .defaultRequest(MockMvcRequestBuilders
                        .get("/products/{id}/cart-items", CHICKEN.ID)
                        .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo))
                .setCustomArgumentResolvers(new MemberArgumentResolver())
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void 모든_상품_목록_조회_문서화() throws Exception {
        // given
        given(productService.getAllProducts())
                .willReturn(List.of(CHICKEN.RESPONSE, SALAD.RESPONSE, PIZZA.RESPONSE));

        // when, then
        mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("products/getAllProducts",
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("[].imageUrl").type(JsonFieldType.STRING).description("상품 이미지 URL"))
                        )
                );
    }

    @Test
    void 특정_상품_조회_장바구니에_담긴_상품_문서화() throws Exception {
        // given
        Long productId = CHICKEN.ID;
        given(productService.findProductCartItems(any(AuthMember.class), eq(productId)))
                .willReturn(Dooly_CartItem1.PRODUCT_CART_ITEM_RESPONSE);


        // when, then
        mockMvc.perform(get("/products/{id}/cart-items", productId)
                        .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("products/getProductCartItems/exist",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("조회할 상품 아이디")
                        ),
                        responseFields(
                                fieldWithPath("product.id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                                fieldWithPath("product.name").type(JsonFieldType.STRING).description("상품 이름"),
                                fieldWithPath("product.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                fieldWithPath("product.imageUrl").type(JsonFieldType.STRING).description("상품 이미지 URL"),
                                fieldWithPath("cartItem.id").type(JsonFieldType.NUMBER).description("장바구니 상품 아이디"),
                                fieldWithPath("cartItem.quantity").type(JsonFieldType.NUMBER).description("장바구니 상품 수량")
                        )
                ));
    }

    @Test
    void 특정_상품_조회_장바구니에_담기지_않은_상품_문서화() throws Exception {
        // given
        Long productId = PANCAKE.ID;
        given(productService.findProductCartItems(any(AuthMember.class), eq(productId)))
                .willReturn(PANCAKE.PRODUCT_CART_ITEM_RESPONSE);

        final String encodeAuthInfo = Base64Utils.encodeToString((Dooly.EMAIL + ":" + Dooly.PASSWORD).getBytes());

        // when, then
        mockMvc.perform(get("/products/{id}/cart-items", productId)
                        .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("products/getProductCartItems/not-exist",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("조회할 상품 아이디")
                        ),
                        responseFields(
                                fieldWithPath("product.id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                                fieldWithPath("product.name").type(JsonFieldType.STRING).description("상품 이름"),
                                fieldWithPath("product.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                fieldWithPath("product.imageUrl").type(JsonFieldType.STRING).description("상품 이미지 URL"),
                                fieldWithPath("cartItem").type(JsonFieldType.NULL).description("장바구니에 담기지 않은 상품 NULL")
                        )
                ));
    }


    @Test
    void 상품_추가_문서화() throws Exception {
        // given
        given(productService.createProduct(any()))
                .willReturn(CHICKEN.ID);

        // when, then
        mockMvc.perform(post("/products")
                        .content(objectMapper.writeValueAsString(CHICKEN.REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("products/createProduct",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지 URL")
                                )
                        )
                );
    }

    @Test
    void 상품_수정_문서화() throws Exception {
        // given
        willDoNothing().given(productService).updateProduct(CHICKEN.ID, CHICKEN.REQUEST);

        // when, then
        mockMvc.perform(put("/products/{id}", CHICKEN.ID)
                        .content(objectMapper.writeValueAsString(CHICKEN.REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("products/updateProduct",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("수정할 상품 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("수정할 상품 이름"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("수정할 상품 가격"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("수정할 상품 이미지 URL")
                        )
                ));
    }

    @Test
    void 상품_삭제_문서화() throws Exception {
        // given
        willDoNothing().given(productService).deleteProduct(CHICKEN.ID);

        // when, then
        mockMvc.perform(delete("/products/{id}", CHICKEN.ID))
                .andExpect(status().isNoContent())
                .andDo(document("products/deleteProduct",
                                pathParameters(
                                        parameterWithName("id").description("삭제할 상품 아이디")
                                )
                        )
                );
    }
}
