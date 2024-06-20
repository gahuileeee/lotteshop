# 4조 LotteON 쇼핑몰 프로젝트

### 프로젝트 개요
 1) 프로젝트 주제 : 설계 요구서를 바탕으로 lotte-on 홈 쇼핑 웹 사이트 구현
 2) 프로젝트 기간 : 2024-05-15 ~ 2024-05-17
 3) 배경 및 목적 : 실제 업무 환경과 유사하게, 직접 요구사항 명세서를 분석하고 이를 구현함을 목적으로 한다.
 4) 주요기능 : 쇼핑 (장바구니, 바로 구매 , 찜 , 쿠폰 적용 등) , 검색 , 로그인 , 회원가입 , 관리자 (상품 매출 현황 , 상품 현황, 배너/약관 등록 등)
 5) 팀 구성 : 이가희(조장) , 오아람 , 정원구 , 송상도

기능 시현 영상 : https://www.youtube.com/watch?v=2U5d2RnTh78

### 사용한 기술
Spring Boot, Java, HTML5/CSS3/JavaScript, MyBaits, JPA, AWS, CICD, THYMLEAF

### 프로젝트에서 담당한 기능
조장으로서 프로젝트 배포, 서버 관리, github CI/CD 파이프라인 구축, ERD 정리 및 전반적인 프로젝트 진행사항을 관리하였습니다.
구현한 기능 : Spring Security 구축, 상품 기능(카테고리, 등록, 수정, 찜, 장바구니, 결제, 포인트, 주문내역 보기), 실시간 인기 검색어, 쿠폰 기능, 
관리자 메인화면, 주문 매출 현황, 광고보기 기능, Exception Handler, Login redirect 

### 간단한 기능 구현 설명
:scroll: 스프링 시큐리티 설정


우선 Spring boot- Securityd에 Spring Security 선택하여 의존성을 주입함.
로그인 page와 defaultSeccessUrl 및 로그아웃, 인가 설정 코드를 작성한 SecurityConfig Class를 만들어 @Configuration 처리함.
이후 UserDetailService 를 구현한 Class를 만들어 해당 사용자가 존재하면 인증 객체 생성하도록 하고, UserDetails를 구현한 Class를 만들어 계정이 갖는 권
한 목록, password, userName, 계정 만료 여부 등을 조회 할 method들을 작성해 주었음.
Security Config Class의 인가 설정 코드(requestMatchers("/").permitAll 과 같은) 를 통하여, 최고 관리자, 중간 관리자, 일반 사용자 페이지를 나누었음

:scroll: 서버 및 CICD


AWS 에서 instance 생성 후 , putty 및 file zillar 를 통해 작업한 파일의 jar 파일을 등록 후 먼저 java -jar로 실행, 오류가 없는 것을 확인 후
nohup으로 실행하여 초기 배포를 하였음.
이후 AWS에서 고정 IP 작업 , AWS S3 구축, 필요한 설정 파일 구축 (.github > worklows > deploy.yml , scripts > deploy.sh, appspec.yml) , github
secrets and variables - actions에서 APP_PROPERTIES로 application.yml 파일 등록하여 github으로 CICD를 구축하여 main branch로 commit 하면 배
포까지 이뤄지도록 하였음.
이렇게 하였더니 file 이미지가 웹 사이트에 불러와지지 않는 오류가 생겨, addResourceLocations 위치를 다르게 하여 이를 해결하고,
server가 계속 down 되는 문제가 있어 swap 설정을 하여 이를 해결함.

:scroll: 상품 기능 (카테고리, 등록, 수정 , 찜, 장바구니, 결제, 포인트, 주문내역 ) 


