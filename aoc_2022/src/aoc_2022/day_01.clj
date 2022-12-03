(ns aoc-2022.day-01
  (:require [clojure.string :as str])

  )

(slurp "resources/day_01_samples.txt")
;; => "1000\n2000\n3000\n\n4000\n\n5000\n6000\n\n7000\n8000\n9000\n\n10000\n"

(defn read-file-lines [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (reduce conj [] (line-seq rdr)))
  )
(def data (with-open [rdr (clojure.java.io/reader "resources/day_01_samples.txt")]
            (reduce conj [] (line-seq rdr))))

(def data (read-file-lines "resources/day_01_samples.txt"))

(->> data
     (partition-by #(empty? %))
     (remove #(= '("") %))
     (map #(map (fn [a] (Integer/parseInt a)) %))
     (map #(apply + %))
     (apply max)
     )
;; => 24000

(defn calc-totals [data]
  (->> data
       (partition-by #(empty? %))
       (remove #(= '("") %))
       (map #(map (fn [a] (Integer/parseInt a)) %))
       (map #(apply + %))))

(def input (read-file-lines "resources/day_01_a_input.txt"))

(->> input
     calc-totals
     (apply max)
     )
;; => 69795

;; End first half



(->> input
     calc-totals
     (sort >)
     (take 3)
     (apply +)
     )
;; => 208437
