import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AFD {
    private ArrayList<String> estados;
    private char[] alfabeto;
    private String estadoInicial;
    private ArrayList<String> estadosFinais;
    private String[][] transicoes;


    public AFD(String estados, String alfabeto, String estadoInicial, String estadosFinais, String[][] transicoes){
        this.estados = new ArrayList<>(Arrays.asList(estados.trim().split("\\s*,\\s*")));
        this.alfabeto = alfabeto.toCharArray();
        this.estadoInicial = estadoInicial;
        this.estadosFinais = new ArrayList<>(Arrays.asList(estadosFinais.trim().split("\\s*,\\s*")));;
        this.transicoes = transicoes;
    }


    public static void main(String[] args) {
        // DADOS INSERIDOS PELO USUARIO
            Scanner in = new Scanner(System.in);
/*
        System.out.println("Digite o alfabeto?");
        this.alfabeto = in.nextLine();
        this.alf = this.alfabeto.toCharArray();

                    // ESTADO INICIAL EX: A
        System.out.println("Digite o estado inicial?");
        this.estinicial = in.nextLine();
        this.ini = this.estinicial.charAt(i);
        this.atual = this.ini;
                    // ESTADO FINAL PODE SER MAIS DE UM EX: BC
        System.out.println("Digite o estado final?");
        estfinal = in.nextLine();
        estfim = estfinal.toCharArray();
                    // FUNÇOES DE TRANSIÇÃO DE ESTADOS
                    // EX: A0B    ESTADO ATUAL: A  VALOR DA TRANSIÇÃO: 0 PROX. ESTADO: B
        System.out.println("Digite as transiçoes de estado separando por ',' uma da outra?");
        funcoes = in.nextLine();

        String transicao[] = funcoes.split(",");
                // PALAVRA PARA TESTAR O AUTOMATO
        System.out.println("Digite a palavra para testar o automato?");
        palavra = in.nextLine();
        pal = palavra.toCharArray();
*/

            // ALFABETO
            String alfabeto = "01";

            // ESTADOS
            String estados = "a,b";

            // ESTADO INICIAL EX: A
            String estadoInicial = "a";

            // ESTADO FINAL PODE SER MAIS DE UM EX: BC
            String estadosFinais = "a";

            // FUNÇOES DE TRANSIÇÃO DE ESTADOS
            String [][] transicoes = new String[][]{
                    {"a", "0", "b"},
                    {"a", "1", "a"},
                    {"b", "0", "a"},
                    {"b", "1", "b"},
            };

            AFD meuAFD = new AFD(estados, alfabeto, estadoInicial, estadosFinais, transicoes);

            // PALAVRA PARA TESTAR O AUTOMATO
            String palavra = "0001";

            List<String[]> configuracoes = new ArrayList<String[]>();

            meuAFD.Reconhecer(palavra,configuracoes);
    }
    
    boolean Reconhecer(String palavra, List<String[]> matriz) {//Função onde será realizado as transições, e retorna true: reconhecida e false: não reconhecida
        List<String> palavraList = new ArrayList<>(Arrays.asList(palavra.split("")));
        String estadoAtual = this.estadoInicial;

        // VERIFICA O TAMANHO DA PALAVRA PARA SER TESTADA
        System.out.println("----------TRANSIÇÕES----------");

        // LOGICA APLICADA PARA FAZER A TRANSIÇÃO DE UM ESTADO PARA O OUTRO
        while (palavraList.size() > 0){
            for (int i = 0; i < this.transicoes.length; ++i) {
                if (this.transicoes[i][1].equals(palavraList.get(0))) {
                    if (this.transicoes[i][0].equals(estadoAtual)) {
                        String estadoAnterior = estadoAtual;
                        estadoAtual = this.transicoes[i][2];

                        palavraList.remove(0);

                        System.out.print("(" + estadoAnterior + " ,");//Print para mostrar o estado onde estava

                        if (palavraList.isEmpty()) {
                            System.out.println("" + "&)");
                            break;
                        }

                        for (int j = 0; j < palavraList.size(); j++) {
                            System.out.print("" + palavraList.get(j));//Print para mostrar a palavra consumida
                        }
                        System.out.println(")");
                    }
                }
            }
        }
           System.out.println("----------TESTE INICIO FINALIZADO----------");
           System.out.println("Automato finalizado!!!");
           System.out.println("Estado final: " + estadoAtual);
           System.out.println("--------------------");

           // MOSTRA SE FOI RECONHECIDA A PALAVRA OU NAO
           if(this.estadosFinais.contains(estadoAtual)){
            System.out.println("Palavra reconhecida pelo autômato!!!");
            return true;
          }else{
               System.out.println("Palavra rejeitada pelo autômato!!!");
               return false;
         }

    }
/*
    public AFD minimizar(AFD automato){

    }
    */
}
