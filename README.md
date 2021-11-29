<h1 id="호스트들의 Alive 상태 모니터링 서버 개발">호스트들의 Alive 상태 모니터링 서버 개발</h1>
<h2 id="[요구 사항]">[요구 사항]</h2>
<p>		<h2>1. 호스트 등록 관리 REST API. </h2></p>
<ul><li>			호스트 등록.
</li></ul>
<p>			<img width="861" alt="스크린샷 2021-11-29 오후 11 27 52" src="https://user-images.githubusercontent.com/82119007/143885903-580a6700-b748-4f09-a256-7060dee34b02.png">

</p>
<p>			/hosts HTTP POST 메서드로 요청을 하면 해당 컨트롤러가 호출되고,</p>
<p>			호스트 등록(저장) 하기 위해 HostService.saveHost()가 실행됩니다.</p>
<p>			<img width="519" alt="스크린샷 2021-11-27 오후 5 06 02" src="https://user-images.githubusercontent.com/82119007/143839529-681ec2cf-9a5e-4ba9-9ab3-4ffeb418e017.png"></p>
<p>			<img width="460" alt="스크린샷 2021-11-27 오후 5 11 00" src="https://user-images.githubusercontent.com/82119007/143839534-118688c3-d882-4053-8ba8-e9ab95ae7935.png"> 를 이용해  DNS서버에서 </p>
<p>			호스트네임과 호스트주소를 가져오고, 가져온 값을</p>
<p>			Host Entity의 빌더 패턴으로 name과 address필드에 입력하고 Host				객체를 반환합니다.</p>
<p>			(name과 address필드는 중복을 허용하지 않게 unique제약 조건을 				사용했습니다.)</p>
<p>			반환받은 Host객체를  요구사항에 맞는 Dto객체로</p>
<p>			(Entity -> DTO)변환 후 리턴합니다. </p>

<p>			<img width="341" alt="스크린샷 2021-11-29 오전 11 02 31" src="https://user-images.githubusercontent.com/82119007/143839542-30c9a287-df30-4b00-94e9-6d277d0b2822.png">
</p>
<p>			호스트 등록은 100개로 제한하기 위해 HostRepository에서 					데이터베이스의 카운트를 구해오는 findCount() 생성해 사용했습니다.</p>

<p>			<img width="328" alt="스크린샷 2021-11-29 오전 11 05 14" src="https://user-images.githubusercontent.com/82119007/143840802-3fb43e5c-e0dc-4c5b-ac90-9dd2fe27e218.png"></p>
<p>			서버가 재시작 되어도 등록된 호스트의 리스트를 유지 하기 위해 </p>
<p>			application.yml파일의 ddl-auto: update 설정했습니다.</p>
<p>			</p>
<ul><li>			호스트 조회.
</li></ul>
<p>			<img width="483" alt="스크린샷 2021-11-29 오후 11 28 09" src="https://user-images.githubusercontent.com/82119007/143885908-b7a41e77-bb09-4042-9477-e160fff10b51.png">
<p>			/hosts/{hostId} HTTP GET 메서드로 요청을 하면  해당 컨트롤러가 호				출됩니다.  호스트 등록시 PK로  host_id를 사용하고 조회 하고자 하는  				호스트의 id를 @PathVariable로 JPA에서 제공하는  findById() 					사용해서 조회 후 반환 합니다.</p>
<p>			<img width="390" alt="스크린샷 2021-11-29 오전 10 03 37" src="https://user-images.githubusercontent.com/82119007/143840817-0bd69f96-fafc-447c-8aae-fc7e745f6f93.png">  </p>
<p>			조회 결과 필드에는 등록/수정 시간을 포함하기 위해  해당 기능을 					담당하는 BaseTimeEntity객체를 생성하고 JPA Auditing기능을 					사용했습니다.  </p>
<p>			JPA Auditing을 사용하기 위해서 Application클래스에 </p>
<p>			@EnableJpaAuditing 어노테이션을 사용해 활성화를 해줍니다.</p>
<p>			<img width="324" alt="스크린샷 2021-11-29 오전 10 19 31" src="https://user-images.githubusercontent.com/82119007/143840846-0693494e-2c97-48f6-a555-6fc2a02691fb.png"></p>
<p>			등록<i>수정 시간이 필요한 Entity가 있을경우 BaseTimeEntity	객체					를 상속을 받으면 @MappedSuperclass어노테이션에 의해 등록</i>수정 				시간	필드가 상속받은 Entity의 컬럼으로 등록합니다.</p>
<p>			<img width="377" alt="스크린샷 2021-11-29 오전 10 10 41" src="https://user-images.githubusercontent.com/82119007/143840882-c22c6c50-76d2-409c-90d0-ee978647fe7d.png"></p>
<p>			<img width="359" alt="스크린샷 2021-11-29 오전 10 25 24" src="https://user-images.githubusercontent.com/82119007/143840895-d0755602-d0d6-4cce-85a2-ebd83f5f0d7b.png"></p>

