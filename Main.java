import java.io.*;
import java.util.Scanner;


abstract class Pessoa {
    private String nome, cpf;

    public Pessoa(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    @Override
    public String toString() { return nome + ";" + cpf; }
}


class Cliente extends Pessoa {
    private String telefone;

    public Cliente(String nome, String cpf, String telefone) {
        super(nome, cpf);
        this.telefone = telefone;
    }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    @Override
    public String toString() { return getNome() + ";" + getCpf() + ";" + telefone; }
}


class Carro {
    private String placa, modelo;
    private double valorDiaria;

    public Carro(String placa, String modelo, double valorDiaria) {
        this.placa = placa;
        this.modelo = modelo;
        this.valorDiaria = valorDiaria;
    }

    public String getPlaca() { return placa; }
    public String getModelo() { return modelo; }
    public double getValorDiaria() { return valorDiaria; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public void setValorDiaria(double valorDiaria) { this.valorDiaria = valorDiaria; }

    @Override
    public String toString() { return placa + ";" + modelo + ";" + valorDiaria; }
}


class Locacao {
    private Cliente cliente; 
    private Carro carro;     
    private int dias;
    private boolean tanqueCheio;
    private double valorTotal;

    public Locacao(Cliente cliente, Carro carro, int dias, boolean tanqueCheio, double valorTotal) {
        this.cliente = cliente;
        this.carro = carro;
        this.dias = dias;
        this.tanqueCheio = tanqueCheio;
        this.valorTotal = valorTotal;
    }

    @Override
    public String toString() { 
        return cliente.getCpf() + ";" + carro.getPlaca() + ";" + dias + ";" + (tanqueCheio ? "Sim" : "Não") + ";" + valorTotal; 
    }
}

interface Relatorio {
    void gerarRelatorio();
}

class RelatorioLocacao implements Relatorio {
    @Override
    public void gerarRelatorio() {
        File arquivo = new File("locacoes.txt");
        if (!arquivo.exists()) {
            System.out.println("Nenhuma locação encontrada.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            System.out.println("\n===== RELATÓRIO DE LOCAÇÕES =====");
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue; 
                String[] d = linha.split(";");
                System.out.println("\nCPF Cliente: " + d[0] + "\nPlaca Carro: " + d[1] + "\nDias: " + d[2]);
                System.out.println("Tanque Cheio: " + (d.length > 3 ? d[3] : "Não informado"));
                System.out.println("Valor Total: R$ " + (d.length > 4 ? d[4] : "Não calculado"));
                System.out.println("---------------------------------");
            }
        } catch (Exception e) {
            System.out.println("Erro ao ler o relatório.");
        }
    }
}

class Login {
    private static final String ARQUIVO_SENHAS = "senhas.txt";

    public static boolean autenticar() {
        Scanner sc = new Scanner(System.in);
        String senhaAtual = lerUltimaSenha();

        if (senhaAtual == null) {
            System.out.println("Primeiro acesso.");
            cadastrarNovaSenha();
            return true;
        }

        for (int t = 0; t < 3; t++) {
            System.out.print("Senha: ");
            if (sc.nextLine().equals(senhaAtual)) {
                System.out.println("Login realizado!");
                return true;
            }
            System.out.println("Senha incorreta.");
        }

        System.out.println("3 tentativas excedidas.");
        cadastrarNovaSenha();
        return true;
    }

    private static String lerUltimaSenha() {
        File arq = new File(ARQUIVO_SENHAS);
        if (!arq.exists()) return null;
        try (BufferedReader br = new BufferedReader(new FileReader(arq))) {
            String linha, ultima = null;
            while ((linha = br.readLine()) != null) ultima = linha;
            return ultima;
        } catch (Exception e) { return null; }
    }

    private static boolean senhaExiste(String senha) {
        File arq = new File(ARQUIVO_SENHAS);
        if (!arq.exists()) return false;
        try (BufferedReader br = new BufferedReader(new FileReader(arq))) {
            String[] ultimas = new String[3];
            String linha;
            int i = 0;
            while ((linha = br.readLine()) != null) {
                ultimas[i % 3] = linha;
                i++;
            }
            for (String s : ultimas) {
                if (senha.equals(s)) return true;
            }
        } catch (Exception e) {}
        return false;
    }

