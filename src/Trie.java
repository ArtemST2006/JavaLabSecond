import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;


class Node {
    char value;
    MyList<Node> child;
    boolean isTerminate;

    public Node(char v) {
        this.value = v;
        this.isTerminate = false;
    }

    public void insert(String data) {
        if (data.isEmpty()) {
            isTerminate = true;
            return;
        }
        if (child == null) {
            child = new MyList<>();
        }

        char c = data.charAt(0);
        Node childnode = find(c);
        if (childnode == null) {
            childnode = new Node(c);
            child.add(childnode);
        }
        childnode.insert(data.substring(1));
    }

    public boolean contains(String data) {
        if (data.isEmpty()) {
            return isTerminate;
        }
        char c = data.charAt(0);
        Node childnode = find(c);
        if (childnode == null) {
            return false;
        }
        return childnode.contains(data.substring(1));
    }

    public boolean startsWith(String prefix) {
        if (prefix.isEmpty()) {
            return true;
        }
        char c = prefix.charAt(0);
        Node childnode = find(c);
        if (childnode == null) {
            return false;
        }
        return childnode.startsWith(prefix.substring(1));
    }

    public MyList<String> collectWords(String prefix) {
        MyList<String> result = new MyList<>();
        collectWordsHelper(prefix, new StringBuilder(), result);
        return result;
    }

    private void collectWordsHelper(String prefix, StringBuilder current, MyList<String> result) {
        if (prefix.isEmpty()) {
            collectAllWords(current, result);
            return;
        }
        char c = prefix.charAt(0);
        Node childnode = find(c);
        if (childnode != null) {
            current.append(c);
            childnode.collectWordsHelper(prefix.substring(1), current, result);
            current.deleteCharAt(current.length() - 1);
        }
    }

    private void collectAllWords(StringBuilder current, MyList<String> result) {
        if (isTerminate) {
            result.add(current.toString());
        }
        if (child != null) {
            for (Node n : child) {
                current.append(n.value);
                n.collectAllWords(current, result);
                current.deleteCharAt(current.length() - 1);
            }
        }
    }

    private Node find(char c) {
        if (child != null) {
            for (Node node : child) {
                if (node.value == c) {
                    return node;
                }
            }
        }
        return null;
    }

    public boolean delete(String word) {
        if (word.isEmpty()) {
            if (!isTerminate) {
                return false;
            }
            isTerminate = false;
            return child == null || !child.iterator().hasNext();
        }

        char c = word.charAt(0);
        Node childNode = find(c);
        if (childNode == null) {
            return false;
        }

        boolean shouldDeleteChild = childNode.delete(word.substring(1));

        if (shouldDeleteChild) {
            removeChild(c);
            return !isTerminate && (child == null || !child.iterator().hasNext());
        }

        return false;
    }

    private void removeChild(char c) {
        if (child == null) return;

        NodeList<Node> prev = null;
        NodeList<Node> current = ((MyList<Node>) child).head;
        while (current != null) {
            if (current.data.value == c) {
                if (prev == null) {
                    ((MyList<Node>) child).head = current.next;
                    if (((MyList<Node>) child).head == null) {
                        ((MyList<Node>) child).tail = null;
                    }
                } else {
                    prev.next = current.next;
                    if (current.next == null) {
                        ((MyList<Node>) child).tail = prev;
                    }
                }
                return;
            }
            prev = current;
            current = current.next;
        }
    }

    public void printTree(String prefix, StringBuilder indent, boolean isLast) {
        if (value != ' ') {
            System.out.println(indent + (isLast ? "└── " : "├── ") + value + (isTerminate ? " [*]" : ""));
            if (child != null) {
                indent.append(isLast ? "    " : "│   ");
                int size = 0;
                for (Node n : child) size++;
                int i = 0;
                for (Node n : child) {
                    i++;
                    n.printTree(prefix, indent, i == size);
                }
                indent.setLength(indent.length() - 4);
            }
        } else {
            if (child != null) {
                System.out.println("Trie:");
                int size = 0;
                for (Node n : child) size++;
                int i = 0;
                for (Node n : child) {
                    i++;
                    n.printTree(prefix, indent, i == size);
                }
            } else {
                System.out.println("пусто");
            }
        }
    }
}

class Trie {
    private final Node root;

    public Trie() {
        root = new Node(' ');
    }

    public void insert(String data) {
        root.insert(data);
    }

    public boolean contains(String data) {
        return root.contains(data);
    }

    public boolean startsWith(String prefix) {
        return root.startsWith(prefix);
    }

    public MyList<String> getByPrefix(String prefix) {
        return root.collectWords(prefix);
    }

    public void loadFromFile(String filename) {
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String word = fileScanner.nextLine().trim();
                if (!word.isEmpty()) {
                    insert(word);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + filename);
        }
    }

    public void delete(String word) {
        if (word == null || word.isEmpty()) return;
        root.delete(word);
    }

    public void printTree() {
        StringBuilder indent = new StringBuilder();
        root.printTree("", indent, true);
    }

    public void saveToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            MyList<String> allWords = getByPrefix("");
            for (String word : allWords) {
                writer.write(word);
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + filename);
        }
    }
}