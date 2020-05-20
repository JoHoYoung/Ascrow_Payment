### 결제를 할 수 있어야 한다
* 여러 플랫폼
* 직접 결제하지는 않고.. 다른 플랫폼에서 결제의 올바른 응답을 받아와서 처리

### 어디에 결제를 할것인가를 정해야 한다.
### 상품 명..
### 결제 된것은 저장? 큐?
##### 추후 생각

## Group

### 그룹 생성
EndPoint 
```
curl -X POST /api/v1/group/matched -d '{"groupId" : 1, orderIdList:[1,2,3,4]}'
```
Param

* groupId : 생성된 그룹의 id 
* orderIdList : 그룹에 속한 주문의 Id

response
```json
{
    "statusCode":200,
    "statusMsg":"success"
}
```

### 그룹 만료
EndPoint
```
curl -X GET /api/v1/group/expired?groupId=1
```
Param

* groupId : 만료된 그룹의 id

* * *

## Order
### 특정 유저의 주문 리스트
Endpoint
```
curl -X GET /api/v1/order/list/user?userId=1
```
Param
* userId : 유저의 아이디

response
```
{
    "statusCode": 200,
    "statusMsg": "success",
    "data": [
        {
            "id": 16,
            "minShare": 2,
            "maxShare": 0,
            "quantitiy": 0,
            "transAmount": 0,
            "productId": 2,
            "productOptionId": 0,
            "userId": 10,
            "groupId": 1,
            "transactionId": "83e658d1-23d1-4023-b627-e9854fe9510a",
            "state": null,
            "createdAt": 1589783163000,
            "updatedAt": 1589783163000
        },
        {
            "id": 17,
            "minShare": 2,
            "maxShare": 0,
            "quantitiy": 0,
            "transAmount": 0,
            "productId": 2,
            "productOptionId": 0,
            "userId": 10,
            "groupId": 0,
            "transactionId": "2e6f7ac9-bb02-44ea-b694-752c0cb99893",
            "state": null,
            "createdAt": 1589808571000,
            "updatedAt": 1589808571000
        }
    ]
}
```

### 주문
EndPoint 
```
curl -X POST /api/v1/order/execute?payType="KAKAO_PAY" -d ....
```
request body

* Transaction 필드의 경우 openbank일경우 계좌정보등이 들어가고, 카카오페이 네이버페이(유저측에서 이루어지는 결제)의 경우에는 받지 않음
```
{
	"userId":3,
	"productId":2,
	"productOptionId":2,
	"minShare":3,
	"transaction": ""
	"quantity":1
}
```
> 실제 서비스에서 userId는 받지 않고, jwt토큰을 통해 처리. 테스트용에서만 userId받음

response
```
{
    "statusCode":200,
    "statusMsg":"success"
    "data" : 각 페이 플랫폼에서 던져주는 Response
}
```

### 주문 콜백
> 3rd party에서 유저측에서 이루어지는 결제를 던져줌

EndPoint
```
curl -X POST /api/v1/order/transaction/done/{payType}/{transactionId} -d ...
```


### 구매 확정
EndPoint
```
curl -X GET /api/v1/order/confirm?orderId=10
```

Param

* orderId : 구매확정 처리할 구매건의 id

response
```json
{
    "statusCode":200,
    "statusMsg":"success",
}
```

* * *

## Transaction

### 카카오페이 실패 콜백
```
curl -X POST /api/v1/transaction/kakaoPay/cancel/{transactionId}
```
### 카카오페이 취소 콜백
```
curl -X POST /ap1/v1/transaction/kakaoPay/cancel/{transactionId}
```

### 특정 그룹의 모든 유저가 구매확정을 했는지 여부
EndPoint
```
curl -X GET /api/v1/transaction/status/group?groupId=1&numberOfMember=10
```
Param

* groupId : 그룹의 id
* numberOfMember : 그룹의 멤버 수

response
```
{
    statusCode:200,
    statusMsg:"success",
    data : true (or false)
  
}
```
* * *

## User

### 특정 상품을 주문한 유저 리스트
EndPoint
```
curl -X GET /api/v1/user/list/product?productId=1
```
Param

* productId : 상품의 id

response
```
{
    "statusCode": 200,
    "statusMsg": "success",
    "data": [
        {
            "id": 5,
            "name": "HY5",
            "createdAt": 1589975042000,
            "updatedAt": 1589975042000
        },
        {
            "id": 4,
            "name": "HY4",
            "createdAt": 1589975040000,
            "updatedAt": 1589975040000
        },
        {
            "id": 3,
            "name": "HY3",
            "createdAt": 1589975038000,
            "updatedAt": 1589975038000
        },
        {
            "id": 2,
            "name": "HY2",
            "createdAt": 1589975035000,
            "updatedAt": 1589975035000
        }
    ]
}
```
