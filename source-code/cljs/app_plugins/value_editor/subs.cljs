
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.17
; Description:
; Version: v0.7.6
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.value-editor.subs
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a :refer [r]]
              [x.app-db.api     :as db]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-value-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (item-path vector)
  [editor-id]
  (db/path ::value-editors editor-id :value))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-editor-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (get-in db (db/path ::value-editors editor-id)))

(defn- get-editor-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (keyword) prop-id
  ;
  ; @return (*)
  [db [_ editor-id prop-id]]
  (get-in db (db/path ::value-editors editor-id prop-id)))

(defn get-editor-value
  ; @param (keyword) editor-id
  ;
  ; @return (string)
  [db [_ editor-id]]
  (let [edit-path (r get-editor-prop db editor-id :edit-path)]
       (get-in db edit-path)))

; @usage
;  [:value-editor/get-editor-value :my-editor]
(a/reg-sub :value-editor/get-editor-value get-editor-value)
