
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (integer)
(def DEFAULT-DOWNLOAD-LIMIT 20)

; WARNING! DEPRECATED! DO NOT USE!
; @constant (namespaced keywords in vector)
(def DEFAULT-ORDER-BY-OPTIONS [:modified-at/descending :modified-at/ascending :name/ascending :name/descending])
; WARNING! DEPRECATED! DO NOT USE!
