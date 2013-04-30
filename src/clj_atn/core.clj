(ns clj-atn.core
  (:import java.io.File)
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

(def nr-actions (partial parse-field :nr-actions read-int-32))

(defn- actions [{:keys [stream tree]}]
  {:stream stream
   :tree (assoc tree :actions '())})

(defn- valid-version? [version]
  (or (= version 16) (= version 12)))

(defn parse
  "Parse the binary array"
  [filename stream]
  (let [header (version {:stream stream :tree {:filename filename}})]
    (if (valid-version? (-> header :tree :version))
      (->
       header
       set-name
       expanded
       nr-actions
       actions)
      header)))

(defn read-atn
  "Read Photoshop action file and return map"
  [filename]
  (:tree
   (parse filename (read-bin-file filename))))

(defn- atn-file?
  [filename]
  true)

(defn- get-all-files [directory]
  (map #(.getPath %) (filter #(.isFile %) (.listFiles (File. directory)))))

(defn read-all-atns
  "Read all Photoshop action files in a given directory"
  [directory]
  (map read-atn (filter atn-file? (get-all-files directory))))
  