    public static void cadastrarNovaSenha() {
        Scanner sc = new Scanner(System.in);
        try {
            String senha;
            while (true) {
                System.out.print("Nova senha: ");
                senha = sc.nextLine();
                if (!senhaExiste(senha)) break;
                System.out.println("Não pode repetir as últimas 3 senhas.");
            }
            try (FileWriter fw = new FileWriter(ARQUIVO_SENHAS, true)) {
                fw.write(senha + "\n");
                System.out.println("Senha salva.");
            }
        } catch (Exception e) { System.out.println("Erro."); }
    }
}

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void cadastrarCliente() {
        try (FileWriter fw = new FileWriter("clientes.txt", true)) {
            System.out.print("Nome: "); String nome = sc.nextLine();
            System.out.print("CPF: "); String cpf = sc.nextLine();
            System.out.print("Telefone: "); String tel = sc.nextLine();
            fw.write(new Cliente(nome, cpf, tel).toString() + "\n");
            System.out.println("Cliente cadastrado!");
        } catch (Exception e) { System.out.println("Erro."); }
    }

    public static void cadastrarCarro() {
        try (FileWriter fw = new FileWriter("carros.txt", true)) {
            System.out.print("Placa: "); String placa = sc.nextLine();
            System.out.print("Modelo: "); String modelo = sc.nextLine();
            System.out.print("Valor diária: "); double valor = Double.parseDouble(sc.nextLine());
            fw.write(new Carro(placa, modelo, valor).toString() + "\n");
            System.out.println("Carro cadastrado!");
        } catch (Exception e) { System.out.println("Erro."); }
    }

    public static void consultarClientes() {
        try (BufferedReader br = new BufferedReader(new FileReader("clientes.txt"))) {
            System.out.println("\n===== CLIENTES =====\n");
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] d = linha.split(";");
                System.out.println("Nome: " + d[0] + " | CPF: " + d[1] + " | Tel: " + d[2]);
            }
        } catch (Exception e) { System.out.println("Nenhum cliente encontrado."); }
    }

    public static void consultarCarros() {
        try (BufferedReader br = new BufferedReader(new FileReader("carros.txt"))) {
            System.out.println("\n===== CARROS =====\n");
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] d = linha.split(";");
                System.out.println("Placa: " + d[0] + " | Modelo: " + d[1] + " | Diária: R$ " + d[2]);
            }
        } catch (Exception e) { System.out.println("Nenhum carro encontrado."); }
    }

    
    public static void excluirCarros() {
        try (FileWriter fw = new FileWriter("carros.txt", false)) { // false limpa o arquivo
            System.out.println("Todos os registros de carros foram excluídos com sucesso!");
        } catch (Exception e) { System.out.println("Erro ao excluir."); }
    }

    private static Carro buscarCarroObjeto(String placa) {
        File arq = new File("carros.txt");
        if (!arq.exists()) return null;
        try (BufferedReader br = new BufferedReader(new FileReader(arq))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] d = linha.split(";");
                if (d[0].equalsIgnoreCase(placa)) return new Carro(d[0], d[1], Double.parseDouble(d[2]));
            }
        } catch (Exception e) {}
        return null; 
    }

    public static void registrarLocacao() {
        try (FileWriter fw = new FileWriter("locacoes.txt", true)) {
            System.out.print("CPF Cliente: "); String cpf = sc.nextLine();
            System.out.print("Placa Carro: "); String placa = sc.nextLine();
            System.out.print("Dias de locação: "); int dias = Integer.parseInt(sc.nextLine());
            System.out.print("Tanque cheio? (S/N): "); boolean tanque = sc.nextLine().trim().equalsIgnoreCase("S");

            Carro carro = buscarCarroObjeto(placa);
            if (carro == null) {
                System.out.println("Carro não cadastrado!");
                return;
            }

            Cliente cliente = new Cliente("Cliente Locação", cpf, "Não informado");
            double total = carro.getValorDiaria() * dias;

            // Instancia a classe Locacao passando objetos compostos reais!
            Locacao locacao = new Locacao(cliente, carro, dias, tanque, total);
            fw.write(locacao.toString() + "\n");
            System.out.println("Locação registrada! Total: R$ " + total);
        } catch (Exception e) { System.out.println("Erro ao registrar locação."); }
    }

    public static void main(String[] args) {
        if (!Login.autenticar()) return;

        int opcao;
        do {
            System.out.println("\n========================\nLOCADORA DE CARROS\n========================");
            System.out.println("1 - Cadastrar Cliente\n2 - Consultar Clientes\n3 - Cadastrar Carro\n4 - Consultar Carros\n5 - Registrar Locação\n6 - Relatório\n7 - Limpar Registros de Carros (Excluir)\n0 - Sair");
            System.out.print("Opção: ");
            opcao = Integer.parseInt(sc.nextLine());

            
            switch (opcao) {
                case 1 -> cadastrarCliente();
                case 2 -> consultarClientes();
                case 3 -> cadastrarCarro();
                case 4 -> consultarCarros();
                case 5 -> registrarLocacao();
                case 6 -> new RelatorioLocacao().gerarRelatorio();
                case 7 -> excluirCarros();
                case 0 -> System.out.println("Sistema encerrado.");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }
}
