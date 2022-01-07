
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.07
; Description:
; Version: v0.3.0
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.print-handler
    (:require [x.app-details :as details]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def CONSOLE-PREFIX (str "x" details/app-version " - "))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn console
  ; @param (*) content
  ;
  ; @return (*)
  [content]
  (let [prefixed-content (str CONSOLE-PREFIX content)]
       (println prefixed-content)))
