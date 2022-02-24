
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.09
; Description:
; Version: v0.6.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.html
    (:require [hiccup.page      :refer [html5]]
              [mid-fruits.candy :refer [param]]))



;; -- Components --------------------------------------------------------------
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
  ;
  ; @return (hiccup)
  [head body]
  (html5 {:id    "x-document-element"
          ; XXX#7659
          :style "overflow-y: hidden"}
         (param head)
         (param body)))