원칙적으로 **카테고리**에 대분류, 중분류, 소분류가 있다면 3 개의 table로 나눠 처리하는 것이 맞아 보이지만 팀원분들 이 이를 다루기 힘들어하여 하나의 talbe 내
에서 처리함. (ex pk:1 , 의류(컬럼1) , 여성(컬럼2) , 신발(컬럼3) ). 따라서 상품 등록 시 카테고리를 출력할 때, 먼저 모든 카테고리의 컬럼1 을 출력 후 set형태로
받아 중복 제거하고, 대분류를 선택하 면 동적으로 select * from 카테고리 where 컬럼1. = 선택한 컬럼 한 후 set으로 중복처리 하여 중분류를 표기하는 식으 로
처리를 함. 옵션 기능의 경우 table을 따로 빼어 (pk, 원래 상품의 pk, option value, 재고량 으로 구성) 처리하였음.


등록된 **상품 수정**의 경우 먼저 새 이미지 등록 여부를 확인하 여 이미지가 수정되지 않으면 기존 이미지가 유지되도록 하여 data 손실을 방지하였음. 또한 설정한 스
프링 시큐리티 를 바탕으로 Authentication에서 로그인 한 사용자 정보를 얻어 role을 바탕으로 최고 관리자의 경우 모든 상품에 대한 정보를, 중간 판매자(selle
r)의 경우 자신이 해당하는 상품만 보고, 수정가능하도록 구현하였음. 또한 기본적으로 어느 카테고리를 눌러도 해당 되는 상품들만 출력되도록 하였음 (paramete
r에 대분류 중분류 소분류를 계속 넣어 처리함),


**찜 기능**의 경우 table을 만들었고, authentication으로 로그인 상태를 확인하여 login을 하지 않았을 시 기본적으로 빈 하트를 출력하고, 하트를 누르면
alert로 로그인 시 이용해 달라고 처리하였음. 로그인을 한 경우 controller에서 해당 페이지 상품 들의 pk값으로 user id와 pk를 통해 찜 table을 확인하여 찜
이 되어 있으면 채워진 하트, 아니면 빈 하트가 되도록 구현함. User가 찜 버튼을 누르면 fetch를 통해 실시간으로 요청을 하여 table의 값을 isnert / delete하
는 식으로 도적 처리를 함.


또한 **장바구니**의 경우 cart table을 만들어 user가 login을 한 상태에서 장바구니를 담으면 해당 상품들의 pk 및 수량을 insert 하였고, 이를 장바구니 페이
지에서 조회하고, user가 수량을 수정하면 fetch를 보내어 즉각적으로 db에 반영하고, front에서는 addEventListener로 동적 처리를 하였음

**결제**의 경우, 우선 장바구니를 통하지 않고 바로 결제를 한 경우 해당 상품들 및 수량을 session에 저장하고, 장바구니를 거친 경우 장바구니 table
을 참조하는 식으로 두 경우를 분리하였음. 기본적인 주문 정보에 대한 유효성 검사 후 결제 완료 시 별도의 api 사용 없이 단순히 DB에 반영 (produ
ct 재고량 변경, 쿠폰 사용시 변경, 포인트 사용시 변경, order talbe insert 및 장바구니 이용시 해당 상품들 delete 등) 후 결제 완료 창으로 넘어
가도록 구현하였음.


그리고 **마이페이지 주문내역**에서 이를 날짜별로 주문 가능하게 하였으며, order table의 상태값 컬럼을 이용하여 구매 확정 시에만 리뷰를 작성할
수 있도록 구현함. 또한 주문취소/환불 기능을 넣어, 환불 사유를 입력받도록 하였고 만약 복합 결제(쿠폰 , 포인트 등을 사용한 결제)의 경우 해당 주
분 건 모두를 환불하도록 함. 이 경우 해당 상품 외에 한불 사유는 모두 복합 결제로 인한 취소/환불 처리 로 넣었음. 처리가 되면 DB에 이를 반영하
였음 (product 재고량 변경, 구매 적립된 포인트 수거, 사용한 쿠폰 돌려주기, 사용한 포인트 돌려주기 등)
이를 **관리자페이지에서 확인가능 하도록 설정**함. 최고 관리자의 경우 모든 주문건을 조회, 중간 관리자의 경우 해당하는 주문건만 조회 가능하도록
하였음. 날짜별로 조회 기능을 넣었으며, 주문 상태 (주문 취소 / 주문 접수 / 환불 / 배송/ 배송 중 등) 에 따른 조회가 가능하도록 구현함.
주문 접수의 경우 판매자가 확인을 클릭하면 (일관 확인 기능도 구현) 배송 상태로 넘어가는 식으로 구현함.


