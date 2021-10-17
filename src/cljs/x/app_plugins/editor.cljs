
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.08
; Description:
; Version: v0.2.0
; Compatibility: x4.2.1



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-plugins.editor
    (:require [mid-fruits.candy :refer [param]]
              [x.app-core.api   :as a]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- editor-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) editor-props
  ; 
  ; @return (map)
  [sortable-props]
  (merge {}
         (param sortable-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- editor
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ; 
  ; @return (hiccup)
  [editor-id editor-props]
  [:div])

(defn view
  ; @param (keyword)(opt) editor-id
  ; @param (map) editor-props
  ;  {}
  ; 
  ; @return (component)
  ([editor-props]
   [view nil editor-props])

  ([editor-id editor-props]
   (let [editor-id    (a/id   editor-id)
         editor-props (a/prot editor-props editor-props-prototype)]
        [editor editor-id editor-props])))
