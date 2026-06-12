import java.io.*;
import java.util.Scanner;

abstract class Pessoa {

    private String nome;
    private String cpf;

    public Pessoa(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return nome + ";" + cpf;
    }
}

class Cliente extends Pessoa {

    private String telefone;

    public Cliente(String nome, String cpf, String telefone) {
        super(nome, cpf);
        this.telefone = telefone;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return getNome() + ";" + getCpf() + ";" + telefone;
    }
}

class Carro {

    private String placa;
    private String modelo;
    private double valorDiaria;

    public Carro(String placa, String modelo, double valorDiaria) {
        this.placa = placa;
        this.modelo = modelo;
        this.valorDiaria = valorDiaria;
    }

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public double getValorDiaria() {
        return valorDiaria;
    }

    @Override
    public String toString() {
        return placa + ";" + modelo + ";" + valorDiaria;
    }
}

class Locacao {

    private String cpfCliente;
    private String placaCarro;
    private int dias;

    public Locacao(String cpfCliente, String placaCarro, int dias) {
        this.cpfCliente = cpfCliente;
        this.placaCarro = placaCarro;
        this.dias = dias;
    }

    @Override
    public String toString() {
        return cpfCliente + ";" + placaCarro + ";" + dias;
    }
}

interface Relatorio {
    void gerarRelatorio();
}

class RelatorioLocacao implements Relatorio {

    @Override
    public void gerarRelatorio() {

        try {

            BufferedReader br =
                    new BufferedReader(
                            new FileReader("locacoes.txt"));

            String linha;

            System.out.println("\n===== RELATÓRIO =====\n");

            while ((linha = br.readLine()) != null) {
                System.out.println(linha);
            }

            br.close();

        } catch (Exception e) {
            System.out.println("Nenhuma locação encontrada.");
        }
    }
}

class Login {

    public static boolean autenticar() {

        Scanner sc = new Scanner(System.in);

        String senhaAtual = lerUltimaSenha();

        if (senhaAtual == null) {

            System.out.println("Primeiro acesso.");
            cadastrarNovaSenha();
            return true;
        }

        int tentativas = 0;

        while (tentativas < 3) {

            System.out.print("Senha: ");
            String senha = sc.nextLine();

            if (senha.equals(senhaAtual)) {

                System.out.println("Login realizado!");
                return true;
            }

            tentativas++;

            System.out.println("Senha incorreta.");

        }

        System.out.println("3 tentativas excedidas.");
        cadastrarNovaSenha();

        return true;
    }

