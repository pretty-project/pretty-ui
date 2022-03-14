
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns ajax.config
    (:require [app-fruits.dom :as dom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def CSRF-TOKEN (when-let [element (dom/get-element-by-id "sente-csrf-token")]
                          (dom/get-element-attribute element "data-csrf-token")))