:scroll:실시간 인기 검색어


DB 성능을 위해 캐싱을 처리 해야 하지만, 시간 관계상 기본적 기능만 구현하였음.
먼저 사용자가 검색을 하면, 욕설 filter를 거쳐 DB에 insert or update 함(없는 키워드면 insert, 있는 키워드면 검색량을 ++ 하였음.)
메인 화면에서 10초마다 검색어 table을 검색 순으로 조회하여 가장 많이 검색 된 10개를 상단에 띄우는 방식으로 구현하였음.


:scroll:쿠폰 기능 (등록, 사용)


쿠폰 table을 만든 후, 관리자가 등록하게 만들었음.(기본적 유효성 검사 완료) 그리고 download coupon table을 만들어 다운로드 현황을 관리함.
user 해당 쿠폰을 다운로드 하면, userId, 쿠폰 pk 등을 insert하였고, 해당 user가 이미 다운로드 한 쿠폰이라면 다시 다운로드 하지 못하게 설계함.
또한 회원이 로그인 할 때 download coupon table을 조회하여 현재 날짜와 비교하여 만료된 경우 자동적으로 상태값을 변경시켜 사용하지 못하게 만들었음.
그리고 쿠폰존을 들어가면 자동적으로 coupon table에 다운로드 받을 수 있는 쿠폰들의 날짜를 확인하여 만료되었다면 이를 다운로드 받지 못하도록 상태값을
변경시켰음.

:scroll:관리자 메인 화면


Authentication 을 통해 로그인 한 user의 role에 따라 최고 관리자, 중간 관리자가 볼 수 있는 page를 나누었음. 최고 관리자의 경우 모든 상품 및 모든 글에 대한
현황이 보이도록 구현함. 중간 관리자의 경우 자신의 상품 및 자신의 상품에 대한 문의글 현황만 보이도록 구현함.


:scroll:관리자 : 주문 매출 현황


마찬가지로 Authentication을 통해 user의 role에 따라 최고관리자, 중간 관리자의 매출 표를 달리 표현하였음.
Chart.js를 활용하여 그래프로 나타내었고, 현재 날짜를 Local.DateTImeNow()를 통해 확인하여 일주일, 월간, 연간 날짜 별 조회를 가능하게 만들었음.


:scroll:광고보기 기능


Iframe통해 유튜브 영상을 해당 페이지에 넣었고, src의 parameter에 autoplay=1 , controls=0, mute=1을 넣어 해당 페이지 들어갈 시 바로 영상이 재생되도
록 하였음.
Script 처리로 해당 user가 10초 이상 머무를 시 포인트 적립 button을 활성화 시켰고 login을 하지 않은 경우 눌러도 login을 하라는 alert를 띄었고, login을 한
경우 point talbe을 조회하여 user가 현재 날짜에 포인트를 적립하지 않은 경우에만 point 적립 안내문구를 띄우고 DB 에 이를 반영하였음.

:scroll:Exception Handler


시간 관계상 Exception의 종류 별 처리는 하지 못하였고 모든 Exception에 대해 @ExceptionHandle annotation을 이용하여 직접 제작한 오류 페
이지로 redirect 처리를 하였음.


:scroll:Login redirect


로그인 성공 시 요청한 이전 페이지로 redirect 되는 기능을 구현함 (ex 상품뷰 페이지에서 > 로그인 > 성공 시 다시 상품뷰 페이지로 돌아가게)
우선 login Controller에서 HttpServletRequest request.getHeader("Referer"))을 이용하여 이전 요청 페이지 주소를 받아 이를 session에
저장함. 그 후 로그인 성공 시 해당 페이지로 redirect 시키고 해당 session을 삭제 처리하였음. 또한 session에 저장한 주소 값이 있으면 새로 updat
e 하지 않는 식으로 로그인 실패 후 성공 하면 다시 로그인 페이지로 redirect 되는 것을 방지하였음.

