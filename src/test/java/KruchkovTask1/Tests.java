
package KruchkovTask1;

import org.junit.Test;

import java.util.*;

public class Tests {
    @Test

// Account creating with wrong nam
    public void accCreate() {
    //

        Account vAccount = new Account("Иванов Иван Иванович");
        if (!vAccount.getName().equals("Иванов Иван Иванович")) {
            throw new RuntimeException("Error test : Account creating with wrong name");
        }
    }
    @Test
    // Account on creating don't check for empty or null
    public void AccEmptyCreate()    {
        Account vAccount = new Account("Петров Леон");
        try {
            vAccount.undo();
        } catch (IllegalArgumentException err) {return;}
        throw new RuntimeException("Error test : Account on creating don't check for empty");
    }
    @Test
    public void accNullCreate()    {
        try {
            Account vAccount = new Account(null);
        } catch (IllegalArgumentException err) {return;}
        throw new RuntimeException("Error test : Account on creating don't check for null");
    }
    @Test
    // Не записывается валюта  либо количество
    public void CcurrencyCreate()    {
        Account vAccount = new Account("Иванов Петр Иванович");
        Currency vUsd = Currency.getInstance("USD");
        Currency vEur= Currency.getInstance("EUR");


        vAccount.changeBal(vUsd,2000);
        vAccount.changeBal(vEur,1000);


        Map<Currency, Integer> vCurrCount = vAccount.getAccCurrCount();


        if(vCurrCount.get(vUsd) != 2000 )    {
                throw new RuntimeException("Error test : Не записывается валюта 1 либо количество");

        }

        if(vCurrCount.get(vEur) != 1000 )    {

                throw new RuntimeException("Error test : Не записывается валюта 2 либо количество");

        }

    }
    @Test
    //////////////////////////////////////////////////////////////////////
    // Getter  валюты позволяет менять суммы
    public void CurrencyGetCheck()    {
        Account vAccount = new Account("Иванов Петр Иванович");
        Currency vUsd = Currency.getInstance("USD");



        vAccount.changeBal(vUsd,2000);



        Map<Currency, Integer> vCurrCount = vAccount.getAccCurrCount();

        vCurrCount.replace(vUsd, 100);

        vCurrCount = vAccount.getAccCurrCount();
        if(vCurrCount.get(vUsd) != 2000 )    {
            throw new RuntimeException("Error test : Getter  валюты позволяет менять суммы");

        }



    }
    @Test
    /////////////////////////////////////////////////////////////////////
    // валюта не проверяется по списку допустимых значений
    public void currInValidCreate() {
        Account vAccount = new Account("Иванов Петр Иванович");



        try {
            vAccount.changeBal(null,1000);
        } catch (IllegalArgumentException err) {return;}
        throw new RuntimeException("Error test : валюта не проверяется по списку допустимых значений");
    }
    @Test
    // Количество валюты не проверяется на отрицательное значение
    public void CurrNegotCreate() {
        Account vAccount = new Account("Иванов Петр Иванович");
        Currency vEur= Currency.getInstance("EUR");


        try {
            vAccount.changeBal(vEur,-1000);
        } catch (IllegalArgumentException err) {return;}
        throw new RuntimeException("Error test : Количество валюты не проверяется на отрицательное значение");
    }
    /*
    @Test
    // Проверка метод undo при попытке откатить без измененицй выдает ошибку
    public void undoNoChange() {
        Account vAccount = new Account("Иванов Петр Иванович");
        try {
            vAccount.undo();
        }
        catch (IllegalArgumentException err) {return;}
        throw new RuntimeException("Error test : метод undo при попытке откатить без измененицй не выдает ошибку");

    }

     */
    @Test
    // Проверка работы undo
    public void undoNWorkFrame()    {
        Account vAccount = new Account("Кузнецов Николай Петрович");
        Currency vRur= Currency.getInstance("RUR");
        vAccount.changeBal(vRur,100);
        vAccount.setName("Василий Иванов");

        vAccount.changeBal(vRur,100);
        vAccount.undo();
        Map<Currency, Integer> vCurrCount = vAccount.getAccCurrCount();




        if (vCurrCount.get(vRur) != 100) {
                    throw new RuntimeException("Error test : метод undo неверно откатывает 1 раз колчество валюты");
        }

        if (vCurrCount.get(vRur)==null) {
            throw new RuntimeException("Error test : метод undo при откате колчества валюты , валюта удаляет из объекта");

        }


        vAccount.undo();
        if (!vAccount.getName().equals("Кузнецов Николай Петрович"))   {
            throw new RuntimeException("Error test : метод undo  неверно откатывает наименование");

        }
        vAccount.undo();
        vCurrCount = vAccount.getAccCurrCount();

        if (vCurrCount.get(vRur)!=null) {
            throw new RuntimeException("Error test : метод undo при откате  валюты , валюта не пропадает из объекта");

        }

    }
    // Проверка метод undo при попытке откатить без измененицй выдает ошибку
    @Test
    public void checkEmptyUndo() {
        Account vAccount = new Account("Сидоров И.К");
        try {
            vAccount.undo();
        } catch (IllegalArgumentException err) {return;}
        throw new RuntimeException("Error test : Account on undo don't check for undo nothing");
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
        String aSaveName1 ="sp1";




        AccSaver vAccSaver =  vAccount.save( aSaveName1);
        // System.out.printlnBAccount("After save vAccSaver = "+vAccSaver);
        accSavers.saves = new HashMap<String , AccSaver>();
        accSavers.saves.put(aSaveName1, vAccSaver);

        // System.out.printlnBAccount("After put reread vAccSaver = "+accSavers.saves.get(aSaveName1));

        vAccount.setName("Иван Васильевич");



        vAccount.changeBal(vUsd,6000);
        vAccount.changeBal(vEur,3000);


        String aSaveName2 = "sp2";

        vAccSaver =  vAccount.save( aSaveName2);


        accSavers.saves.put(aSaveName2, vAccSaver);
        vAccount.setName("Pol Robson");




        // System.out.printlnBAccount("Before restore save1 = "+accSavers.saves.get(aSaveName1));
        vAccount.restore(accSavers.saves.get(aSaveName1));
        if (!vAccount.getName().equals("Сидоров И.К") )   {
            throw new RuntimeException("Error test : метод save  неверно сохраняет наименование");
        }
        Map<Currency, Integer> vCurrCount = vAccount.getAccCurrCount();

        if(vCurrCount.get(vUsd) == null | vCurrCount.get(vEur) == null )  {
            throw new RuntimeException("Error test : метод save не сохраняет валюту");

        }



        if(vCurrCount.get(vEur) != 1500 | vCurrCount.get(vUsd) != 2000) {
                    throw new RuntimeException("Error test : метод save неверно сохраняет колчество валюты");

        }




        vAccount.restore(accSavers.saves.get(aSaveName2));


        if (!vAccount.getName().equals("Иван Васильевич") )   {
            throw new RuntimeException("Error test : метод save  неверно сохраняет наименование");
        }
        vCurrCount = vAccount.getAccCurrCount();
        if(vCurrCount.get(vUsd) == null | vCurrCount.get(vEur) == null )  {
            throw new RuntimeException("Error test : метод save не сохраняет валюту");

        }



        if(vCurrCount.get(vUsd) != 6000 | vCurrCount.get(vEur) != 3000) {
            throw new RuntimeException("Error test : метод save неверно сохраняет колчество валюты");

        }


    }

