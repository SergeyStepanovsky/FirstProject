package com.example.demo;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import webreduce.data.TableType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}



	@GetMapping("/extract")
	public Map<Element, TableType>[] extract(@RequestParam(value = "url", defaultValue = "") String url) throws Exception {


		//TODO фильтр (есть)

		Document doc = Jsoup.connect(url).get();
		String htmlResource = GetHTMLCode.getHtmlResourceByURL(url, "UTF-8");
		Document document = Jsoup.parse(htmlResource);
		Elements tables = document.getElementsByTag("table");
		Elements tablesForUse = FIlter.FilterForTable(tables);

		//TODO дискриминация(?)
		Map<Element, TableType> discrimenatedTables = new HashMap<>();
		Discriminator discriminator = new DiscriminatorErebius();
		for(Element tableForuse : tablesForUse)
		{
			discrimenatedTables.put(tableForuse, discriminator.classify(tableForuse)); //добавили в мапу список таблиц и типы таблиц
		}

		//TODO классификация(?)
		Map<Element, TableType> classifiedTables = new HashMap<>();
		TableClassifier classificator = new ClassifierErebius();
		for(Element tableForuse : tablesForUse)
		{
			classifiedTables.put(tableForuse, classificator.classify(tableForuse)); //добавили в мапу список таблиц и типы таблиц
		}


		//TODO упоковать в map


		return new Map[]{classifiedTables, discrimenatedTables};

	}


	@GetMapping("/CounttableJsoup")
	public String counttables(@RequestParam(value = "url", defaultValue = "https://example.com") String url) throws Exception {


		Document doc = Jsoup.connect(url).get(); //document – объект документа представляет HTML DOM.
		                                         //Jsoup – основной класс для подключения URL- адреса и получения строки HTML.
		                                         //метод get () возвращает html запрошенного URL.


		String htmlResource = GetHTMLCode.getHtmlResourceByURL(url,"UTF-8"); //Получаем код со страницы

		Document document = Jsoup.parse(htmlResource);
//Element главнй класс jsoup

		System.out.println(htmlResource); //Вывод HTML кода в консоль

		Elements tables = document.getElementsByTag("table");
//Elements Наследует ArrayList так шо здесь у нас список тэйблов

		System.out.println("\nРАЗДЕЛЕНИЕ");

		//getAllElements выбирает все дочерние элементы и дочерние элементы дочерних элементов
		System.out.println("\nввывод всех таблиц кода");
		System.out.println(tables);

		System.out.println("\nввывод всех отфильтровнных таблиц");
		System.out.println(FIlter.FilterForTable(tables));
		System.out.println("\n" + FIlter.FilterForTable(tables).size());




//		Gson gson = new GsonBuilder()
//				.setPrettyPrinting()
//				.create();
//
//		try(FileWriter writer = new FileWriter("TableToJson.json", false))
//		{
//			writer.write(gson.toJson(tableExample));
//		}
//		catch(IOException ex){
//			System.out.println(ex.getMessage());
//		}

		int count = tables.size(); //подсчет колличества тегов <table>
		return String.format("Кол - во тегов " + count + "\n" );


	}
}