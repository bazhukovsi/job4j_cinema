В этом задании вам нужно разработать сайт по покупки билетов в кинотеатр.
В этом задании нужно использовать Spring boot, Thymeleaf, Bootstrap, JDBC.
Опишем виды.
1. Главная страница форма с выбором фильма. Ряда и места.
Три списка. Если места заняты, то их в списках не показываем.
2. После загрузки формы отобразить результат покупки. 
3. Важно. Пользователь может не купить билет, потому что его купил другой пользователь. 
4. То есть одновременно выбрали одинаковые места.
5. Модели. User. Session (Сеансы). Ticket (Купленный билет).

Вычисление свободных мест для сеанса необходимо сделать в слое SessionService. Вычисления делаем по купленным билетам.