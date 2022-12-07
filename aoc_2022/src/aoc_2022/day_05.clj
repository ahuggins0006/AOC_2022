(ns aoc-2022.day-05
  (:require [clojure.string :as str]
            [aoc-2022.file-io :as fio]
            ))

(def sample-data "
    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2")

(str/split-lines sample-data)
;; => ["" "    [D]    " "[N] [C]    " "[Z] [M] [P]" " 1   2   3 " "" "move 1 from 2 to 1" "move 3 from 1 to 3" "move 2 from 2 to 1" "move 1 from 1 to 2"]

;; account for columnar spacing
(remove empty? (map #(str/replace % #"   " "[ ]" ) (str/split-lines sample-data)))
;; => ("[ ] [D][ ] " "[N] [C][ ] " "[Z] [M] [P]" " 1[ ]2[ ]3 " "move 1 from 2 to 1" "move 3 from 1 to 3" "move 2 from 2 to 1" "move 1 from 1 to 2")

(def split-data (map str/trim (remove empty? (map #(str/replace % #"   " "[ ]" ) (str/split-lines sample-data)))))

split-data 


(defn num-stacks [split-data]
  (Long/parseLong (str (last (first (filter #(str/starts-with? % "1") split-data))))))

(num-stacks split-data)
;; => 3

(defn stack-data [split-data ](take (inc (.indexOf (map #(str/starts-with? % "1") split-data) true)) split-data))

(butlast (stack-data split-data))
;; => ("[ ] [D][ ]" "[N] [C][ ]" "[Z] [M] [P]")

(last (stack-data split-data))
;; => "1[ ]2[ ]3"
(str/replace "1[ ]2[ ]3" #"[\[ \]]" " ")
;; => "1   2   3"
(str/split "1   2   3" #"   ")
(defn str->range [s] (map #(Long/parseLong %) (str/split (str/replace s #"[\[ \]]" " ") #"   ")))

(str->range "1[ ]2[ ]3")
;; => (1 2 3)
(str->range (last (stack-data split-data)))
;; => (1 2 3)

;; create map of stack to boxes
;; don't lose box spacing

;; => ("[ ] [D][ ]" "[N] [C][ ]" "[Z] [M] [P]")
;; => (1 2 3)
(def boxes (map #(str/replace % #" " "") (drop-last (stack-data split-data))))

boxes 
(def stacks (map #(str/replace % #"[\[\]]" "") (map #(str/replace % #"[\[]]" " ") boxes))) 
;; => (" D " "NC " "ZMP")

(map #(nth % 0) stacks)
;; => (\space \N \Z)
(map #(nth % 1) stacks)
;; => (\D \C \M)
(map #(nth % 2) stacks)
;; => (\space \space \P)

(loop [i 0] (when (< i 3) (println (map #(nth % i) stacks)) (recur (inc i))))
(def stack-map (zipmap  (str->range (last (stack-data split-data))) (map #(str/replace % #"[\[\]\" \"]" "") (drop-last (stack-data split-data)))))
stack-map 


;; get instructions
(def instructions (last (split-at (inc (count stack-map)) split-data)))

instructions 

(first instructions) 
;; => "move 1 from 2 to 1"
(cons (first (stack-map 2)) (stack-map 1)) 
(rest (stack-map 2))

;; next instruction
(second instructions) 
;; => "move 3 from 1 to 3"


(defn translate-instruction [instruction])
