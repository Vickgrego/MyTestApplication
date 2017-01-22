# MyTestApplication
Тестова навчальна програма, станом на 22-01-2017 вміє:
 - підключаєтьсяя до серверу, завантажує дані за допомогою бібліотеки Google Volley,
- обробляє JSON, формує базу даних SQL.
- завантаження даних з серверу відбувається за допомогою Сервісу
- використовує BroadcastReceiver для отримання повідомлення від Сервісу, що дані завантажені і можна оновлювати UI
- використовує AlarmManager щоб періодично (кожну годину) запускати сервіс для оновлення даних.
- програма перевіряє на підключення до інтернету у разі, якщо база даних не сформована (перший запуск програми)
- Головне вікно відображає Таби: Список каналів \ Список категорій \ Улюблені каналі (TabLayout and ViewerPage)
- у кожному фрагменті відображається Список, який формується з бази даних
- використовується Navigation Drawer
- імплемментовано додавання до списку улюблених каналів

* Data are loaded asynchronously and are written in a local SQL database and are shown to a user from there. Firstly, you should download program schedule for today, then for a week/month - + ВИКОНАНО
* Optionally. Manual database synchronization. - ВІДСУТНЯ. є автоматична
* Optionally. Automatically synchronize data for today several times a day. - + ВИКОНАНО за допомогою АлармМенеджера
* Optionally Show a progress of download/synchronization. - + ВИКОНАНО при завантаженні програми
* Optionally Show Notification while loading. + ВИКОНАНО показує Тост повідомлення після завантаження
* Optionally Use ContentProvider for work with a database. - використовується SQLLiteHelper який реалізований у форматі Sigleton щоб забезпечити лише один обєкт класу для всієї програми

  On the main screen of the application is located TabLayout with channel titles and ViewPager with programs for these channels. The program is displayed for today by default. - ВИКОНАНО частково, я відійшов від завдання, тому що 130 каналів переповнюють TabLayout - не видно ні заголовків, ні підвічування вибраних Табів. Натомість Таби відображать Список каналів, Список категорій і список улюблених каналів
* Optionally ability to select  data for which is shown a TV schedule. - програми відображаються окремо на сьогодні і окремо на весь тиждень. Зі зміною дня список програм і дата змінюються автоматично
* Optionally select channels which are preferred. - ВИКОНАНО у списку улюблених каналів
* Optionally sort the list of channels on the main screen. - відсутня

Menu includes the following items: 
list of categories; + ВИКОНАНО
list of channels; + ВИКОНАНО
list of preferred channels; + ВИКОНАНО
program schedule. (Main screen) + ВИКОНАНО, хоча телепрограма відображається не на Main screen, потрібно вибрати конкретний канал
* Optionally use NavigationDrawer + ВИКОНАНО

The list of categories is shown on the screen with categories. When user click on some category, the list of channels of this category is displayed. When user sees the list of channels he can click on the item and by this action add or remove single item to the list of preferred channels. + ВИКОНАНО
For a list of all the channels and preferred ones, actions are the same. + ВИКОНАНО


Початок створення 15.01.17р
Кінець - 22-01-2017


