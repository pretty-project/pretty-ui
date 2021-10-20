
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author:
; Created:
; Description:



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns project-emulator.core
    (:require [x.boot-loader]
              [extensions.clients.api]

              ; TEMP
              [playground.api]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- app
  ; @param (component) ui-structure
  ;
  ; @usage
  ;  (defn- ui-structure [] [:div ...])
  ;  [app #'ui-structure]
  ;
  ; @return (component or hiccup)
  [ui-structure]
  [ui-structure])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn boot-app!   [] (x.boot-loader/boot-app!   #'app))
(defn render-app! [] (x.boot-loader/render-app! #'app))
