
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



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
  ; XXX#7659
  (html5 {:data-hide-scrollbar true
          :id    "x-document-element"
          :style "overflow-y: hidden"}
         (param head)
         (param body)))