    // Проверка метода undo при добавлении поля вид счета в класс Account
    // При наличии поля private String acntType  создадим расширение
    @Test
    public void etendsCheck()   {
        class BAccount {
            private String name;
            private final Map<Currency, Integer> accCurrCount= new HashMap<>();
            private String acntType;
            BAccount(String vName)  {
                setName(vName);
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

            ////////////////////////////////
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

            ////////////////////////////////



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

                    } else {
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




        if (vCurrCount.get(vRur) != 100) {
            throw new RuntimeException("Error test : метод undo неверно откатывает 1 раз колчество валюты");
        }

        if (vCurrCount.get(vRur)==null) {
            throw new RuntimeException("Error test : метод undo при откате колчества валюты , валюта удаляет из объекта");

        }


        vAccount.undo();
        if (!vAccount.getName().equals("Кузнецов Николай Петрович"))   {
            throw new RuntimeException("Error test : метод undo  неверно откатывает наименование");

        }

        vAccount.undo();
        vCurrCount = vAccount.getAccCurrCount();

        if (vCurrCount.get(vRur)!=null) {
            throw new RuntimeException("Error test : метод undo при откате  валюты , валюта не пропадает из объекта");

        }

        vAccount.undo();
        if (vAccount.getAccType() != "Специальный") {
            throw new RuntimeException("Error test : добавление нового поля в класс требует зменения метода undo");
        }

    }


}
