/*
Реализовать класс Trie, поддерживающий:
- void insert(String word) — вставка слова;
- boolean contains(String word) — проверка наличия слова;
- boolean startsWith(String prefix) — проверка существования слов с данным префиксом;
- List<String> getByPrefix(String prefix) — получение всех слов по префиксу.
*/

import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Trie trie = new Trie();
        Scanner scanner = new Scanner(System.in);

        printInfo();

        out:
        while (true) {
            String text = scanner.nextLine();
            String[] command = text.trim().split("\\s+");
            try {
                switch (command[0]) {
                    case "insert":
                        trie.insert(command[1]);
                        System.out.println("successful added");
                        break;

                    case "contains":
                        System.out.println(trie.contains(command[1]));
                        break;

                    case "startsWith":
                        System.out.println(trie.startsWith(command[1]));
                        break;

                    case "stop":
                        break out;

                    case "getByPrefix":
                        MyList<String> arr = trie.getByPrefix(command[1]);
                        for (String s: arr){
                            System.out.println(s);
                        }
                        break;

                    case "load":
                        trie.loadFromFile(command[1]);
                        break;

                    case "all":
                        MyList<String> allw = trie.getByPrefix("");
                        for (String s: allw){
                            System.out.println(s);
                        }
                        break;

                    case "info":
                        printInfo();

                    default:
                        System.out.println("Неверная команда");
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Ошибка: недостаточно аргументов для команды");
            } catch (Exception e) {
                System.out.println("Неожиданная ошибка: " + e.getMessage());
            }
        }
    }


    private static void printInfo() {
        System.out.println("Доступные команды:");
        System.out.println("  insert <слово>     — Добавить слово в Trie.");
        System.out.println("  contains <слово>   — Проверить, содержится ли слово в Trie.");
        System.out.println("  startsWith <префикс> — Проверить, есть ли слова с указанным префиксом.");
        System.out.println("  getByPrefix <префикс> — Вывести все слова, начинающиеся с префикса.");
        System.out.println("  all                — Вывести все слова в Trie.");
        System.out.println("  load <путь_к_файлу> — Загрузить слова из файла в Trie.");
        System.out.println("  info               — Вывести это сообщение с информацией о командах.");
        System.out.println("  stop               — Завершить выполнение программы.");
    }
}