<ul><li>			호스트 수정.
</li></ul>
<p>			<img width="723" alt="스크린샷 2021-11-29 오후 11 28 31" src="https://user-images.githubusercontent.com/82119007/143886891-0778a7ab-548e-48ac-9e91-c667add47c6a.png">
<p>			/hosts/{hostId} HTTP PUT 메서드로 요청을 하면 해당 컨트롤러가 				호출됩니다. 조회와 같이 호스트의 id로 수정이 필요한 호스트를 </p>
<p>			조회합니다. </p>
<p>			</p>
<p>			<img width="461" alt="스크린샷 2021-11-29 오전 10 40 41" src="https://user-images.githubusercontent.com/82119007/143841702-fd7be5ee-8f88-4dd1-bad2-1f91e0d12f12.png"></p>
<p>			<img width="374" alt="스크린샷 2021-11-29 오전 10 41 55" src="https://user-images.githubusercontent.com/82119007/143841706-bcdc9102-ab9d-44e5-822d-148833351a84.png">호스트의 수정은 임의로 호스트네임만 변경 가능하게 정했습니다. </p>
<p>			@Setter를 닫아두어 변경지점을 최소화하고, updateHostName()를 				생성해 사용했습니다. 호스트 등록과는 호스트수정의 요구사항은 서로 				다를 수 있기 때문에 수정할 필드만 수정을 하기 위해 변경감지를 					사용했습니다. </p>

<ul><li>			호스트 삭제.
</li></ul>
<p>			<img width="546" alt="스크린샷 2021-11-29 오후 11 43 36" src="https://user-images.githubusercontent.com/82119007/143888314-13feef4b-9126-4c3b-b4a3-2e9f25229455.png">

</p>
<p>			/hosts/{hostId} HTTP DELETE 메서드를 호출하면 해당 컨트롤러가 				실행되고, deleteHostId 값을 응답합니다.</p>


<p>	<h2> 2. Alive 상태 조회 REST API.</h2> </p>
<ul><li>			호스트 상태 단건 조회.
</li></ul>
<p>			<img width="543" alt="스크린샷 2021-11-29 오후 11 29 01" src="https://user-images.githubusercontent.com/82119007/143887156-3d221078-65d8-429a-9b24-3df6d938d33f.png">
<p>			상태를 조회하고 싶은 호스트를 /hosts/status/{hostId} 							HTTP GET 메서드로 조회 요청을 하면 해당 컨트롤러가 							실행됩니다.</p>
<p>			<img width="552" alt="스크린샷 2021-11-29 오후 8 48 04" src="https://user-images.githubusercontent.com/82119007/143862840-e3242eff-d9fc-4332-a7eb-f12c67828911.png">
</p>
<p>			hostService.findHostStatus()가 실행되면 호스트의 상태를 확인하는 				서비스를 담당하는 AliveCheckService의 isAlive()가 실행됩니다. </p>
<p>			<img width="468" alt="스크린샷 2021-11-29 오후 8 09 08" src="https://user-images.githubusercontent.com/82119007/143857527-b246676f-cdad-42e3-84c7-1a4734a5c3c4.png"></p>
<p>			상태를 조회하기 위해 InetAddress.isReachable()를 사용했습니다. </p>
<p>			isReachable.()의 timeout은 임의로 3000ms로 정하여 					사용했습니다. isAlive()는 boolean타입으로 반환값은 true 또는 				false를 반환합니다.</p>
<p>			<img width="337" alt="스크린샷 2021-11-29 오전 11 31 48" src="https://user-images.githubusercontent.com/82119007/143847824-c74f3ac0-29f0-43d6-bd8c-034c8b82d482.png"></p>
<p>			Enum타입으로 호스트의 상태를 구분하는 객체를 만들고 isAlive()의 				반환되어 오는 값이 true면 ALIVE, false면 DEAD로 호스트의 상태를 				설정합니다. </p>
<p>			<img width="479" alt="스크린샷 2021-11-29 오전 11 53 03" src="https://user-images.githubusercontent.com/82119007/143847837-3c64a8e4-73ca-4d98-9eaa-f760cfa3b3b9.png"></p>
<p>			<img width="716" alt="스크린샷 2021-11-29 오후 4 05 21" src="https://user-images.githubusercontent.com/82119007/143847854-c2a46921-04ca-4cb4-9a12-b0902c5c8ce4.png"></p>
<p>			그리고 상태를 조회 시,  호스트 상태가</p>
<p>			 ALIVE하고 LastAliveDate() == null 이면 									setLastAliveDate()를 사용해 최초 조회를 기준으로 마지막 Alive의 				시간을 설정해주었습니다.</p>

