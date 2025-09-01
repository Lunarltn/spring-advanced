# 스프링 심화 과제
<br>

## 📑 소개
코드 개선 및 테스트 코드 작성
<br><br>

## 📆 기간
* 25/08/25 ~ 25/09/01
<br>

# 구현
***

## 구현 단계
### 필수 기능
Lv 0. 에러 분석<br>
Lv 1. ArgumentResolver<br>
Lv 2. 코드 개선<br>
Lv 3. N+1 문제<br>
Lv 4. 테스트코드 연습<br>
### 도전 기능<br>
Lv 5. API 로깅<br>

## 필수 기능
***
### Lv 0. 에러 분석
* 에플리케이션 실행 실패를 해결하는 문제<br>
![](https://velog.velcdn.com/images/qpsrltn/post/60d376c6-7e07-4ebe-862f-d681e331d8dd/image.png)<br>
* 위와 같은 오류가 발생했는데 밑에부터 읽어보면 `Could not resolve placeholder 'jwt.secret.key' in value "${jwt.secret.key}"` 즉, 키를 찾을 수 없어서 발생한 오류였다.<br>
![](https://velog.velcdn.com/images/qpsrltn/post/7a6f2706-ca49-4018-97fb-0887274b41b7/image.png)<br>
* 사인 알고리즘으로 `HS256`을 사용하는데 이에 맞는 키를 [https://jwtsecrets.com/](https://jwtsecrets.com/)에서 발급해 `application.properties`에 넣어줬다.<br>

## Lv 1. ArgumentResolver
* `org.example.expert.config;` 에 위치한 `AuthUserArgumentResolver` 코드를 동작하게 하는 문제<br>
* `WebMvcConfigurer`을 상속받는 `WebConfig`에 `addArgumentResolvers`로 등록하지 않아 발생한 문제였다.<br>
* [Git](https://github.com/Lunarltn/spring-advanced/commit/59deafbb3aa817c6acb7097db422af83519d0bac)<br>

## Lv 2. 코드 개선
### 1. Early Return
* 불필요한 로직 실행 방지하기<br>
![](https://velog.velcdn.com/images/qpsrltn/post/d57cb0ff-63ab-4a6e-8840-9a469fb18e0d/image.png)<br>
* 이메일 검증로직을 위로 옮겨 불필요하게 발생하는 로직을 해결했다.<br>
* [Git](https://github.com/Lunarltn/spring-advanced/commit/ad67e75ebbb3ea211abdd8ab1297a7dbac0ad86e)<br>

### 2. 불필요한 if-else 피하기
* 불필요한 else 블록을 없애 코드를 간결하게 하기<br>
![](https://velog.velcdn.com/images/qpsrltn/post/c0e479a3-5b95-4372-8229-63a8b48e43ed/image.png)<br>
* 불필요한 else문을 제거했다.<br>
* [Git](https://github.com/Lunarltn/spring-advanced/commit/095cee7292f923e713744c7b115019f9d3e7f8ab)<br>

### 3. Validation
* 값에 대한 조건 DTO로 처리하기<br>
![](https://velog.velcdn.com/images/qpsrltn/post/c1c6befa-5997-4370-91b2-f63eadb4b809/image.png)<br>
* 서비스에 있던 조건문들을 DTO로 옮기고 컨트롤러에서 `@Valid`를 적용해 해결했다.<br>
![](https://velog.velcdn.com/images/qpsrltn/post/5c77b937-e81b-45af-a418-ffa315ab90e5/image.png)![](https://velog.velcdn.com/images/qpsrltn/post/603ccc05-ab02-46f5-93ff-2366913ca767/image.png)<br>

## Lv 3. N+1 문제
* JPQL fetch join을 사용하여 N+1 문제를 해결하고 있는 `TodoRepository`를 동일한 동작을 하는 `@EntityGraph` 기반의 구현으로 수정하기<br>
![](https://velog.velcdn.com/images/qpsrltn/post/e50f170a-aaf0-47d8-b2ce-77b081445a9b/image.png)<br>
* `@Query` 어노테이션에서 `user`를 fatch join하였기에 `@EntityGraph`에 `user`를 넣어주고 정렬 기능은 쿼리 메서드로 해결했다.<br>

## Lv 4. 테스트코드 연습
### 1. 예상대로 성공하는지에 대한 케이스
* rawPassword와 encodedPassword가 바뀌어 있었다.<br>
![](https://velog.velcdn.com/images/qpsrltn/post/80868ae0-fdd4-4260-8b59-fe10b4bdec47/image.png)<br>

### 2. 예상대로 예외처리 하는지에 대한 케이스
* 테스트 패키지 `org.example.expert.domain.manager.service;` 의 `ManagerServiceTest` 의 클래스에 있는 `manager_목록_조회_시_Todo가_없다면_NPE_에러를_던진다()` 테스트가 성공하고 컨텍스트와 일치하도록 테스트 코드와 테스트 코드 메서드 명을 수정하기<br>
![](https://velog.velcdn.com/images/qpsrltn/post/6739dc9a-a860-4532-a201-fabdd53ef1b4/image.png)<br>

* 테스트 패키지 `org.example.expert.domain.comment.service;` 의 `CommentServiceTest` 의 클래스에 있는 `comment_등록_중_할일을_찾지_못해_에러가_발생한다()` 테스트가 성공할 수 있도록 테스트 코드를 수정하기<br>
![](https://velog.velcdn.com/images/qpsrltn/post/f6e6d071-f4c9-42dd-ac7a-13cb110b193c/image.png)<br>

* 테스트 패키지 `org.example.expert.domain.manager.service`의 `ManagerServiceTest` 클래스에 있는 `todo의_user가_null인_경우_예외가_발생한다()` 테스트가 성공할 수 있도록 서비스 로직을 수정하기<br>
![](https://velog.velcdn.com/images/qpsrltn/post/f6f111fc-aea4-4fb4-a2c8-d1da950458b7/image.png)<br>

* [Git](https://github.com/Lunarltn/spring-advanced/commit/1d3c834bce8ea446c085b4235f32e3c8a02c86f1)<br>
# 도전 기능
***

## Lv 5. API 로깅
* 어드민 사용자만 접근할 수 있는 특정 API에 접근할 때마다 접근 로그를 기록하기<br>
![](https://velog.velcdn.com/images/qpsrltn/post/07d2ce1d-1b29-4173-971d-355e99e391b8/image.png)![](https://velog.velcdn.com/images/qpsrltn/post/7e0a9e47-72c3-404a-ad7e-94bcd44481ed/image.png)<br>
* AOP를 사용해 구현했고, ResponseBody를 로깅해야하기 때문에 `RequestContextHolder` 를 통해 `HttpServletRequest`를 불러와야 했다.<br>
* `RequestContextHolder`는 Spring 프레임워크 전 구간에서 `HttpServletRequest`에 접근할 수 있게 도와주는 구현체이다.<br>
* 이렇게 읽어들인 `HttpServletRequest`는 한번 읽으면 휘발되기 때문에 `ContentCachingRequestWrapper`으로 메모리에 캐싱하여 문제를 해결했다.<br>
* [Git](https://github.com/Lunarltn/spring-advanced/commit/f0539f7c522d169149a1749d4fae6ffe1fe40669)<br>

# 트러블 슈팅
***
* [HttpServletRequest 여러 번 읽기](https://velog.io/@qpsrltn/250829-HttpServletRequest-%EC%97%AC%EB%9F%AC-%EB%B2%88-%EC%9D%BD%EA%B8%B0)<br>
