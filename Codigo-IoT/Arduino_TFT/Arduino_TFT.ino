#include <MCUFRIEND_kbv.h>
#include <Adafruit_GFX.h>

MCUFRIEND_kbv tft;

#define PRETO     0x0000
#define BRANCO    0xFFFF
#define VERDE     0x07E0
#define VERMELHO  0xF800
#define AMARELO   0xFFE0

const int TARIFA_CENTAVOS = 500;        // R$ 5,00 (so para exibir na tela inicial)

String mensagem = "";

// ===== MOTOR DE PASSOS 28BYJ-48 + ULN2003 (catraca) =====
// IN1=D30, IN2=D31, IN3=D32, IN4=D33
const int MOTOR_PINS[4] = {30, 31, 32, 33};
const int PASSOS_VOLTA = 2040;      // meio passo: 4096 passos = 1 volta completa
const int DIRECAO = 1;              // use -1 para girar ao contrario
const int ATRASO_PASSO_MS = 2;      // 2 ms = ~8 s por volta (nao va abaixo de 2)

const byte SEQ[8][4] = {
  {1, 0, 0, 0},
  {1, 1, 0, 0},
  {0, 1, 0, 0},
  {0, 1, 1, 0},
  {0, 0, 1, 0},
  {0, 0, 1, 1},
  {0, 0, 0, 1},
  {1, 0, 0, 1}
};

void desligarMotor() {
  for (int i = 0; i < 4; i++) digitalWrite(MOTOR_PINS[i], LOW);
}

void girarCatraca() {
  for (int p = 0; p < PASSOS_VOLTA; p++) {
    int idx = (DIRECAO > 0) ? (p % 8) : (7 - (p % 8));
    for (int i = 0; i < 4; i++) digitalWrite(MOTOR_PINS[i], SEQ[idx][i]);
    delay(ATRASO_PASSO_MS);
  }
  desligarMotor();  // solta as bobinas (evita aquecer)
}

void mostrarDinheiro(int centavos) {
  tft.print("R$ ");
  tft.print(centavos / 100);
  tft.print(",");
  int resto = centavos % 100;
  if (resto < 10) tft.print("0");
  tft.print(resto);
}

// ===== TELA FIXA: aguardando cartao =====
void telaInicial() {
  tft.fillScreen(PRETO);

  tft.setTextColor(BRANCO);
  tft.setTextSize(3);
  tft.setCursor(52, 30);
  tft.print("APROXIME SEU");
  tft.setCursor(106, 62);
  tft.print("CARTAO");

  tft.setTextColor(AMARELO);
  tft.setTextSize(2);
  tft.setCursor(115, 130);
  tft.print("Tarifa:");

  tft.setTextColor(VERDE);
  tft.setTextSize(4);
  tft.setCursor(76, 160);
  mostrarDinheiro(TARIFA_CENTAVOS);
}

void telaErro() {
  tft.fillScreen(PRETO);
  tft.setTextColor(VERMELHO);
  tft.setTextSize(3);
  tft.setCursor(115, 50);
  tft.print("ERRO");
  tft.setTextColor(BRANCO);
  tft.setTextSize(2);
  tft.setCursor(70, 120);
  tft.print("Falha comunicacao");
  delay(3000);
  telaInicial();
}

void telaNegado() {
  tft.fillScreen(PRETO);
  tft.setTextColor(VERMELHO);
  tft.setTextSize(3);
  tft.setCursor(70, 40);
  tft.print("PAGAMENTO");
  tft.setCursor(106, 80);
  tft.print("NEGADO");
  tft.setTextColor(BRANCO);
  tft.setTextSize(2);
  tft.setCursor(64, 140);
  tft.print("Saldo insuficiente");
  delay(3000);
  telaInicial();
}

// ===== TELA APOS PAGAMENTO: catraca liberada + saldo restante =====
void telaLiberada(int saldoRestante) {
  tft.fillScreen(PRETO);

  tft.setTextColor(VERDE);
  tft.setTextSize(4);
  tft.setCursor(76, 40);
  tft.print("CATRACA");
  tft.setCursor(64, 80);
  tft.print("LIBERADA");

  tft.setTextColor(BRANCO);
  tft.setTextSize(2);
  tft.setCursor(70, 150);
  tft.print("Saldo restante:");

  tft.setTextColor(AMARELO);
  tft.setTextSize(3);
  tft.setCursor(85, 180);
  mostrarDinheiro(saldoRestante);

  girarCatraca();   // a tela fica visivel durante a volta (~8 s)
  telaInicial();
}

void processarMensagem(String texto) {
  texto.trim();
  Serial.print("PROCESSANDO: <"); Serial.print(texto); Serial.println(">");

  if (texto == "NEGADO") { telaNegado(); return; }
  if (texto == "ERRO")   { telaErro();   return; }

  int p1 = texto.indexOf("SALDO_ANTES:");
  int p2 = texto.indexOf(";DESCONTO:");
  int p3 = texto.indexOf(";SALDO_RESTANTE:");

  if (p1 >= 0 && p2 >= 0 && p3 >= 0) {
    String sRest = texto.substring(p3 + 16);
    telaLiberada(sRest.toInt());
  } else {
    Serial.println("Mensagem ignorada (formato incorreto).");
  }
}

void setup() {
  Serial.begin(115200);
  Serial1.begin(9600);   // RX1 = pino 19, TX1 = pino 18

  for (int i = 0; i < 4; i++) pinMode(MOTOR_PINS[i], OUTPUT);
  desligarMotor();

  uint16_t ID = tft.readID();
  tft.begin(ID);
  tft.setRotation(1);
  telaInicial();

  Serial.println("\n==============================");
  Serial.println("MEGA INICIADO - AGUARDANDO ESP32");
  Serial.println("==============================");
}

void loop() {
  while (Serial1.available() > 0) {
    char c = Serial1.read();
    Serial.print(c);   // depuracao: mostra tudo que chega (pode remover depois)

    if (c == '\n') {
      if (mensagem.length() > 0) processarMensagem(mensagem);
      mensagem = "";
    } else if (c != '\r') {
      mensagem += c;
    }
  }
}
