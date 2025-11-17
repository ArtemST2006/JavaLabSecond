import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
}