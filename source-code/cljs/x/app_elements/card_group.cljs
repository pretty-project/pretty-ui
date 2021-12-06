
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.26
; Description:
; Version: v0.4.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.card-group
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.card       :rename {element card}]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- group-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) group-props
  ;
  ; @return (map)
  ;  {:horizontal-align (keyword)}
  [group-props]
  (merge {:horizontal-align :center}
         (param group-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- card-group
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:cards (maps in vector)}
  ;
  ; @return (hiccup)
  [group-id {:keys [cards] :as group-props}]
  (reduce #(vector/conj-item %1 [card %2])
           [:div.x-card-group (engine/element-attributes group-id group-props)]
           (param cards)))

(defn element
  ; XXX#3240
  ; A card-group elem számára :cards tulajdonságként átadott vektor a card-group elemen megjelenített
  ; card elemek paraméter térképeit tartalmazza.
  ; A card elemek paraméterézének leírását a card elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ;  {:cards (maps in vector)
  ;    [{...} {...}]
  ;   :class (string or vector)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/card-group {...}]
  ;
  ; @usage
  ;  [elements/card-group :my-card-group {...}]
  ;
  ; @return (component)
  ([group-props]
   [element (a/id) group-props])

  ([group-id group-props]
   (let [group-props (a/prot group-props group-props-prototype)]
        [card-group group-id group-props])))
