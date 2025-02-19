#!/bin/bash

# Diretório base para instalações locais
BASE_DIR="$HOME/dev-tools"
mkdir -p "$BASE_DIR"

echo "Configurando ambiente de desenvolvimento na pasta $BASE_DIR..."

# 1. Instalar SDKMAN!
if ! command -v sdk &> /dev/null; then
    echo "Instalando SDKMAN!..."
    curl -s "https://get.sdkman.io" | bash
    source "$HOME/.sdkman/bin/sdkman-init.sh"
else
    echo "SDKMAN! já está instalado."
fi

# 2. Instalar Java usando SDKMAN!
echo "Instalando Java via SDKMAN!..."
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 21.0.4-oracle

# 3. Instalar NVM
if ! command -v nvm &> /dev/null; then
    echo "Instalando NVM..."
    curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.5/install.sh | bash
    export NVM_DIR="$HOME/.nvm"
    [ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"
    [ -s "$NVM_DIR/bash_completion" ] && \. "$NVM_DIR/bash_completion"
else
    echo "NVM já está instalado."
fi

echo "Instalando Node.js via NVM..."
nvm install --lts
nvm use --lts

# 4. Instalar Angular CLI globalmente (via npm)
echo "Instalando Angular CLI..."
npm install -g @angular/cli

# 5. Instalar Docker Desktop (requer permissão de root) ou Docker Toolbox (sem root)
# Como não temos root, usaremos Docker Toolbox
#if ! command -v docker &> /dev/null; then
#    echo "Baixando e instalando Docker Toolbox..."
#    DOCKER_TOOLBOX_URL="https://github.com/docker/toolbox/releases/download/v19.03.1/DockerToolbox-19.03.1.pkg"
#    curl -L "$DOCKER_TOOLBOX_URL" -o "$BASE_DIR/docker-toolbox.pkg"
#    echo "Execute o instalador manualmente: $BASE_DIR/docker-toolbox.pkg"
#else
#    echo "Docker já está instalado."
#fi

# 6. Baixar e configurar DBeaver
if [ ! -d "$BASE_DIR/dbeaver" ]; then
    echo "Baixando DBeaver..."
    DBEAVER_URL="https://dbeaver.io/files/dbeaver-ce-latest-linux.gtk.x86_64.tar.gz"
    curl -L "$DBEAVER_URL" -o "$BASE_DIR/dbeaver.tar.gz"
    tar -xzf "$BASE_DIR/dbeaver.tar.gz" -C "$BASE_DIR"
    rm "$BASE_DIR/dbeaver.tar.gz"
    echo "DBeaver extraído em $BASE_DIR/dbeaver"
else
    echo "DBeaver já está configurado."
fi

# 7. Baixar e configurar IntelliJ IDEA Community Edition (instalação local)
if [ ! -d "$BASE_DIR/idea" ]; then
    echo "Baixando IntelliJ IDEA Community Edition..."
    IDEA_URL="https://www.jetbrains.com/idea/download/download-thanks.html?platform=linux"
    curl -L "$IDEA_URL" -o "$BASE_DIR/idea.tar.gz"
    tar -xzf "$BASE_DIR/idea.tar.gz" -C "$BASE_DIR"
    rm "$BASE_DIR/idea.tar.gz"
    echo "IntelliJ IDEA extraído em $BASE_DIR/idea"
else
    echo "IntelliJ IDEA já está configurado."
fi

# 8. Instalar Visual Studio Code
if [ ! -d "$BASE_DIR/vscode" ]; then
    echo "Baixando Visual Studio Code..."
    VSCODE_URL="https://update.code.visualstudio.com/latest/linux-deb-x64/stable"
    curl -L "$VSCODE_URL" -o "$BASE_DIR/vscode.deb"
    dpkg-deb -x "$BASE_DIR/vscode.deb" "$BASE_DIR/vscode"
    rm "$BASE_DIR/vscode.deb"
    echo "Visual Studio Code extraído em $BASE_DIR/vscode"
else
    echo "Visual Studio Code já está configurado."
fi

# 9. Baixar e configurar Sublime Text
#if [ ! -d "$BASE_DIR/sublime" ]; then
#    echo "Baixando Sublime Text..."
#    SUBLIME_URL="https://download.sublimetext.com/sublime_text_build_4152_x64.tar.xz"
#    curl -L "$SUBLIME_URL" -o "$BASE_DIR/sublime.tar.xz"
#    tar -xf "$BASE_DIR/sublime.tar.xz" -C "$BASE_DIR"
#    rm "$BASE_DIR/sublime.tar.xz"
#    echo "Sublime Text extraído em $BASE_DIR/sublime_text"
#else
#    echo "Sublime Text já está configurado."
#fi

# Finalização
echo "Ambiente configurado com sucesso!"
echo "Certifique-se de adicionar os binários ao PATH, se necessário:"
echo "export PATH=\$PATH:$BASE_DIR/dbeaver:$BASE_DIR/idea/bin:$BASE_DIR/vscode/bin:$BASE_DIR/sublime_text"