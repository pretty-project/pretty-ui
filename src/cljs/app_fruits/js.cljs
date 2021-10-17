
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.02
; Description:
; Version: v0.1.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-fruits.js
    (:require [app-fruits.dom :as dom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn append-script!
  ; @param (string) script
  ;
  ; @usage
  ;  (js/append-script! "console.log('420')")
  [script]
  (let [body-element   (dom/get-body-element)
        script-element (dom/create-element! "script")]
       (dom/set-element-attribute! script-element "type" "text/javascript")
       (dom/set-element-content!   script-element script)
       (dom/append-element!        body-element   script-element)))
