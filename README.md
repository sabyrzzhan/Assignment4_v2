# Assignment 4 — Smart City / Smart Campus Scheduling (v2)

**Student:** Akylbek Sabyrzhan · **Group:** SE-2424

Implements:
- **SCC detection** (Tarjan) + **Condensation DAG**
- **Topological Sort** (Kahn) + **derived order of original tasks**
- **DAG Single-Source Shortest Paths** (edge-weight model)
- **Critical Path (Longest Path)** via DP over topological order

## Build & Run
Requirements: Java 17+, Maven 3.9+

```bash
mvn -q -DskipTests package
mvn -q -DskipTests exec:java -Dexec.args="data/tasks.json"
# Or run tests
mvn test
```

## Data
All datasets are under `data/` with CSV mirrors under `data/csv/`. Weight model: **edge**.

| File | n | edges | Cyclic? |
|---|---:|---:|---|
| data/large1.json | 25 | 26 | Yes |
| data/large2.json | 35 | 41 | Yes |
| data/large3.json | 50 | 65 | Yes |
| data/medium1.json | 12 | 12 | Yes |
| data/medium2.json | 15 | 16 | Yes |
| data/medium3.json | 18 | 18 | No |
| data/small1.json | 6 | 5 | Yes |
| data/small2.json | 5 | 6 | No |
| data/small3.json | 8 | 9 | Yes |
| data/tasks.json | 8 | 7 | Yes |
