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
;; => ("[ ] [D][ ] " "[N] [C][ ] " "[Z] [M] [P]" " 1[ ]2[ ]3 " "move 1 from 2 to 1" "move 3 from 1 to 3" "move 2 from 2 to 1" "move 1 from 1 to 2")

(def split-data (map str/trim (remove empty? (map #(str/replace % "   " "[ ]" ) (str/split-lines sample-data)))))

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

(def stack-map (loop [i 0 columnar-oriented {}] (if (< i 3) (recur (inc i) (assoc columnar-oriented (inc i) (map #(nth % i) stacks))) columnar-oriented)))
;; => {1 (\space \N \Z), 2 (\D \C \M), 3 (\space \space \P)}


;; get instructions
(def instructions (last (split-at (inc (count stack-map)) split-data)))

instructions 

(first instructions) 
;; => "move 1 from 2 to 1"
(cons (first (stack-map 2)) (stack-map 1)) 
;; => (\D \space \N \Z)
(rest (stack-map 2))

;; next instruction
(second instructions) 
;; => "move 3 from 1 to 3"


(defn translate-instruction [instruction-str]
  (let [split (str/split instruction-str #" ")
        moves (Long/parseLong (split 1))
        source (Long/parseLong (split 3))
        dest (Long/parseLong (split 5))
        ]
    [moves source dest]
    {:moves moves
     :source source
     :dest dest}
  ))

(translate-instruction "move 3 from 1 to 3")
;; => {:moves 3, :source 1, :dest 3}

(map translate-instruction instructions)
;; => ({:moves 1, :source 2, :dest 1} {:moves 3, :source 1, :dest 3} {:moves 2, :source 2, :dest 1} {:moves 1, :source 1, :dest 2})

(def removed-spaces (apply merge (for [stack stack-map]
                                   {(key stack) (remove #(= \space %) (val stack))}
                                   )))

removed-spaces 

(defn move-crates [crate-map instruction]
  (let [moves (:moves instruction)
        source (:source instruction)
        dest (:dest instruction)
        ]

    ;; apply for all moves
    (loop [i 0 cm crate-map]
      (if (> i (dec moves))
        cm
        ;;more moves please
        (recur
         (inc i)
         (assoc
          (assoc
           cm dest (cons (first (cm source)) (cm dest))) ;; add to dest
          source (rest (cm source))) ;; remove from source
         )
        ))

    ))
  
  
(defn move-crates-9001 [crate-map instruction]
  (let [moves (:moves instruction)
        source (:source instruction)
        dest (:dest instruction)
        ]

    ;; apply for all moves
    (loop [i 0 cm crate-map]
      (if (> i (dec moves))
        cm
        ;;more moves please
        (recur
         (inc i)
         (assoc
          (assoc
           cm dest (cons (first (cm source)) (cm dest))) ;; add to dest
          source (rest (cm source))) ;; remove from source
         )
        ))

    ))

;;working through example
;; completed one move instruction
(def first-instruction (move-crates removed-spaces (translate-instruction "move 1 from 2 to 1")))
;; => {1 (\D \N \Z), 2 (\C \M), 3 (\P)}

(def second-instruction (move-crates first-instruction (translate-instruction "move 3 from 1 to 3")))
;; => {1 (), 2 (\C \M), 3 (\Z \N \D \P)}

(def third-instruction (move-crates second-instruction (translate-instruction "move 2 from 2 to 1")))
;; => {1 (\M \C), 2 (), 3 (\Z \N \D \P)}

(def fourth-instruction (move-crates third-instruction (translate-instruction "move 1 from 1 to 2")))
;; => {1 (\C), 2 (\M), 3 (\Z \N \D \P)}

(apply str (map first (vals fourth-instruction)))
;; => "CMZ"

;; automate the example

;; need to keep track of the map as it passes from instruction to instruction
(defn rearrange [stack-map instructions]
  (loop [sm stack-map
         i instructions]
    (if (empty? i)
      sm
      (recur
       (move-crates sm (translate-instruction (first i)))
       (rest i)))))
;; then take the first crate from each stack and concat them to form one string
(rearrange removed-spaces instructions)
;; => {1 (\C), 2 (\M), 3 (\Z \N \D \P)}
(->> (rearrange removed-spaces instructions)
     vals
     (map first)
     (apply str)
     )
;; => "CMZ"

;; get puzzle input file

(def puzzle-input
  (fio/read-file-lines "resources/day_05_input.txt"))

(= "[J]             [F] [M]            ")(first puzzle-input)
;; => "[J]             [F] [M]            "
(second puzzle-input)
;; => "[Z] [F]     [G] [Q] [F]            "
(map count (take 9 puzzle-input))
(str/replace (first puzzle-input) #"    " "[ ]" )
;; => "[J][ ][ ][ ][ ] [F] [M][ ][ ][ ][ ]"
(first (remove empty? (map #(str/replace % #"    " "[ ]" ) puzzle-input)))
;; => "[J][ ][ ][ ] [F] [M][ ][ ][ ]"
;; => "[J][ ][ ][ ][ ] [F] [M][ ][ ][ ][ ]"

(def split-puzzle-input (map str/trim (remove empty? (map #(str/replace % #"    " "[ ]" ) puzzle-input))))
(first split-puzzle-input)
;; => "[J][ ][ ][ ] [F] [M][ ][ ][ ]"
(nth split-puzzle-input 7)

(def puzzle-input-boxes (map #(str/replace % #" " "") (drop-last (stack-data split-puzzle-input))))
(first puzzle-input-boxes)
;; => "[J][][][][F][M][][][]"
(map count puzzle-input-boxes)
;; => (21 23 24 25 26 27 27 27)

(def puzzle-input-stacks (map #(take (num-stacks split-puzzle-input) %)
                              (map #(str/replace % #"[\[\]]" "")
                                   (map #(str/replace % #"[\[]]" " ") puzzle-input-boxes))))
(map #(take (num-stacks split-puzzle-input) %) puzzle-input-stacks) 
;; => ((\J \space \space \space \F \M \space \space \space) (\Z \F \space \G \Q \F \space \space \space) (\G \P \space \H \Z \S \Q \space \space) (\V \W \Z \P \D \G \P \space \space) (\T \D \S \Z \N \W \B \N \space) (\D \M \R \J \J \P \V \P \J) (\B \R \C \T \C \V \C \B \P) (\N \S \V \R \T \N \G \Z \W))

(nth puzzle-input-stacks 0)
;; => (\J \space \space \space \F \M \space \space \space)
(nth puzzle-input-stacks 1)
;; => (\Z \F \space \G \Q \F \space \space \space)
(nth puzzle-input-stacks 2)
;; => (\G \P \space \H \Z \S \Q \space \space)
(nth puzzle-input-stacks 3)
;; => (\V \W \Z \P \D \G \P \space \space)
(nth puzzle-input-stacks 4)
;; => (\T \D \S \Z \N \W \B \N \space)
(nth puzzle-input-stacks 5)
;; => (\D \M \R \J \J \P \V \P \J)
(nth puzzle-input-stacks 6)
;; => (\B \R \C \T \C \V \C \B \P)
(nth puzzle-input-stacks 7)
;; => (\N \S \V \R \T \N \G \Z \W)
(map count puzzle-input-stacks) 
;; => (9 9 9 9 9 9 9 9)

(map #(nth % 4) puzzle-input-stacks )
;; => (\F \Q \Z \D \N \J \C \T)
;; => (\space \Q \Z \D \N \J \C \T)
(def puzzle-input-stack-map (loop [i 0 columnar-oriented {}] (if (< i (num-stacks split-puzzle-input)) (recur (inc i) (assoc columnar-oriented (inc i) (map #(nth % i) puzzle-input-stacks))) columnar-oriented)))


(count puzzle-input-stack-map)
(puzzle-input-stack-map 1)
;; => (\J \Z \G \V \T \D \B \N)
(puzzle-input-stack-map 2)
;; => (\space \F \P \W \D \M \R \S)
(puzzle-input-stack-map 3)
;; => (\space \space \space \Z \S \R \C \V)
(puzzle-input-stack-map 4)
;; => (\space \G \H \P \Z \J \T \R)
(puzzle-input-stack-map 5)
;; => (\F \Q \Z \D \N \J \C \T)
;; => (\space \Q \Z \D \N \J \C \T)
(puzzle-input-stack-map 6)
;; => (\M \F \S \G \W \P \V \N)
;; => (\F \F \S \G \W \P \V \N)
(puzzle-input-stack-map 7)
;; => (\space \space \Q \P \B \V \C \G)
;; => (\M \space \Q \P \B \V \C \G)
(puzzle-input-stack-map 8)
;; => (\space \space \space \space \N \P \B \Z)
;; => (\space \space \space \space \N \P \B \Z)
(puzzle-input-stack-map 9)
;; => (\space \space \space \space \space \J \P \W)
;; => (\space \space \space \space \space \J \P \W)

puzzle-input-stack-map 

;; get the instructions

(def puzzle-input-instructions (last (split-at (inc (count puzzle-input-stack-map)) puzzle-input)))
(first puzzle-input-instructions)
;; => "move 2 from 4 to 6"

;solve part 1
(def removed-spaces-puzzle-input (apply merge (for [stack puzzle-input-stack-map]
                                   {(key stack) (remove #(= \space %) (val stack))}
                                   )))
(sort (rearrange removed-spaces-puzzle-input puzzle-input-instructions))
;; => ([1 (\C)] [2 (\F \Z \B)] [3 (nil \S \C)] [4 (\N \N \V \T)] [5 (\R \V \W \M \F \T \C \S)] [6 (\B \N \W \H \G \P \P \M \P \T \W \R \D \J \G \J \B \N \P \Z \J)] [7 (\Z \P \R \V \S \Z \G)] [8 (\P \J \Z \G \Q)] [9 (\Q \F \D \D \V)])

(->> (rearrange removed-spaces-puzzle-input puzzle-input-instructions)
     sort
     vals
     (map first)
     (apply str)
     )
;; => "GFTNRBZPF"


;;part two please

;; crates get moved as a unit now
