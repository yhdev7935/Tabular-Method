## 2020-01 Digital Logic Design / Tabular Method


### 각 클래스의 용도

- **Minterm 클래스**는 각각의 minterm를 다루기 위해 만들어진 클래스입니다. 또한, Don't Care도 포함한다.
<br>
- **PITable 클래스**는 각각의 minterm를 합치고 더이상 합칠 수 없는 minterm들을 가지고 Pi Table를 구성합니다.
<br>
- **LogicExpression 클래스**는 PiTable에서 EPI를 제거하고, Row/Column Dominance까지 진행한 후에도 결과를 도출할 수 없을 때, Petrick's Method를 사용하기 위해 만들어진 클래스입니다.
<br>
- **Main 클래스**는 Minterm, PITable, LogicExpression 클래스들을 가지고 **Quine–McCluskey Algorithm**를 구현하는 과정을 보여줍니다.


### 입력

minterm과 don't care를 입력받아 Minterm 객체의 배열에 넣는다.<br>

```java
// sorting
Collections.sort(dontCareNumbers);
minterm = Minterm.sort(minterm);
```
위 코드는 Minterm들을 `# of 1s`기준으로 정렬하고, 똑같을 경우 각 Minterm의 숫자 개수에 따라 정렬한다. 즉, Grouping 과정이다.<br>

### Step 1 : Find Prime Implicants

```java
boolean flag = true;
while(flag)
	flag = combineImplicants(flag);
```
위 코드는 Minterm들을 더이상 합칠 수 없을 때까지 계속 합친다.

### Step 2 : Find Essential Prime Implicants

```java
PITable pit = new PITable(notCombinedMinterm, origin_minterm, binarySize);
```
위 코드는 더이상 못합치는 Minterm, 처음 입력한 Minterms, binary Size를 입력받아 PITable를 구성한다.

```java
pit.findEPI();
```
위 코드는 EPI를 찾고 제거하는 과정까지 포함한다. 실제로 보여줄 때 제거한 채로 보인다.

```java
if(pit.mintermValidation())
{
    pit.printResult();
    return;
}
```
위 코드는 EPI만으로 모든 minterm를 커버할 수 있는지 테스트하고, 가능하면 결과를 출력하고 종료한다. 실패한다면 Step 3으로 넘어간다.


### Step 3 : Minimum Cover

```java
pit.columnDominace();
pit.rowDominace();

boolean validation = pit.mintermValidation();
if(validation == true)
{
    pit.printResult();
    return;
}
```
위 코드는 Row/Column Dominance를 진행하고, 또 다시 minterm를 커버할 수 있는 테스트한다.<br>
코드를 간결하게 하기 위해서 NEPI를 따로 표기하지 않고, EPI 제거하듯이 똑같이 작업한다.<br>
여기까지 과정만으로 minterm를 커버할 수 있으면 출력하고 종료한다. 실패한다면 Step 4으로 넘어간다.


### Step 4 : Petrick's Method

Petrick's Method를 이용해 식을 전개하고, minimize하고, 가장 짧은 SOP와 EPI와 합쳐 결과를 출력한다.<br>
minimize과정은 X + XY = X만 이용했다. X + X도 포함되어 있고, HashSet를 이용하기에 X * X의 경우도 배제할 수 있다.
			