### 팀장으로서 팀원을 도운 사례
1. 검색을 담당한 팀원이 1번 페이지에서 2번으로 가려 하면, 검색된 상품이 아닌 모든 상품이 출력되는 오류가 있어 도움을 요청함. Pagination에 keyword
parameter를 넣지 않아 생긴 문제로, 이를 넣어 해결함.

2. 회사 소개 페이지를 담당한 팀원이 <video> tag로 유튜브 영상을 넣으려 하는데 되지 않아 도움을 요청함. 구글링 후 유튜브 영상의 경우 iFrame tag를 이용해야
함을 알고 이를 통해 해결함.

3. Top/down button을 구현하는 팀원이 해당 영역별로 down이 되도록 구현하고 싶은데 down button을 아무리 눌러도 한 번만 아래 영역으로 내려가지는 오류가
발생함. script에서 var topDownNum을 만들어 초기/top button 클릭시 0, down button 클릭시 ++ 되도록 하여 현재 위치값을 topDownNum에 저장하여
이를 처리함.

4. 팀원이 회사 소개 글을 수정할 때 이미지를 첨부하지 않으면 기존 이미지가 사라지는 문제가 있어 도움을 요청함. 우선 ( image.getOriginalFilename() == null || i
mage..getOriginalFilename() == "")를 이용하여 파일 등록 유무를 확인하는 로직을 작성함. 그 이후 이미지를 첨부하지 않았다면 저장할 dto에 기존 dto의 이미
지 값을 넣고 저장시켜 data 손실을 방지함.

5. 그 밖에 값이 받아지지 않는 기본적인 오류, 자신이 구현하는 기능의 ERD 에 대한 질문, method 구현 방안에 대한 질문 등 여러가지 상황에서 도움을 드렸고,
항상 해당 오류가 왜 야기되었는지, 어떤 식으로 로직이 처리되어 고쳐지는지 (예를 들어, form data가 안 받아지는 문제에 대해서 spring boot의 값을 받는 흐름을
설명하며, input tag의 name 속성과 받으려는 DTO의 이름이 같아야 해당 값들이 dto에 받아짐을 설명함) 원리를 설명하여 이해시키려 도모함.

### 활동을 통해 학습하게 된 점
이전에는 cookie, session을 이용하여 직접 코드를 작성해서 로그인 상태 값을 확인했다면, Spring Security 설정을 통해 토큰 및 Authentication을 통해 로그인 상태 여부를 확인하는 법을 알게 되었습니다. 또한 myBatis, JPA를 이용해 data 처리 시간을 단축시키는 법을 익혔습니다. 
그리고 gitHub CI/CD 파이프라인 구축, chart.js, iframe, Spring Security, ExceptionHandler와 같은 새롭게 알게된 기능들을 구현하면서 존재하는 api 코드들을 해석하고 프로젝트에 응용하는 능력을 함양할 수 있었습니다. 
개발을 하며 거듭 table을 수정한 경험으로 처음부터 완벽하게 ERD를 작성하는 것이 아닌, 개발을 진행하며 유동적으로 ERD를 수정하는, 애자일 기법의 장점을 경험할 수 있었습니다. 
또한 팀원들과 여러 오류들을 함께 고쳐나가고, 개발 방향에 대해 논의하면서 협업 능력을 기를 수 있었고 프로젝트를 진행할 때 어떻게 의사소통을 하면 좋을지를 깊이 생각할 수 있는 시간을 가지게 되었습니다. 성공적으로 팀프로젝트를 완수한 경험을 바탕으로, 앞으로도 팀 프로젝트에서 의견은 정확히 전달하되 팀원의 마음은 배려하는 방식으로 소통을 하여 성공적으로 마무리하고 싶습니다.

