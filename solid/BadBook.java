public public class BadBook {
    //...

    void printTextToConsole(){
        // our code for formatting and printing the text
    }

    
}
public class BookPrinter {

    // methods for outputting text
    void printTextToConsole(String text){
        //our code for formatting and printing the text
    }

    void printTextToAnotherMedium(String text){
        // code for writing to any other location..
    }
}}
}
public class Guitar {

    private String make;
    private String model;
    private int volume;

    //Constructors, getters & setters
}public class MotorCar implements Car {

    private Engine engine;

    //Constructors, getters + setters

    public void turnOnEngine() {
        //turn on the engine!
        engine.on();
    }

    public void accelerate() {
        //move forward!
        engine.powerOn(1000);
    }
}src
├── App.js
├── index.js
└── components
    ├── BackButton.js
    ├── Calculator.js
    ├── ClearButton.js
    ├── Display.js
    ├── EqualButton.js
    ├── FunctionButton.js
    ├── NegativeButton.js
    ├── NumberButton.js
    ├── NumberProvider.js
    └── styles
        └── Styles.js
        import React from 'react';
import NumberButton from './NumberButton';
import FunctionButton from './FunctionButton';
import ClearButton from './ClearButton';
import Display from './Display';
import EqualButton from './EqualButton';
import BackButton from './BackButton';
import NegativeButton from './NegativeButton';
import { CalculatorStyles } from './styles/Styles';

const Calculator = () => (
  <CalculatorStyles>
    <div className='display'>
      <h1>CALC-U-LATER</h1>
      <Display />
    </div>
    <div className='number-pad'>
      <ClearButton />
      <BackButton />
      <NegativeButton />
      <FunctionButton buttonValue='/' />
      <NumberButton buttonValue={7} />
      <NumberButton buttonValue={8} />
      <NumberButton buttonValue={9} />
      <FunctionButton buttonValue='*' />
      <NumberButton buttonValue={4} />
      <NumberButton buttonValue={5} />
      <NumberButton buttonValue={6} />
      <FunctionButton buttonValue='-' />
      <NumberButton buttonValue={1} />
      <NumberButton buttonValue={2} />
      <NumberButton buttonValue={3} />
      <FunctionButton buttonValue='+' />
      <div className='zero-button'>
        <NumberButton buttonValue={0} />
      </div>
      <NumberButton buttonValue='.' />
      <EqualButton />
    </div>
  </CalculatorStyles>
);

export default Calculator;

    
Каждый компонент кнопок следует одной базовой структуре. Нулевая кнопка лежит в отдельном div-блоке, так как мы используем для макета грид.

Кстати, о гридах
Если хотите узнать больше о CSS Grid, мы написали несколько статей на эту тему.
Обратите внимание, что свойство buttonValue необходимо только для NumberButton и FunctionButton.

Создание Context API Provider
NumberProvider.js – это сердце приложения и место, где наши функции обретают жизнь. Здесь же используется React Context API – отличный инструмент передачи данных между компонентами.

Чтобы передать данные или функции через вложенную структуру компонентов, пришлось бы попотеть. Обертка из компонента провайдера позволяет передавать данные в любой вложенный компонент независимо от глубины вложенности. Всякий раз, когда нужно получить данные или использовать функцию, находящуюся внутри провайдера, она будет доступна глобально.

Предварительная версия NumberProvider.js
        
import React from 'react';

export const NumberContext = React.createContext();

const NumberProvider = (props) => {
  const number = '0';
  return (
    <NumberContext.Provider
      value={{
        number,
      }}>
      {props.children}
    </NumberContext.Provider>
  );
};

export default NumberProvider;

    
Любое переданное значение теперь доступно всем вложенным компонентам. Так что есть всё необходимое для заполнения App.js:

App.js
        
import React from 'react';
import Calculator from './components/Calculator';
import NumberProvider from './components/NumberProvider';

const App = () => (
  <NumberProvider>
    <Calculator />
  </NumberProvider>
);

export default App;

    
Используем Context Provider
Теперь добавим код для дисплея. Можно выводить значение, передав его в функцию useContext из нового React Hooks API. Больше не нужно передавать проп через вложенные компоненты.

Предварительная версия Display.js
        
import React, { useContext } from 'react';
import { NumberContext } from './NumberProvider';
import { DisplayStyles } from './styles/Styles';

