
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.chip-group.views
    (:require [elements.chip-group.helpers    :as chip-group.helpers]
              [elements.chip-group.prototypes :as chip-group.prototypes]
              [elements.chip.views            :as chip.views]
              [elements.label.views           :as label.views]
              [mid-fruits.loop                :refer [reduce-indexed]]
              [mid-fruits.random              :as random]
              [mid-fruits.vector              :as vector]
              [re-frame.api                   :as r]
              [x.app-components.api           :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip-group-no-chips-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:no-chips-label (metamorphic-content)(opt)}
  [_ {:keys [no-chips-label]}]
  (if no-chips-label [:div.x-chip-group--no-chips-label (x.components/content no-chips-label)]))

(defn- chip-group-chip
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) chip-dex
  ; @param (*) chip
  [group-id group-props chip-dex chip]
  (let [chip-props (chip-group.prototypes/chip-props-prototype group-id group-props chip-dex chip)]
       [chip.views/element chip-props]))

(defn- chip-group-chips
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:value-path (vector)}
  [group-id {:keys [value-path] :as group-props}]
  (let [chips @(r/subscribe [:db/get-item value-path])]
       (if (vector/nonempty? chips)
           (letfn [(f [chip-list chip-dex chip] (conj chip-list [chip-group-chip group-id group-props chip-dex chip]))]
                  (reduce-indexed f [:div.x-chip-group--chips] chips))
           [chip-group-no-chips-label group-id group-props])))

(defn- chip-group-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  [:div.x-chip-group--body (chip-group.helpers/chip-group-body-attributes group-id group-props)
                           [chip-group-chips                              group-id group-props]])

(defn- chip-group-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {}
  [_ {:keys [helper info-text label]}]
  (if label [label.views/element {:content   label
                                  :helper    helper
                                  :info-text info-text}]))

(defn- chip-group
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  [:div.x-chip-group (chip-group.helpers/chip-group-attributes group-id group-props)
                     [chip-group-label                         group-id group-props]
                     [chip-group-body                          group-id group-props]])

(defn element
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :chip-label-f (function)(opt)
  ;    Default: return
  ;   :deletable? (boolean)(opt)
  ;    Default: false
  ;   :helper (metamorphic-content)(opt)
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :no-chips-label (metamorphic-content)(opt)
  ;   :style (map)(opt)
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [chip-group {...}]
  ;
  ; @usage
  ;  [chip-group :my-chip-group {...}]
  ([group-props]
   [element (random/generate-keyword) group-props])

  ([group-id group-props]
   (let [group-props (chip-group.prototypes/group-props-prototype group-id group-props)]
        [chip-group group-id group-props])))
