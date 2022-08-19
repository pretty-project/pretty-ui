

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-dictionary.term-handler.lifecycles
    (:require [x.app-core.api         :as a]
              [x.mid-dictionary.books :refer [BOOKS]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-app-init [:dictionary/add-terms! BOOKS]})
