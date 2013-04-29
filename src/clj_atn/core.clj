(ns clj-atn.core
  (:use [clj-atn util]))

(defn- parse-field
  "Parse field and add it to the map"
  [key reader {:keys [stream tree]}]
  (let [{rest :stream val :val} (reader stream)]
    {:stream rest 
     :tree (assoc tree key val)}))

(def version (partial parse-field :version read-int-32))

(def set-name (partial parse-field :set-name read-unicode-string))

(def expanded (partial parse-field :expanded read-byte))

(defn parse
  "Parse the binary array"
  [stream]
  (-> {:stream stream :tree {}}
      version
      set-name
      expanded))

(defn read-atn
  "Read Photoshop action file and return map"
  [filename]
  (-> filename read-bin-file parse :tree))