
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.multi-field.views
    (:require [mid-fruits.loop                       :refer [reduce-indexed]]
              [x.app-core.api                        :as a]
              [x.app-elements.combo-box.views        :as combo-box.views]
              [x.app-elements.multi-field.helpers    :as multi-field.helpers]
              [x.app-elements.multi-field.prototypes :as multi-field.prototypes]
              [x.app-elements.text-field.views       :as text-field.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- multi-field-text-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  [group-id {:keys [initial-options options options-path] :as group-props} field-dex]
  (let [field-props (multi-field.prototypes/field-props-prototype group-id group-props field-dex)
        field-key   (multi-field.helpers/field-dex->react-key     group-id group-props field-dex)]
       [:div.x-multi-field--text-field {:key field-key}
                                       (if (or initial-options options options-path)
                                           [combo-box.views/element  field-props]
                                           [text-field.views/element field-props])]))

(defn- multi-field-field-group
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  (let [group-value @(a/subscribe [:elements.multi-field/get-group-value group-id group-props])]
       (letfn [(f [field-group field-dex _] (conj field-group [multi-field-text-field group-id group-props field-dex]))]
              (reduce-indexed f [:<>] group-value))))

(defn- multi-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  [:div.x-multi-field (multi-field.helpers/group-attributes group-id group-props)
                      [multi-field-field-group              group-id group-props]])

(defn element
  ; XXX#0711
  ; A multi-field elem alapkomponense a text-field vagy a combo-box elem.
  ; A multi-field elem további paraméterezését a text-field vagy a combo-box
  ; elem dokumentációjában találod.
  ; Abban az esetben, amikor a combo-box elem :initial-options, :options vagy :options-path
  ; tulajdonságát használod egy multi-field elemnél, akkor a mezők text-field elem
  ; helyett combo-box elemként jelennek meg.
  ;
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ;  {:max-field-count (integer)(opt)
  ;    Default: 8}
  ;
  ; @usage
  ;  [elements/multi-field {...}]
  ;
  ; @usage
  ;  [elements/multi-field :my-multi-field {...}]
  ([group-props]
   [element (a/id) group-props])

  ([group-id group-props]
   (let [group-props (multi-field.prototypes/group-props-prototype group-id group-props)]
        [multi-field group-id group-props])))
