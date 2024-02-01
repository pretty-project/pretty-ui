
(ns pretty-tables.api
    (:require [pretty-tables.data-cell.views :as data-cell.views]
              ;[pretty-tables.data-row.views :as data-row.views]
              ;[pretty-tables.data-column.views :as data-column.views]
              [pretty-tables.data-table.views :as data-table.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (*/view)
(def data-cell  data-cell.views/view)
(def data-table data-table.views/view)
