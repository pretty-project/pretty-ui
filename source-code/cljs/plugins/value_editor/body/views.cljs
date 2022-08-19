
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.value-editor.body.views
    (:require [plugins.value-editor.body.helpers :as body.helpers]
              [x.app-core.api                    :as a]
              [x.app-elements.api                :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (if-let [helper @(a/subscribe [:value-editor/get-editor-prop editor-id :helper])]
          [elements/text ::editor-helper
                         {:content helper
                          :indent  {:vertical :m}}]))

(defn editor-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [field-props (body.helpers/editor-props->field-props editor-id)]
       [elements/text-field :value-editor/editor-field field-props]))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  [:<> [editor-field  editor-id]
       [editor-helper editor-id]])
