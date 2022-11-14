
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.html.views
    (:require [candy.api   :refer [param]]
              [hiccup.page :refer [html5]]))




;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (hiccup) head
  ; @param (hiccup) body
  ;
  ; @usage
  ;  (html [:head] [:body])
  [head body]
  ; XXX#7659 (source-code/cljs/x/app_environment/scroll_prohibitor/README.md)
  (html5 {:data-hide-scrollbar  "true"
          :data-scroll-disabled "true"
          :id                   "x-document-element"
          :style                "overflow-y: hidden"}
         (param head)
         (param body)))
