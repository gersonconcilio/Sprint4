import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
            String alfabeto = "ab";

            // ESTADOS
            String estados = "q1,q2,q3,q4,q5,q6,q7,q8";

            // ESTADO INICIAL EX: A
            String estadoInicial = "q1";

            // ESTADO FINAL PODE SER MAIS DE UM EX: BC
            String estadosFinais = "q1,q3,q7";

            // FUNÇÕES DE TRANSIÇÃO DE ESTADOS
            String [][] transicoes = new String[][]{
                    {"q1", "a", "q2"},
                    {"q1", "b", "q4"},
                    {"q2", "a", "q5"},
                    {"q2", "b", "q3"},
                    {"q3", "a", "q2"},
                    {"q3", "b", "q6"},
                    {"q4", "a", "q1"},
                    {"q4", "b", "q5"},
                    {"q5", "a", "q5"},
                    {"q5", "b", "q5"},
                    {"q6", "a", "q3"},
                    {"q6", "b", "q5"},
                    {"q7", "a", "q6"},
                    {"q7", "b", "q8"},
                    {"q8", "a", "q7"},
                    {"q8", "b", "q3"},
            };

            // CRIA UM AFD COM OS DADOS INSERIDOS
            AFD meuAFD = new AFD(estados, alfabeto, estadoInicial, estadosFinais, transicoes);

            // PALAVRA PARA TESTAR O AUTOMATO
            String palavra = "baababbaab";

            ArrayList<ArrayList<String>> configuracoes = new ArrayList<ArrayList<String>>();

            meuAFD.Reconhecer(palavra,configuracoes);


        System.out.println("--------------------");
        System.out.println("--------------------");
        System.out.println("--------------------");
        System.out.println("--------------------");
        System.out.println("--------------------");
        System.out.println("--------------------");

        meuAFD.minimizar();

        System.out.println("--------------------");
        System.out.println("--------------------");
        System.out.println("--------------------");
        System.out.println("--------------------");
        System.out.println("--------------------");
        System.out.println("--------------------");
    }
    
    boolean Reconhecer(String palavra, ArrayList<ArrayList<String>> configuracoes) {//Função onde será realizado as transições, e retorna true: reconhecida e false: não reconhecida
        String palavraAux = palavra;
        String estadoAtual = this.estadoInicial;

        // VERIFICA O TAMANHO DA PALAVRA PARA SER TESTADA
        System.out.println("----------TRANSIÇÕES----------");

        configuracoes.add( new ArrayList<String>(Arrays.asList(new String[]{estadoAtual, palavraAux})));

        // LOGICA APLICADA PARA FAZER A TRANSIÇÃO DE UM ESTADO PARA O OUTRO
            for (int i = 0; i < this.transicoes.length; ++i) {
                if (this.transicoes[i][1].equals(palavraAux.substring(0, 1))) {
                    if (this.transicoes[i][0].equals(estadoAtual)) {
                        System.out.println("(" + estadoAtual + ", " + palavraAux + ")");

                        palavraAux = palavraAux.substring(1);
                        estadoAtual = this.transicoes[i][2];

                        if (palavraAux.length() == 0) {
                            configuracoes.add( new ArrayList<String>(Arrays.asList(new String[]{estadoAtual, "ε"})));
                            System.out.println("(" + estadoAtual + ", ε)");
                            break;
                        }

                        configuracoes.add( new ArrayList<String>(Arrays.asList(new String[]{estadoAtual, palavraAux})));

                        i = -1;
                    }
                }
            }

           System.out.println("----------TESTE INICIO FINALIZADO----------");
           System.out.println("Autômato finalizado!!!");
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

    public void imprimeConfig(ArrayList<ArrayList<String>> configuracoes){
        for(int i = 0; i < configuracoes.size(); i++){
            System.out.println("(" + configuracoes.get(i).get(0) + ", " + configuracoes.get(i).get(1) + ")");
        }
    }

    private void estadosAtingiveis(String estado, ArrayList<String> estados) {
        for (int i = 0; i < this.transicoes.length; i++) {
            if (estado.equals(this.transicoes[i][0]) && !estados.contains(this.transicoes[i][2])) {
                estados.add(this.transicoes[i][2]);
                Collections.sort(estados);
                estadosAtingiveis(this.transicoes[i][2], estados);
            }
        }
    }

    public void minimizar(){
        ArrayList<String> estados = new ArrayList<String>();

        // Adiciona todos os estados atingíveis
        estados.add(this.estadoInicial);

        estadosAtingiveis(estadoInicial, estados);

        for(int i = 0; i < estados.size(); i++){
            System.out.println(estados.get(i));
        }


    }
}
