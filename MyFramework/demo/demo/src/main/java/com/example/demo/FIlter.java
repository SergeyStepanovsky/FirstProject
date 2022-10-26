package com.example.demo;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;


public abstract class FIlter {

// В этом методе фильтруются таблицы по колличеству колонок, строк и по наличию тега
//    table нутри других таблиц
    public static void FilterByTableRowsColumns(Element element) {
        Elements elements = element.getAllElements(); // Получаем все дочерние элементы нашего аргумента element
        Elements tablesForUse = null;
        // В этом цикле отбрасываем ненужные таблицы и добавляем нуужные в список

        boolean FilterForRows;
        Elements rows = element.getElementsByTag("tr");
        int countRows = rows.size();
        if( countRows > 1){
            FilterForRows = true;
        }
        else{
            FilterForRows = false;
        }

       boolean FilterForColumns;
        int[] buf = new int[countRows]; //буфернй массив для подсчета количества столбцов в каждой строке
        //считаем количество столбцов
        for (int i = 0; i < buf.length; i++) {
            buf[i] = rows.get(i).getElementsByTag("td").size()
                    + rows.get(i).getElementsByTag("th").size();
        }
        Arrays.sort(buf); // сортируем массив buf, получаем
        int countColumns = buf[buf.length - 1];// переменная с количесвом столбцов
       if(countColumns > 1){
           FilterForColumns = true;
       }
       else {
           FilterForColumns = false;
       }

        for (int i = 0; i < elements.size() ; i++) {
            if (elements.get(i).getElementsByTag("table").size() == 0 && FilterForRows == true && FilterForColumns == true){
        tablesForUse.add(elements.get(i));
            }
        }
    }
}