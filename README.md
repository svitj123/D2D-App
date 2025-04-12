# 📍 D2D-App

Aplikacija za interaktivno prikazovanje naslovov za teren (Door-to-door) na zemljevidu. Uporabna za hitro navigacijo in pregled naslovov, ki jih je treba obiskati na terenu.

## 🚀 Glavne tehnologije

- **Backend**: Java, Spring Boot, Maven, Apache POI
- **Frontend**: Angular, Leaflet.js / Google Maps
- **Baza (opcijsko)**: MongoDB

---

## 📦 Struktura projekta

D2D-App
├── backend       # Spring Boot REST API
└── frontend      # Angular frontend aplikacija

---

## ⚙️ Navodila za zagon projekta lokalno

### Backend (Spring Boot):

- Zaženi z Mavenom:

```bash
cd backend
./mvnw spring-boot:run
```
API teče na naslovu http://localhost:8080.

---

## Frontend (Angular):

Namesti dependencies in zaženi Angular aplikacijo:

```bash
cd frontend
npm install
ng serve
```
Frontend teče na naslovu http://localhost:4200.

---

## 🗺️ Funkcionalnosti aplikacije

•	Upload Excel datoteke z naslovi.
•	Prikaz naslovov na interaktivnem zemljevidu.
•	Opcijska možnost shranjevanja statusa obiskov.
 
## 📌 Predvidene prihodnje izboljšave

•	Shramba podatkov v MongoDB.
•	Možnost označevanja statusov obiskov (obiskano, neobiskano itd.).
•	Avtentikacija uporabnikov.