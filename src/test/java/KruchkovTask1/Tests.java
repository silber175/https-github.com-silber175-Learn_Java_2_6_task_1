
package KruchkovTask1;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class Tests {
    @Test

// Account creating with wrong nam
    public void accCreate() {
        try {
            Account vAccount = new Account("Иванов Иван Иванович");
            Assertions.assertEquals("Иванов Иван Иванович", vAccount.getName(),"Account  создается с неверным именем " + vAccount.getName() + " вместо "+"Иванов Иван Иванович");
        }
        catch (Exception e)   {
            throw new RuntimeException("Ошибка в тесте или классе : Account  создается с неверным именем "+e.getLocalizedMessage());
        }
    }
// Account метод undo не проверяет, что для отката нет данны
    @Test
    public void AccEmptyCreate()    {

        Account vAccount = new Account("Петров Леон");

        try {
                Assertions.assertThrows(IllegalArgumentException.class,() -> vAccount.undo(),  "Account метод undo не проверяет, что для отката нет данных");
        }
        catch (Exception e)   {
                throw new RuntimeException("Ошибка в тесте или классе : Account метод undo не проверяет, что для отката нет данных "+e.getLocalizedMessage());
        }
    }
    // Account on creating don't check for empty or null
    @Test
    public void accNullCreate()    {

        try {

           Assertions.assertThrows(IllegalArgumentException.class,
                   () -> {  Account vAccount = new Account(null);} ,
                   "Account при создании не проверяется null  значение name");

           Assertions.assertThrows(IllegalArgumentException.class,
                   () -> {Account vAccount = new Account("");}, "Account при создании не проверяется пустое значение name");
        }
        catch (Exception e)   {
            throw new RuntimeException("Ошибка в тесте или классе : Account при создании не проверяется null или пустое значение name "+e.getLocalizedMessage());
        }
    }
    @Test
    // Не записывается валюта  либо количество
    public void CcurrencyCreate()    {
        Account vAccount = new Account("Иванов Петр Иванович");
        try {
            Currency vUsd = Currency.getInstance("USD");
            Currency vEur = Currency.getInstance("EUR");

            vAccount.changeBal(vUsd, 2000);
            vAccount.changeBal(vEur, 1000);
            Map<Currency, Integer> vCurrCount = vAccount.getAccCurrCount();

            Assertions.assertEquals( 2000, (int) vCurrCount.get(vUsd), "Не записывается валюта 1 либо количество ");
            Assertions.assertEquals( 1000, (int) vCurrCount.get(vEur) , "Не записывается валюта 2 либо количество ");
        }
        catch (Exception e)   {
                throw new RuntimeException("Ошибка в тесте или классе : Account Не записывается валюта  либо количество "+e.getLocalizedMessage());
            }
    }
    @Test
    // Getter  валюты позволяет менять суммы
    public void CurrencyGetCheck()    {
        Account vAccount = new Account("Иванов Петр Иванович");
        Currency vUsd = Currency.getInstance("USD");
        vAccount.changeBal(vUsd,2000);
        try {
            Map<Currency, Integer> vCurrCount = vAccount.getAccCurrCount();
            vCurrCount.replace(vUsd, 100);
            vCurrCount = vAccount.getAccCurrCount();

            Assertions.assertEquals(2000, (int) vCurrCount.get(vUsd) , "Account Getter  валюты позволяет менять суммы");
        }
        catch (Exception e)   {
            throw new RuntimeException("Ошибка в тесте или классе : Account Getter  валюты позволяет менять суммы "+e.getLocalizedMessage());
        }
    }
    @Test
    // валюта не проверяется по списку допустимых значений
    public void currInValidCreate() {
        boolean thrown = false;
        Account vAccount = new Account("Иванов Петр Иванович");

        try {
                Assertions.assertThrows(IllegalArgumentException.class,
                        () -> vAccount.changeBal(null,1000),
                        "Account : валюта не проверяется по списку допустимых значений");
        }
        catch (Exception e)   {
            throw new RuntimeException("Ошибка в тесте или классе : Account : валюта не проверяется по списку допустимых значений "+e.getLocalizedMessage());
        }
    }
    @Test
    // Количество валюты не проверяется на отрицательное значение
    public void CurrNegotCreate() {
        boolean thrown = false;
        Account vAccount = new Account("Иванов Петр Иванович");
        Currency vEur= Currency.getInstance("EUR");

        try {
                Assertions.assertThrows(IllegalArgumentException.class,
                        () -> vAccount.changeBal(vEur,-1000),
                        "Account : Количество валюты не проверяется на отрицательное значение");
        }
        catch (Exception e)   {
            throw new RuntimeException("Ошибка в тесте или классе : Account : Количество валюты не проверяется на отрицательное значение "+e.getLocalizedMessage());
        }
    }
       @Test
    // Проверка работы undo
    public void undoNWorkFrame()    {
        Account vAccount = new Account("Кузнецов Николай Петрович");
        Currency vRur= Currency.getInstance("RUR");
        vAccount.changeBal(vRur,100);
        vAccount.setName("Василий Иванов");
        vAccount.changeBal(vRur,10);
        try {
            vAccount.undo();
           Map<Currency, Integer> vCurrCount = vAccount.getAccCurrCount();
            Assertions.assertEquals(100, (int)  vCurrCount.get(vRur)  , "Error test : метод undo неверно откатывает 1 раз колчество валюты");
            Assertions.assertNotEquals( null, vCurrCount.get(vRur) , "Error test : метод undo при откате колчества валюты , валюта удаляется из объекта");

            vAccount.undo();
            Assertions.assertEquals("Кузнецов Николай Петрович", vAccount.getName(), "Error test : метод undo  неверно откатывает наименование");

            vAccount.undo();
            vCurrCount = vAccount.getAccCurrCount();
            Assertions.assertEquals(  null , vCurrCount.get(vRur),  "Error test : метод undo при откате  валюты , валюта не пропадает из объекта ");
        }
        catch (Exception e)   {
                throw new RuntimeException("Ошибка в тесте или классе : Account : Проверка работы undo "+e.getLocalizedMessage());
        }
    }

    @Test
    // Проверка метода сохранения
    public void savePointCheck()  {
        Manager accSavers = new Manager();
        Account vAccount = new Account("Сидоров И.К");
        Currency vUsd = Currency.getInstance("USD");
        Currency vEur= Currency.getInstance("EUR");
        vAccount.changeBal(vUsd,2000);
        vAccount.changeBal(vEur,1000);
        vAccount.changeBal(vEur,1500);
        try {
            String aSaveName1 ="sp1";
            AccSaver vAccSaver =  vAccount.save( aSaveName1);
            accSavers.saves = new HashMap<>();
            accSavers.saves.put(aSaveName1, vAccSaver);

            vAccount.setName("Иван Васильевич");
            vAccount.changeBal(vUsd,6000);
            vAccount.changeBal(vEur,3000);

            String aSaveName2 = "sp2";
            vAccSaver =  vAccount.save( aSaveName2);
            accSavers.saves.put(aSaveName2, vAccSaver);

            vAccount.setName("Pol Robson");
            vAccount.restore(accSavers.saves.get(aSaveName1));
            Assertions.assertEquals( "Сидоров И.К" , vAccount.getName(),"Error test : метод save  неверно сохраняет наименование");

            Map<Currency, Integer> vCurrCount = vAccount.getAccCurrCount();
            Assertions.assertNotEquals( null, vCurrCount.get(vUsd)  , "Error test : метод save не сохраняет валюту 1 ");
            Assertions.assertNotEquals( null, vCurrCount.get(vEur), "Error test : метод save не сохраняет валюту 2 ");
            Assertions.assertEquals(1500, (int) vCurrCount.get(vEur)  , "Error test : метод save неверно сохраняет колчество валюты");
            Assertions.assertEquals( 2000, (int) vCurrCount.get(vUsd) , "Error test : метод save неверно сохраняет колчество валюты");

            vAccount.restore(accSavers.saves.get(aSaveName2));
            Assertions.assertEquals("Иван Васильевич" , vAccount.getName(), "Error test : метод save  неверно сохраняет наименование");

            vCurrCount = vAccount.getAccCurrCount();
            Assertions.assertNotEquals(  null , vCurrCount.get(vUsd) , "Error test : метод save не сохраняет валюту");
            Assertions.assertNotEquals( null,  vCurrCount.get(vEur), "Error test : метод save не сохраняет валюту");
            Assertions.assertEquals( 6000, (int) vCurrCount.get(vUsd)  , "Error test : метод save неверно сохраняет колчество валюты");
            Assertions.assertEquals( 3000, (int) vCurrCount.get(vEur), "Error test : метод save неверно сохраняет колчество валюты");
        }
        catch (Exception e)   {
            throw new RuntimeException("Ошибка в тесте или классе : Account : Проверка работы undo "+e.getLocalizedMessage());
        }

    }

    // Проверка метода undo при добавлении поля вид счета в класс Account
    // При наличии поля private String acntType  тесты
    @Test
    public void extendsCheck()   {
        class BAccount {
            private String name;
            private final Map<Currency, Integer> accCurrCount= new HashMap<>();
            private String acntType;

            BAccount(String vName)  {
               this.name = vName;
            }

            private Deque<Command> saves = new ArrayDeque<>();
            public String getName() {
                return this.name;
            }

            public void setName(String vName) {
                if (vName == null)
                    throw new IllegalArgumentException("Имя не может быть null ");
                if (vName.isBlank() )
                    throw new IllegalArgumentException("Имя не может быть пустым ");
                String tmp = BAccount.this.name;
                saves.push(()->BAccount.this.name=tmp);
                this.name = vName;
            }

            public String  getAccType() {
                return  this.acntType;
            }

            public void setAccType(String vAccType) {
                if (vAccType == null)
                    throw new IllegalArgumentException("vAccType не может быть null ");
                if (vAccType.isBlank() )
                    throw new IllegalArgumentException("vAccType не может быть пустым ");
                String tmp = BAccount.this.acntType;
                saves.push(()->BAccount.this.acntType=tmp);
                this.acntType = vAccType;
            }

            public Map<Currency, Integer> getAccCurrCount() {
                return new HashMap<>(accCurrCount);
            }

            public void changeBal(Currency  vCurr, int vCount) {
                if (vCount < 0)
                    throw new IllegalArgumentException("Количество валюты не может быть отрицательным");
                if (vCurr == null)
                    throw new IllegalArgumentException( "Ошибка : Недопустимая валюта ");
                if (this.accCurrCount.size() == 0)   {
                    //  еще не было никакай валюты до ввода баланса, то команда по удалению
                    // добавляемой валюты
                    saves.push(()->BAccount.this.accCurrCount.remove(vCurr));
                    this.accCurrCount.put(vCurr, vCount);
                }
                else    {
                    if (this.accCurrCount.containsKey(vCurr)) {
                        int tmp = BAccount.this.accCurrCount.get(vCurr);
                        saves.push(()->BAccount.this.accCurrCount.replace(vCurr, tmp));
                        this.accCurrCount.replace(vCurr, vCount);
                    }
                    else {
                        //  еще не было добавляемой  валюты , то команда по удалению
                        // добавляемой валюты
                        saves.push(()->BAccount.this.accCurrCount.remove(vCurr));
                        this.accCurrCount.put(vCurr, vCount);
                    }
                }
            }

            // Метод для тестирования результатов изменений валюты
            public String printAccCurrCount() {
                String sRes = " Остаки валют : ";
                sRes = sRes + this.accCurrCount.toString();
                return sRes;
            }

            // Откат на сохраненное состояние с именем aSaveName ля тестирования
            public void undo()  {
                saves.pop().make();
            }
        }
        // Test undo
        try {
        System.out.println("Тестирование undo после добаления нового поля в класс");
        BAccount vAccount = new BAccount("Кузнецов Николай Петрович");
        vAccount.setAccType("Специальный");
        vAccount.setAccType("Премиальный");
        Currency vRur= Currency.getInstance("RUR");
        vAccount.changeBal(vRur,100);
        vAccount.setName("Василий Иванов");
        vAccount.changeBal(vRur,20);

        // undo begin
        vAccount.undo();
        Map<Currency, Integer> vCurrCount = vAccount.getAccCurrCount();
        Assertions.assertEquals(  100, (int) vCurrCount.get(vRur) , "Error test : метод undo неверно откатывает 1 раз колчество валюты");
        Assertions.assertNotEquals(null , vCurrCount.get(vRur), "Error test : метод undo при откате колчества валюты , валюта удаляется из объекта");

        vAccount.undo();
        Assertions.assertEquals( "Кузнецов Николай Петрович", vAccount.getName(), "Error test : метод undo  неверно откатывает наименование");

        vAccount.undo();
        vCurrCount = vAccount.getAccCurrCount();
        Assertions.assertEquals(null, vCurrCount.get(vRur), "Error test : метод undo при откате  валюты , валюта не пропадает из объекта");

        vAccount.undo();
        Assertions.assertEquals( "Специальный" , vAccount.getAccType(), "Error test : добавление нового поля в класс требует зменения метода undo");
    }
        catch (Exception e)   {
        throw new RuntimeException("Ошибка в тесте или классе : Проверка метода undo при добавлении поля вид счета в класс Account "+e.getLocalizedMessage());
    }

    }


}