<ul><li>			호스트 상태 전체 조회.
</li></ul>
<p>			<img width="565" alt="스크린샷 2021-11-29 오후 11 29 16" src="https://user-images.githubusercontent.com/82119007/143887176-7a6442f8-6a0b-42a3-adb7-88319330549b.png">
<p>			<i>/hosts/status HTTP GET 메서드로 호스트 상태 전체 조회 컨트롤러가 				실행됩니다.</p>
<p>			<img width="431" alt="스크린샷 2021-11-29 오후 12 31 31" src="https://user-images.githubusercontent.com/82119007/143851657-a618ff8d-935f-4dc1-b3ea-4ea40f591925.png"></p>
<p>			<img width="289" alt="스크린샷 2021-11-29 오후 12 32 50" src="https://user-images.githubusercontent.com/82119007/143851665-5a1c8da9-c84e-4e76-b7dc-95b2be357114.png">
</p>
<p>			hostService.getHostsStatus()가 호출되면, 먼저 findAllByHost()가 				실행됩니다. </p>
<p>			findAllByHost()는 JPA가 제공하는 findAll()를 사용해 조회를 하고 				호스트들을 리스트 형태로 반환을 받고, 반환받은 리스트를 for loop를 돌려 단건 조회와 같이 findHostStaust()를 실행합니다.</p>
<p>			루프를 다 돌고 나면 stream()를 이용해 요구사항에 맞는 DTO로 변환 후  			Collection으로 반환합니다.  </p>
<p>			<img width="434" alt="스크린샷 2021-11-29 오후 7 57 33" src="https://user-images.githubusercontent.com/82119007/143858497-885d265c-dd12-471e-8f3e-170c68ec47e8.png"> </p>
<p>			<img width="755" alt="스크린샷 2021-11-29 오후 1 19 09" src="https://user-images.githubusercontent.com/82119007/143858500-4b805022-96f2-4174-9f2a-59e990ddf949.png"> </p>
<p>			기존에 getHostsStatus()의 for Loop로
<p>			 isReachable(3000ms), 10개의 호스트의 조건으로 전체 	
<p>			조회시간은 27.52s 가 소요됩니다. 
<p>			<img width="369" alt="스크린샷 2021-11-29 오후 8 05 54" src="https://user-images.githubusercontent.com/82119007/143858511-46d5fce1-8ef3-48e6-91cb-48f2987c8d96.png">
<p>			<img width="576" alt="스크린샷 2021-11-29 오후 8 07 23" src="https://user-images.githubusercontent.com/82119007/143858533-5436bb16-9f9a-4f10-8e0a-a46de4333180.png">

<p>			조회시간을 줄이기 위해 stream().parallel()를 사용해 병렬로 나눠 				처리하는방법을 사용했습니다. </p>

<h2 id="[제약 사항]">[제약 사항]</h2>
<ul><li>MariaDB (ver. 10.6.4)
</li><li>Java 11
</li><li>Gradle (ver. 7.2)
</li><li>Spring-Boot (ver. 2.6.0)</li></ul>