    private static String lerUltimaSenha() {

        try {

            File arquivo = new File("senhas.txt");

            if (!arquivo.exists()) {
                return null;
            }

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(arquivo));

            String linha;
            String ultima = null;

            while ((linha = br.readLine()) != null) {
                ultima = linha;
            }

            br.close();

            return ultima;

        } catch (Exception e) {

            return null;
        }
    }

    private static boolean senhaExiste(String senha) {

        try {

            File arquivo = new File("senhas.txt");

            if (!arquivo.exists()) {
                return false;
            }

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(arquivo));

            String[] ultimas = new String[3];

            String linha;
            int i = 0;

            while ((linha = br.readLine()) != null) {

                ultimas[i % 3] = linha;
                i++;
            }

            br.close();

            for (String s : ultimas) {

                if (senha.equals(s)) {
                    return true;
                }
            }

        } catch (Exception e) {
        }

        return false;
    }

    public static void cadastrarNovaSenha() {

        Scanner sc = new Scanner(System.in);

        try {

            String senha;

            while (true) {

                System.out.print("Nova senha: ");
                senha = sc.nextLine();

                if (!senhaExiste(senha)) {
                    break;
                }

                System.out.println(
                        "Não pode repetir as últimas 3 senhas.");
            }

            FileWriter fw =
                    new FileWriter("senhas.txt", true);

            fw.write(senha + "\n");

            fw.close();

            System.out.println("Senha salva.");

        } catch (Exception e) {

            System.out.println("Erro.");
        }
    }
}

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void cadastrarCliente() {

        try {

            System.out.print("Nome: ");
            String nome = sc.nextLine();

            System.out.print("CPF: ");
            String cpf = sc.nextLine();

            System.out.print("Telefone: ");
            String telefone = sc.nextLine();

            Cliente c =
                    new Cliente(nome, cpf, telefone);

            FileWriter fw =
                    new FileWriter(
                            "clientes.txt", true);

            fw.write(c.toString() + "\n");

            fw.close();

            System.out.println("Cliente cadastrado!");

        } catch (Exception e) {

            System.out.println("Erro.");
        }
    }

    public static void cadastrarCarro() {

        try {

            System.out.print("Placa: ");
            String placa = sc.nextLine();

            System.out.print("Modelo: ");
            String modelo = sc.nextLine();

            System.out.print("Valor diária: ");
            double valor = Double.parseDouble(
                    sc.nextLine());

            Carro carro =
                    new Carro(placa, modelo, valor);

            FileWriter fw =
                    new FileWriter(
                            "carros.txt", true);

            fw.write(carro.toString() + "\n");

            fw.close();

            System.out.println("Carro cadastrado!");

        } catch (Exception e) {

            System.out.println("Erro.");
        }
    }

        public static void consultarClientes() {

        try {

            BufferedReader br =
                    new BufferedReader(
                            new FileReader("clientes.txt"));

            String linha;

            System.out.println("\n===== CLIENTES =====\n");

            while ((linha = br.readLine()) != null) {

                String[] dados = linha.split(";");

                System.out.println("Nome: " + dados[0]);
                System.out.println("CPF: " + dados[1]);
                System.out.println("Telefone: " + dados[2]);
                System.out.println("----------------------");
            }

            br.close();

        } catch (Exception e) {

            System.out.println("Nenhum cliente encontrado.");
        }
    }

    public static void consultarCarros() {

        try {

            BufferedReader br =
                    new BufferedReader(
                            new FileReader("carros.txt"));

            String linha;

            System.out.println("\n===== CARROS =====\n");

            while ((linha = br.readLine()) != null) {

                String[] dados = linha.split(";");

                System.out.println("Placa: " + dados[0]);
                System.out.println("Modelo: " + dados[1]);
                System.out.println("Valor Diária: R$ " + dados[2]);
                System.out.println("----------------------");
            }

            br.close();

        } catch (Exception e) {

            System.out.println("Nenhum carro encontrado.");
        }
    }

    public static void registrarLocacao() {

        try {

            System.out.print("CPF Cliente: ");
            String cpf = sc.nextLine();

            System.out.print("Placa Carro: ");
            String placa = sc.nextLine();

            System.out.print("Dias de locação: ");
            int dias = Integer.parseInt(
                    sc.nextLine());

            Locacao locacao =
                    new Locacao(cpf, placa, dias);

            FileWriter fw =
                    new FileWriter(
                            "locacoes.txt", true);

            fw.write(locacao.toString() + "\n");

            fw.close();

            System.out.println("Locação registrada!");

        } catch (Exception e) {

            System.out.println("Erro ao registrar locação.");
        }
    }

    public static void menu() {

        int opcao;

        do {

            System.out.println("\n========================");
            System.out.println("LOCADORA DE CARROS");
            System.out.println("========================");
            System.out.println("1 - Cadastrar Cliente");
            System.out.println("2 - Consultar Clientes");
            System.out.println("3 - Cadastrar Carro");
            System.out.println("4 - Consultar Carros");
            System.out.println("5 - Registrar Locação");
            System.out.println("6 - Relatório");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            opcao = Integer.parseInt(
                    sc.nextLine());

            switch (opcao) {

                case 1:
                    cadastrarCliente();
                    break;

                case 2:
                    consultarClientes();
                    break;

                case 3:
                    cadastrarCarro();
                    break;

                case 4:
                    consultarCarros();
                    break;

                case 5:
                    registrarLocacao();
                    break;

                case 6:

                   new RelatorioLocacao().gerarRelatorio();

                   
                    break;

                case 0:

                    System.out.println(
                            "Sistema encerrado.");
                    break;

                default:

                    System.out.println(
                            "Opção inválida.");
            }

        } while (opcao != 0);
    }

    public static void main(String[] args) {

        if (Login.autenticar()) {

            menu();

        }
    }
}
