
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.lister-handler.config)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (integer)
(def DEFAULT-DOWNLOAD-LIMIT 20)

; @constant (namespaced keywords in vector)
(def DEFAULT-ORDER-BY-OPTIONS [:modified-at/descending :modified-at/ascending :name/ascending :name/descending])

; @constant (keywords in vector)
(def DEFAULT-SEARCH-KEYS [:name])
