(ns aoc-2022.day-02
  (:require [clojure.string :as str])
  )

(def scoring {
               "A X" (+ 3 1)
               "A Y" (+ 6 2)
               "A Z" (+ 0 3)

               "B X" (+ 0 1)
               "B Y" (+ 3 2)
               "B Z" (+ 6 3)

               "C X" (+ 6 1)
               "C Y" (+ 0 2)
               "C Z" (+ 3 3)
               })

(scoring "A Z")

(def sample ["A Y"
             "B X"
             "C Z"])

;; run with puzzle input file
;; TODO make a utility
(defn read-file-lines [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (reduce conj [] (line-seq rdr))))

(def puzzle-input (read-file-lines "resources/day_02_input.txt"))

(apply + (map scoring puzzle-input)) 
;; => 9177

;; part two 

(def scoring2 {
               "A X" (+ 0 3)
               "A Y" (+ 3 1)
               "A Z" (+ 6 2)

               "B X" (+ 0 1)
               "B Y" (+ 3 2)
               "B Z" (+ 6 3)

               "C X" (+ 0 2)
               "C Y" (+ 3 3)
               "C Z" (+ 6 1)
               })

(apply + (map scoring2 sample))

(apply + (map scoring2 puzzle-input))
