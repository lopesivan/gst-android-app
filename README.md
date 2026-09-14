# gst-android-app

Aplicativo Android mínimo e funcional que incorpora o GStreamer de forma
estática usando CMake. O botão principal executa este pipeline de áudio:

```text
audiotestsrc wave=sine freq=440 ! audioconvert ! audioresample ! openslessink
```

## Requisitos

- Android SDK 35
- Android NDK `29.0.14206865`
- JDK 17 ou 21
- Gradle 8.9 (o alvo `make init` cria o wrapper)
- GStreamer Android ARM64 extraído em `/opt/gstreamer/arm64`

O diretório ARM64 precisa conter, entre outros arquivos:

```text
/opt/gstreamer/arm64/share/gst-android/ndk-build/gstreamer-1.0.mk
/opt/gstreamer/arm64/share/cmake/GStreamerMobileConfig.cmake
/opt/gstreamer/arm64/lib/libgstreamer-1.0.a
/opt/gstreamer/arm64/lib/gstreamer-1.0/libgstaudiotestsrc.a
/opt/gstreamer/arm64/lib/gstreamer-1.0/libgstaudioconvert.a
/opt/gstreamer/arm64/lib/gstreamer-1.0/libgstaudioresample.a
/opt/gstreamer/arm64/lib/gstreamer-1.0/libgstopensles.a
```

## Compilar e executar

```bash
make init
make build
make run
```

Outro local de instalação pode ser informado sem editar o projeto:

```bash
make build GSTREAMER_ROOT_ANDROID=/outro/diretorio/gstreamer
```

O APK produzido suporta `arm64-v8a`, correspondente à instalação acima.
