
(ns pretty-tables.api
    (:require [pretty-tables.cell.views   :as cell.views]
              [pretty-tables.row.views    :as row.views]
              [pretty-tables.table.views  :as table.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (*/view)
(def cell  cell.views/view)
(def row   row.views/view)
(def table table.views/view)
