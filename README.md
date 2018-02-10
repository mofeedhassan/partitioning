# GPABA

This project is created to develop GPABA, Graph Partitioning and Balancing Algorithm.

## Usage

```bash
mvn compile package
java -Xmx1g -jar gpaba-0.0.1-SNAPSHOT-jar-with-dependencies.jar <ALGORITHM> <FILE>
```

`<ALGORITHM>` can be: `genetic`, `growing`, or `unionfind`.

`<FILE>` describes a weighted graph, e.g. `graph10k`.
