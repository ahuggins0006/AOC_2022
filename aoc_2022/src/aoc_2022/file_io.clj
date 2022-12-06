(ns aoc-2022.file-io)


(defn read-file-lines [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (reduce conj [] (line-seq rdr))))
