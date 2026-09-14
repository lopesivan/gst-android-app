SHELL := /bin/bash

APP_ID       := dev.ivan.gstapp
ACTIVITY     := .MainActivity
GRADLE       := ./gradlew
MY_GRADLE_LOCAL := /opt/gradle/gradle-9.4.1/bin/gradle
APK_DEBUG    := app/build/outputs/apk/debug/app-debug.apk
GSTREAMER_ROOT_ANDROID ?= /opt/gstreamer
SDK_DIR      ?= /home/ivan/Android/Sdk
ADB          := $(SDK_DIR)/platform-tools/adb
JAVA_HOME    ?= $(shell jenv prefix 17 2>/dev/null || jenv prefix 21 2>/dev/null || readlink -f "$$(command -v java)" | sed 's:/bin/java$$::')
export JAVA_HOME

GREEN  := \033[0;32m
YELLOW := \033[0;33m
RED    := \033[0;31m
NC     := \033[0m

.PHONY: init configure check-gstreamer build install uninstall clean run

init:
	@echo -e "$(GREEN)==> Verificando wrapper do Gradle$(NC)"
	@if [ ! -f gradlew ]; then \
		echo -e "$(YELLOW)gradlew ausente, gerando wrapper$(NC)"; \
		$(MY_GRADLE_LOCAL) wrapper --gradle-version 8.9; \
	fi
	@chmod +x $(GRADLE)
	@echo -e "$(GREEN)==> init concluido$(NC)"

check-gstreamer:
	@echo -e "$(GREEN)==> Verificando GSTREAMER_ROOT_ANDROID$(NC)"
	@if [ ! -f "$(GSTREAMER_ROOT_ANDROID)/arm64/share/gst-android/ndk-build/gstreamer-1.0.mk" ]; then \
		echo -e "$(RED)GStreamer nao encontrado em $(GSTREAMER_ROOT_ANDROID)$(NC)"; \
		exit 1; \
	fi

configure: check-gstreamer
	@echo -e "$(GREEN)==> Verificando SDK Android$(NC)"
	@if [ ! -d "$(SDK_DIR)" ]; then \
		echo -e "$(RED)SDK nao encontrado em $(SDK_DIR)$(NC)"; \
		exit 1; \
	fi
	@echo -e "$(GREEN)==> Verificando JDK (Gradle 8.9 nao roda em JDK > 21)$(NC)"
	@if [ -z "$(JAVA_HOME)" ]; then \
		echo -e "$(RED)Nenhuma JDK 17/21 encontrada via jenv$(NC)"; \
		echo -e "$(YELLOW)Rode: jenv versions   e depois   jenv local <versao-17-ou-21>$(NC)"; \
		exit 1; \
	fi
	@echo -e "$(YELLOW)JAVA_HOME=$(JAVA_HOME)$(NC)"
	@printf 'sdk.dir=%s\n' "$(SDK_DIR)" > local.properties
	@echo -e "$(GREEN)==> configure concluido$(NC)"

build: configure
	@echo -e "$(GREEN)==> Compilando APK debug$(NC)"
	@$(GRADLE) assembleDebug -PGSTREAMER_ROOT_ANDROID=$(GSTREAMER_ROOT_ANDROID)

install: build
	@echo -e "$(GREEN)==> Instalando no dispositivo$(NC)"
	@$(ADB) install -r $(APK_DEBUG)

uninstall:
	@echo -e "$(YELLOW)==> Removendo do dispositivo$(NC)"
	@$(ADB) uninstall $(APP_ID) || true

clean:
	@echo -e "$(YELLOW)==> Limpando build$(NC)"
	@$(GRADLE) clean
	@rm -f local.properties

run: install
	@echo -e "$(GREEN)==> Iniciando $(APP_ID)/$(ACTIVITY)$(NC)"
	@$(ADB) shell am start -n $(APP_ID)/$(ACTIVITY)
