

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.html.views
    (:require [hiccup.page      :refer [html5]]
              [mid-fruits.candy :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (hiccup) head
  ; @param (hiccup) body
  ;
  ; @usage
  ;  (ui/html (ui/head) (ui/body))
  ;
  ; @usage
  ;  (ui/html [:head] [:body])
  [head body]
  (html5 {:id    "x-document-element"
          ; XXX#7659
          :style "overflow-y: hidden"}
         (param head)
         (param body)))
