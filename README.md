# 🛍️ 장바구니 주문 애플리케이션

---

## 🎯 기능 목록

### ✅ 장바구니 (CartItem)

- [ ] 모든 기능에서 인증되지 않은 사용자면 예외가 발생한다.
  - Authorization 헤더가 존재하지 않는 경우
  - Authorization 헤더에 'basic' prefix가 존재하지 않는 경우
  - Authorization 헤더에 담긴 사용자가 존재하지 않는 경우

### 1. 장바구니 상품 추가 기능
  - [ ] 사용자와 상품 ID를 받아서 사용자의 장바구니 상품을 추가한다.
    - [ ] 상품 ID에 해당하는 상품이 없으면 예외가 발생한다.


### 2. 장바구니 상품 수량 변경 기능
  - [ ] 사용자와 변경할 장바구니 상품 수량, 장바구니 상품 ID를 받아서 사용자의 장바구니 상품 수량을 변경한다.
    - [ ] 변경할 장바구니 상품 ID에 해당하는 장바구니 상품이 존재하지 않으면 예외가 발생한다.
    - [ ] 변경할 상품 수량이 0 이하이면 예외가 발생한다.

### 3. 장바구니 상품 삭제 기능
  - [ ] 사용자와 삭제할 상품 ID를 받아서 사용자의 장바구니 상품을 삭제한다.
    - [ ] 삭제할 상품 ID에 해당하는 상품이 없으면 예외가 발생한다.

### 4. 장바구니 상품 목록 조회 기능
  - [ ] 사용자의 장바구니 상품 목록을 조회한다.

<br>

### ✅ 주문 (Order)

- [ ] 모든 기능에서 인증되지 않은 사용자면 예외가 발생한다.
  - Authorization 헤더가 존재하지 않는 경우
  - Authorization 헤더에 담긴 사용자가 존재하지 않는 경우

### 1. 주문 기능
  - [ ] 장바구니 상품 ID와 주문 당시 장바구니 상품의 정보를 받아서 해당하는 상품을 주문한다.
    - [ ] 주문 당시 상품 정보와 장바구니에 담겨 있는 상품 정보가 다르면 예외가 발생한다.
    - 쿠폰 적용 시
      - [ ] 쿠폰이 적용된 가격으로 주문된다.
      - [ ] 주문 후에 사용자의 쿠폰을 제거한다.


### 2. 주문 목록 조회 기능
  - [ ] 사용자의 주문 목록을 조회한다.

### 3. 특정 주문 조회 기능
  - [ ] 주문 ID를 받아서 해당 주문 상세를 조회한다.
    - [ ] 주문 ID에 해당하는 주문이 사용자의 주문이 아니면 예외가 발생한다.

---

## 📘 사용자 시나리오

### ✅ 사용자
  - 사용자를 선택하여 메인 화면에 진입한다.

<br>

### ✅ 장바구니 상품

### 1. 장바구니 상품 담기
  - 마음에 드는 상품을 사용자의 장바구니에 담을 수 있다.
  - 장바구니 상품 담기 실패하는 경우
    - 사용자를 선택하지 않은 경우 실패한다.
    - 올바른 인증 방식이 아닌 경우 실패한다.
    - 없는 사용자를 선택한 경우 실패한다.
    - 해당 상품이 존재하지 않는 경우 실패한다.

    
### 2. 장바구니 상품 수량 변경
  - 사용자의 장바구니 상품 수량을 변경할 수 있다.
  - 0개로 변경 시 장바구니 상품을 삭제한다.
  - 장바구니 상품 수량 변경이 실패하는 경우
    - 사용자를 선택하지 않은 경우 실패한다.
    - 올바른 인증 방식이 아닌 경우 실패한다.
    - 없는 사용자를 선택한 경우 실패한다.
    - 해당 장바구니 상품이 존재하지 않는 경우 실패한다.
    - 0개 이하로 변경할 시 실패한다.

### 3. 장바구니 상품 삭제
  - 사용자의 장바구니 상품을 삭제할 수 있다.
  - 장바구니 상품 삭제가 실패하는 경우
    - 사용자를 선택하지 않은 경우 실패한다.
    - 올바른 인증 방식이 아닌 경우 실패한다.
    - 없는 사용자를 선택한 경우 실패한다.
    - 해당 장바구니 상품이 존재하지 않는 경우 실패한다.

### 4. 장바구니 상품 목록 조회
  - 사용자의 장바구니에 담긴 상품 목록을 조회할 수 있다.
  - 장바구니 상품 목록 조회가 실패하는 경우
    - 사용자를 선택하지 않은 경우 실패한다.
    - 올바른 인증 방식이 아닌 경우 실패한다.
    - 없는 사용자를 선택한 경우 실패한다.

<br>

### ✅ 주문

### 1. 장바구니 상품 주문
  - 사용자의 장바구니에 담긴 상품을 주문할 수 있다.
    - 주문 시 쿠폰을 장바구니 상품에 적용하여 할인받을 수 있다.
      - 주문할 때 적용한 쿠폰은 주문 후에 사라진다.
    - 주문이 실패하는 경우
      - 사용자를 선택하지 않은 경우 실패한다.
      - 올바른 인증 방식이 아닌 경우 실패한다.
      - 없는 사용자를 선택한 경우 실패한다.
      - 주문 화면의 장바구니에 담긴 상품 정보와 주문 당시의 장바구니에 담긴 상품 정보가 달라지면 실패한다.

### 2. 주문 목록 조회
  - 사용자가 주문한 목록을 조회할 수 있다.
  - 주문 목록 조회에 실패하는 경우
    - 사용자를 선택하지 않은 경우 실패한다.
    - 올바른 인증 방식이 아닌 경우 실패한다.
    - 없는 사용자를 선택한 경우 실패한다.

### 3. 특정 주문 조회
  - 사용자의 특정 주문을 조회할 수 있다.
  - 특정 주문 조회에 실패하는 경우
    - 사용자를 선택하지 않은 경우 실패한다.
    - 올바른 인증 방식이 아닌 경우 실패한다.
    - 없는 사용자를 선택한 경우 실패한다.
    - 해당 주문이 존재하지 않으면 실패한다.
