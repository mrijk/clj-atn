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
(def nr-actions (partial parse-field :nr-actions read-int-32))
(def index (partial parse-field :index read-int-16))
(def shift-key (partial parse-field :shift-key read-byte))
(def command-key (partial parse-field :command-key read-byte))
(def color-index (partial parse-field :color-index read-int-16))
(def action-name (partial parse-field :action-name read-unicode-string))
(def nr-action-events (partial parse-field :nr-action-events read-int-32))
(def enabled (partial parse-field :enabled read-byte))
(def with-dialog (partial parse-field :with-dialog read-byte))
(def dialog-options (partial parse-field :dialog-options read-byte))
(def text (partial parse-field :text read-four-byte-string))
(def event-name (partial parse-field :event-name read-string))

(defn- read-action-event [stream]
  (-> {:stream stream :tree {}}
      expanded
      enabled
      with-dialog
      dialog-options
      text
      event-name))

(defn- action-events [{:keys [stream tree]}]
  {:stream stream
   :tree (assoc tree :events (list (:tree (read-action-event stream))))})

(defn- read-action [stream]
  (-> {:stream stream :tree {}}
      index
      shift-key
      command-key
      color-index
      action-name
      expanded
      nr-action-events
      action-events))

(defn- actions [{:keys [stream tree]}]
  {:stream stream
   :tree (assoc tree :actions (list (:tree (read-action stream))))})

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

; Fix me: check for extension
(defn- atn-file?
  [filename]
  true)

(defn read-all-atns
  "Read all Photoshop action files in a given directory"
  [directory]
  (map read-atn (filter atn-file? (get-all-files directory))))

