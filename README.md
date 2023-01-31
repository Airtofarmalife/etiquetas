# Passos utilizados para criacão:

1. Buildar o jar file com o maven
	 ```
	 mvn clean package
	 ```

2. Utilizar a ferramenta Launch4j (ou similar) para criar o executável para windows:
	https://launch4j.sourceforge.net/

3. Criar um instalador para instalar o programa em "C: Arquivos de programas/etiquetas"

	Foi utilizado o Inno Setup Compiler: https://jrsoftware.org/isinfo.php

	** Os seguintes diretorios são criados em C:etiquetas pelo programa durante a execucão:
	 	- Banco de dados 
	 	- Pasta de arquivos de imagens/pdf
	 	- Arquivos do Jasper Reports
