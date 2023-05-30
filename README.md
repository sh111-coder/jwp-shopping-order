# 🛍️ 장바구니 Project

## 📘 장바구니 API 명세서
[장바구니 API 명세](http://43.200.181.131:8080/docs/index.html)

---


## 🎯 사용자 시나리오

## 1. 사용자(Member)
- [x] 사용자의 금액을 충전할 수 있다.
  - 인증된 사용자가 아니면 예외가 발생한다.
  - 인증된 사용자와 충전할 금액을 담아서 요청한다.
    - 충전할 금액이 0 이하이면 예외가 발생한다.
    - 충전한 금액을 더한 사용자의 금액을 반환한다.

<br>

- [ ] 사용자의 현재 금액을 확인할 수 있다.
  - 인증된 사용자가 아니면 예외가 발생한다.
  - 사용자의 현재 잔액을 반환한다.

<br>

## 2. 상품(Product)

- TODO : 인증 정보가 없으면 예외가 발생한다.

- [x] 상품 추가를 할 수 있다.
  - TODO : 관리자가 아니면 예외가 발생한다.
  - 추가할 상품 정보를 담아서 요청한다.
    - 상품 정보인 상품 이름과 상품 가격, 상품 이미지 URL을 검증한다.
      - 이름, 이미지 URL 중 하나라도 빈 값이면 예외가 발생한다. 
      - 이름이 한글과 영어가 아니면 예외가 발생한다.
      - 가격이 0이하이면 예외가 발생한다.
      - 이미지 URL 형식이 아니면 예외가 발생한다.
    - 정상적으로 상품이 추가된다.

<br>

- [x] 상품 삭제를 할 수 있다.
  - TODO : 관리자가 아니면 예외가 발생한다.
  - 삭제할 상품 ID를 담아서 요청한다.
    - 삭제할 상품 ID가 존재하지 않으면 예외가 발생한다.
    - 삭제할 상품 ID가 존재하면 정상적으로 상품이 삭제된다.

<br>

- [x] 상품 수정을 할 수 있다.
  - TODO : 관리자가 아니면 예외가 발생한다.
  - 수정할 상품 ID와 상품 정보를 담아서 요청한다.
    - 수정할 상품 ID에 해당하는 상품이 존재하지 않으면 예외가 발생한다.
    - 수정할 상품 ID에 해당하는 상품이 존재하면 상품 정보를 수정할 수 있다.
        - 수정할 상품명이 한글과 영어가 아니면 예외가 발생한다.
        - 가격이 숫자가 아니면 예외가 발생한다.
        - 가격이 0이하이면 예외가 발생한다.
        - 이미지 URL 형식이 아니면 예외가 발생한다.
        - 정상적으로 상품이 수정된다.

<br>

- [x] 상품 목록 조회를 할 수 있다.
    - 인증된 사용자가 아니면 예외가 발생한다.
    - 장바구니에 담긴 상품이면 장바구니에 담긴 수량도 조회된다.
    - 장바구니에 담기지 않은 상품이면 장바구니에 담긴 수량은 조회되지 않는다.
    - 조회한 상품이 마지막 상품인지 알 수 있다.

<br>

- [x] 상품 상세 조회를 할 수 있다.
    - 인증된 사용자가 아니면 예외가 발생한다.
    - 조회할 상품 ID에 해당하는 상품이 존재하지 않으면 예외가 발생한다. 
    - 조회할 상품 ID에 해당하는 상품이 존재한다면 상세 조회가 된다.
        - 장바구니에 담긴 상품이면 장바구니에 담긴 수량도 조회된다.
        - 장바구니에 담기지 않은 상품이면 장바구니에 담긴 수량은 조회되지 않는다.

<br>

## 3. 장바구니(Cart-Item)

- 인증 정보가 없으면 예외가 발생한다.

- [x] 장바구니에 상품을 담을 수 있다.
  - 인증된 사용자가 아니면 예외가 발생한다.
  - 추가할 상품 ID와 추가할 수량을 담아서 요청한다.
    - 상품 ID에 해당하는 상품이 존재하지 않으면 예외가 발생한다.
    - 추가할 수량이 1 이상의 정수가 아니면 예외가 발생한다.
    - 사용자의 장바구니에 이미 해당 상품 ID가 존재하면 수량을 합쳐서 업데이트한다.
    - 정상적으로 장바구니에 상품이 담긴다.

<br>

- [x] 장바구니의 상품 수량을 수정할 수 있다.
  - 인증된 사용자가 아니면 예외가 발생한다.
  - 수량을 수정할 상품 ID와 수정할 수량을 담아서 요청한다.
    - 수량을 수정할 상품 ID에 해당하는 상품이 존재하지 않으면 예외가 발생한다.
    - 수정할 수량이 0 이상의 정수가 아니면 예외가 발생한다.
    - 수정할 상품 수량이 0이면 상품을 삭제한다.
    - 사용자가 가진 장바구니 상품이 아니면 예외가 발생한다.
    - 정상적으로 장바구니의 상품 수량이 수정된다.

<br>

- [x] 장바구니에 담긴 상품을 삭제할 수 있다.
  - 인증된 사용자가 아니면 예외가 발생한다.
  - 삭제할 장바구니에 담긴 상품 ID를 담아서 요청한다.
    - 장바구니에 담긴 상품 ID에 해당하는 상품이 존재하지 않으면 예외가 발생한다.
    - 사용자가 가진 장바구니 상품이 아니면 예외가 발생한다.
    - 정상적으로 장바구니에 담긴 상품이 삭제된다.

<br>

- [x] 장바구니에 담긴 상품 목록 조회를 할 수 있다.
  - 인증된 사용자가 아니면 예외가 발생한다.
  - 장바구니에 담긴 상품 정보 목록이 조회된다.

<br>

## 4. 결제 & 주문 (Order)

- [ ] 장바구니에 담긴 상품을 주문할 수 있다.
  - 인증된 사용자가 아니면 예외가 발생한다.
  - 인증된 사용자와 사용자의 장바구니 상품 아이디 리스트를 담아서 요청한다.
    - 요청 받은 장바구니 상품 아이디 중 하나라도 해당 사용자의 장바구니 상품이 아니면 예외가 발생한다.
    - 총 주문 금액보다 사용자의 현재 잔액이 적다면 예외가 발생한다.
    - 정상적으로 주문이 된다.
      - 주문되면 사용자의 현재 금액에서 총 주문 금액을 차감한다.
      - 주문되면 사용자의 장바구니 상품에서 주문한 장바구니 상품을 삭제한다.
      - 주문되면 주문 목록에 주문한 장바구니 상품들이 추가된다.

<br>


- [ ] 특정 주문의 상세 정보를 조회할 수 있다.
  - 인증된 사용자가 아니면 예외가 발생한다.
  - 인증된 사용자와 주문 ID를 담아서 요청한다.
    - 주문 ID에 해당하는 주문이 없으면 예외가 발생한다.
    - 주문 ID에 해당하는 주문이 있으면 해당 주문의 정보가 조회된다.


<br>

- [ ] 주문 목록을 조회할 수 있다.
  - 인증된 사용자가 아니면 예외가 발생한다.
  - 인증된 사용자를 담아서 요청한다.
    - 해당 사용자가 주문한 상품 목록이 조회된다.

<br>

---

## 🎯DB ERD

![장바구니 ERD](https://github.com/woowacourse/jwp-shopping-order/assets/95729738/724d15b4-edbd-4935-80c3-1f8b39daa080)