const Display = () => {
  const { number } = useContext(NumberContext);
  return (
    <DisplayStyles>
      <h2>{number}</h2>
      <p>Enter Some Numbers</p>
    </DisplayStyles>
  );
};

export default Display;

    
Число, которое передано на три уровня выше в NumberProvider, становится сразу же доступным компоненту Display при вызове useContext и передаче созданного нами NumberContext. Цифровой дисплей теперь включен, работает и отображает number, исходно приравненное нулю.

Начинаем работать с Hooks
Хуки позволяют «облегчить» синтаксис класса и получить состояние внутри функциональных компонентов. Добавим следующий код в NumberProvider.js, чтобы создать хук:

NumberProvider.js
        
import React, { useState } from 'react';

export const NumberContext = React.createContext();

const NumberProvider = (props) => {
  const [number, setNumber] = useState('');

  const handleSetDisplayValue = (num) => {
    if (!number.includes('.') || num !== '.') {
      setNumber(`${(number + num).replace(/^0+/, '')}`);
    }
  };

  return (
    <NumberContext.Provider
      value={{
        handleSetDisplayValue,
        number,
      }}>
      {props.children}
    </NumberContext.Provider>
  );
};

export default NumberProvider;

    
Вместо написания класса с состоянием мы разбиваем состояние и переносим каждую его часть в переменную number. Здесь же используется setNumber, действующая, как функция setState. Для инициализации используется useState.

Создание компонента кнопки
Теперь мы можем вызвать функцию с помощью Context API в любом из вложенных компонентов.

NumberButton.js
        
import React, { useContext } from 'react';
import { NumberContext } from './NumberProvider';

const NumberButton = ({ buttonValue }) => {
  const { handleSetDisplayValue } = useContext(NumberContext);
  return (
    <button type='button' onClick={() => handleSetDisplayValue(buttonValue)}>
      {buttonValue}
    </button>
  );
};

export default NumberButton;

    
Обратите внимание, как можно начать вводить значения, заданные в NumberProvider, в другие компоненты приложения с помощью функции useContext. Состояние и функции, влияющие на них, хранятся в NumberProvider . Просто вызываем определенный контекст – и готово.

Функции провайдера
Завершенный NumberProvider.js находится ниже и содержит следующие функции, которые используются вместе с хуками:

handleSetDisplayValue: задает значение, выводимое на дисплей. Мы проверяем, что в числовой строке есть только один десятичный знак, и ограничиваем длину числа 8 символами. Он передает свойство buttonValue в NumberButton.js.
handleSetStoredValue: принимает строку и сохраняет ее, позволяя ввести другое число.
handleClearValue: сбрасывает всё в 0. Это «функция очистки», которая передается в ClearButton.js.
handleBackButton: позволяет удалять ранее введенные символы по одному, пока вы не вернетесь в 0. Код привязан к BackButton.js.
handleSetCalcFunction: срабатывает при выборе математической функции, передается в FunctionButton.js и в свойства buttonValue.
handleToggleNegative: оперирует отображаемыми или сохраненными значениями, передаваемыми в NegativeButton.js.
doMath: запускает выбранную математическую операцию.
NumberProvider.js
        
import React, { useState } from 'react';

export const NumberContext = React.createContext();

