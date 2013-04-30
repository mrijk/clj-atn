clj-atn
=======

Clojure code to parse Photoshop action files

Example 1:

clj-atn.core=> (read-atn "Fragment.atn")
{:actions ({:nr-action-events 1, :expanded 1, :name "Action 1", :color-index 0, :command-key 0, :shift-key 0, :index 0}), :nr-actions 1, :expanded 1, :set-name "Fragment", :version 16, :filename "Fragment.atn"}

Example 2:

clj-atn.core=> (write-json (read-atn "Fragment.atn") "/tmp/Fragment.json")

Writes the following file contents:

{
    actions: [
        {
            nr-action-events: 1,
            expanded: 1,
            name: "Action 1",
            color-index: 0,
            command-key: 0,
            shift-key: 0,
            index: 0
        }
    ],
    nr-actions: 1,
    expanded: 1,
    set-name: "Fragment",
    version: 16,
    filename: "Fragment.atn"
}