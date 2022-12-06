(ns aoc-2022.day-04
  (:require [clojure.string :as str]
            [clojure.set :as s]
            [aoc-2022.file-io :as fio]
            ))

(def sample-data
  "2-4,6-8
   2-3,4-5
   5-7,7-9
   2-8,3-7
   6-6,4-6
   2-6,4-8")



(def split-data (map str/trim (str/split sample-data #"\n")))

(map str/trim split-data) 

(def a-pair (first (str/split sample-data #"\n")))

a-pair 
;; => "2-4,6-8"

(def single-assignment (first (str/split a-pair #",")))

single-assignment 
;; => "2-4"

;;destructure an assignment into beginning and ending
(defn calc-assignment [pair]
  (let [s (str/split pair #"-")
        b (Integer/parseInt (first s))
        e (Integer/parseInt (second s))]
    (range b (inc e)))
  )

(calc-assignment single-assignment)
(calc-assignment "8-17")


(apply s/intersection (map (comp set calc-assignment) (str/split a-pair #",")))

(defn count-assignment-pairs [split-data]
  (count (remove false?(for [a-pair split-data]
                         (apply (fn [s1 s2] (or (s/superset? s1 s2) (s/subset? s1 s2))) (map (comp set calc-assignment) (str/split a-pair #",")))

                         ))))

(def puzzle-input (fio/read-file-lines "resources/day_04_input.txt"))

(first puzzle-input)
;; => "8-17,16-49"

(count-assignment-pairs puzzle-input)