const NumberProvider = (props) => {
  const [number, setNumber] = useState('');
  const [storedNumber, setStoredNumber] = useState('');
  const [functionType, setFunctionType] = useState('');

  const handleSetDisplayValue = (num) => {
    if ((!number.includes('.') || num !== '.') && number.length < 8) {
      setNumber(`${(number + num).replace(/^0+/, '')}`);
    }
  };

  const handleSetStoredValue = () => {
    setStoredNumber(number);
    setNumber('');
  };

  const handleClearValue = () => {
    setNumber('');
    setStoredNumber('');
    setFunctionType('');
  };

  const handleBackButton = () => {
    if (number !== '') {
      const deletedNumber = number.slice(0, number.length - 1);
      setNumber(deletedNumber);
    }
  };

  const handleSetCalcFunction = (type) => {
    if (number) {
      setFunctionType(type);
      handleSetStoredValue();
    }
    if (storedNumber) {
      setFunctionType(type);
    }
  };

  const handleToggleNegative = () => {
    if (number) {
      if (number > 0) {
        setNumber(`-${number}`);
      } else {
        const positiveNumber = number.slice(1);
        setNumber(positiveNumber);
      }
    } else if (storedNumber > 0) {
      setStoredNumber(`-${storedNumber}`);
    } else {
      const positiveNumber = storedNumber.slice(1);
      setStoredNumber(positiveNumber);
    }
  };

  const doMath = () => {
    if (number && storedNumber) {
      switch (functionType) {
        case '+':
          setStoredNumber(
            `${Math.round(`${(parseFloat(storedNumber) + parseFloat(number)) * 100}`) / 100}`
          );
          break;
        case '-':
          setStoredNumber(
            `${Math.round(`${(parseFloat(storedNumber) - parseFloat(number)) * 1000}`) / 1000}`
          );
          break;
        case '/':
          setStoredNumber(
            `${Math.round(`${(parseFloat(storedNumber) / parseFloat(number)) * 1000}`) / 1000}`
          );
          break;
        case '*':
          setStoredNumber(
            `${Math.round(`${parseFloat(storedNumber) * parseFloat(number) * 1000}`) / 1000}`
          );
          break;
        default:
          break;
      }
      setNumber('');
    }
  };

  return (
    <NumberContext.Provider
      value={{
        doMath,
        functionType,
        handleBackButton,
        handleClearValue,
        handleSetCalcFunction,
        handleSetDisplayValue,
        handleSetStoredValue,
        handleToggleNegative,
        number,
        storedNumber,
        setNumber,
      }}>
      {props.children}
    </NumberContext.Provider>
  );
};

export default NumberProvider;

    
Итоговое представление экрана
Обновим файл для отображения экрана. Он должен показывать number и storedNumber в соответствии с functionType. Еще есть несколько проверок, таких как отображение 0 при вставке пустой строки вместо числа.

Display.js
        
import React, { useContext } from 'react';
import { NumberContext } from './NumberProvider';
import { DisplayStyles } from './styles/Styles';

const Display = () => {
  const { number, storedNumber, functionType } = useContext(NumberContext);
  return (
    <DisplayStyles>
      <h2>{!number.length && !storedNumber ? '0' : number || storedNumber}</h2>
      <p>{!storedNumber ? 'ENTER SOME NUMBERS' : `${storedNumber} ${functionType} ${number}`}</p>
    </DisplayStyles>
  );
};

export default Display;

    
Заключение
Библиотека программиста надеется, что данный материал немного прояснит вопрос о том, как можно использовать хуки React вместе с Context API. Использование встроенных функций React дает ряд преимуществ:

простой для понимания синтаксис и отсутствие беспорядка в компонентах класса. Больше никаких super и constructor – просто чистые переменные;
проще устанавливать и использовать состояние внутри компонентов и между ними;
нет необходимости в Redux для небольших проектов.
React Hooks и Context API – отличные способы упростить приложения React и писать более чистый код. О других технологиях React читайте в нашем руководстве для React-разработчика.

Источники
https://dev.to/theranbrig/build-a-react-calculator-with-hooks-and-context-api-on

11
Обсудить

18

🔥
0

💧
0

💩
0
Frontend
JavaScript
Пет-проект
МЕРОПРИЯТИЯ
Хакатон GO.ALGO
01 декабря
Москва
Онлайн
Бесплатно
+ Показать еще
Поделитесь в комментариях вашими проектами выходного дня
Ваш ответ (можно использовать markdown)


Отправить
ЛУЧШИЕ СТАТЬИ ПО ТЕМЕ
DeepFake-туториал: создаем собственный дипфейк в DeepFaceLab
Рассказываем о технологии DeepFake и шаг за шагом учимся делать дипфейки в DeepFaceLab – нейросетевой программе, меняющей лица в видеороликах.
Подборка материалов по HTML и CSS
В этой подборке представлены актуальные книги и видеоматериалы по HTML, CSS и верстке в целом для начинающих и продвинутых веб-разработчиков.
Идеи для проектов для практики навыков программирования
Задачи для начинающих и их реализации, которые могут быть решены на любых языках программирования. Проекты распределены по нескольким категориям.
О проекте
Реклама
Пользовательское соглашение
Публичная оферта
Политика конфиденциальности
Контакты

Push-уведомления

Темная тема
© 2023, Proglib. При копировании материала ссылка на источник обязательна.
 LIVE
МЫ ИСПОЛЬЗУЕ