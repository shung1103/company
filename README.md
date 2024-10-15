# 기업 과제 2. 재입고 알림 시스템

## 과제 설명

- 상품이 재입고 되었을 때, 재입고 알림을 설정한 유저들에게 재입고 알림을 보내줍니다.
- 재입고 알림을 전송할 때 신청했었던 유저들을 리스트에서 삭제하고 전송 완료 리스트에 저장하는 방식으로 구현
- redis에 bucket4j를 연동하여 rate limit 기능을 구현하여 기술적 요구사항을 충족
    - bucket4j의 데이터는 db에 저장할 필요가 없는 휘발성 데이터이며, 속도를 높이기 위해 redis 사용 

## 구현한 비즈니스 요구 사항

- 재입고 알림을 전송하기 전, 상품의 재입고 회차를 1 증가 시킨다.
- 상품이 재입고 되었을 때, 재입고 알림을 설정한 유저들에게 알림 메시지를 전달해야 한다.
- 재입고 알림은 재입고 알림을 설정한 유저 순서대로 메시지를 전송한다.
    - ProductUserNotification에 재입고 알림을 신청한 유저들을 순서대로 저장
- 회차별 재입고 알림을 받은 유저 목록을 저장해야 한다.
    - ProductNotificationHistory 테이블에 저장
- 재입고 알림을 보내던 중 재고가 모두 없어진다면 알림 보내는 것을 중단합니다.
    - 매번 유저에게 알림을 보낼 때 재고상태(boolean)를 확인하는 방식으로 구현
- 재입고 알림 전송의 상태를 DB 에 저장해야 한다.
    - IN_PROGRESS (발송 중)
        - API를 호출하여 발송을 시작할 때 재입고 알림 전송 상태를 업데이트
    - CANCELED_BY_SOLD_OUT (품절에 의한 발송 중단)
    - CANCELED_BY_ERROR (예외에 의한 발송 중단)
        - try-catch 문을 사용하여 에러에 의한 발송 중단 판별
    - COMPLETED (완료)
- 알림 메시지는 1초에 최대 500개의 요청을 보낼 수 있다.
    - 서드 파티 연동을 하진 않고, ProductNotificationHistory 테이블에 데이터를 저장한다.
    - bucket4j를 redis에 연동하여 rate limit 기능을 구현
- Mysql 조회 시, 인덱스를 잘 탈 수 있게 설계해야 합니다.
- (Optional) 예외에 의해 알림 메시지 발송이 실패한 경우, manual 하게 상품 재입고 알림 메시지를 다시 보내는 API를 호출한다면 마지막으로 전송 성공한 이후 유저부터 다시 알림 메시지를 보낼 수 있어야 한다.
    - 10번째 유저까지 알림 메시지 전송에 성공했다면, 다음 요청에서 11번째 유저부터 알림 메시지를 전송할 수 있어야 한다.


### 재입고 알림 전송 API

- POST  /products/{productId}/notifications/re-stock

### 재입고 알림 전송 API (manual)

- POST /admin/products/{productId}/notifications/re-stock

## **기술 스택**

- Spring Boot 3.3.4 (docker)
- Mysql latest (docker)
- Redis latest (docker)
