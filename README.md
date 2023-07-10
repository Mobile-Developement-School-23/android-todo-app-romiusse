# Информация о домашнем задании *№4* для проверяющих


## Оценки проверяющих:

Проверяющий 1: *12/12*
Проверяющий 2: *Не оценено*


## Часть 1

В некоторые файлы не получилось реализовать в <200 строк, т.к я реализовал свои
фишки для доп балла в части 3.

Испорльзуется MVVM модель.

Продукоментировал все файлы, за что он отвечают.

*Инициатива:*
Структура проетка, чтобы легче его понимать
![Структура проетка](https://github.com/Mobile-Developement-School-23/android-todo-app-romiusse/blob/master/ProjectStructure.png)



## Часть 2

В проекте реализовал межмодульный DI с помощью Component Dependencies

Т.к приложение многомодальное, то делать subcomponent нет смысла
Родитель всегда должен знать про всех своих детей, но дети не должны
знать про своего родителя в многомодальном проиложении, если бы приложение
было не многомодальном, то я бы сделал subcomponent для всех feature модулей.


## Часть 3

Немного про синхронизацию данных:

            Т.к синхронизация на бэке работает не очень, то я добавил возможность
            либо перенести несохраненные данные на сервер, либо обновить существующие
            данные с сервера. Для этого, если данные на устростве и сервере не совпадают,
            то вылезает Bottom sheet dialog, где можно посмотреть данные на сервере и
            нажать на 2 кнопки. (Это окно можно вызвать либо обновив данные без интернета,
            либо обглвить данные на другом устройтве, а затем на первом сделать pull to
            refresh)

*Как раз инициатива:*
Bottom sheet dialog c показом списка на сервере, и 2 функциональные кнопки.
Кроме этого добавил статусн ый значок, чтобы понимать синхронизированы данные или нет