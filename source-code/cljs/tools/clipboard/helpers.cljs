
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.clipboard.helpers
    (:require [dom.api :as dom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copy-text-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [clipboard (dom/get-element-by-id "clipboard")]
       (-> clipboard .-value js/navigator.clipboard.writeText)))
