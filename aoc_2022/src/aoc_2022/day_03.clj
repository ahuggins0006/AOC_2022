(ns aoc-2022.day-03
  (:require [clojure.string :as str]
            [clojure.set :as s]
            [clojure.test :as test])
  )



(defn char-range [start end]
  (map char (range (int start) (inc (int end)))))

(def priorities (zipmap (concat (char-range \a \z) 
                                (char-range \A \Z)) (range 1 53)))

(sort-by val priorities)
;; => ([\a 1] [\b 2] [\c 3] [\d 4] [\e 5] [\f 6] [\g 7] [\h 8] [\i 9] [\j 10] [\k 11] [\l 12] [\m 13] [\n 14] [\o 15] [\p 16] [\q 17] [\r 18] [\s 19] [\t 20] [\u 21] [\v 22] [\w 23] [\x 24] [\y 25] [\z 26] [\A 27] [\B 28] [\C 29] [\D 30] [\E 31] [\F 32] [\G 33] [\H 34] [\I 35] [\J 36] [\K 37] [\L 38] [\M 39] [\N 40] [\O 41] [\P 42] [\Q 43] [\R 44] [\S 45] [\T 46] [\U 47] [\V 48] [\W 49] [\X 50] [\Y 51] [\Z 52])

(def sample-data ["vJrwpWtwJgWrhcsFMMfFFhFp"
                  "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL"
                  "PmmdzqPrVvPwwTWBwg"
                  "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn"
                  "ttgJtRGJQctTZtZT"
                  "CrZsJsPPZsGzwwsLwLmpwMDw"])

(priorities (first (apply s/intersection (map set (split-at (/ (count (first sample-data)) 2) (first sample-data))))))
;; => 16
;; => #{\p}

(defn calc-priority [rucksack] (priorities (first (apply s/intersection (map set (split-at (/ (count rucksack) 2) rucksack))))))

(apply + (map calc-priority sample-data))
;; => 157

;; run on puzzle input
;; goodnight
