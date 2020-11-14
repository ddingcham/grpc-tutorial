# gRPC Overview

## summary
> https://chaewonkong.github.io/posts/grpc.html
```
구글이 최초로 개발한 오픈 소스 원격 프로시저 호출 (RPC) 시스템이다. 
전송을 위해 HTTP/2를, 
인터페이스 정의 언어로 프로토콜 버퍼를 사용하며 인증, 
양방향 스트리밍 및 흐름 제어, 
차단 및 비차단 바인딩, 
취소 및 타임아웃 등의 기능을 제공한다. 
수많은 언어를 대상으로 크로스 플랫폼 클라이언트 및 서버 바인딩을 생성한다.
(wiki)
```
* HTTP/2
> https://developers.google.com/web/fundamentals/performance/http2?hl=ko
```
HTTP/2의 주요 목표는 전체 요청을 통해 지연 시간을 줄이고, 
응답 다중화를 지원하며, 
HTTP 헤더 필드의 효율적 압축을 통해 프로토콜 오버헤드를 최소화하고, 
요청 우선순위 지정을 추가하며, 
서버 푸시를 지원하는 것입니다.
``` 
  * 주로 request 라는 트리거만 지원하는 HTTP/1.1의 한계와 연관된 듯 (커넥션 재사용)  
    * terms    
      * 스트림: 구성된 연결 내에서 전달되는 바이트의 양방향 흐름이며, 하나 이상의 메시지가 전달될 수 있습니다.  
      * 메시지: 논리적 요청 또는 응답 메시지에 매핑되는 프레임의 전체 시퀀스입니다.  
      * 프레임: HTTP/2에서 통신의 최소 단위이며 각 최소 단위에는 하나의 프레임 헤더가 포함됩니다.    
            이 프레임 헤더는 최소한으로 프레임이 속하는 스트림을 식별합니다.  
    * 모든 통신은 단일 TCP 연결을 통해 수행되며 전달될 수 있는 양방향 스트림의 수는 제한이 없습니다.  
    * 각 스트림에는 양방향 메시지 전달에 사용되는 고유 식별자와 우선순위 정보(선택 사항)가 있습니다.  
    * 각 메시지는 하나의 논리적 HTTP 메시지(예: 요청 또는 응답)이며 하나 이상의 프레임으로 구성됩니다.  
    * 프레임은 통신의 최소 단위이며 특정 유형의 데이터(예: HTTP 헤더, 메시지 페이로드 등)를 전달합니다.   
     다른 스트림들의 프레임을 인터리빙한 다음, 각 프레임의 헤더에 삽입된 스트림 식별자를 통해 이 프레임을 다시 조립할 수 있습니다.
       
    * 일단 컨셉에 대한 이미지는 이런 느낌  
     ![일단 컨셉에 대한 이미지는 이런 느낌](https://developers.google.com/web/fundamentals/performance/http2/images/multiplexing01.svg?hl=ko)  
     > 이 스냅샷은 동일한 연결 내의 여러 스트림을 캡처한 것입니다.
  
  * 주요 특징 정리   
    * Header Compression  
      > Header Table 과 Huffman Encoding 기법을 통해, 헤더 정보 압축    
      > 주어진 문자열을 트리를 이용해 2진수로 압축하는 알고리즘  
    * Multiplexed Streams  
      > 하나의 커넥션으로 동시에 여러 개의 메시지 전송 가능  
    * Server Push  
      > 클라이언트의 요청 없이도, 서버가 리소스 전송 가능    
    * Stream Priority  
      > 요청에 우선순위를 지정하여 중요한 리소스를 먼저 처리  
  
* Protocol Buffer
> Serialized Data Structure (like JSON/XML, But Lightweight)
> JSON/XML 이랑 비교해서, 기계에 더 친화 (가벼움을 택한 만큼 ..) 
* Stream
* Stub
> 서버와 클라이언트는 서로 다른 주소 공간을 사용 하므로, 함수 호출에 사용된 매개 변수를 꼭 변환해줘야 합니다. 
> 안그러면 메모리 매개 변수에 대한 포인터가 다른 데이터를 가리키게 될 테니까요. 이 변환을 담당하는게 스텁입니다.    
> [출처] [네이버클라우드 기술&경험] 시대의 흐름, gRPC 깊게 파고들기 #1|작성자 NAVER CLOUD PLATFORM

## gRPC & HTTP API (JSON)
> https://docs.microsoft.com/ko-kr/aspnet/core/grpc/comparison?view=aspnetcore-3.1  
* contract  
  * gRPC : Required (.proto)  
  * HTTP APIs with JSON : Optional (OpenAPI)   
* protocol  
  * gRPC : HTTP/2  
  * HTTP APIs with JSON : HTTP   
* payload  
  * gRPC : Protobuf (small, binary)  
  * HTTP APIs with JSON : JSON (large, human readable)   
* Prescriptiveness (규범)  
  * gRPC : strict specification  
    > 메시지 시그니처에 대한 코드 레벨에서의 제약을 말하는 듯, return/argument 이런 것들  
    > IDL : 함수명, 인자, 반환값에 대한 데이터형이 정의된 IDL 파일을 rpcgen 로 컴파일하면 stub code가 자동으로 생성  
      
  * HTTP APIs with JSON : Loose. Any HTTP is valid   
* Streaming  
  * gRPC : Client, server, bi-directional (양/단방향 둘다 지원)  
  * HTTP APIs with JSON : Client, server   
* Browser support  
  * gRPC : No (requires grpc-web)   
  * HTTP APIs with JSON : Yes    
* Security   
  * gRPC : Transport (TLS)   
  * HTTP APIs with JSON : Transport (TLS)   
  * Transport Layer Security (TLS)   
    ```
    전송 계층 보안(영어: Transport Layer Security, TLS, 
    과거 명칭: 보안 소켓 레이어/Secure Sockets Layer, SSL)[1]는 
    컴퓨터 네트워크에 통신 보안을 제공하기 위해 설계된 암호 규약이다.
    
    나중에 볼 것 : https://jusths.tistory.com/135 (gRPC SSL/TLS 3. 실제 구현)
    ```
* Client code-generation  
  * gRPC : Yes   
  * HTTP APIs with JSON : OpenAPI + third-party tooling    

## 좀 더 알고 싶을 때, 링크들  

### Protocol Buffer
> https://bcho.tistory.com/1182?category=76068

### Stream & Channel
> https://bcho.tistory.com/1294  
* Channel
  > gRPC 는 데이터 전송을 위해, HTTP 2.0 기반에 HTTP Streaming 을 활용  
  > 하나의 Connection (TCP)을 맺어놓고, 재활용해서 메시지(데이터)를 전송  
  > 재활용되는 Connection 을 Channel 이라고 표현  
* Stream   
  > 하나의 원격 호출을 Stream 이라고 표현.   
  > 원격 호출이 시작되면, Stream open  
  > 종료되면 Stream close  
* 하나의 Channel 에서의 Stream 들간 동시성 지원   
  > 시간이 오래걸리는 stream(A) 으로 인해, 그 다음 stream(B) 이 지연 되는 시나리오 (channel 점유)   
  > gRPC는 stream들 간 channel 사용에 대해 동시성 제공  
  * **스트림을 패킷 단위로 쪼갠 후에, Round robin 방식으로 번걸아 가면서 전송**  

* Multi channel ?  : 가이드 상으로는, 한 주소로는 1개의 channel 만을 사용하는 걸 권장   
> throughput 을 늘리려고, connection pool 방식으로 여러 채널을 여는 경우도 있지만, 매우 드뭄  

### Stub
> https://blog.naver.com/n_cloudplatform/221751268831  

### IDL
> 함수명, 인자, 반환값에 대한 데이터형이 정의된 IDL 파일을 rpcgen으로 컴파일하면 stub code가 자동으로 생성

# Core Concepts (official doc)
> https://grpc.io/docs/what-is-grpc/core-concepts/  

## Overview
### Service definition  
여러 RPC 시스템처럼, gRPC 도 서비스 정의 아이디어를 기반으로 한다.  
(서비스 정의 : 매개 변수 및 반환 유형을 사용하여 원격으로 호출 할 수 있는 메서드 지정)  
기본적으로, gRPC 는 서비스 인터페이스와 페이로드 메시지의 구조를 설명하기 위해 protocol buffer 를 
IDL (interface definition language)로 사용. (*.proto)  
* eg. hello.proto  
```
message HelloRequest {
  string clientName = 1;
}

message HelloResponse {
  string welcomeMessage = 1;
}

service HelloDdingcham {

  rpc unaryHello(HelloRequest) returns (HelloResponse) {}
}
```  
#### four kinds of service method  
* Unary RPCs  
  : 클라이언트가 서버에 단일 요청을 보내고, 일반 함수 호출처럼 단일 응답을 받는 단항(Unary) RPC  
  ```
  rpc SayHello(HelloRequest) returns (HelloResponse);
  ```  
* Server streaming RPCs  
  : 클라이언트가 서버에 요청을 보내고, 메시지 시퀀스를 다시 읽기 위한 스트림을 가져 오는 서버 스트리밍 RPC   
    클라이언트는 더 이상 메시지가 없을 때까지 반환된 스트림을 읽음 (종료에 대한 뭔가가 있을 듯)   
    gRPC는 개별 RPC 호출 내에서 메시지 순서를 보장 (내부적으로 뭔가 큐 기반 인 듯)    
    **// 반환이 stream 형태**
  ```
  rpc LotsOfReplies(HelloRequest) returns (stream HelloResponse);  
  ```    
* Client streaming RPCs  
  : 클라이언트가 메시지 시퀀스를 작성하고 제공된 스트림을 사용하여 서버로 보내는 클라이언트 스트리밍 RPC  
    클라이언트는 메시지 쓰기를 마치고, 서버가 메시지를 읽고 응답을 반환할 때까지 대기  
    **// 매개변수가 stream 형태**  
    이 때에도, gRPC는 개별 RPC 호출 내에서 메시지 순서를 보장 (내부적으로 뭔가 큐 기반 인 듯)  
  ```
  rpc LotsOfGreetings(stream HelloRequest) returns (HelloResponse);
  ```    
* Bidirectional streaming RPCs  
  : 양측이 읽기-쓰기 스트림을 사용하여 메시지 시퀀스를 전송하는 양방향 스트리밍 RPC  
    두 스트림 (읽기/쓰기) 은 독립적으로 작동하므로 클라이언트와 서버는 읽기/쓰기 순서를 원하는 대로 구현할 수 있음  
    
    (동기 프롬프트 스타일) 
    서버는 응답을 작성하기 전에 모든 클라이언트 메시지를 수신 할 때 까지 
    기다리거나 메시지를 읽은 다음 메시지를 쓸 수 있음.  
    (비동기 채팅 스타일)    
    메시지 읽기와 상관 없이 메시지 작성 가능  
    
    각 스트림의 메시지 순서는 보장됨  
    
  ```
  rpc BidiHello(stream HelloRequest) returns (stream HelloResponse);
  ```    
각 스트림이 순서를 보장한다는 내용이 많은데, 순서를 보장하니까 스트림이라고 표현한 듯  
 
### Using the API  
.proto 파일에 서비스를 정의하는 것으로 부터 시작  
gRPC 가 제공하는 protocol buffer 컴파일러를 사용해서, 클라이언트/서버 측 코드를 생성  
일반적으로 gRPC 사용자는 클라이언트 측에서 생성된 API 를 호출하고, 서버 측에서 해당 API를 구현  
* server side  
  > .proto 에 선언 한 메소드 구현 및 gRPC 서버를 실행하여 클라이언트 호출을 처리  
  * 이 때, gRPC 인프라가 해주는 것들  
    * 수신한 요청 디코딩  (protocol buffer 에 대한 이야기 ?)  
    * 서비스 메서드를 실행 (java 로 해봤을 땐, thread-pool 기반의 executor 로 했었음)  
    * 서비스 응답을 인코딩 (protocol buffer 에 대한 이야기 ?)  
* client side         
  * 클라이언트에는 서비스와 동일한 메서드를 구현하는 stub 이라는 local object 가 있음  
  (일부 언어에서는 stub 대신 client 라는 용어를 선호하기도 함)  
  * stub 을 통해 해당 메서드를 호출하여, 적절한 protocol buffer 메시지 유형으로 호출 매개 변수를 래핑  
  * gRPC 는 요청을 서버로 보내고, 서버의 응답(protocol buffer)을 반환   
* design by contract 이랑 연관지어서 생각해보면 좋을 것 같음  

### Synchronous vs. asynchronous  
* 서버에서 응답이 도착할 때까지 블로킹하는 동기식 RPC 호출  
  (일반적인 로컬 프로시저 호출 흐름이랑 비슷하므로, RPC 가 추구하는 추상화에 가장 가까운 형태)  
* 하지만, 네트워크는 본질적으로 비동기적이므로, 많은 사용 시나리오에서, 스레드를 차단하지 않고 RPC 를 시작하는 게 적합(유용)  
**gRPC 는 대부분의 언어에 대해, 두 가지 호출 방식을 모두 지원**    

* 2 phase commit, eventual consistency  
  > https://www.popit.kr/rest-%EA%B8%B0%EB%B0%98%EC%9D%98-%EA%B0%84%EB%8B%A8%ED%95%9C-%EB%B6%84%EC%82%B0-%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98-%EA%B5%AC%ED%98%84-2%ED%8E%B8-tcc-cancel-timeout/  
                                           
## RPC life cycle  
> gRPC 클라이언트가 gRPC 서버 메서드를 호출 할 때 발생 하는 일들  
> 실제 구현에 대한 세부 사항은 언어별 페이지를 참고하면 됨 (이건 당연한 듯)  
> 여기 패키지를 참고  
> https://github.com/grpc/grpc-java/tree/b0f423295b4674cb5247a6143fd211b050ef0065/core/src/test/java/io/grpc/internal    
### Unary RPC  
1. 클라이언트가 스텁 메서드를 호출하면,  
   해당 호출에 대한 클라이언트의 메타 데이터/메서드 명/deadline(optional) 을 사용하여,   
   RPC 호출을 서버에 알림    

2. 서버는 its own initial metadata 를 즉시 다시 보내거나, 클라이언트의 요청을 대기   
   (its own initial metadata 보내는 시점은 어플리케이션에 따라 다름)  
3. 서버에 클라이언트의 요청 메시지가 있으면, 응답을 위한 작업들을 수행  
   응답을 위한 작업들을 성공한 경우, 클라이언트에 응답을 보냄   
   이 때, status details(status code/optional status message), optional trailing metadata 를 함께 전송  
   
4. 응답 상태가 OK 이면 클라이언트가 응답을 받고, 클라이언트는 호출을 완료 함  
   ( StreamObserver.onCompleted()/.onError()  같은 콜백 제공 )   
### Server streaming RPC  
### Client streaming RPC  
### Bidirectional streaming RPC    

### Terms for RPC life cycle  
* Deadlines/Timeouts  
* RPC termination   
  gRPC 에서는, 클라이언트와 서버는 호출의 성공 여부를 각자의 로컬 상에서 독립적으로 결정함  
  (클라이언트와 서버 같의 호출 성공 여부에 대한 일관성을 보장하지 않음)   
* Cancelling an RPC   
  클라이언트 나 서버는 RPC 를 언제든 지 취소할 수 있음  
  **하지만, 취소 하기전에 변경 된 사항에 대한 롤백을 지원하지 않음**  
  ex) 서버 측은 성공적으로 응답했지만, 클라이언트에서는 해당 요청이 deadline 이후에 처리된 시나리오  
  (이건 HTTP 에서도 똑같은 거고)  
  클라이언트가 모든 요청을 보내기 전에 서버가 완료를 결정할 수도 있음  
  (뭐 인증 같은 거?)  
  
* Metadata   
  키/밸류 형태의 특정 RPC 호출 정보 (ex : 인증 세부 정보)  
  키/밸류는 일반적으로 문자열 이지만 binary 일 수도 있음  
* Channels   
  지정된 호스트 및 포트에서 gRPC 서버에 대한 연결을 제공  
  클라이언트는 스텁 생성 시, 채널을 인수로 활용  
  (메시지 압축 여부 같은 gRPC 의 기본 동작을 설정할 수 있음)  
  채널은 연결중(connected) 나 유휴(idle) 같은 상태에 대한 인터페이스를 제공함   