package ejercicio1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Ejercicio1 {
	char colorMancha;
	int tamanoDeMacha;
	int cont = 1;
	boolean[][] matrizLogica;
	
	public boolean [][] getMatrizLogica(){
		return this.matrizLogica;
	}
	private void restartContador() {
		this.cont = 1;
	}

	public void setMatrizLogica(int m, int n) {
		matrizLogica = new boolean[m][n];
			}

	public void porDerecha(int i, int j, char[][] matriz) {
		if(matriz[0].length -2 < j) return ;
		if(matriz[i][j] == matriz[i][j+1] ) {
			if(!matrizLogica[i][j+1]) {
				cont++;
				matrizLogica[i][j+1] = true;
				this.porDerecha(i, j+1, matriz);
				this.porAbajo(i, j+1, matriz);
				this.porArriba(i, j+1, matriz);
			}	
		}	
	}

	public void porAbajo(int i, int j, char[][] matriz) {
		if(matriz.length-2 < i ) return;
		if(matriz[i][j] == matriz[i+1][j]) {
			if(!matrizLogica[i+1][j]) {
				cont++;
				matrizLogica[i+1][j] = true;
				this.porDerecha(i+1, j, matriz);
				this.porIzquierda(i+1, j, matriz);
				this.porAbajo(i+1, j, matriz);
			}
		}
	}
	public void porArriba(int i, int j, char[][] matriz) {
		if(1 > i ) return;
		if(matriz[i][j] == matriz[i-1][j]) {
			if(!matrizLogica[i-1][j]) {
				cont++;
				matrizLogica[i-1][j] = true;
				this.porDerecha(i-1, j, matriz);
				this.porIzquierda(i-1, j, matriz);
				this.porArriba(i-1, j, matriz);
			}
		}
	}

	public void porIzquierda(int i, int j, char[][] matriz) {
		if(1 > j) return ;
		if(matriz[i][j] == matriz[i][j-1] ) {
			if(!matrizLogica[i][j-1]) {
				cont++;
				matrizLogica[i][j-1] = true;
				this.porIzquierda(i, j-1, matriz);
				this.porAbajo(i, j-1, matriz);
				this.porArriba(i, j-1, matriz);
			}
		}
	}
	
	public String bucarMancha(char[][] matriz) {
		for(int y = 0; y < this.matrizLogica.length; y++) {
			for(int x = 0; x < this.matrizLogica[y].length; x++) {
				if(!this.matrizLogica[y][x]) {
					this.matrizLogica[y][x] = true;
					this.porDerecha(y, x, matriz);
					this.porIzquierda(y, x, matriz);
					this.porAbajo(y, x, matriz);
					this.porArriba(y, x, matriz);
					if(this.tamanoDeMacha <this.cont) {
						this.tamanoDeMacha = cont;
						this.colorMancha = matriz[y][x];
					}
					this.restartContador();
				}
			}
		}
		return "("+(char)34+this.colorMancha+(char)34+", " +this.tamanoDeMacha+")";
	}

	public static void main(String[]args) throws IOException {
		long start = System.currentTimeMillis();
		
		FileReader fr = new FileReader(new File("ejercicio1-imput.txt"));
		BufferedReader br = new BufferedReader(fr);
		String linea;
		Ejercicio1 a1 = new Ejercicio1();
		ArrayList<char[]> matrizFake = new ArrayList<char[]>();
		boolean banderaCase = false;
		String caso ="";
		do{
			linea = br.readLine();
			if(linea==null ||35 == (linea.charAt(0))){
				if(banderaCase) {
					char[][] matriz = new char[matrizFake.size()][matrizFake.get(0).length];
					int indice = 0;
					for(char[] arr: matrizFake) {
						matriz[indice] = arr;
						indice++;
					}
					a1.setMatrizLogica(matrizFake.size(), matrizFake.get(0).length);
					System.out.println(caso);
					System.out.println(a1.bucarMancha(matriz));
					
				}
				caso = linea;
				banderaCase = true;
				a1 = new Ejercicio1();
				matrizFake.clear();

			}else {
				matrizFake.add(linea.toCharArray());
			}
		}while(linea !=null);
		System.out.println(System.currentTimeMillis()-start);
	}	
}
